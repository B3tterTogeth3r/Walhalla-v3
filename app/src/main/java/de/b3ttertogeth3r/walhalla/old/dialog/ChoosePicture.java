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

package de.b3ttertogeth3r.walhalla.old.dialog;

import static de.b3ttertogeth3r.walhalla.old.utils.Variables.CAMERA;
import static de.b3ttertogeth3r.walhalla.old.utils.Variables.GALLERY;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.old.interfaces.OnDoneListener;

public class ChoosePicture extends AlertDialog.Builder implements DialogInterface.OnClickListener {
    private final OnDoneListener listener;
    private final Intent intent = new Intent();

    public ChoosePicture (@NonNull Context context, OnDoneListener listener) {
        super(context);
        this.listener = listener;
        intent.putExtra("ImageSelector", CAMERA);
        String[] list = new String[2];
        list[0] = (context.getString(R.string.image_camera));
        list[1] = (context.getString(R.string.image_gallery));
        setIcon(R.drawable.ic_image_search)
                .setTitle(R.string.select_image)
                .setSingleChoiceItems(list, 0, (dialog, which) -> {
                    if (which == 0) {
                        intent.putExtra("ImageSelector", CAMERA);
                    } else {
                        intent.putExtra("ImageSelector", GALLERY);
                    }
                });
        setNegativeButton(R.string.abort, this);
        setPositiveButton(R.string.send, this);
    }

    @Override
    public void onClick (DialogInterface dialog, int which) {
        listener.sortResult(which, intent);
    }
}
