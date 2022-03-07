package de.b3ttertogeth3r.walhalla.fragments_signin;

import android.net.Uri;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Objects;

import de.b3ttertogeth3r.walhalla.App;
import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.SignInActivity;
import de.b3ttertogeth3r.walhalla.abstraction.CustomFragment;
import de.b3ttertogeth3r.walhalla.design.MyButton;
import de.b3ttertogeth3r.walhalla.design.MyEditText;
import de.b3ttertogeth3r.walhalla.design.MyImageView;
import de.b3ttertogeth3r.walhalla.design.MyTitle;
import de.b3ttertogeth3r.walhalla.design.MyToast;
import de.b3ttertogeth3r.walhalla.enums.Display;
import de.b3ttertogeth3r.walhalla.firebase.Authentication;
import de.b3ttertogeth3r.walhalla.firebase.Crashlytics;
import de.b3ttertogeth3r.walhalla.firebase.Firestore;
import de.b3ttertogeth3r.walhalla.firebase.Storage;
import de.b3ttertogeth3r.walhalla.interfaces.MyCompleteListener;
import de.b3ttertogeth3r.walhalla.models.Person;
import de.b3ttertogeth3r.walhalla.utils.Format;

import static de.b3ttertogeth3r.walhalla.SignInActivity.*;

public class StartFragment extends CustomFragment implements View.OnClickListener {
    private static final String TAG = "signIn.StartFragment";
    private MyButton send;
    private MyEditText email;

    @Override
    public void start () {

    }

    @Override
    public void analyticsProperties () {

    }

    @Override
    public void stop () {

    }

    @Override
    public void viewCreated () {
        send.setEnabled(false);
    }

    @Override
    public void toolbarContent () {
        toolbar.setTitle(R.string.menu_login);
        // TODO: 15.02.22 add an cancel button
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.default_save_abort);
        toolbar.getMenu().removeItem(R.id.save);
        toolbar.setOnMenuItemClickListener(item -> {
            if(item.getItemId() == R.id.abort) {
                return true;
            }
            return false;
        });
    }

    @Override
    public void createView (@NonNull View view, @NonNull LayoutInflater inflater) {
        LinearLayout layout = view.findViewById(R.id.fragment_container);
        int padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                16f,
                this.getResources().getDisplayMetrics()
        );
        layout.removeAllViews();
        layout.removeAllViewsInLayout();
        if(App.isInternetConnection()) {

            RelativeLayout linearLayout = new RelativeLayout(getContext());
            linearLayout.setPadding(padding, padding, padding, padding);

            MyImageView imageView = new MyImageView(getContext());
            imageView.setDescription(R.string.image_description);
            Storage.getUri("images/PICT3030-104675293.jpeg", new MyCompleteListener<Uri>() {
                @Override
                public void onSuccess (Uri imageUri) {
                    imageView.setImage(imageUri);
                }

                @Override
                public void onFailure (Exception exception) {

                }
            });
            imageView.setId(R.id.imageView);
            imageView.setGravity(Gravity.CENTER_HORIZONTAL);
            imageView.setBackground(null);
            linearLayout.addView(imageView);

            RelativeLayout.LayoutParams emailParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            email = new MyEditText(getContext());
            email.setHint(R.string.fui_email_hint);
            email.setId(R.id.email);
            email.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            email.setPadding(padding, (int) (0.5 * padding), padding, padding);
            email.setTextAppearance(R.style.TextAppearance_AppCompat_Body1);
            email.setContent(user.getEmail());
            emailParams.addRule(RelativeLayout.BELOW, imageView.getId());
            linearLayout.addView(email, emailParams);
            email.addTextWatcher(s -> {
                if (Format.isValidEmail(s)) {
                    send.setEnabled(true);
                    send.setOnClickListener(StartFragment.this);
                } else {
                    send.setEnabled(false);
                }
            });

            send = new MyButton(getContext());
            send.setText(R.string.fui_button_text_send);
            RelativeLayout.LayoutParams sendParams = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            sendParams.addRule(RelativeLayout.BELOW, email.getId());
            sendParams.addRule(RelativeLayout.ALIGN_PARENT_END);
            linearLayout.addView(send, sendParams);
            layout.addView(linearLayout);
        }
        else {
            MyTitle noInternet = new MyTitle(requireContext());
            noInternet.setText(R.string.sign_in_no_internet);
            MyButton goBack = new MyButton(requireContext());
            goBack.setText(R.string.go_back);
            goBack.setOnClickListener(v -> requireActivity().finish());
            layout.addView(noInternet);
            layout.addView(goBack);
        }
    }

    @Override
    public void onClick (View v) {
        if (v == send) {
            // check email not valid, send error message
            if (!Format.isValidEmail(email.getText())){
                MyToast toast = new MyToast(getContext());
                toast.setMessage(R.string.fui_invalid_email_address)
                        .show();
                return;
            }
            // email valid -> check, if an user already exists
            String emailStr = Objects.requireNonNull(email.getText());
            Firestore.getUserByEmail(emailStr, new MyCompleteListener<QuerySnapshot>() {
                @SuppressWarnings("CatchMayIgnoreException")
                @Override
                public void onSuccess (QuerySnapshot querySnapshot) {
                    // if size != 0
                    // -> email does exist in firestore ->
                    //  -> is verified? => ask password --> Auth
                    //  -> not verified => set password --> CloudFunction
                    // else -> create new user
                    try {
                        List<DocumentSnapshot> list = querySnapshot.getDocuments();
                        MyToast toast = new MyToast(getContext());
                        user.setEmail(emailStr);
                        if (list.size() != 0) {
                            DocumentSnapshot ds = querySnapshot.getDocuments().get(0);
                            Person p = ds.toObject(Person.class);
                            if (p == null) {
                                toast.setMessage(R.string.account_not_found).show();
                                return;
                            }
                            p.setId(ds.getId());
                            if (p.isDisabled()) {
                                // Send error message
                                toast.setMessage(R.string.account_disabled).show();
                                return;
                            }
                            if (!p.isVerified()) {
                                // send a verification email
                                Log.d(TAG, "onSuccess: email not verified");
                                toast.setMessage(R.string.account_send_verification).show();
                                // TODO: 15.02.22 send the mail after the sign in
                                Authentication.sendVerificationMail();
                                // -> still sign in after password, if password was already set.
                            }
                            Display kind = Display.SHOW;
                            // Ask for password
                            if (p.isPassword()) {
                                Log.d(TAG, "onSuccess: ask for password");
                                toast.setMessage(R.string.account_sign_in).show();
                            } else {
                                // set a new password
                                Log.d(TAG, "onSuccess: set a new password");
                                toast.setMessage(R.string.account_no_password).show();
                                kind = Display.EDIT;
                            }
                            user = (Person) p.clone();
                            getParentFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, new PasswordFragment(kind))
                                    .addToBackStack(SignInActivity.TAG)
                                    .commit();
                        } else {
                            // email has no account -> register new one
                            Log.d(TAG, "onSuccess: no account so far");
                            toast.setMessage(R.string.account_create).show();
                            getParentFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, new DataFragment())
                                    .addToBackStack(SignInActivity.TAG)
                                    .commit();
                        }
                    } catch (Exception e) {
                        onFailure(e);
                    }
                }

                @Override
                public void onFailure (Exception exception) {
                    // display an error message
                    Crashlytics.error("StartFragment", TAG, exception);
                }
            });
        }
    }

    @Override
    public void authStatusChanged () {
        getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, this).commit();
    }
}
