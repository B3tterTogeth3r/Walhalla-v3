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
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.itextpdf.text.exceptions.BadPasswordException;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstract_generic.Dialog;
import de.b3ttertogeth3r.walhalla.design.EditText;
import de.b3ttertogeth3r.walhalla.enums.DialogSize;
import de.b3ttertogeth3r.walhalla.enums.PasswordStrength;
import de.b3ttertogeth3r.walhalla.exception.CreateDialogException;
import de.b3ttertogeth3r.walhalla.exception.UserDataError;

public class SetPasswordDialog extends Dialog<String> {
    private static final String TAG = "PasswordDialog";
    private EditText passwordET;
    private EditText password2ET;

    public SetPasswordDialog() {
        super(DialogSize.WRAP_CONTENT);
    }

    @NonNull
    public static SetPasswordDialog display(FragmentManager fragmentManager) throws CreateDialogException {
        try {
            SetPasswordDialog dialog = new SetPasswordDialog();
            dialog.show(fragmentManager, TAG);
            return dialog;
        } catch (Exception e) {
            throw new CreateDialogException("unable to create dialog", e);
        }
    }

    @Override
    public String done() throws Exception {
        if (e != null) {
            throw e;
        }
        return password2ET.getText();
    }

    @Override
    public void createDialog(@NonNull RelativeLayout container, @NonNull LayoutInflater inflater) {
        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        passwordET = new EditText(requireContext());
        passwordET.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passwordET.setDescription("setPassword");
        password2ET = new EditText(requireContext());
        password2ET.setDescription("Repeat");
        password2ET.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layout.setLayoutParams(params);
        passwordET.setLayoutParams(params);
        password2ET.setLayoutParams(params);
        layout.addView(passwordET);
        layout.addView(password2ET);
        container.addView(layout);
    }

    @Override
    public void configDialog(@NonNull AlertDialog.Builder builder) {
        passwordET.addTextWatcher(s -> {
            PasswordStrength strength = PasswordStrength.getStrength(s);
            switch (strength) {
                default:
                case WEAK:
                    passwordET.setError(true, "pw to short");
                    break;
                case MEDIUM:
                    passwordET.setError(true, "pw not save enough.");
                    break;
                case STRONG:
                    passwordET.setError(true, "pw not save enough.");
                case VERY_STRONG:
                    passwordET.setError(false, null);
                    break;
            }

        });
        password2ET.addTextWatcher(s -> {
            password2ET.setError(true, "passwords don't match");
            if (s.toString().equals(passwordET.getText())) {
                password2ET.setError(false, null);
            }
        });
        builder.setPositiveButton(R.string.send, (dialogInterface, i) -> {
            String password = passwordET.getText();
            String password2 = password2ET.getText();
            if (password == null || password.isEmpty()) {
                this.e = new UserDataError("No password written");
            } else if (password.equals(password2)) {
                onDismiss(dialogInterface);
            } else {
                this.e = new BadPasswordException("passwords didn't match");
            }
        });
        builder.setNegativeButton(R.string.abort, this);
    }

    @Override
    public void configToolbar(@NonNull Toolbar toolbar) {
        toolbar.setTitle(R.string.password);
    }
}
