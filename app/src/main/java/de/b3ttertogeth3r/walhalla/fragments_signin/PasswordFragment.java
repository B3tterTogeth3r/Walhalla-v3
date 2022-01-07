package de.b3ttertogeth3r.walhalla.fragments_signin;

import static de.b3ttertogeth3r.walhalla.SignInActivity.user;

import android.graphics.Color;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.ListenerRegistration;

import de.b3ttertogeth3r.walhalla.App;
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
import de.b3ttertogeth3r.walhalla.interfaces.CustomFirebaseCompleteListener;
import de.b3ttertogeth3r.walhalla.models.Person;

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
    public void authStatusChanged () {
    }

    @Override
    public void stop () {

    }

    @Override
    public void viewCreated () {
        goOnButton.setEnabled(false);
        if (kind == Display.EDIT) {
            password_one.setEnabled(false);
            //TODO Add checker for current password
        }
        password_one.addTextWatcher(s -> {
            PasswordStrength strength = PasswordStrength.calculateStrength(s.toString());
            password_one.setHintTextColor(ContextCompat.getColorStateList(requireContext(),
                    strength.getColor()));
            if (strength == PasswordStrength.STRONG || strength == PasswordStrength.VERY_STRONG) {
                //TODO make password_two editable
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

    @Override
    public void toolbarContent () {
        if (kind == Display.EDIT) {
            toolbar.setTitle(R.string.edit_password);
        } else {
            toolbar.setTitle(R.string.set_password);
        }
    }

    @Override
    public void createView (@NonNull @NotNull View view,
                            @NonNull @NotNull LayoutInflater inflater) {
        layout = view.findViewById(R.id.fragment_container);
        layout.removeAllViewsInLayout();
        layout.removeAllViews();
        if (kind == Display.EDIT) {
            editPassword();
        } else {
            addPassword();
        }
    }

    private void editPassword () {
        //TODO create view to edit password of current user
        layout.setBackgroundColor(Color.RED);
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

    @Override
    public void onClick (View v) {
        if (v == goOnButton) {
            Log.d(TAG, "onClick: goObButton pressed");
            password_one.setEnabled(false);
            password_two.setEnabled(false);
            if (kind != Display.EDIT) {
                user.setId(null);
                user.setPassword(password_two.getString());
                Firestore.uploadPerson(user, new CustomFirebaseCompleteListener() {
                    @Override
                    public void onSuccess (String string) {
                        waitForCF(string);
                        registration.add(waitForCF(string));
                    }

                    @Override
                    public void onFailure (Exception exception) {

                    }
                });
            }
        }
    }

    /**
     * listen to updates on the created user, until cloud functions have created the user and
     * cleared the password. When the password is cleared, the user gets sign in and the activity
     * dismissed.
     * @return
     */
    @NonNull
    private ListenerRegistration waitForCF (String path) {
        return Firestore.FIRESTORE.document(path).addSnapshotListener((value, error) -> {
            if (error != null || value == null) {
                return;
            }
            try {
                Person p = value.toObject(Person.class);
                if (p != null && p.getPassword() == null) {
                    Log.i(TAG, "onSuccess: sign in start");
                    Authentication.signIn(user.getEmail(), user.getPassword(),
                            success -> {
                                if(success) {
                                    MyToast toast = new MyToast(App.getContext());
                                    toast.setMessage("register successful");
                                    Log.d(TAG, "authStatusChanged: register successful");
                                    toast.show();
                                    SignInActivity.finish.site();
                                }
                            });
                }
            } catch (Exception ignored) {
            }
        });
    }
}
