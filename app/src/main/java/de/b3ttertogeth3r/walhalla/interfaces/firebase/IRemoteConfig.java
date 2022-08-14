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

package de.b3ttertogeth3r.walhalla.interfaces.firebase;

import de.b3ttertogeth3r.walhalla.annotation.RemoteConfigKeys;

/**
 * Interface to communicate with Firebase Remote Config.
 *
 * @author B3tterTogeth3r
 * @version 1.1
 * @since 2.0
 */
public interface IRemoteConfig {
    String CURRENT_SEMESTER = "current_semester_id";
    String SEMESTER_NOTES = "semester_notes";
    String FRATERNITY_GERMANY = "fraternity_germany";
    String FRATERNITY_CITY = "fraternity_wuerzburg";
    String ROOMS = "rooms";
    String ABOUT_US = "about_us";
    String IBAN_VALUES = "iban_values";
    String HISTORY_SHORT = "history_short";
    String CHARGEN_DESCRIPTION = "chargen_description";

    /**
     * Download the newest version of data from remote config.
     *
     * @see <a href="https://firebase.google.com/docs/remote-config/loading">FirebaseRemoteConfig Loading</a>
     * @since 1.1
     */
    void update();

    /**
     * Activate the latest version. <br>If no changes found, nothing will be changed. <br>If the
     * download is incomplete or there isn't any (first app start) the default values will be used.
     *
     * @see <a href="https://firebase.google.com/docs/remote-config/loading">FirebaseRemoteConfig Loading</a>
     * @since 1.1
     */
    void apply();

    String getString(@RemoteConfigKeys String key);

    long getLong(@RemoteConfigKeys String key);

    int getInt(@RemoteConfigKeys String key);
}
