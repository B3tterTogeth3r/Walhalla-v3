package de.b3ttertogeth3r.walhalla.firebase;

import static de.b3ttertogeth3r.walhalla.firebase.Firebase.CRASHLYTICS;

import android.util.Log;

import de.b3ttertogeth3r.walhalla.App;

/**
 * @see <a href="https://firebase.google.com/docs/crashlytics">Firebase Crashlytics</a>
 */
public class Crashlytics {
    private static final String TAG = "Firebase.Crashlytics";

    public static void error (String class_name, String message) {
        Log.e(class_name, message);
        CRASHLYTICS.setCustomKey("class_name", class_name);
        CRASHLYTICS.setCustomKey("error_message", message);
        sendUnsent();
    }

    public static void verbose (String class_name, String message) {
        Log.v(class_name, message);
        CRASHLYTICS.setCustomKey("class_name", class_name);
        CRASHLYTICS.setCustomKey("verbose_message", message);
        sendUnsent();
    }

    public static void sendUnsent () {
        try {
            Firebase.CRASHLYTICS.sendUnsentReports();
        } catch (Exception ignored) {
        }
    }

    public static void info (String class_name, String message) {
        Log.i(class_name, message);
        CRASHLYTICS.setCustomKey("class_name", class_name);
        CRASHLYTICS.setCustomKey("info_message", message);
        sendUnsent();
    }

    public static void warn (String class_name, String message) {
        Log.w(class_name, message);
        CRASHLYTICS.setCustomKey("class_name", class_name);
        CRASHLYTICS.setCustomKey("warn_message", message);
        sendUnsent();
    }

    public static void error (String class_name, String message, Throwable e) {
        Log.e(class_name, message, e);
        CRASHLYTICS.setCustomKey("class_name", class_name);
        CRASHLYTICS.setCustomKey("error_message", message);
        CRASHLYTICS.recordException(e);
        sendUnsent();
    }

    public static void warn (String class_name, String message, Throwable e) {
        Log.w(class_name, message, e);
        CRASHLYTICS.setCustomKey("class_name", class_name);
        CRASHLYTICS.setCustomKey("warn_message", message);
        CRASHLYTICS.recordException(e);
        sendUnsent();
    }

    public static void info (String class_name, String message, Throwable e) {
        Log.i(class_name, message, e);
        CRASHLYTICS.setCustomKey("class_name", class_name);
        CRASHLYTICS.setCustomKey("info_message", message);
        CRASHLYTICS.recordException(e);
        sendUnsent();
    }

    public static void verbose (String class_name, String message, Throwable e) {
        Log.v(class_name, message, e);
        CRASHLYTICS.setCustomKey("class_name", class_name);
        CRASHLYTICS.setCustomKey("verbose_message", message);
        CRASHLYTICS.recordException(e);
        sendUnsent();
    }

    public static void debug (String class_name, String message, Throwable e) {
        Log.d(class_name, message, e);
        CRASHLYTICS.setCustomKey("class_name", class_name);
        CRASHLYTICS.setCustomKey("debug_message", message);
        CRASHLYTICS.recordException(e);
        sendUnsent();
    }

    public static void debug (String class_name, String message) {
        Log.d(class_name, message);
        CRASHLYTICS.setCustomKey("class_name", class_name);
        CRASHLYTICS.setCustomKey("debug_message", message);
        sendUnsent();
    }
}
