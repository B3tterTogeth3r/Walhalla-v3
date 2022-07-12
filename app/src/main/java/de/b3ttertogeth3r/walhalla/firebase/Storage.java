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

import de.b3ttertogeth3r.walhalla.abstract_classes.Loader;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IInit;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IStorageDownload;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IStorageUpload;
import de.b3ttertogeth3r.walhalla.object.File;
import de.b3ttertogeth3r.walhalla.object.Log;

public class Storage implements IInit {
    private static final String TAG = "Storage";
    private static FirebaseStorage storage;

    @Override
    public boolean init (Context context) {
        storage = FirebaseStorage.getInstance();
        return true;
    }

    public static class Download implements IStorageDownload {
        @Override
        public Loader<byte[]> image(@NonNull File file) {
            Loader<byte[]> loader = new Loader<>();
            Storage.storage
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

            return loader;
        }
    }

    public static class Upload implements IStorageUpload {
        @Override
        public void image (File file, Loader<String> loader) {

        }

        @Override
        public void file (File file, Loader<String> loader) {

        }
    }
}
