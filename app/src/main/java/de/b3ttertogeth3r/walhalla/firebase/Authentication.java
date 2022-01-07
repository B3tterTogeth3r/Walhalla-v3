package de.b3ttertogeth3r.walhalla.firebase;

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

import de.b3ttertogeth3r.walhalla.MainActivity;
import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.SignInActivity;
import de.b3ttertogeth3r.walhalla.StartActivity;
import de.b3ttertogeth3r.walhalla.abstraction.CustomFragment;
import de.b3ttertogeth3r.walhalla.enums.Rank;
import de.b3ttertogeth3r.walhalla.exceptions.PersonException;
import de.b3ttertogeth3r.walhalla.interfaces.AuthListener;
import de.b3ttertogeth3r.walhalla.interfaces.CustomFirebaseCompleteListener;
import de.b3ttertogeth3r.walhalla.models.Person;
import de.b3ttertogeth3r.walhalla.models.ProfileError;
import de.b3ttertogeth3r.walhalla.utils.CacheData;

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

    public static void changeLoggingData(CustomFirebaseCompleteListener listener) {
        changeLoggingData(false, listener);
    }

    protected static void changeLoggingData (boolean isInit, CustomFirebaseCompleteListener listener) {
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
                    listener.onSuccess();
                }
            } else {
                // Sets the user id to the crash and analytics values.
                String uid = AUTH.getCurrentUser().getUid();
                Firestore.findUserById(uid, new CustomFirebaseCompleteListener() {
                    @Override
                    public void onSuccess (DocumentSnapshot documentSnapshot) {
                        try {
                            Person user = documentSnapshot.toObject(Person.class);
                            user.setId(documentSnapshot.getId());
                            Messaging.getFCMToken(new CustomFirebaseCompleteListener() {
                                @Override
                                public void onSuccess (String string) {
                                    user.setFcm_token(string);
                                    Firestore.uploadPerson(user);
                                }

                                @Override
                                public void onFailure (Exception e) {
                                }
                            });
                            Firebase.CRASHLYTICS.setUserId(uid);
                            Firebase.ANALYTICS.setUserId(uid);
                            Firebase.REALTIME_DB.getReference("/online_users/" + uid).setValue(true);
                            Firebase.REALTIME_DB.getReference("/online_users/" + uid).onDisconnect().setValue(false);
                            Realtime.internet(uid);
                            Analytics.setRank(user.getRank());
                            //Save user in cache
                            CacheData.saveUser(user);
                            CacheData.changeAnalyticsCollection(true);
                            Firestore.getUserCharge(null);
                            try {
                                CacheData.getUser();
                            } catch (PersonException pe) {
                                CacheData.setProfileError(new ProfileError(R.string.menu_profile,
                                        true, "profile incomplete"));
                            }
                        } catch (Exception e) {
                            Crashlytics.log(TAG, "Person could not be parsed", e);
                            //Users profile is incomplete or has some errors.
                            CacheData.setProfileError(new ProfileError(R.string.menu_profile,
                                    true, "person values contain errors"));
                            Firebase.CRASHLYTICS.setUserId(PROFILE_DATA);
                            Firebase.ANALYTICS.setUserId(PROFILE_DATA);
                            Analytics.setRank(Rank.ERROR.toString());
                        }
                        if (isInit) {
                            StartActivity.newDone.firebaseDone();
                        }
                        if(listener != null){
                            listener.onSuccess();
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
                            listener.onSuccess();
                        }
                    }
                });
            }
        } catch (Exception e) {
            Log.e(TAG, "changeLoggingData: ", e);
        }
        if (!isInit) {
            // ?? TODO MainActivity.authListener.statusChange();
            if(listener != null) {
                listener.onSuccess();
            }
        }
    }

    public static boolean isSignIn () {
        return AUTH.getUid() != null;
    }

    public static void linkGoogle (String idToken, CustomFirebaseCompleteListener listener) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        AUTH.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete (@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            listener.onSuccess();

                        }
                    }
                });
    }

    public static FirebaseUser getUser () {
        return AUTH.getCurrentUser();
    }

    public static void setCustomAuthListener (AuthListener listener) {
        AUTH.addIdTokenListener((FirebaseAuth.IdTokenListener) firebaseAuth -> listener.statusChange());
    }

    public interface SignInListener {
        default void failed (@NonNull Task<AuthResult> task) {
            // If sign failed, display a message to the user.
            Crashlytics.log(TAG, "signInWithCredential:failure", task.getException());
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
            MainActivity.authListener.statusChange();
        }
    }
}
