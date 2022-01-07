package de.b3ttertogeth3r.walhalla.firebase;

import static de.b3ttertogeth3r.walhalla.firebase.Authentication.AUTH;
import static de.b3ttertogeth3r.walhalla.firebase.Firestore.FIRESTORE;
import static de.b3ttertogeth3r.walhalla.firebase.Functions.FUNCTIONS;
import static de.b3ttertogeth3r.walhalla.firebase.Storage.REFERENCE;
import static de.b3ttertogeth3r.walhalla.firebase.Storage.STORAGE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.Timestamp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Calendar;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstraction.CustomFragment;
import de.b3ttertogeth3r.walhalla.models.Semester;

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
    protected static final String NOT_SIGNED_IN = "not signed in";
    private static final String TAG = "Firebase";
    protected static FirebaseApp FIREBASE_APP;
    protected static FirebaseRemoteConfig REMOTE_CONFIG;
    protected static FirebaseCrashlytics CRASHLYTICS;
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
    protected static FirebaseAnalytics ANALYTICS;
    protected static FirebaseDatabase REALTIME_DB;
    protected static FirebaseMessaging MESSAGING;

    /**
     * Initialize Firebase Services
     *
     * @param ctx
     *         Context needed
     */
    public static void init (@NonNull @NotNull Context ctx) {
        Log.e(TAG, "init: Firebase");
        FIREBASE_APP = FirebaseApp.initializeApp(ctx);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
                SafetyNetAppCheckProviderFactory.getInstance());
        ANALYTICS = FirebaseAnalytics.getInstance(ctx);
        AUTH = FirebaseAuth.getInstance();
        CRASHLYTICS = FirebaseCrashlytics.getInstance();
        FIRESTORE = FirebaseFirestore.getInstance();
        REALTIME_DB = FirebaseDatabase
                .getInstance("https://walhalla-adfc5-default-rtdb.europe-west1.firebasedatabase" +
                        ".app/");
        REMOTE_CONFIG = FirebaseRemoteConfig.getInstance();
        REMOTE_CONFIG.setDefaultsAsync(R.xml.remote_config_defaults);
        RemoteConfig.update();
        STORAGE = FirebaseStorage.getInstance("gs://walhalla-adfc5.appspot.com");
        REFERENCE = STORAGE.getReference();

        MESSAGING = FirebaseMessaging.getInstance();

        FUNCTIONS = FirebaseFunctions.getInstance(FIREBASE_APP, "europe-west1");

        Authentication.changeLoggingData(true, null);
        //TODO remove before publishing
        //debugSettings();
    }

    private static void debugSettings () {
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setFetchTimeoutInSeconds(2)
                .setMinimumFetchIntervalInSeconds(0)
                .build();
        REMOTE_CONFIG.setConfigSettingsAsync(configSettings);
        FUNCTIONS.useEmulator("10.0.2.2", 5001);
        FIRESTORE.useEmulator("10.0.2.2", 8080);
        FirebaseFirestoreSettings firestoreSettings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        FIRESTORE.setFirestoreSettings(firestoreSettings);
        // STORAGE.useEmulator("10.0.2.2", 9199);
        AUTH.useEmulator("10.0.2.2", 9099);

        /* activate after a emulator reset
        int counter = 0;
        for (int i = 1864; i <= 2050; i++) {
            Calendar c = Calendar.getInstance();
            // WS
            c.set(i, 10, 1, 0, 0);
            Timestamp begin = new Timestamp(c.getTime());
            String oneUp = String.valueOf((i + 1)).substring(2);
            String beginStr = "Wintersemester " + i + "/" + (i + 1);
            String beginStrShort = "WS" + i + "/" + oneUp;
            i++;
            c.set(i, 3, 30, 23, 59, 59);
            Timestamp end = new Timestamp(c.getTime());
            Semester sem = new Semester(counter, begin, end, beginStr, beginStrShort);
            FIRESTORE.collection("Semester").document(String.valueOf(counter)).set(sem)
                    .addOnCompleteListener(task -> {
                        if(task.getException() != null) {
                            Log.e(TAG, "onComplete: error found", task.getException());
                            return;
                        }
                        Log.d(TAG, "onComplete: semester created");
                    });
            counter++;

            // SS
            c.set(i, 4, 1, 0, 0);
            begin = new Timestamp(c.getTime());
            beginStr = "Sommersemester " + i;
            beginStrShort = "SS" + String.valueOf(i).substring(2);
            c.set(i, 9,30, 23, 59, 59);
            end = new Timestamp(c.getTime());
            sem = new Semester(counter, begin, end, beginStr, beginStrShort);
            FIRESTORE.collection("Semester").document(String.valueOf(counter)).set(sem)
                    .addOnCompleteListener(task -> {
                        if(task.getException() != null) {
                            Log.e(TAG, "onComplete: error found", task.getException());
                            return;
                        }
                        Log.d(TAG, "onComplete: semester created");
                    });
            counter++;
        }
        /* end of semester creation */
    }

    /**
     * Sets value of Person/{userID}/online
     *
     * @param uid
     *         {userID}
     * @param status
     *         true or false
     */
    protected static void onlineStatus (String uid, boolean status) {
        FIRESTORE.collection("Person").document(uid).update("online", status);
    }
}
