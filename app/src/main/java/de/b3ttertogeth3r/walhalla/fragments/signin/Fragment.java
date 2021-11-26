package de.b3ttertogeth3r.walhalla.fragments.signin;

import static de.b3ttertogeth3r.walhalla.utils.Variables.GOOGLE_CLIENT_ID;

import android.content.Intent;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firebase.ui.auth.util.ui.SupportVectorDrawablesButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.annotations.NotNull;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstraction.CustomFragment;
import de.b3ttertogeth3r.walhalla.dialog.SignInDialogMail;
import de.b3ttertogeth3r.walhalla.enums.LoginKinds;
import de.b3ttertogeth3r.walhalla.firebase.Firebase;
import de.b3ttertogeth3r.walhalla.firebase.Firebase.Authentication.SignInListener;

/**
 * @author B3tterToegth3r
 * @version 1.0
 * @see <a href="https://firebase.google.com/docs/auth/android/firebaseui">Firebase sign in ui</a>
 * @since 1.0
 */
public class Fragment extends CustomFragment implements View.OnClickListener, SignInListener {
    private static final String TAG = "signIn.Fragment";
    private static final int GOOGLE_SIGN_IN = 9001;
    public static SignInListener signInListener;
    private Button register;
    private SupportVectorDrawablesButton email, google, github, twitter, facebook;

    //region unused override methods
    @Override
    public void start () {
    }

    @Override
    public void analyticsProperties () {

    }

    @Override
    public void stop () {

    }
    //endregion

    @Override
    public void viewCreated () {
        register.setOnClickListener(this);
        email.setOnClickListener(this);
        google.setOnClickListener(this);
        github.setOnClickListener(this);
    }

    @Override
    public void toolbarContent () {
        toolbar.setTitle(R.string.menu_login);
    }

    @Override
    public void createView (@NonNull @NotNull View view,
                            @NonNull @NotNull LayoutInflater inflater) {
        signInListener = this;
        int padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                8f,
                getResources().getDisplayMetrics()
        );
        LinearLayout layout = view.findViewById(R.id.fragment_container);
        RelativeLayout.LayoutParams layoutParams =
                new RelativeLayout.LayoutParams(layout.getLayoutParams());
        layoutParams.setMargins(padding, padding, padding, padding);

        //Twitter
        Twitter.initialize(new TwitterConfig.Builder(requireContext())
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(getString(R.string.twitter_consumer_key),
                        getString(R.string.twitter_consumer_secret)))
                .debug(true).build());

        // register
        register = (Button) inflater.inflate(R.layout.custom_button, null);
        register.setText(R.string.fui_title_register_email);
        // or log in with google, facebook, etc.
        email = (SupportVectorDrawablesButton) inflater.inflate(R.layout.fui_provider_button_email, null);
        google = (SupportVectorDrawablesButton) inflater.inflate(R.layout.fui_idp_button_google,
                null);
        github = (SupportVectorDrawablesButton) inflater.inflate(R.layout.fui_idp_button_github,
                null);

        //region add to layout
        layout.addView(register, layoutParams);
        layout.addView(email, layoutParams);
        layout.addView(google, layoutParams);
        //layout.addView(facebook, layoutParams);
        //layout.addView(twitter, layoutParams);
        layout.addView(github, layoutParams);
        //endregion
    }

    @Override
    public void onClick (View v) {
        if (v == register) {
            Log.d(TAG, "onClick: register");
            Toast.makeText(getContext(), R.string.error_dev, Toast.LENGTH_LONG).show();
        } else if (v == email) {
            Log.d(TAG, "onClick: email");
            Toast.makeText(getContext(), R.string.error_dev, Toast.LENGTH_LONG).show();
        } else if (v == google) {
            Log.d(TAG, "onClick: google");
            signInGoogle();
        } else if (v == facebook) {
            Log.d(TAG, "onClick: facebook");
            Toast.makeText(getContext(), R.string.error_dev, Toast.LENGTH_LONG).show();
        } else if (v == github) {
            Log.d(TAG, "onClick: github");
            SignInDialogMail.display(getParentFragmentManager(), LoginKinds.GITHUB);
        } else {
            Log.d(TAG, "onClick: an invalid button was clicked.");
        }
    }

    private void signInGoogle () {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(GOOGLE_CLIENT_ID)
                        .requestEmail()
                        .build();
        // Build a GoogleSignInClient with the options specified by gso.
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso);
        Intent googleIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(googleIntent, GOOGLE_SIGN_IN);
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode " + requestCode);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                Firebase.Authentication.firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.e(TAG, "Google sign in failed", e);
            }
        }
    }

    @Override
    public void statusChange (boolean success) {
        if (success) {
            //TODO switch back to home site
            Firebase.Analytics.screenChange(R.string.menu_home, getString(R.string.menu_home));
            getParentFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new de.b3ttertogeth3r.walhalla.fragments.program.Fragment()).commit();
        }
    }
}
