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

package de.b3ttertogeth3r.walhalla.dialog;

import android.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstract_classes.Dialog;
import de.b3ttertogeth3r.walhalla.design.EditText;
import de.b3ttertogeth3r.walhalla.enums.DialogSize;
import de.b3ttertogeth3r.walhalla.exception.CreateDialogException;
import de.b3ttertogeth3r.walhalla.interfaces.TextWatcher;

public class ProfileEditDialog extends Dialog<CharSequence> {
    private static final String TAG = "ProfileEditDialog";
    private final CharSequence start;
    private EditText edit;
    private int inputType;

    public ProfileEditDialog(DialogSize size, CharSequence result) {
        super(size);
        this.start = result;
    }

    @NonNull
    public static ProfileEditDialog display(FragmentManager fragmentManager, CharSequence result) throws CreateDialogException {
        try {
            ProfileEditDialog ped = new ProfileEditDialog(DialogSize.WRAP_CONTENT, result);
            ped.show(fragmentManager, TAG);
            return ped;
        } catch (Exception e) {
            throw new CreateDialogException("Unable to create " + TAG + ".", e);
        }
    }

    @Override
    public CharSequence done() throws Exception {
        String done = edit.getText();
        if (done != null && !done.isEmpty()) {
            return done;
        } else {
            return start;
        }
    }

    @Override
    public void createDialog(@NonNull RelativeLayout container, @NonNull LayoutInflater inflater) {
        edit = new EditText(requireContext());
        if (inputType != 0) {
            edit.setInputType(InputType.TYPE_CLASS_TEXT | inputType);
        } else {
            edit.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        edit.setText(start);
        container.addView(edit);
    }

    @Override
    public void configDialog(@NonNull AlertDialog.Builder builder) {
        builder.setPositiveButton(R.string.send, this);
        builder.setNegativeButton(R.string.abort, this);
    }

    @Override
    public void configToolbar(@NonNull Toolbar toolbar) {
        toolbar.setTitle(R.string.edit);
    }

    public ProfileEditDialog changeInputType(int type) {
        try {
            edit.setInputType(InputType.TYPE_CLASS_TEXT | type);
        } catch (Exception e) {
            this.inputType = type;
        }
        return this;
    }

    public ProfileEditDialog addTextWatcher(TextWatcher watcher) {
        edit.addTextWatcher(watcher);
        return this;
    }
}
