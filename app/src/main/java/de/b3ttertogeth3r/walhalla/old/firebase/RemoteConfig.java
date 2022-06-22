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

import android.util.Log;

import de.b3ttertogeth3r.walhalla.old.enums.Page;
import de.b3ttertogeth3r.walhalla.old.utils.CacheData;
import de.b3ttertogeth3r.walhalla.old.utils.FormatJSON;

/**
 * @see <a href="https://firebase.google.com/docs/remote-config/get-started?platform=android">Remote
 * Config</a>
 */
public class RemoteConfig {
    private static final String TAG = "RemoteConfig";

    /**
     * load the remote config data in the background after the app already has started.
     */
    public static void apply () {
        Firebase.REMOTE_CONFIG.activate().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean updated = task.getResult();
                Log.d(TAG, "init: Config params updated: " + updated);
                CacheData.setCurrentSemester((int) Firebase.REMOTE_CONFIG.getDouble(
                        "current_semester_id"));

                Thread[] threadList = new Thread[7];
                threadList[0] = new Thread(
                        new FormatJSON(Page.ROOM,
                                Firebase.REMOTE_CONFIG.getString(Page.ROOM.getName())));
                threadList[0].setName("download_rooms");

                threadList[1] = new Thread(
                        new FormatJSON(Page.OWN_HISTORY,
                                Firebase.REMOTE_CONFIG.getString(Page.OWN_HISTORY.getName())));
                threadList[1].setName("download_short_history");

                threadList[2] = new Thread(
                        new FormatJSON(Page.ABOUT_US,
                                Firebase.REMOTE_CONFIG.getString(Page.ABOUT_US.getName())));
                threadList[2].setName("download_about_us");

                threadList[3] = new Thread(
                        new FormatJSON(Page.FRAT_GER,
                                Firebase.REMOTE_CONFIG.getString(Page.FRAT_GER.getName())));
                threadList[3].setName("download_about_us");

                threadList[4] = new Thread(
                        new FormatJSON(Page.FRAT_WUE,
                                Firebase.REMOTE_CONFIG.getString(Page.FRAT_WUE.getName())));
                threadList[4].setName("download_about_us");

                threadList[5] = new Thread(
                        new FormatJSON(Page.SEMESTER_NOTES,
                                Firebase.REMOTE_CONFIG.getString(Page.SEMESTER_NOTES.getName())));
                threadList[5].setName("download_semester_notes");
                threadList[6] = new Thread(
                        new FormatJSON(Page.CHARGEN_DESCRIPTION,
                                Firebase.REMOTE_CONFIG.getString(Page.CHARGEN_DESCRIPTION.getName())));
                threadList[6].setName("download_chargen_description");

                for (Thread t : threadList) {
                    try {
                        //Log.e(TAG, "thread: starting " + t.getName());
                        t.start();
                    } catch (Exception e) {
                        Crashlytics.error(TAG, "starting remote config thread unable to start",
                                e);
                    }
                }
            } else {
                Crashlytics.error(TAG, "Fetching remote config data failed", task.getException());
            }
        });
    }

    public static void update () {
        Firebase.REMOTE_CONFIG.fetch();
    }
}
