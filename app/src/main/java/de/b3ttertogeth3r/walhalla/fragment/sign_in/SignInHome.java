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

package de.b3ttertogeth3r.walhalla.fragment.sign_in;

import android.annotation.SuppressLint;
import android.text.InputType;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.itextpdf.text.exceptions.BadPasswordException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstract_classes.Fragment;
import de.b3ttertogeth3r.walhalla.abstract_classes.Touch;
import de.b3ttertogeth3r.walhalla.design.Button;
import de.b3ttertogeth3r.walhalla.design.EditText;
import de.b3ttertogeth3r.walhalla.design.Image;
import de.b3ttertogeth3r.walhalla.design.Toast;
import de.b3ttertogeth3r.walhalla.dialog.PasswordDialog;
import de.b3ttertogeth3r.walhalla.fragment.Home;
import de.b3ttertogeth3r.walhalla.interfaces.activityMain.IOnBackPressed;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IAuth;
import de.b3ttertogeth3r.walhalla.mock.AuthMock;
import de.b3ttertogeth3r.walhalla.object.Log;

public class SignInHome extends Fragment implements IOnBackPressed {
    private static final String TAG = "SignInHome";
    FragmentManager fm;
    private EditText email;
    private String emailStr;
    private Button signIn;
    private IAuth auth;

    public SignInHome() {

    }

    public SignInHome(String email) {
        this.emailStr = email;
    }

    @Override
    public void createView(@NonNull LinearLayout view) {
        fm = getParentFragmentManager();
        int dp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                4f,
                requireContext().getResources().getDisplayMetrics()
        );
        Image logo = new Image(getContext());
        logo.setImage(R.drawable.wappen_round);
        logo.setPadding(50, 50, 50, 50);
        logo.setLayoutParams(new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                (45 * dp)
        ));
        email = new EditText(requireContext());
        email.setDescription(R.string.fui_email_hint);
        if (emailStr != null && !emailStr.isEmpty()) {
            email.setContent(emailStr);
        }
        signIn = new Button(requireContext());
        signIn.setText(R.string.menu_login);
        signIn.setEnabled(false);
        view.setOrientation(LinearLayout.VERTICAL);
        view.addView(logo);
        view.addView(email);
        view.addView(signIn);
        // TODO: 29.06.22 add the buttons for the other services
    }

    @Override
    public void toolbarContent() {
        toolbar.setTitle(R.string.menu_login);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void viewCreated() {
        email.addTextWatcher(s -> {
            Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
            Matcher mat = pattern.matcher(s);
            signIn.setEnabled(mat.matches());
        }).setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        signIn.setOnTouchListener(new Touch() {
            @Override
            public void onClick(View view) {
                /* Check, if email is in auth
                 *  1. if true -> open dialog for password
                 *  2. if false -> register via PersonalData-Fragment
                 */
                emailStr = email.getText();
                auth.exitsEmail(emailStr)
                        .setOnSuccessListener(result -> {
                            if (result == null) {
                                throw new NullPointerException("Check for account failed");
                            }
                            // email is in auth -> open dialog and ask for password
                            else if (result) {
                                // Oen dialog and ask for password
                                Log.i(TAG, "onSuccessListener: i do work and my existence is " + true);
                                openDialog();
                            } else {
                                Log.i(TAG, "onSuccessListener: i do work and my existence is " + false);

                                fm.beginTransaction()
                                        .replace(R.id.fragment_container, new PersonalData(emailStr))
                                        .addToBackStack("SignIn")
                                        .commit();
                            }
                        }).setOnFailListener(e -> {

                        });
            }
        });
    }

    @Override
    public void stop() {

    }

    @Override
    public String analyticsProperties() {
        return TAG;
    }

    @Override
    public void start() {
        auth = new AuthMock();
    }

    private void openDialog() {
        try {
            PasswordDialog.display(fm, email.getText())
                    .setOnSuccessListener(result -> {
                        if (result == null || !result) {
                            throw new BadPasswordException("Password is empty or false");
                        }
                        Log.i(TAG, "openDialog: password correct");
                        fm.beginTransaction()
                                .replace(R.id.fragment_container, new Home())
                                .commit();
                    }).onFailureListener(e -> {
                                Log.e(TAG, "openDialog: something went wrong", e);
                                Toast.makeToast(requireActivity(), R.string.sign_in_failed).show();
                            }
                    );
        } catch (Exception e) {
            Log.e(TAG, "openDialog: ", e);
        }
    }

    @Override
    public boolean onBackPressed() {
        if (fm.getBackStackEntryCount() != 0) {
            fm.popBackStack();
            return true;
        }
        return false;
    }
}