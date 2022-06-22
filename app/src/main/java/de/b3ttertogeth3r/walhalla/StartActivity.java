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

package de.b3ttertogeth3r.walhalla;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import de.b3ttertogeth3r.walhalla.firebase.Analytics;
import de.b3ttertogeth3r.walhalla.firebase.Authentication;
import de.b3ttertogeth3r.walhalla.firebase.CloudMessaging;
import de.b3ttertogeth3r.walhalla.firebase.Crashlytics;
import de.b3ttertogeth3r.walhalla.firebase.DynamicLinks;
import de.b3ttertogeth3r.walhalla.firebase.Firestore;
import de.b3ttertogeth3r.walhalla.firebase.InAppMessaging;
import de.b3ttertogeth3r.walhalla.firebase.RemoteConfig;
import de.b3ttertogeth3r.walhalla.firebase.Storage;
import de.b3ttertogeth3r.walhalla.interfaces.FirebaseInit;
import de.b3ttertogeth3r.walhalla.object.Log;


public class StartActivity extends AppCompatActivity implements FirebaseInit {
    private static final String TAG = "StartActivity";
    @SuppressWarnings("FieldCanBeLocal")
    private final int TOTAL = 9;
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
        new App();

        //Init cacheData here

        FirebaseInit firebaseInit = this;

        firebaseInit.Analytics(getApplicationContext());
        firebaseInit.Crashlytics(getApplicationContext());
        firebaseInit.Authentication(getApplicationContext());
        firebaseInit.CloudMessaging(getApplicationContext());
        firebaseInit.DynamicLinks(getApplicationContext());
        firebaseInit.Firestore(getApplicationContext());
        firebaseInit.InAppMessaging(getApplicationContext());
        firebaseInit.RemoteConfig(getApplicationContext());
        firebaseInit.Storage(getApplicationContext());

        /*
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
            new de.b3ttertogeth3r.walhalla.old.App();
        }*/
    }

    public boolean isOnline () {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void updateProgressbar () {
        if (counter == TOTAL) {
            progressBar.setProgress(progress, true);
            /* Go to MainActivity */
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        } else {
            counter++;
            progress = progress + (100 / TOTAL);
            progressBar.setProgress(progress, true);
            if (counter == TOTAL) {
                updateProgressbar();
            }
        }
    }

    @Override
    public void Analytics (Context context) {
        if (new Analytics().init(context)) {
            updateProgressbar();
            Log.i(TAG, "Analytics init complete");
            return;
        }
        android.util.Log.e(TAG, "Analytics init incomplete");
    }

    @Override
    public void Authentication (Context context) {
        if (new Authentication().init(context)) {
            updateProgressbar();
            Log.i(TAG, "Authentication init complete");
            return;
        }
        Log.e(TAG, "Authentication init incomplete");
    }

    @Override
    public void CloudMessaging (Context context) {
        if (new CloudMessaging().init(context)) {
            updateProgressbar();
            Log.i(TAG, "CloudMessaging init complete");
            return;
        }
        Log.i(TAG, "CloudMessaging init incomplete");
    }

    @Override
    public void Crashlytics (Context context) {
        if (new Crashlytics().init(context)) {
            updateProgressbar();
            Log.i(TAG, "Crashlytics init complete");
            return;
        }
        Log.i(TAG, "Crashlytics init incomplete");
    }

    @Override
    public void DynamicLinks (Context context) {
        if (new DynamicLinks().init(context)) {
            updateProgressbar();
            Log.i(TAG, "DynamicLinks init complete");
            return;
        }
        Log.i(TAG, "DynamicLinks init incomplete");
    }

    @Override
    public void Firestore (Context context) {
        if (new Firestore().init(context)) {
            updateProgressbar();
            Log.i(TAG, "Firestore init complete");
            return;
        }
        Log.i(TAG, "Firestore init incomplete");
    }

    @Override
    public void InAppMessaging (Context context) {
        if (new InAppMessaging().init(context)) {
            updateProgressbar();
            Log.i(TAG, "InAppMessaging init complete");
            return;
        }
        Log.i(TAG, "InAppMessaging init incomplete");
    }

    @Override
    public void RemoteConfig (Context context) {
        if (new RemoteConfig().init(context)) {
            RemoteConfig.apply();
            RemoteConfig.update();
            updateProgressbar();
            Log.i(TAG, "RemoteConfig init complete");
            return;
        }
        Log.i(TAG, "RemoteConfig init incomplete");
    }

    @Override
    public void Storage (Context context) {
        if (new Storage().init(context)) {
            updateProgressbar();
            Log.i(TAG, "Storage init complete");
            return;
        }
        Log.i(TAG, "Storage init incomplete");
    }
}
