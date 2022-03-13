package de.b3ttertogeth3r.walhalla;

import static de.b3ttertogeth3r.walhalla.utils.Variables.CHANGE_COMPLETE;
import static de.b3ttertogeth3r.walhalla.utils.Variables.REGISTER_COMPLETE;
import static de.b3ttertogeth3r.walhalla.utils.Variables.SIGN_IN_COMPLETE;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import de.b3ttertogeth3r.walhalla.design.MyToast;
import de.b3ttertogeth3r.walhalla.firebase.Authentication;
import de.b3ttertogeth3r.walhalla.firebase.Crashlytics;
import de.b3ttertogeth3r.walhalla.fragments_signin.StartFragment;
import de.b3ttertogeth3r.walhalla.interfaces.MyCompleteListener;
import de.b3ttertogeth3r.walhalla.models.Person;
import de.b3ttertogeth3r.walhalla.utils.MyLog;

public class SignInActivity extends AppCompatActivity implements MyCompleteListener<Integer> {
    public static final String TAG = "SignInActivity";
    public static Person user = new Person();
    public static Bitmap imageBitmap;
    public static MyCompleteListener<Integer> uploadListener;
    private FragmentManager fm;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        fm = getSupportFragmentManager();
        uploadListener = this;

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new StartFragment())
                    .commit();
        }
    }

    @Override
    public void onBackPressed () {
        MyLog.e(TAG, "BackStackSize: " + fm.getBackStackEntryCount());
        if (fm.getBackStackEntryCount() == 0) {
            // TODO: 15.02.22 doesn't work anymore, but did a month ago...
            setResult(Activity.RESULT_CANCELED);
            finish();
            return;
        }
        fm.popBackStack();
    }

    @Override
    public void onSuccess (Integer result) {
        if (result == null) {
            return;
        }
        switch (result) {
            case REGISTER_COMPLETE:
                Authentication.sendVerificationMail();
                setResult(Activity.RESULT_OK);
                finish();
                MyLog.d(TAG, "onSuccess: register complete");
                break;
            case CHANGE_COMPLETE:
                MyLog.d(TAG, "onSuccess: change password complete");
                break;
            case SIGN_IN_COMPLETE:
                MyLog.d(TAG, "onSuccess: sign in complete");
                break;
            default:
                break;
        }
    }

    @Override
    public void onFailure (Exception exception) {
        Crashlytics.error(TAG, "MyCompleteListener failed", exception);
    }
}
