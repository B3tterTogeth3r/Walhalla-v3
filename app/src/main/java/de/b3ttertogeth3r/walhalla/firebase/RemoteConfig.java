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

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IInit;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IRemoteConfig;
import de.b3ttertogeth3r.walhalla.object.Log;

/**
 * @author B3tterTogeth3r
 * @version 1.1
 * @see <a href="https://firebase.google.com/docs/remote-config/get-started?platform=android">Remote
 * Config</a>
 * @since 2.0
 */
@SuppressLint("StaticFieldLeak")
public class RemoteConfig implements IInit, IRemoteConfig {
    private static final String TAG = "RemoteConfig";
    protected static IRemoteConfig config;
    private static FirebaseRemoteConfig remoteConfig;

    /**
     * Download the newest version of data from remote config.
     *
     * @see <a href="https://firebase.google.com/docs/remote-config/loading">FirebaseRemoteConfig Loading</a>
     * @since 1.1
     */
    @Override
    public void update() {
        remoteConfig.fetch();
    }

    /**
     * Activate the latest version. <br>If no changes found, nothing will be changed. <br>If the
     * download is incomplete or there isn't any (first app start) the default values will be used.
     *
     * @see <a href="https://firebase.google.com/docs/remote-config/loading">FirebaseRemoteConfig Loading</a>
     * @since 1.1
     */
    @Override
    public void apply() {
        remoteConfig.activate().addOnCompleteListener(task -> {
            if (task.isCanceled() || task.isSuccessful()) {
                return;
            }
            boolean updated = task.getResult();
            Log.d(TAG, "Config params updated: " + updated);
        });
    }

    @Override
    @NonNull
    public String getString(String key) {
        return remoteConfig.getString(key);
    }

    @Override
    public long getLong(String key) {
        return remoteConfig.getLong(key);
    }

    @Override
    public int getInt(String key) {
        return (int) remoteConfig.getLong(key);
    }

    @Override
    public boolean init(Context context, boolean isEmulator) {
        remoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600)
                .build();
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);
        remoteConfig.setConfigSettingsAsync(configSettings);
        config = this;
        return true;
    }
}
