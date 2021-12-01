package de.b3ttertogeth3r.walhalla;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

import de.b3ttertogeth3r.walhalla.enums.Charge;
import de.b3ttertogeth3r.walhalla.firebase.Firebase;
import de.b3ttertogeth3r.walhalla.interfaces.OnGetDataListener;
import de.b3ttertogeth3r.walhalla.interfaces.SplashInterface;
import de.b3ttertogeth3r.walhalla.utils.CacheData;

/**
 * This Activity is the loading screen of the app. Every needed data is being loaded and the user is
 * displayed a progressbar. If an error occurred, the user gets an alert dialog with that error.
 * Also most variables, that could change over time are created here.
 *
 * @author B3tterTogeth3r
 * @version 1.0
 * @see AppCompatActivity
 * @since 1.0
 */
public class StartActivity extends AppCompatActivity implements SplashInterface {
    private static final String TAG = "StartActivity";
    public static SplashInterface newDone;
    private final String PREFS_NAME = "FirstStart";
    private final int total = 4;
    private int counter = 0;
    private int progress = 0;
    private ProgressBar progressBar;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setProgress(0);
        progressBar.setVisibility(View.VISIBLE);
        newDone = this;
        //Check if is apps  first start
        // https://stackoverflow.com/questions/4636141/determine-if-android-app-is-being-used-for
        // -the-first-time
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        if (settings.getBoolean("my_first_time", true) && !isOnline()) {
            Log.d(TAG, "Comments: no internet on first start");
            //TODO Display dialog with message to get internet. Terminate app on cancel and dismiss
            AlertDialog.Builder internetDialog = new AlertDialog.Builder(this);
            internetDialog.setCancelable(false)
                    .setMessage(R.string.fui_no_internet)
                    .setTitle(R.string.error)
                    .setOnDismissListener(dialog -> finish())
                    .show();
        } else {
            updateProgressbar();
            settings.edit().putBoolean("my_first_time", false).apply();
            new App();
            App.init();
        }
    }

    public boolean isOnline () {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void updateProgressbar () {
        if (counter == total) {
            progressBar.setProgress(progress, true);
            /* Go to MainActivity */
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        } else {
            counter++;
            progress = progress + (100 / total);
            progressBar.setProgress(progress, true);
        }
    }

    @Override
    public void appDone () {
        updateProgressbar();
        try {
            CacheData.init(getApplicationContext());
            Firebase.init(getApplicationContext());
        } catch (Exception e) {
            Log.e(TAG, "appDone: ", e);
        }
    }

    @Override
    public void firebaseDone () {
        // fetch dynamic links and push message intents
        updateProgressbar();
        dynamicLink();
        goOn();
    }

    /**
     * @see <a href="https://firebase.google.com/docs/dynamic-links/android/receive">Firebase
     * Dynamic Links</a>
     */
    private void dynamicLink () {
        updateProgressbar();
        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent()).addOnSuccessListener(pendingDynamicLinkData -> {
            try {
                Uri deepLink = pendingDynamicLinkData.getLink();
                if (deepLink != null) {
                    // TODO Manage dynamic link
                    Firebase.Crashlytics.log("Dynamic link found. Value is: " + deepLink);
                }
            } catch (Exception ignored) {
            }
        }).addOnFailureListener(this, e -> Firebase.Crashlytics.log(TAG + ":getDynamicLink" +
                ":onFailure", e));
    }

    void goOn () {
        // Go to start Activity after fetching dynamic links and intents from push messages
        getCharge(new OnGetDataListener(){
            @Override
            public void onSuccess () {
                updateProgressbar();
            }
        });
    }

    private void getCharge (OnGetDataListener listener) {
        if(Firebase.Authentication.isSignIn()){
            Firebase.Firestore.findUserCharge(new OnGetDataListener(){
                @Override
                public void onSuccess (String string) {
                    Charge charge = Charge.find(string);
                    CacheData.putCharge(charge);

                    listener.onSuccess();
                }

                @Override
                public void onFailure () {
                    listener.onSuccess();
                }
            });
        }
    }
}
