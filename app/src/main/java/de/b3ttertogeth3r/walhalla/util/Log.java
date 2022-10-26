/*
 * Copyright (c) 2022-2022.
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

package de.b3ttertogeth3r.walhalla.util;

import de.b3ttertogeth3r.walhalla.firebase.Crashlytics;

public class Log implements de.b3ttertogeth3r.walhalla.interfaces.ILog {
    public static void e(String TAG, String message) {
        Crashlytics c = new Crashlytics();
        c.log(ERROR, TAG, message, null);
        log(ERROR, TAG, message, null);
    }

    private static void log(@de.b3ttertogeth3r.walhalla.annotation.Log int type, String TAG, String message, Throwable e) {
        switch (type) {
            case VERBOSE:
                android.util.Log.v(TAG, message, e);
                break;
            case DEBUG:
                android.util.Log.d(TAG, message, e);
                break;
            case ERROR:
                android.util.Log.e(TAG, message, e);
                break;
            case WARN:
                android.util.Log.w(TAG, message, e);
                break;
            case INFO:
            default:
                android.util.Log.i(TAG, message, e);
                break;

        }
    }

    public static void e(String TAG, String message, Throwable e) {
        Crashlytics c = new Crashlytics();
        c.log(ERROR, TAG, message, e);
        log(ERROR, TAG, message, e);
    }

    public static void v(String TAG, String message) {
        Crashlytics c = new Crashlytics();
        c.log(VERBOSE, TAG, message, null);
        log(VERBOSE, TAG, message, null);

    }

    public static void v(String TAG, String message, Throwable e) {
        Crashlytics c = new Crashlytics();
        c.log(VERBOSE, TAG, message, e);
        log(VERBOSE, TAG, message, e);

    }

    public static void w(String TAG, String message) {
        Crashlytics c = new Crashlytics();
        c.log(WARN, TAG, message, null);
        log(WARN, TAG, message, null);

    }

    public static void w(String TAG, String message, Throwable e) {
        Crashlytics c = new Crashlytics();
        c.log(WARN, TAG, message, e);
        log(WARN, TAG, message, e);

    }

    public static void i(String TAG, String message) {
        log(INFO, TAG, message, null);
    }

    public static void i(String TAG, String message, Throwable e) {
        log(INFO, TAG, message, e);
    }

    public static void d(String TAG, String message) {
        Crashlytics c = new Crashlytics();
        c.log(DEBUG, TAG, message, null);
        log(DEBUG, TAG, message, null);
    }

    public static void d(String TAG, String message, Throwable e) {
        Crashlytics c = new Crashlytics();
        c.log(DEBUG, TAG, message, e);
        log(DEBUG, TAG, message, e);
    }
}