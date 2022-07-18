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

import org.jetbrains.annotations.Contract;

import de.b3ttertogeth3r.walhalla.interfaces.firebase.IAnalytics;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IAuth;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IFirestoreDownload;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IFirestoreUpload;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IRemoteConfig;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IStorageDownload;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IStorageUpload;
import de.b3ttertogeth3r.walhalla.mock.FirestoreMock;
import de.b3ttertogeth3r.walhalla.mock.StorageMock;

/**
 * A static interface to get the needed functions from the Firebase services
 *
 * @author B3tterTogeth3r
 * @version 1.0
 * @see IFirestoreUpload
 * @see IFirestoreDownload
 * @see IAnalytics
 * @see IAuth
 * @see IStorageUpload
 * @see IStorageDownload
 * @since 3.1
 */
public interface Firebase {
    /**
     * Calls the interface inside the {@link Analytics} class to pass on the public functions.
     *
     * @return The interface.
     * @see IAnalytics
     * @see Analytics
     * @since {@link Firebase} version 1.0
     */
    static IAnalytics analytics() {
        return Analytics.iAnalytics;
    }

    /**
     * Calls the interface inside the {@link Authentication} class to pass on the public functions.
     *
     * @return The Interface.
     * @see IAuth
     * @see Authentication
     * @since {@link Firebase} version 1.0
     */
    @Contract(pure = true)
    static IAuth authentication() {
        return Authentication.iAuth;
    }

    /**
     * Calls the interface inside the {@link Firestore.Download} class to pass on the public functions.
     *
     * @return The interface.
     * @see Firestore
     * @see IFirestoreDownload
     * @see Firestore.Download
     * @since {@link Firebase} version 1.0
     */
    static IFirestoreDownload firestoreDownload() {
        return new FirestoreMock.Download();
        //return Firestore.download;
    }

    /**
     * Calls the interface inside the {@link Firestore.Upload} class to pass on the public functions.
     *
     * @return The interface.
     * @see Firestore
     * @see IFirestoreUpload
     * @see Firestore.Upload
     * @since {@link Firebase} version 1.0
     */
    static IFirestoreUpload firestoreUpload() {
        return new FirestoreMock.Upload();
        //return Firestore.upload;
    }

    /**
     * Calls the interface inside the {@link Storage.Upload} class to pass on the public functions.
     *
     * @return The interface.
     * @see Storage
     * @see IStorageUpload
     * @see Storage.Upload
     * @since {@link Firebase} version 1.0
     */
    static IStorageUpload storageUpload() {
        return new StorageMock.Upload();
        //return Storage.upload;
    }

    /**
     * Calls the interface inside the {@link Storage.Download} class to pass on the public functions.
     *
     * @return The interface.
     * @see Storage
     * @see IStorageDownload
     * @see Storage.Download
     * @since {@link Firebase} version 1.0
     */
    static IStorageDownload storageDownload() {
        //return new StorageMock.Download();
        return Storage.download;
    }

    static IRemoteConfig remoteConfig() {
        return RemoteConfig.config;
    }
}
