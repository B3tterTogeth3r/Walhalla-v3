package de.b3ttertogeth3r.walhalla.firebase;

import android.util.Log;

import de.b3ttertogeth3r.walhalla.App;

/**
 * @see <a href="https://firebase.google.com/docs/crashlytics">Firebase Crashlytics</a>
 */
public class Crashlytics {
    private static final String TAG = "Firebase.Crashlytics";

    public static void log (String message) {
        try {
            Log.e(TAG, "recordException: " + message);
            Firebase.CRASHLYTICS.log(message);
        } catch (Exception ignored) {
        }
    }

    public static void log (String message, Throwable e) {
        try {
            Log.e(TAG, "recordException: " + message, e);
            Firebase.CRASHLYTICS.log(message);
            Firebase.CRASHLYTICS.recordException(e);
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
            Firebase.CRASHLYTICS.log(error);
        } catch (Exception ignored) {
        }
    }

    public static void log (String TAG, String message, Throwable e) {
        try {
            String error = "recordException: " + TAG + ":" + message;
            Log.e(TAG, error, e);
            Firebase.CRASHLYTICS.log(error);
            Firebase.CRASHLYTICS.recordException(e);
        } catch (Exception ignored) {
        }
    }

    public static void sendUnsent () {
        try {
            Firebase.CRASHLYTICS.sendUnsentReports();
        } catch (Exception ignored) {

        }
    }

    public static void log (String message, Exception exception) {
    }
}
