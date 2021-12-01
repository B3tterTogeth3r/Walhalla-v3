package de.b3ttertogeth3r.walhalla.firebase;

import static com.google.firebase.analytics.FirebaseAnalytics.UserProperty.ALLOW_AD_PERSONALIZATION_SIGNALS;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.OAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.b3ttertogeth3r.walhalla.App;
import de.b3ttertogeth3r.walhalla.MainActivity;
import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.StartActivity;
import de.b3ttertogeth3r.walhalla.enums.Charge;
import de.b3ttertogeth3r.walhalla.enums.Rank;
import de.b3ttertogeth3r.walhalla.exceptions.InvalidFirestorePathException;
import de.b3ttertogeth3r.walhalla.exceptions.NoUploadDataException;
import de.b3ttertogeth3r.walhalla.exceptions.PersonException;
import de.b3ttertogeth3r.walhalla.fragments.signin.Fragment;
import de.b3ttertogeth3r.walhalla.interfaces.OnGetDataListener;
import de.b3ttertogeth3r.walhalla.models.Account;
import de.b3ttertogeth3r.walhalla.models.Image;
import de.b3ttertogeth3r.walhalla.models.Person;
import de.b3ttertogeth3r.walhalla.models.ProfileError;
import de.b3ttertogeth3r.walhalla.utils.CacheData;
import de.b3ttertogeth3r.walhalla.utils.Format;

/**
 * A collection of all the uses Firebase services and the functions used in this android app.
 *
 * @author B3tterTogeth3r
 * @version 1.0
 * @see <a href="https://firebase.google.com/docs/crashlytics">Crashlytics</a>
 * @see <a href="https://firebase.google.com/docs/auth/android/password-auth">Authentication</a>
 * @see <a href="https://firebase.google.com/docs/analytics">Analytics</a>
 * @see <a href="https://firebase.google.com/docs/database/android/start">Realtime Database</a>
 * @see <a href="https://firebase.google.com/docs/firestore/quickstart">Cloud Firestore</a>
 * @see <a href="https://firebase.google.com/docs/remote-config/get-started?platform=android">Remote
 * Config</a>
 * @see <a href="https://firebase.google.com/docs/storage">Storage</a>
 * @since 1.0
 */
@SuppressLint("StaticFieldLeak")
public class Firebase {
    private static final String TAG = "Firebase";
    private static final String NOT_SIGNED_IN = "not signed in";
    public static FirebaseFirestore FIRESTORE;
    public static FirebaseRemoteConfig REMOTE_CONFIG;
    public static FirebaseAuth AUTH;
    private static FirebaseCrashlytics CRASHLYTICS;
    /**
     * to activate analytics debug write in terminal with only one device connected:
     * <ul>
     *     <li>
     *         MacOs:<br>
     *             <u>connect:</u><br>
     *             <code>adb devices</code><br>
     *             <code>adb shell setprop debug.firebase.analytics.app
     *             de.b3ttertogeth3r.walhalla</code><br>
     *             <u>disconnect:</u><br>
     *             <code>adb shell setprop debug.firebase.analytics.app .none.</code>
     *     </li>
     * </ul>
     */
    private static FirebaseAnalytics ANALYTICS;
    private static FirebaseDatabase REALTIME_DB;
    private static FirebaseStorage STORAGE;
    private static FirebaseMessaging MESSAGING;

    public static void init (@NonNull @NotNull Context ctx) {
        Log.e(TAG, "init: Firebase");
        FirebaseApp.initializeApp(ctx);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
                SafetyNetAppCheckProviderFactory.getInstance());
        ANALYTICS = FirebaseAnalytics.getInstance(ctx);
        AUTH = FirebaseAuth.getInstance();
        CRASHLYTICS = FirebaseCrashlytics.getInstance();
        REALTIME_DB = FirebaseDatabase
                .getInstance("https://walhalla-adfc5-default-rtdb.europe-west1.firebasedatabase" +
                        ".app/");
        FIRESTORE = FirebaseFirestore.getInstance();
        REMOTE_CONFIG = FirebaseRemoteConfig.getInstance();
        //TODO remove before publishing
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setFetchTimeoutInSeconds(2)
                .setMinimumFetchIntervalInSeconds(0)
                .build();
        REMOTE_CONFIG.setConfigSettingsAsync(configSettings);
        REMOTE_CONFIG.setDefaultsAsync(R.xml.remote_config_defaults);
        REMOTE_CONFIG.fetchAndActivate().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean updated = task.getResult();
                Log.d(TAG, "init: Config params updated: " + updated);
                CacheData.setCurrentSemester((int) REMOTE_CONFIG.getDouble("current_semester_id"));
            } else {
                Crashlytics.log("Fetching remote config data failed", task.getException());
            }
        });
        STORAGE = FirebaseStorage.getInstance("gs://walhalla-adfc5.appspot.com");

        MESSAGING = FirebaseMessaging.getInstance();

        Authentication.changeLoggingData(true);
    }

    /**
     * Sets value of Person/{userID}/online
     *
     * @param uid
     *         {userID}
     * @param status
     *         true or false
     */
    private static void onlineStatus (String uid, boolean status) {
        FIRESTORE.collection("Person").document(uid).update("online", status);
    }

    // region Firebase Remote Config

    /**
     * @see
     * <a href="https://firebase.google.com/docs/remote-config/get-started?platform=android">Remote
     * Config</a>
     */
    public static class RemoteConfig {
        public static void apply () {
            REMOTE_CONFIG.fetchAndActivate();
        }

        public static void update () {
            REMOTE_CONFIG.fetchAndActivate();
        }
    }
    // endregion

    // region Firebase Cloud Storage

    /**
     * local functions for the apps Firebase storage buckets.
     *
     * @see <a href="https://firebase.google.com/docs/storage">Storage</a>
     */
    public static class Storage {
        private static final String TAG = "Storage";
        /** Reference to the root path */
        private static final StorageReference REFERENCE = STORAGE.getReference();
        private static final long ONE_MEGABYTE = 1024 * 1024;

        @NonNull
        public static Task<Uri> getUri (String imagePath) {
            return REFERENCE.child(imagePath).getDownloadUrl();
        }

        /**
         * Download an image from the {@link Storage Image Storage Bukket}
         *
         * @param image
         *         Image value to download from
         * @return Bitmap of the image
         */
        @NonNull
        public static Task<byte[]> downloadImage (@NonNull Image image) {
            String path = image.getLarge_path();
            return REFERENCE.child(path).getBytes(ONE_MEGABYTE);
        }

        /**
         * Upload an image into the recipe bucket of the firebase storage.
         *
         * @param recipeBitmap
         *         Bitmap of the file
         * @param name
         *         name of the file to upload
         */
        public static void uploadRecipe (Bitmap recipeBitmap, String name) {
            String imageName = Format.imageName(name);
            REFERENCE.child("recipe/" + imageName)
                    .putBytes(compressImage(recipeBitmap))
                    .addOnSuccessListener(taskSnapshot -> Crashlytics.log(TAG + ":onSuccess: " +
                            "upload of image " + imageName + "complete."))
                    .addOnFailureListener(e -> Crashlytics.log(TAG + ":onFailure: upload of image" +
                            " " + imageName + " failed.", e));
        }

        // region private functions
        @NonNull
        private static byte[] compressImage (@NonNull Bitmap original) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            original.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            return baos.toByteArray();
        }

        public static void uploadImage (Bitmap imageBitmap, String name,
                                        OnGetDataListener listener) {
            String imageName = Format.imageName(name);
            REFERENCE.child("image/" + imageName)
                    .putBytes(compressImage(imageBitmap))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess (@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            listener.onSuccess(taskSnapshot.getUploadSessionUri());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure (@NonNull Exception e) {

                        }
                    });
        }

        /**
         * Download the requested recipe
         *
         * @param recipe_name
         *         the name of the recipe to download
         * @return bitmap value of the recipe
         */
        public static Bitmap downloadRecipe (String recipe_name) {
            final Bitmap[] result = {null};
            REFERENCE.child("images" + recipe_name).getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
                // Data for "images/island.jpg" is returns, use this as needed
                result[0] = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            }).addOnFailureListener(exception -> {
                // Handle any errors
                Crashlytics.log(TAG + ":downloadImage:onFailure: download failed", exception);
            });

            while (result[0] == null) {
                // wait
            }
            return result[0];
        }

        /**
         * Upload the protocol file of a specific meeting
         */
        public static void uploadProtocol () {
            // TODO: upload protocol of a meeting only as a pdf file
        }

        /**
         * download the protocol file of a specific meeting.
         *
         * @param protocol_name
         *         name of the file
         */
        public static void downloadProtocol (String protocol_name) {
            // TODO: download protocol of a meeting, if it has a pdf file.
        }
        // endregion
    }
    // endregion

    // region Firebase Cloud Firestore

    /**
     * @see <a href="https://firebase.google.com/docs/firestore/quickstart">Cloud Firestore</a>
     */
    public static class Firestore {
        private static final String TAG = "Firestore";

        @NonNull
        public static DocumentReference getSites (String documentName) {
            return FIRESTORE.collection("Sites").document(documentName);
        }

        @NonNull
        public static CollectionReference getImages () {
            return FIRESTORE.collection("pictures");
        }

        public static void getSemester (int semesterID, OnGetDataListener listener) {
            getSemester(semesterID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete (@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot ds = task.getResult();
                    if (!ds.exists()) {
                        listener.onFailure(task.getException());
                    }
                    listener.onSuccess(ds);
                }
            });
        }

        @NonNull
        public static DocumentReference getSemester (int semesterID) {
            if (semesterID < 0) {
                semesterID = semesterID * (-1);
            }
            Log.d(TAG, "getSemester: " + semesterID);
            return FIRESTORE.collection("Semester").document(String.valueOf(semesterID));
        }

        @NonNull
        public static Task<DocumentReference> upload (CollectionReference collectionReference,
                                                      Object uploadObject)
                throws InvalidFirestorePathException, NoUploadDataException {
            if (collectionReference == null || collectionReference.getPath().isEmpty()) {
                throw new InvalidFirestorePathException(collectionReference);
            }
            if (uploadObject == null) {
                throw new NoUploadDataException();
            }
            return collectionReference.add(uploadObject);
        }

        @NonNull
        public static Task<Void> upload (DocumentReference documentReference,
                                         ArrayList<Map<String, Object>> uploadData)
                throws InvalidFirestorePathException, NoUploadDataException {
            if (documentReference == null || documentReference.getPath().isEmpty()) {
                throw new InvalidFirestorePathException(documentReference);
            }
            if (uploadData == null || uploadData.isEmpty()) {
                throw new NoUploadDataException();
            }
            // format arraylist into map
            Map<String, Object> data = new HashMap<>();
            for (int i = 0; i < uploadData.size(); i++) {
                if (!uploadData.get(i).isEmpty()) {
                    data.put(String.valueOf(i), uploadData.get(i));
                }
            }
            return documentReference.set(data);
        }

        public static void findUserById (String uid, OnGetDataListener listener) {
            FIRESTORE.collection("Person")
                    .whereEqualTo("uid", uid)
                    .limit(1)
                    .get()
                    .addOnSuccessListener(documentSnapshots -> {
                        if (documentSnapshots.isEmpty()) {
                            listener.onFailure();
                            return;
                        }
                        for (DocumentSnapshot ds : documentSnapshots.getDocuments()) {
                            listener.onSuccess(ds);
                            return;
                        }
                    });
        }

        public static void uploadAccountEntry (String semester, Account account) {
            FIRESTORE.collection("Semester/" + semester + "/Account")
                    .add(account)
                    .addOnSuccessListener(documentReference -> {
                        Log.d(TAG, "uploadAccountEntry: success: " + semester);
                    })
                    .addOnFailureListener(e -> Log.e(TAG,
                            "uploadAccountEntry: failure: semester" + semester, e));
        }

        public static void editAccountEntry (String semester_id, Account account) {

        }

        @NonNull
        public static CollectionReference loadSemesterEvents (@NonNull @NotNull String semester_id) {
            return FIRESTORE.collection("Semester")
                    .document(semester_id)
                    .collection("Events");
        }

        public static void uploadPerson (@NonNull Person person) {
            if (person.getId() != null) {
                FIRESTORE.collection("Person").document(person.getId())
                        .set(person)
                        .addOnCompleteListener(task -> Log.d(TAG, "userUpdate: success"))
                        .addOnFailureListener(e -> Firebase.Crashlytics.log(TAG, "onFailure: " +
                                "update user did not work", e));
            } else {
                FIRESTORE.collection("Person").document().set(person)
                        .addOnCompleteListener(task -> Log.d(TAG, "userUpdate: success"))
                        .addOnFailureListener(e -> Firebase.Crashlytics.log(TAG, "onFailure: " +
                                "update user did not work", e));
            }
        }

        public static void findUserCharge (OnGetDataListener listener) {
            if(AUTH.getUid() == null || AUTH.getUid().isEmpty()){
                listener.onFailure();
            }
            FIRESTORE.collection("Current")
                    .whereArrayContains("UID", AUTH.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshots -> {
                        if(documentSnapshots.isEmpty()){
                            listener.onFailure();
                            return;
                        }
                        for(QueryDocumentSnapshot qds : documentSnapshots){
                            String id = qds.getId();
                            if(id.equals(Charge.ADMIN.getName())) {
                                listener.onSuccess(id);
                                break;
                            } else {
                                listener.onSuccess(id);
                            }
                        }
                    });
        }
    }
    // endregion

    //region Firebase Realtime Database

    /**
     * @see <a href="https://firebase.google.com/docs/database/android/start">Realtime Database</a>
     */
    public static class Realtime {
        private static final String TAG = "Realtime";

        /**
         * Sets a {@link App#isInternetConnection() boolean} for the internet connection of the
         * app.
         *
         * @see
         * <a href="https://firebase.google.com/docs/database/android/offline-capabilities#section-connection-state">Connection
         * State</a>
         * @since 1.0
         */
        public static void internet (String uid) {
            REALTIME_DB.getReference("/checker/internet").onDisconnect().setValue("true", (error,
                                                                                           ref) -> {
                // set value of Person/{userID}/online to false
                Firebase.onlineStatus(uid, false);
            });

            DatabaseReference connectedRef = REALTIME_DB.getReference(".info/connected");
            connectedRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange (@NonNull DataSnapshot snapshot) {
                    boolean connected = false;
                    try {
                        //noinspection ConstantConditions
                        connected = snapshot.getValue(Boolean.class);
                    } catch (Exception e) {
                        Log.d(TAG, "not connected");
                        App.setInternetConnection(false);
                    }
                    if (connected) {
                        Log.d(TAG, "connected");
                        App.setInternetConnection(true);
                        // set value of Person/{userID}/online to true
                        Firebase.onlineStatus(uid, true);
                    } else {
                        Log.d(TAG, "not connected");
                        App.setInternetConnection(false);
                    }
                }

                @Override
                public void onCancelled (@NonNull DatabaseError error) {
                    Log.w(TAG, "Listener was cancelled");
                }
            });
        }
    }
    // endregion

    // region Google Analytics via Firebase

    /**
     * @see <a href="https://firebase.google.com/docs/analytics">Analytics</a>
     */
    public static class Analytics {
        public static final String TAG = "Analytics";

        /**
         * Log the used sites of the user to see which sites are used more often and which are not
         * to enable better usage of ads on the pages.
         *
         * @param menu_id
         *         id of the clicked menu item
         * @param fragment_name
         *         name of the fragment the user opened
         */
        public static void screenChange (int menu_id, String fragment_name) {
            if (!App.lastSiteId.empty() && App.lastSiteId.peek() != menu_id) {
                App.lastSiteId.push(menu_id);
            }
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, String.valueOf(menu_id));
            bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "fragment." + fragment_name);
            ANALYTICS.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
        }

        /**
         * Set the users custom start page as user property
         */
        public static void changeStartPage () {
            ANALYTICS.setUserProperty("start_page", String.valueOf(CacheData.getStartPage()));
        }

        /**
         * @see
         * <a href="https://firebase.google.com/docs/analytics/configure-data-collection?platform=android">Data
         * Collection</a>
         */
        public static void changeDataCollection () {
            boolean value = CacheData.getAnalyticsCollection();
            CacheData.ChangeAnalyticsCollection(value);
            MESSAGING.setAutoInitEnabled(value);
            ANALYTICS.setUserProperty(ALLOW_AD_PERSONALIZATION_SIGNALS, "" + value);
            ANALYTICS.setAnalyticsCollectionEnabled(value);
        }

        public static void setRank (String rank) {
            ANALYTICS.setUserProperty(FirebaseAnalytics.Param.GROUP_ID, rank);
        }
    }
    // endregion

    // region Google Crashlytics via Firebase

    /**
     * @see <a href="https://firebase.google.com/docs/crashlytics">Firebase Crashlytics</a>
     */
    public static class Crashlytics {
        private static final String TAG = "Firebase.Crashlytics";

        public static void log (String message) {
            try {
                Log.e(TAG, "recordException: " + message);
                CRASHLYTICS.log(message);
            } catch (Exception ignored) {
            }
        }

        public static void log (String message, Throwable e) {
            try {
                Log.e(TAG, "recordException: " + message, e);
                CRASHLYTICS.log(message);
                CRASHLYTICS.recordException(e);
            } catch (Exception ignored) {
            }
        }

        public static void log (String TAG, int resid) {
            log(TAG, App.getContext().getString(resid));
        }

        public static void log (String TAG, String message) {
            try {
                String error = "recordException: " + TAG + ":" + message;
                Log.e(TAG, error);
                CRASHLYTICS.log(error);
            } catch (Exception ignored) {
            }
        }

        public static void log (String TAG, String message, Throwable e) {
            try {
                String error = "recordException: " + TAG + ":" + message;
                Log.e(TAG, error, e);
                CRASHLYTICS.log(error);
                CRASHLYTICS.recordException(e);
            } catch (Exception ignored) {
            }
        }

        public static void sendUnsent () {
            try {
                CRASHLYTICS.sendUnsentReports();
            } catch (Exception ignored) {

            }
        }
    }
    // endregion

    // region Google Authentication via Firebase

    /**
     * @see <a href="">Firebase Authentication</a>
     */
    @SuppressWarnings("ConstantConditions")
    public static class Authentication {
        public static final String PROFILE_DATA = "Missing profile data";
        private static final String TAG = "Auth";
        // TODO Add Firebase Email-Link auth for android and web apps

        /**
         * @param email
         *         email of the user
         * @param password
         *         password of the user
         * @see <a href="https://firebase.google.com/docs/auth/android/password-auth">Authentication
         * for Android</a>
         */
        public static void createUser (String email, String password) {
            AUTH.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d(TAG, "createUser: onComplete: success");
                    // TODO Fire custom listener and set value of Person/{userID}/online to true
                } else {
                    Log.d(TAG, "createUser: onComplete: failure", task.getException());
                    Crashlytics.log(TAG + "createUser: onComplete: failure", task.getException());
                    Snackbar.make(MainActivity.parentLayout, "Authentication failed",
                            Snackbar.LENGTH_SHORT).show();
                    // TODO Fire custom listener with "null" value
                }
            });
        }

        /**
         * @param email
         *         email of the user
         * @param password
         *         password of the user
         * @see <a href="https://firebase.google.com/docs/auth/android/password-auth">Authentication
         * for Android</a>
         */
        public static void signIn (String email, String password) {
            AUTH.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Fragment.signInListener.statusChange(true);
                        } else {
                            Fragment.signInListener.failed(task);
                        }
                    });
        }

        public static void firebaseAuthWithGoogle (String idToken) {
            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
            AUTH.signInWithCredential(credential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Fragment.signInListener.successful();
                        } else {
                            Fragment.signInListener.failed(task);
                        }
                    });
        }

        public static void firebaseAuthWithTwitter (Activity activity,
                                                    OAuthProvider.Builder provider) {
            firebaseCustomAuth(activity, provider);
        }

        protected static void firebaseCustomAuth (Activity activity,
                                                  OAuthProvider.Builder provider) {
            Task<AuthResult> pendingResultTask = Firebase.AUTH.getPendingAuthResult();
            if (pendingResultTask != null) {
                // There's something already here! Finish the sign-in for your user.
                pendingResultTask
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // User is signed in.
                                // IdP data available in
                                // authResult.getAdditionalUserInfo().getProfile().
                                // The OAuth access token can also be retrieved:
                                // authResult.getCredential().getAccessToken().
                                // The OAuth secret can be retrieved by calling:
                                // authResult.getCredential().getSecret().
                                Fragment.signInListener.successful();
                            } else {
                                Fragment.signInListener.failed(task);
                            }
                        });
            } else {
                // There's no pending result so you need to start the sign-in flow.
                Firebase.AUTH
                        .startActivityForSignInWithProvider(activity, provider.build())
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // User is signed in.
                                // IdP data available in
                                // authResult.getAdditionalUserInfo().getProfile().
                                // The OAuth access token can also be retrieved:
                                // authResult.getCredential().getAccessToken().
                                // The OAuth secret can be retrieved by calling:
                                // authResult.getCredential().getSecret().
                                Fragment.signInListener.successful();
                            } else {
                                Fragment.signInListener.failed(task);
                            }
                        });
            }
        }

        public static void firebaseAuthWithGithub (Activity activity,
                                                   OAuthProvider.Builder provider) {
            firebaseCustomAuth(activity, provider);
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
            changeLoggingData();
        }

        /**
         * Change Analytics and Crashlytics values used in the Console for logging errors, analytics
         * data and ad-mob data.
         */
        public static void changeLoggingData () {
            changeLoggingData(false);
        }

        private static void changeLoggingData (boolean isInit) {
            try {
                if (AUTH == null || AUTH.getCurrentUser() == null || AUTH.getUid() == null) {
                    // if no user is signed in, write "not signed in"
                    CRASHLYTICS.setUserId(NOT_SIGNED_IN);
                    ANALYTICS.setUserId(NOT_SIGNED_IN);
                    Analytics.setRank(Rank.NONE.toString());
                    if (isInit) {
                        StartActivity.newDone.firebaseDone();
                    }
                } else {
                    // Sets the user id to the crash and analytics values.
                    String uid = AUTH.getCurrentUser().getUid();
                    Firestore.findUserById(uid, new OnGetDataListener() {
                        @Override
                        public void onSuccess (DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                try {
                                    Person user = documentSnapshot.toObject(Person.class);
                                    Messaging.getFCMToken(new OnGetDataListener() {
                                        @Override
                                        public void onSuccess (String string) {
                                            user.setFcm_token(string);
                                            Firebase.Firestore.uploadPerson(user);
                                        }
                                    });
                                    CRASHLYTICS.setUserId(uid);
                                    ANALYTICS.setUserId(uid);
                                    REALTIME_DB.getReference("/online_users/" + uid).setValue(true);
                                    REALTIME_DB.getReference("/online_users/" + uid).onDisconnect().setValue(false);
                                    Realtime.internet(uid);
                                    Analytics.setRank(user.getRank());
                                    //Save user in cache
                                    user.setId(documentSnapshot.getId());
                                    CacheData.saveUser(user);
                                    try {
                                        CacheData.getUser();
                                    } catch (PersonException pe) {
                                        CacheData.setProfileError(new ProfileError(R.string.menu_profile, true, "profile incomplete"));
                                    }
                                } catch (Exception e) {
                                    Crashlytics.log(TAG, "Person could not be parsed", e);
                                    //Users profile is incomplete or has some errors.
                                    CacheData.setProfileError(new ProfileError(R.string.menu_profile, true, "person values contain errors"));
                                    CRASHLYTICS.setUserId(PROFILE_DATA);
                                    ANALYTICS.setUserId(PROFILE_DATA);
                                    Analytics.setRank(Rank.ERROR.toString());
                                }
                                if (isInit) {
                                    StartActivity.newDone.firebaseDone();
                                }
                            }
                        }

                        @Override
                        public void onFailure () {
                            Log.e(TAG, "onFailure: No profile exists for that user.");
                            //TODO User profile does not exist.
                            CacheData.setProfileError(new ProfileError(R.string.menu_profile,
                                    true, "person has no profile"));
                            CRASHLYTICS.setUserId(PROFILE_DATA);
                            ANALYTICS.setUserId(PROFILE_DATA);
                            Analytics.setRank(Rank.ERROR.toString());
                            if (isInit) {
                                StartActivity.newDone.firebaseDone();
                            }
                        }
                    });
                }
            } catch (Exception e) {
                Log.e(TAG, "changeLoggingData: ", e);
            }
        }

        public static boolean isSignIn () {
            return AUTH.getUid() != null;
        }

        public static void updateProfileData (Uri imageUri, String name, String email) {
            FirebaseUser user = AUTH.getCurrentUser();
            if (user == null) {
                return;
            }
            UserProfileChangeRequest.Builder profileUpdates =
                    new UserProfileChangeRequest.Builder();
            profileUpdates.setDisplayName(name);
            if (imageUri != null) {
                profileUpdates.setPhotoUri(imageUri);
            }
            user.updateEmail(email).addOnCompleteListener(task ->
                    user.updateProfile(profileUpdates.build())
                            .addOnCompleteListener(task2 ->
                                    Authentication.changeLoggingData()));
        }

        public static FirebaseUser getUser () {
            return AUTH.getCurrentUser();
        }

        public interface SignInListener {
            default void failed (@NonNull Task<AuthResult> task) {
                // If sign failed, display a message to the user.
                Crashlytics.log(TAG, "signInWithCredential:failure", task.getException());
                Snackbar.make(MainActivity.parentLayout, "Authentication failed",
                        Snackbar.LENGTH_SHORT).show();
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
    // endregion

    //region Firebase Messaging
    public static class Messaging {
        private static final String TAG = "Messaging";

        public static void getFCMToken (OnGetDataListener listener) {
            MESSAGING.getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                @Override
                public void onComplete (@NonNull Task<String> task) {
                    if (!task.isSuccessful()) {
                        listener.onFailure(task.getException());
                        return;
                    }

                    // Get new FCM registration token
                    String token = task.getResult();

                    // Log and toast
                    Log.d(TAG, token);

                    listener.onSuccess(token);
                }
            });
        }
    }
    //endregion
}
