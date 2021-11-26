package de.b3ttertogeth3r.walhalla.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.OAuthProvider;

import java.util.ArrayList;
import java.util.List;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.enums.LoginKinds;
import de.b3ttertogeth3r.walhalla.firebase.Firebase;

public class SignInDialogMail extends DialogFragment implements DialogInterface.OnClickListener {
    private static final String TAG = "SignInDialogMail";
    private final LoginKinds kind;
    private EditText email;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public SignInDialogMail(LoginKinds kind){
        this.kind = kind;
    }

    public static void display (FragmentManager fragmentManager, LoginKinds kind) {
        try {
            SignInDialogMail dialog = new SignInDialogMail(kind);
            dialog.show(fragmentManager, TAG);
        } catch (Exception e) {
            Firebase.Crashlytics.log(TAG, "Could not start dialog");
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @NonNull
    @Override
    public Dialog onCreateDialog (@Nullable Bundle savedInstanceState) {
        Activity activity = requireActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.dialog_sign_in_mail, null);
        String title = "";
        Drawable icon = null;
        Drawable background = null;
        switch (kind){
            case GITHUB:
                title = getString(R.string.fui_sign_in_with_github);
                icon = getContext().getDrawable(R.drawable.fui_ic_github_white_24dp);
                background = getContext().getDrawable(R.drawable.fui_idp_button_background_github);
                break;
            case GOOGLE:
                title = getString(R.string.fui_sign_in_with_google);
                icon = getContext().getDrawable(R.drawable.fui_ic_googleg_color_24dp);
                background = getContext().getDrawable(R.drawable.fui_idp_button_background_google);
                break;
            case TWITTER:
                title = getString(R.string.fui_sign_in_with_twitter);
                icon = getContext().getDrawable(R.drawable.fui_ic_twitter_bird_white_24dp);
                background = getContext().getDrawable(R.drawable.fui_idp_button_background_twitter);
                break;
        }
        ImageView image = view.findViewById(R.id.imageView);
        image.setImageDrawable(icon);
        view.setBackground(background);
        email = view.findViewById(R.id.editTextTextEmailAddress);

        builder.setTitle(title);
        builder.setView(view);
        builder.setPositiveButton(R.string.send, this);
        builder.setNeutralButton(R.string.abort, this);

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Override
    public void onClick (DialogInterface dialog, int which) {
        if(which != DialogInterface.BUTTON_POSITIVE){
            dismiss();
            return;
        }
        String emailStr = email.getText().toString();
        if(!emailStr.matches(emailPattern)){
            Toast.makeText(getContext(), R.string.fui_invalid_email_address, Toast.LENGTH_LONG).show();
            return;
        }
        OAuthProvider.Builder provider;
        switch (kind){
            case GITHUB:
                provider = OAuthProvider.newBuilder("github.com");
                provider.addCustomParameter("login", email.getText().toString());
                List<String> scopes = new ArrayList<String>() {
                    {
                        add("user:email");
                    }
                };
                provider.setScopes(scopes);
                Firebase.Authentication.firebaseAuthWithGithub(getActivity(), provider);
                break;
            case TWITTER:


                provider = OAuthProvider.newBuilder("twitter.com");
                Firebase.Authentication.firebaseAuthWithTwitter(getActivity(), provider);
                break;
        }
    }
}
