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

package de.b3ttertogeth3r.walhalla.mock;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstract_classes.Loader;
import de.b3ttertogeth3r.walhalla.design.Toast;
import de.b3ttertogeth3r.walhalla.interfaces.IStorageDownload;
import de.b3ttertogeth3r.walhalla.object.File;
import de.b3ttertogeth3r.walhalla.old.App;
import de.b3ttertogeth3r.walhalla.util.ToastList;

public class StorageMock {

    public static class Download implements IStorageDownload {
        private static final String TAG = "StorageMockDownload";

        public Download () {
            ToastList.addToast(Toast.makeToast(App.getContext(), TAG + "-MOCK-DATA"));
        }

        @Override
        public void image (File file, Loader<byte[]> loader) {
            Drawable d = ContextCompat.getDrawable(App.getContext(), R.drawable.image_garden);
            if (d != null) {
                Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                loader.done(stream.toByteArray());
            } else {
                loader.onFailureListener(null);
            }
        }

        @Override
        public void file (@NonNull File file, Loader<byte[]> loader) {
            InputStream stream = App.getContext().getResources().openRawResource(
                    App.getContext().getResources().getIdentifier("FILENAME_WITHOUT_EXTENSION",
                            "raw", App.getContext().getPackageName()));
            if (stream != null) {
                try {
                    byte[] buffer = new byte[8192];
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                    int bytesRead;
                    while ((bytesRead = stream.read(buffer)) != -1) {
                        baos.write(buffer, 0, bytesRead);
                    }
                    loader.done(baos.toByteArray());
                } catch (Exception e) {
                    loader.done(e);
                }
            } else {
                loader.onFailureListener(null);
            }
        }
    }
}
