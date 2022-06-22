/*
 * Copyright (c) 2022.
 *
 * Licensed under the Apace License, Version 2.0 (the "Licence"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 *  License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 *  either express or implied. See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package de.b3ttertogeth3r.walhalla.old.firebase;

import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.old.MainActivity;
import de.b3ttertogeth3r.walhalla.old.StartActivity;
import de.b3ttertogeth3r.walhalla.old.enums.Rank;
import de.b3ttertogeth3r.walhalla.old.interfaces.MyCompleteListener;
import de.b3ttertogeth3r.walhalla.old.models.Person;
import de.b3ttertogeth3r.walhalla.old.models.ProfileError;
import de.b3ttertogeth3r.walhalla.old.utils.CacheData;

/**
 * @see <a href="https://firebase.google.com/docs/auth">Firebase Authentication</a>
 */
@SuppressWarnings("ConstantConditions")
public class Authentication {
    public static final String PROFILE_DATA = "Missing profile data";
    private static final String TAG = "MyAuth";
    // TODO Add Firebase Email-Link auth for android and web apps
    protected static FirebaseAuth AUTH;

    /**
     * @param email
     *         email of the user
     * @param password
     *         password of the user
     * @see <a href="https://firebase.google.com/docs/auth/android/password-auth">Authentication
     * for Android</a>
     */
    public static void signIn (String email, String password) {
        signIn(email, password, null);
    }

    /**
     * @param email
     *         email of the user
     * @param password
     *         password of the user
     * @see <a href="https://firebase.google.com/docs/auth/android/password-auth">Authentication
     * for Android</a>
     */
    public static void signIn (String email, String password, SignInListener listener) {
        AUTH.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.i(TAG, "signIn: successful");
                        if (listener != null) {
                            listener.successful();
                        }
                    } else {
                        Log.i(TAG, "signIn: failed", task.getException());
                        if (listener != null) {
                            listener.failed(task);
                        }
                    }
                });
    }

    public static void changePassword(Person p, String newPw, MyCompleteListener<Void> listener) {
        changePassword(p, "Walhalla1864", newPw, listener);
    }

    public static void changePassword(Person p, String oldPW,  String newPw, MyCompleteListener<Void> listener) {
        if(p == null) {
            listener.onFailure(new NullPointerException("Person cannot be null"));
        } else if (p.getEmail() == null) {
            listener.onFailure(new NullPointerException("Persons email cannot be null"));
        } else if (p.getEmail().equals("") || p.getEmail().isEmpty()) {
            listener.onFailure(new NullPointerException("Persons email cannot be empty"));
        } else {
            signIn(p.getEmail(), oldPW, success -> {
                if (success) {
                    AUTH.getCurrentUser().updatePassword(newPw).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete (@NonNull Task<Void> task) {
                            if (task.getException() == null && task.isSuccessful()) {
                                if (listener != null) {
                                    listener.onSuccess(null);
                                }
                            } else {
                                if (listener != null) {
                                    listener.onFailure(task.getException());
                                }
                            }
                        }
                    });
                }
            });
        }
    }

    /**
     * log user out of the app
     *
     * @see <a href="https://firebase.google.com/docs/auth/android/password-auth">Authentication
     * for Android</a>
     */
    public static void signOut () {
        Firebase.onlineStatus(AUTH.getUid(), false);
        AUTH.signOut();
        CacheData.clearUserData();
        changeLoggingData();
    }

    /**
     * Change Analytics and Crashlytics values used in the Console for logging errors, analytics
     * data and ad-mob data.
     */
    public static void changeLoggingData () {
        changeLoggingData(false, null);
    }

    public static void changeLoggingData(MyCompleteListener<Void> listener) {
        changeLoggingData(false, listener);
    }

    protected static void changeLoggingData (boolean isInit, MyCompleteListener<Void> listener) {
        try {
            if (AUTH == null || AUTH.getCurrentUser() == null || AUTH.getUid() == null) {
                // if no user is signed in, write "not signed in"
                Firebase.CRASHLYTICS.setUserId(Firebase.NOT_SIGNED_IN);
                Firebase.ANALYTICS.setUserId(Firebase.NOT_SIGNED_IN);
                Analytics.setRank(Rank.NONE.toString());
                if (isInit) {
                    StartActivity.newDone.firebaseDone();
                }
                if(listener != null) {
                    listener.onSuccess(null);
                }
            } else {
                // Sets the user id to the crash and analytics values.
                String uid = AUTH.getCurrentUser().getUid();
                Firestore.findUserById(uid, new MyCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess (DocumentSnapshot documentSnapshot) {
                        try {
                            Person user = documentSnapshot.toObject(Person.class);
                            user.setId(documentSnapshot.getId());
                            Messaging.getFCMToken(new MyCompleteListener<String>() {
                                @Override
                                public void onSuccess (String string) {
                                    user.setFcm_token(string);
                                    Firestore.updateFCM(string);
                                }

                                @Override
                                public void onFailure (Exception e) {
                                }
                            });
                            Firebase.CRASHLYTICS.setUserId(uid);
                            Firebase.ANALYTICS.setUserId(uid);
                            Firebase.REALTIME_DB.getReference("/online_users/" + uid).setValue(true);
                            Firebase.REALTIME_DB.getReference("/online_users/" + uid).onDisconnect().setValue(false);
                            Realtime.internet();
                            Analytics.setRank(user.getRank());
                            //Save user in cache
                            CacheData.saveUser(user);
                            CacheData.changeAnalyticsCollection(true);
                            Firestore.getUserCharge(null);
                            CacheData.getUser();
                        } catch (Exception e) {
                            Crashlytics.error(TAG, "Person could not be parsed", e);
                            //Users profile is incomplete or has some errors.
                            CacheData.setProfileError(new ProfileError(R.string.menu_profile,
                                    true, "person values contain errors"));
                            Firebase.CRASHLYTICS.setUserId(PROFILE_DATA);
                            Crashlytics.error(Authentication.TAG, PROFILE_DATA, e);
                            Firebase.ANALYTICS.setUserId(PROFILE_DATA);
                            Analytics.setRank(Rank.ERROR.toString());
                        }
                        if (isInit) {
                            StartActivity.newDone.firebaseDone();
                        }
                        if(listener != null){
                            listener.onSuccess(null);
                        }
                    }

                    @Override
                    public void onFailure (Exception e) {
                        Log.e(TAG, "onFailure: No profile exists for that user.");
                        //TODO User profile does not exist.
                        CacheData.setProfileError(new ProfileError(R.string.menu_profile,
                                true, "person has no profile"));
                        Firebase.CRASHLYTICS.setUserId(PROFILE_DATA);
                        Firebase.ANALYTICS.setUserId(PROFILE_DATA);
                        Analytics.setRank(Rank.ERROR.toString());
                        if (isInit) {
                            StartActivity.newDone.firebaseDone();
                        }
                        if(listener != null) {
                            listener.onSuccess(null);
                        }
                    }
                });
            }
        } catch (Exception e) {
            Crashlytics.error(TAG, "changeLoggingData: ", e);
        }
        if (!isInit) {
            // ?? TODO MainActivity.authListener.statusChange();
            if(listener != null) {
                listener.onSuccess(null);
            }
        }
    }

    public static boolean isSignIn () {
        return AUTH.getUid() != null;
    }

    public static void linkGoogle (String idToken, MyCompleteListener<Void> listener) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        AUTH.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onSuccess(null);
                    }
                });
    }

    public static FirebaseUser getUser () {
        return AUTH.getCurrentUser();
    }

    public static void sendVerificationMail () {
        FirebaseUser user = AUTH.getCurrentUser();
        if (user != null) {/*
            String url = "https://b3tterTogeth3r.github.io/?verify=" + user.getUid();

            ActionCodeSettings actionCodeSettings =
                    ActionCodeSettings.newBuilder()
                            .setUrl(url)
                            .setAndroidPackageName("de.b3ttertogeth3r.walhalla",
                                    true,
                                    null)
                            .setHandleCodeInApp(true)
                            .build();
            user.sendEmailVerification(actionCodeSettings);*/
            user.sendEmailVerification();
        }
    }

    public interface SignInListener {
        default void failed (@NonNull Task<AuthResult> task) {
            // If sign failed, display a message to the user.
            Crashlytics.error(TAG, "signInWithCredential:failure", task.getException());
            Snackbar.make(MainActivity.parentLayout, "Authentication failed",
                    Snackbar.LENGTH_INDEFINITE).setAction(R.string.close, null)
                    .setActionTextColor(Color.RED).show();
            statusChange(false);
        }

        void statusChange (boolean success);

        default void successful () {
            Log.d(TAG, "successful: true");
            changeLoggingData();
            statusChange(true);
        }
    }
}
