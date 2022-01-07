package de.b3ttertogeth3r.walhalla;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import de.b3ttertogeth3r.walhalla.firebase.Authentication;
import de.b3ttertogeth3r.walhalla.fragments_signin.StartFragment;
import de.b3ttertogeth3r.walhalla.interfaces.Reload;
import de.b3ttertogeth3r.walhalla.models.Person;

public class SignInActivity extends AppCompatActivity implements Reload {
    public static final String TAG = "SignInActivity";
    public static Person user = new Person();
    public static Bitmap imageBitmap;
    private FragmentManager fm;
    public static Reload finish;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        fm = getSupportFragmentManager();
        finish = this;

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new StartFragment())
                    .commit();
        }
    }

    @Override
    public void onBackPressed () {
        if (fm.getBackStackEntryCount() == 0) {
            finishActivity(Activity.RESULT_CANCELED);
            super.onBackPressed();
            return;
        }
        fm.popBackStack();
    }

    @Override
    public void site () {
        finishActivity(Activity.RESULT_OK);
        this.finish();
    }
}
