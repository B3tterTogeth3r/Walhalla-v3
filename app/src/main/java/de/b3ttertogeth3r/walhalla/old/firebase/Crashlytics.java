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

import static de.b3ttertogeth3r.walhalla.old.firebase.Firebase.CRASHLYTICS;

import android.util.Log;

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
