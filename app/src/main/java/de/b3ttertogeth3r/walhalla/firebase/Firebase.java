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
 * A static interface to get the needed functions from the Firebase services. <br> It has the
 * static functions:
 * <ul>
 *     <li>{@link #analytics()}</li>
 *     <li>{@link #authentication()}</li>
 *     <li>{@link #remoteConfig()}</li>
 *     <li>{@link Firestore#download() Firestore.download()}</li>
 *     <li>{@link Firestore#upload() Firestore.upload()}</li>
 *     <li>{@link Storage#download() Storage.download()}</li>
 *     <li>{@link Storage#upload() Storage.upload()}</li>
 * </ul>
 *
 * @author B3tterTogeth3r
 * @version 1.2
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
     * Calls the {@link IRemoteConfig} interface to pass on the public functions
     *
     * @return The interface.
     * @see RemoteConfig
     * @see IRemoteConfig
     * @see <a href="https://firebase.google.com/docs/remote-config">Firebase Remote Config Documentation</a>
     * @since {@link Firebase} version 1.1
     */
    static IRemoteConfig remoteConfig() {
        return RemoteConfig.config;
    }

    /**
     * An interface collecting the Firestore interfaces used to upload and download data into the
     * Firebase Firestore Database.
     *
     * @see <a href="https://firebase.google.com/docs/firestore">Firestore online Docs</a>
     * @see de.b3ttertogeth3r.walhalla.firebase.Firestore Firestore
     */
    interface Firestore {

        /**
         * Calls the interface inside the {@link de.b3ttertogeth3r.walhalla.firebase.Firestore.Download Firestore.Download} class to pass on the public functions.
         *
         * @return The interface.
         * @see IFirestoreDownload
         * @see de.b3ttertogeth3r.walhalla.firebase.Firestore.Download Firestore.Download
         * @since {@link Firebase} version 1.0
         */
        static IFirestoreDownload download() {
            return new FirestoreMock.Download();
            //return Firestore.download;
        }

        /**
         * Calls the interface inside the {@link de.b3ttertogeth3r.walhalla.firebase.Firestore.Upload Firestore.Upload} class to pass on the public functions.
         *
         * @return The interface.
         * @see IFirestoreUpload
         * @see de.b3ttertogeth3r.walhalla.firebase.Firestore.Upload Firestore.Upload
         * @since {@link Firebase} version 1.0
         */
        static IFirestoreUpload upload() {
            return new FirestoreMock.Upload();
            // return Firestore.upload;
        }
    }

    /**
     * An interface to collect the Storage interfaces used to upload and download data into the
     * Firebase Cloud Storage Bucket.
     *
     * @see de.b3ttertogeth3r.walhalla.firebase.Storage Storage
     * @since {@link Firebase} version 1.2
     */
    interface Storage {
        /**
         * Calls the interface inside the {@link de.b3ttertogeth3r.walhalla.firebase.Storage.Download Storage.Download} class to pass on the public functions.
         *
         * @return The interface.
         * @see IStorageDownload
         * @see de.b3ttertogeth3r.walhalla.firebase.Storage.Download Storage.Download
         * @since {@link Firebase} version 1.0
         */
        static IStorageDownload download() {
            return new StorageMock.Download();
            // return de.b3ttertogeth3r.walhalla.firebase.Storage.download;
        }


        /**
         * Calls the interface inside the {@link de.b3ttertogeth3r.walhalla.firebase.Storage.Upload Storage.Upload} class to pass on the public functions.
         *
         * @return The interface.
         * @see IStorageUpload
         * @see de.b3ttertogeth3r.walhalla.firebase.Storage.Upload Storage.Upload
         * @since {@link Firebase} version 1.0
         */
        static IStorageUpload upload() {
            return new StorageMock.Upload();
            //return de.b3ttertogeth3r.walhalla.firebase.Storage.upload;
        }
    }
}
