package de.b3ttertogeth3r.walhalla;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * This activity is just to remove the white loading screen and wait until all the necessary values
 * and methods have been loaded before moving on to {@link StartActivity}.
 *
 * @author B3tterTogeth3r
 * @version 1.0
 * @see AppCompatActivity
 * @since 1.0
 */
public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent mainIntent = new Intent(this, StartActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
