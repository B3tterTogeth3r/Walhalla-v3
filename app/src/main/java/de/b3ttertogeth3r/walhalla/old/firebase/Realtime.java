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

import static de.b3ttertogeth3r.walhalla.old.firebase.Firebase.REALTIME_DB;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import de.b3ttertogeth3r.walhalla.old.App;
import de.b3ttertogeth3r.walhalla.old.utils.MyLog;

/**
 * @see <a href="https://firebase.google.com/docs/database/android/start">Realtime Database</a>
 */
public class Realtime {
    private static final String TAG = "Realtime";

    /**
     * Sets a {@link App#isInternetConnection() boolean} for the internet connection of the
     * app. Also sets the
     *
     * @see
     * <a href="https://firebase.google.com/docs/database/android/offline-capabilities#section-connection-state">Connection
     * State</a>
     * @since 1.0
     */
    public static void internet () {
        //region check internet connection
        REALTIME_DB.getReference(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange (@NonNull DataSnapshot snapshot) {
                try {
                    //noinspection ConstantConditions
                    boolean connected = snapshot.getValue(Boolean.class);
                    if (connected) {
                        userOnlineStatus();
                    }
                    App.setInternetConnection(connected);
                } catch (Exception e) {
                    App.setInternetConnection(false);
                }
            }

            @Override
            public void onCancelled (@NonNull DatabaseError error) {
                MyLog.w(TAG, "Listener was cancelled");
            }
        });
        //endregion
    }

    private static void userOnlineStatus() {
        if (Authentication.isSignIn()) {
            String uid = Authentication.getUser().getUid();
            REALTIME_DB.getReference("/online_users").child(uid).onDisconnect().setValue(null);
            REALTIME_DB.getReference("/online_users").child(uid).setValue(true);
        }
    }
}
