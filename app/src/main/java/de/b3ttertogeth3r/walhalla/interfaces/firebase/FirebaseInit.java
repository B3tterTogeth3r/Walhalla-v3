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

import android.content.Context;

/**
 * An interface to initialize all the firebase functions used in the app.
 *
 * @author B3tterTogeth3r
 * @version 1.0
 * @since 2.0
 */
public interface FirebaseInit {

    /**
     * Initialize the given Firebase service.
     *
     * @param context Application context
     */
    void Analytics(Context context);

    /**
     * Initialize the given Firebase service.
     *
     * @param context Application context
     */
    void Authentication(Context context);

    /**
     * Initialize the given Firebase service.
     *
     * @param context Application context
     */
    void CloudMessaging(Context context);

    /**
     * Initialize the given Firebase service.
     *
     * @param context Application context
     */
    void Crashlytics(Context context);

    /**
     * Initialize the given Firebase service.
     *
     * @param context Application Context
     */
    void CloudFunctions(Context context);

    /**
     * Initialize the given Firebase service.
     *
     * @param context Application context
     */
    void DynamicLinks(Context context);

    /**
     * Initialize the given Firebase service.
     *
     * @param context Application context
     */
    void Firestore(Context context);

    /**
     * Initialize the given Firebase service.
     *
     * @param context Application context
     */
    void InAppMessaging(Context context);

    /**
     * Initialize the given Firebase service.
     *
     * @param context Application context
     */
    void RemoteConfig(Context context);

    /**
     * Initialize the given Firebase service.
     *
     * @param context Application context
     */
    void Storage(Context context);
}
