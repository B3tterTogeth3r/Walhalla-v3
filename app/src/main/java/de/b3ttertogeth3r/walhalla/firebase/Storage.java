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

import androidx.annotation.NonNull;

import com.google.firebase.storage.FirebaseStorage;

import de.b3ttertogeth3r.walhalla.abstract_generic.Loader;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IInit;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IStorageDownload;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IStorageUpload;
import de.b3ttertogeth3r.walhalla.object.File;
import de.b3ttertogeth3r.walhalla.object.Log;

public class Storage implements IInit {
    private static final String TAG = "Storage";
    protected static IStorageDownload download;
    protected static IStorageUpload upload;
    private FirebaseStorage storage;

    @Override
    public boolean init(Context context, boolean isEmulator) {
        storage = FirebaseStorage.getInstance();
        if (isEmulator) {
            storage.useEmulator("10.0.2.2", 9199);
        }
        download = new Download();
        upload = new Upload();
        return true;
    }

    public static class Upload implements IStorageUpload {

        public Upload() {
            upload = this;
        }

        @Override
        public void image(File file, Loader<String> loader) {

        }

        @Override
        public void file(File file, Loader<String> loader) {

        }
    }

    public class Download implements IStorageDownload {
        public Download() {
            download = this;
        }

        @Override
        public Loader<byte[]> image(@NonNull File file) {
            Loader<byte[]> loader = new Loader<>();
            storage
                    .getReference(file.getPath())
                    .getBytes(2048)
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            loader.done(task.getException());
                            return;
                        }
                        Log.i(TAG, "Download complete");
                        if (task.getResult().length != 0) {
                            loader.done(task.getResult());
                        }
                        loader.done();
                    });
            return loader;
        }

        @Override
        public Loader<byte[]> file(File file) {
            Loader<byte[]> loader = new Loader<>();

            return loader.done();
        }
    }
}
