package de.b3ttertogeth3r.walhalla.fragments_signin;

import static de.b3ttertogeth3r.walhalla.SignInActivity.user;

import android.graphics.Color;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.ListenerRegistration;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.SignInActivity;
import de.b3ttertogeth3r.walhalla.abstraction.CustomFragment;
import de.b3ttertogeth3r.walhalla.design.MyButton;
import de.b3ttertogeth3r.walhalla.design.MyEditText;
import de.b3ttertogeth3r.walhalla.design.MyToast;
import de.b3ttertogeth3r.walhalla.enums.Display;
import de.b3ttertogeth3r.walhalla.enums.PasswordStrength;
import de.b3ttertogeth3r.walhalla.firebase.Analytics;
import de.b3ttertogeth3r.walhalla.firebase.Authentication;
import de.b3ttertogeth3r.walhalla.firebase.Firestore;
import de.b3ttertogeth3r.walhalla.interfaces.MyCompleteListener;
import de.b3ttertogeth3r.walhalla.models.Person;
import de.b3ttertogeth3r.walhalla.utils.MyLog;
import de.b3ttertogeth3r.walhalla.utils.Variables;

/**
 * @author B3tterToegth3r
 * @version 1.0
 * @since 1.0
 */
public class PasswordFragment extends CustomFragment implements View.OnClickListener {
    private static final String TAG = "signIn.password";
    private final Display kind;
    MyEditText password_one, password_two;
    MyButton goOnButton;
    MyButton signInButton;
    private LinearLayout layout;

    public PasswordFragment (Display kind) {
        this.kind = kind;
    }

    @Override
    public void start () {

    }

    @Override
    public void analyticsProperties () {
        Analytics.screenChange(0, TAG);
    }

    @Override
    public void stop () {

    }

    @Override
    public void viewCreated () {
        if (kind == Display.EDIT) {
            // toolbar.setTitle(R.string.edit_password);
            add();
        } else if (kind == Display.ADD) {
            add();
        } else {
            // toolbar.setTitle(R.string.sign_in);
            in();
        }
    }

    public void add () {
        goOnButton.setEnabled(false);
        password_one.setEnabled(true);
        password_one.addTextWatcher(s -> {
            PasswordStrength strength = PasswordStrength.calculateStrength(s.toString());
            password_one.setHintTextColor(ContextCompat.getColorStateList(requireContext(),
                    strength.getColor()));
            if (strength == PasswordStrength.STRONG || strength == PasswordStrength.VERY_STRONG) {
                // make password_two editable
                password_one.error(null);
                password_two.setEnabled(true);
            } else {
                password_one.error(strength);
                password_two.setEnabled(false);
            }
        });

        password_two.addTextWatcher(s -> {
            if (s.toString().equals(password_one.getString())) {
                password_two.error(null);
                password_two.setHintTextColor(ContextCompat.getColorStateList(requireContext(),
                        R.color.very_strong));
                // enable "continue" button
                goOnButton.setEnabled(true);
                goOnButton.setOnClickListener(this);
            } else {
                password_two.setErrorEnabled(true);
                password_two.setErrorIconOnClickListener(null);
                password_two.setErrorIconDrawable(null);
                password_two.setHintTextColor(ContextCompat.getColorStateList(requireContext(),
                        R.color.red));
            }
        });
    }

    public void in () {

    }

    @Override
    public void toolbarContent () {
        if (kind == Display.EDIT) {
            toolbar.setTitle(R.string.edit_password);
        } else if (kind == Display.ADD) {
            toolbar.setTitle(R.string.set_password);
        } else {
            toolbar.setTitle(R.string.sign_in);
        }
    }

    @Override
    public void createView (@NonNull @NotNull View view,
                            @NonNull @NotNull LayoutInflater inflater) {
        layout = view.findViewById(R.id.fragment_container);
        layout.removeAllViewsInLayout();
        layout.removeAllViews();
        if (kind == Display.EDIT) {
            addPassword();
            // editPassword();
        } else if (kind == Display.ADD) {
            addPassword();
        } else {
            signIn();
        }
    }

    @Override
    public void authStatusChanged () {
    }

    private void addPassword () {
        password_one = new MyEditText(getContext());
        password_one
                .setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
                .setDescription(R.string.fui_new_password_hint);
        layout.addView(password_one);
        password_two = new MyEditText(getContext());
        password_two
                .setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
                .setDescription(R.string.confirm_password);
        layout.addView(password_two);
        goOnButton = new MyButton(getContext());
        goOnButton.setText(R.string.send);
        layout.addView(goOnButton);
    }

    private void signIn () {
        layout.removeAllViews();
        // TODO: 12.03.22 add title and welcome back message
        password_one = new MyEditText(getContext());
        password_one
                .setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
                .setDescription(R.string.fui_password_hint);
        layout.addView(password_one);
        signInButton = new MyButton(getContext());
        signInButton.setText(R.string.fui_sign_in_default);
        signInButton.setOnClickListener(this);
        layout.addView(signInButton);
    }

    private void editPassword () {
        //TODO create view to edit password of current user
        layout.removeAllViews();
        layout.setBackgroundColor(Color.RED);
    }

    @Override
    public void onClick (View v) {
        if (v == goOnButton) {
            Log.d(TAG, "onClick: goObButton pressed");
            password_one.setEnabled(false);
            password_two.setEnabled(false);
            if (kind == Display.ADD) {
                user.setId(null);
                user.setPasswordStr(password_two.getString());
                Firestore.uploadPerson(user, new MyCompleteListener<String>() {
                    @Override
                    public void onSuccess (String string) {
                        signInButton.setEnabled(false);
                        registration.add(waitForCF(string));
                    }

                    @Override
                    public void onFailure (Exception exception) {

                    }
                });
            }
            if (kind == Display.EDIT) {
                // change users password
                Log.e(TAG,
                        "onClick: email:" + user.getEmail() + "\nnew-password:" + password_one.getString());
                Authentication.changePassword(user, password_one.getString(),
                        new MyCompleteListener<Void>() {
                            @Override
                            public void onSuccess (@Nullable Void result) {
                                Log.e(TAG, "onSuccess: success");
                                signInButton.setEnabled(false);
                                // TODO: 08.02.22
                                //  1. finish fragment/activity
                                //  2. toast success message on MainActivity
                                //  3. update firestore entry with "hasPassword" = true;
                                user.setPassword(true);
                                Firestore.uploadPerson(user, new MyCompleteListener<String>() {
                                    @Override
                                    public void onSuccess (@Nullable String result) {
                                        Log.e(TAG, "onSuccess: " + result);
                                    }

                                    @Override
                                    public void onFailure (@Nullable Exception exception) {
                                        Log.e(TAG, "onFailure: ", exception);
                                    }
                                });
                                SignInActivity.uploadListener.onSuccess(Variables.REGISTER_COMPLETE);
                            }

                            @Override
                            public void onFailure (@Nullable Exception exception) {
                                Log.e(TAG, "onFailure: ", exception);
                                // TODO: 08.02.22 reset password areas and toast an error message
                            }
                        });
            }
        }
        if (v == signInButton) {
            if (kind == Display.SHOW) {
                signInButton.setEnabled(false);
                Authentication.signIn(user.getEmail(), password_one.getString(),
                        success -> {
                            if (success) {
                                SignInActivity.uploadListener.onSuccess(Variables.REGISTER_COMPLETE);
                            } else {
                                signInButton.setEnabled(true);
                                MyToast toast = new MyToast(getContext());
                                toast.setText(R.string.password_wrong);
                                toast.show();
                            }
                        });
            }
        }
    }

    /**
     * listen to updates on the created user, until cloud functions have created the user and
     * cleared the password. When the password is cleared, the user gets sign in and the activity
     * dismissed.
     *
     * @return the listener
     */
    @NonNull
    private ListenerRegistration waitForCF (String path) {
        return Firestore.FIRESTORE.document(path).addSnapshotListener((value, error) -> {
            if (error != null || value == null) {
                return;
            }
            try {
                Person p = value.toObject(Person.class);
                if (p != null && p.getPasswordStr() == null) {
                    Log.i(TAG, "onSuccess: sign in start");
                    Authentication.signIn(user.getEmail(), user.getPasswordStr(),
                            success -> {
                                if (success) {
                                    SignInActivity.uploadListener.onSuccess(Variables.REGISTER_COMPLETE);
                                } else {
                                    SignInActivity.uploadListener.onFailure();
                                }
                            });
                }
            } catch (Exception e) {
                MyLog.e(TAG, "Listener didn't work", e);
            }
        });
    }
}
