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

package de.b3ttertogeth3r.walhalla.firebase;

import android.content.Context;
import android.util.Log;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import de.b3ttertogeth3r.walhalla.interfaces.firebase.IInit;

public class Crashlytics implements IInit, de.b3ttertogeth3r.walhalla.object.Log.ILog {
    private static final String TAG = "Crashlytics";
    private static FirebaseCrashlytics CRASHLYTICS;

    @Override
    public boolean init (Context context) {
        try {
            CRASHLYTICS = FirebaseCrashlytics.getInstance();
            sendUnsent();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Crashlytics init error", e);
            return false;
        }
    }

    @Override
    public void log (int type, String TAG, String message, Throwable e) {
        CRASHLYTICS.setCustomKey("class_name", TAG);
        switch (type) {
            case VERBOSE:
                Crashlytics.CRASHLYTICS.setCustomKey("verbose_message", message);
                break;
            case DEBUG:
                Crashlytics.CRASHLYTICS.setCustomKey("debug_message", message);
            case ERROR:
                Crashlytics.CRASHLYTICS.setCustomKey("error_message", message);
                break;
            case WARN:
                Crashlytics.CRASHLYTICS.setCustomKey("warn_message", message);
                break;
            case INFO:
            default:
                Crashlytics.CRASHLYTICS.setCustomKey("info_message", message);
                break;
        }
        if (e != null) {
            CRASHLYTICS.recordException(e);
        }
        sendUnsent();
    }

    private void sendUnsent () {
        try {
            CRASHLYTICS.sendUnsentReports();
        } catch (Exception e) {
            Log.e(TAG, "sending unsent reports did not work", e);
        }
    }
}
