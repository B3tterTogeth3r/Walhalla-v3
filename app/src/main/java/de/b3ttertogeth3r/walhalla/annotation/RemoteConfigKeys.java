/*
 * Copyright (c) 2022-2023.
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

package de.b3ttertogeth3r.walhalla.annotation;

import static de.b3ttertogeth3r.walhalla.interfaces.firebase.IRemoteConfig.ABOUT_US;
import static de.b3ttertogeth3r.walhalla.interfaces.firebase.IRemoteConfig.CHARGEN_DESCRIPTION;
import static de.b3ttertogeth3r.walhalla.interfaces.firebase.IRemoteConfig.FRATERNITY_CITY;
import static de.b3ttertogeth3r.walhalla.interfaces.firebase.IRemoteConfig.FRATERNITY_GERMANY;
import static de.b3ttertogeth3r.walhalla.interfaces.firebase.IRemoteConfig.HISTORY_SHORT;
import static de.b3ttertogeth3r.walhalla.interfaces.firebase.IRemoteConfig.IBAN_VALUES;
import static de.b3ttertogeth3r.walhalla.interfaces.firebase.IRemoteConfig.ROOMS;
import static de.b3ttertogeth3r.walhalla.interfaces.firebase.IRemoteConfig.SEMESTER_NOTES;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({SEMESTER_NOTES, FRATERNITY_GERMANY, FRATERNITY_CITY, ROOMS, ABOUT_US, IBAN_VALUES, HISTORY_SHORT, CHARGEN_DESCRIPTION})
public @interface RemoteConfigKeys {
}