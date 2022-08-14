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
import android.content.DialogInterface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import de.b3ttertogeth3r.walhalla.MainActivity;
import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstract_generic.Dialog;
import de.b3ttertogeth3r.walhalla.design.EditText;
import de.b3ttertogeth3r.walhalla.enums.DialogSize;
import de.b3ttertogeth3r.walhalla.exception.CreateDialogException;
import de.b3ttertogeth3r.walhalla.exception.UserDataError;
import de.b3ttertogeth3r.walhalla.firebase.Authentication;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IAuth;

public class PasswordDialog extends Dialog<Boolean> {
    private static final String TAG = "PasswordDialog";
    private final IAuth auth;
    private final String email;
    private EditText passwordET;

    public PasswordDialog(DialogSize size, String email) {
        super(size);
        auth = new Authentication();
        this.email = email;
    }

    @NonNull
    public static PasswordDialog display(FragmentManager fragmentManager, String email) throws CreateDialogException {
        try {
            PasswordDialog dialog = new PasswordDialog(DialogSize.WRAP_CONTENT, email);
            dialog.show(fragmentManager, TAG);
            return dialog;
        } catch (Exception e) {
            throw new CreateDialogException("unable to create dialog", e);
        }
    }

    @Override
    public Boolean done() throws Exception {
        MainActivity.hideKeyBoard.hide();
        if (e != null) {
            throw e;
        } else {
            return true;
        }
    }

    @Override
    public void createDialog(@NonNull RelativeLayout container, @NonNull LayoutInflater inflater) {
        passwordET = new EditText(requireContext());
        passwordET.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        container.addView(passwordET);
    }

    @Override
    public void configDialog(@NonNull AlertDialog.Builder builder) {
        builder.setPositiveButton(R.string.send, (dialogInterface, i) -> {
            String password = passwordET.getText();
            if (password == null || password.isEmpty()) {
                this.e = new UserDataError("No password written");
            }
            super.onClick(dialogInterface, i);
        });
        builder.setNegativeButton(R.string.abort, this);
    }

    @Override
    public void configToolbar(@NonNull Toolbar toolbar) {
        toolbar.setTitle(R.string.password);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        auth.signIn(email, passwordET.getText())
                .setOnSuccessListener(result -> {
                    if (result != null && result.getUser() != null) {
                        super.onDismiss(dialog);
                        return;
                    }
                    throw new UserDataError("Sign in did not work");
                }).setOnFailListener(e -> {
                    this.e = e;
                    super.onDismiss(dialog);
                });
    }
}
