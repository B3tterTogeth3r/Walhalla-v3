/*
 * Copyright (c) 2022.
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

package de.b3ttertogeth3r.walhalla.old;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.old.firebase.DynamicLinks;
import de.b3ttertogeth3r.walhalla.old.firebase.Firebase;
import de.b3ttertogeth3r.walhalla.old.firebase.Firestore;
import de.b3ttertogeth3r.walhalla.old.interfaces.MyCompleteListener;
import de.b3ttertogeth3r.walhalla.old.interfaces.SplashInterface;
import de.b3ttertogeth3r.walhalla.old.utils.CacheData;
import de.b3ttertogeth3r.walhalla.old.utils.MyLog;

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
    private final int total = 6;
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
        try {
            CacheData.init(getApplicationContext());
        } catch (Exception e) {
            Log.e(TAG, "CacheData:", e);
        }

        if (!CacheData.getFirstStart() && !isOnline()) {
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
            if(counter == total) {
                updateProgressbar();
            }
        }
    }

    @Override
    public void appDone () {
        updateProgressbar();
        try {
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
        DynamicLinks.DYNAMIC_LINKS = FirebaseDynamicLinks.getInstance();
        try {
            DynamicLinks.getLink(getIntent(), new MyCompleteListener<Void>() {
                @Override
                public void onSuccess (@Nullable Void result) {
                    updateProgressbar();
                }

                @Override
                public void onFailure (@Nullable Exception exception) {
                    updateProgressbar();
                }
            });
        } catch (NullPointerException npe) {
            updateProgressbar();
        }
    }

    void goOn () {
        // Go to start Activity after fetching dynamic links and intents from push messages
        Firestore.getUserCharge(new MyCompleteListener<Void>() {

            @Override
            public void onSuccess (Void voids) {
                setSize();
            }

            @Override
            public void onFailure (Exception exception) {
                setSize();
            }
        });
    }

    void setSize(){
        updateProgressbar(); // from goOn()
        // TODO: 08.02.22 find a unit to use and use it for every design value
        float size = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_PT,
                1f,
                getApplicationContext().getResources().getDisplayMetrics()
        );
        MyLog.d(TAG, "setSize: " + size);
        updateProgressbar();
    }
}
