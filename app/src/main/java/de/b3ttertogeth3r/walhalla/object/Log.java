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

package de.b3ttertogeth3r.walhalla.object;

import de.b3ttertogeth3r.walhalla.firebase.Crashlytics;

public class Log {
    public static void e(String TAG, String message) {
        Crashlytics c = new Crashlytics();
        c.log(ILog.ERROR, TAG, message, null);
        de.b3ttertogeth3r.walhalla.util.Log l = new de.b3ttertogeth3r.walhalla.util.Log();
        l.log(ILog.ERROR, TAG, message, null);

    }

    public static void e(String TAG, String message, Throwable e) {
        Crashlytics c = new Crashlytics();
        c.log(ILog.ERROR, TAG, message, e);
        de.b3ttertogeth3r.walhalla.util.Log l = new de.b3ttertogeth3r.walhalla.util.Log();
        l.log(ILog.ERROR, TAG, message, e);

    }

    public static void v(String TAG, String message) {
        Crashlytics c = new Crashlytics();
        c.log(ILog.VERBOSE, TAG, message, null);
        de.b3ttertogeth3r.walhalla.util.Log l = new de.b3ttertogeth3r.walhalla.util.Log();
        l.log(ILog.VERBOSE, TAG, message, null);

    }

    public static void v(String TAG, String message, Throwable e) {
        Crashlytics c = new Crashlytics();
        c.log(ILog.VERBOSE, TAG, message, e);
        de.b3ttertogeth3r.walhalla.util.Log l = new de.b3ttertogeth3r.walhalla.util.Log();
        l.log(ILog.VERBOSE, TAG, message, e);

    }

    public static void w(String TAG, String message) {
        Crashlytics c = new Crashlytics();
        c.log(ILog.WARN, TAG, message, null);
        de.b3ttertogeth3r.walhalla.util.Log l = new de.b3ttertogeth3r.walhalla.util.Log();
        l.log(ILog.WARN, TAG, message, null);

    }

    public static void w(String TAG, String message, Throwable e) {
        Crashlytics c = new Crashlytics();
        c.log(ILog.WARN, TAG, message, e);
        de.b3ttertogeth3r.walhalla.util.Log l = new de.b3ttertogeth3r.walhalla.util.Log();
        l.log(ILog.WARN, TAG, message, e);

    }

    public static void i(String TAG, String message) {
        de.b3ttertogeth3r.walhalla.util.Log l = new de.b3ttertogeth3r.walhalla.util.Log();
        l.log(ILog.INFO, TAG, message, null);
    }

    public static void i(String TAG, String message, Throwable e) {
        de.b3ttertogeth3r.walhalla.util.Log l = new de.b3ttertogeth3r.walhalla.util.Log();
        l.log(ILog.INFO, TAG, message, e);
    }

    public static void d(String TAG, String message) {
        Crashlytics c = new Crashlytics();
        c.log(ILog.DEBUG, TAG, message, null);
        de.b3ttertogeth3r.walhalla.util.Log l = new de.b3ttertogeth3r.walhalla.util.Log();
        l.log(ILog.DEBUG, TAG, message, null);
    }

    public static void d(String TAG, String message, Throwable e) {
        Crashlytics c = new Crashlytics();
        c.log(ILog.DEBUG, TAG, message, e);
        de.b3ttertogeth3r.walhalla.util.Log l = new de.b3ttertogeth3r.walhalla.util.Log();
        l.log(ILog.DEBUG, TAG, message, e);
    }

    public interface ILog {
        int VERBOSE = 2;
        int DEBUG = 3;
        int INFO = 4;
        int WARN = 5;
        int ERROR = 6;

        void log(int type, String TAG, String message, Throwable e);
    }
}