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

package de.b3ttertogeth3r.walhalla.interfaces;

import android.content.Context;

/**
 * An interface to initialize all the firebase functions used in the app.
 *
 * @author B3tterTogeth3r
 * @version 1.0
 * @since 2.0
 */
public interface FirebaseInit {

    void Analytics (Context context);

    void Authentication (Context context);

    void CloudMessaging (Context context);

    void Crashlytics (Context context);

    void DynamicLinks (Context context);

    void Firestore (Context context);

    void InAppMessaging (Context context);

    void RemoteConfig (Context context);

    void Storage (Context context);
}
