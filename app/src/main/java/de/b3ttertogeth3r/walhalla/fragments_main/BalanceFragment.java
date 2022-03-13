package de.b3ttertogeth3r.walhalla.fragments_main;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.NotNull;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstraction.CustomFragment;
import de.b3ttertogeth3r.walhalla.design.MyTextView;
import de.b3ttertogeth3r.walhalla.design.MyTitle;
import de.b3ttertogeth3r.walhalla.firebase.Analytics;
import de.b3ttertogeth3r.walhalla.firebase.Authentication;
import de.b3ttertogeth3r.walhalla.firebase.Crashlytics;
import de.b3ttertogeth3r.walhalla.models.Person;
import de.b3ttertogeth3r.walhalla.utils.Variables;
import static de.b3ttertogeth3r.walhalla.firebase.Firestore.*;

/**
 * @author B3tterToegth3r
 * @version 1.0
 * @since 1.0
 */
public class BalanceFragment extends CustomFragment {
    private static final String TAG = "balance.Fragment";
    private Person person;
    private MyTextView amount;


    @Override
    public void authStatusChanged () {
        getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, this).commit();
    }
    @Override
    public void start () {
        FirebaseUser user = Authentication.getUser();
        if(user == null || user.getUid().isEmpty()) {
            goHome();
        } else {
            registration.add(
                    FIRESTORE
                            .collection("Person")
                            .document(user.getUid())
                            .addSnapshotListener((value, error) -> {
                                if (error != null) {
                                    String message = "trying to listen to realtime updates of " + person.getId();
                                    Crashlytics.error(TAG, message, error);
                                    goHome();
                                }
                                if (value != null && value.exists()) {
                                    try {
                                        person = value.toObject(Person.class);
                                        updateAmount();
                                    } catch (Exception e){
                                        Crashlytics.error(TAG, "Parsing person", e);
                                        goHome();
                                    }
                                } else {
                                    // Go back to home
                                    String message = "trying to listen to realtime updates of " + user.getUid();
                                    Crashlytics.error(TAG, message, error);
                                    goHome();
                                }
                            })
            );
        }
    }

    private void goHome () {
        Analytics.screenChange(this.getId(), getString(R.string.menu_home));
        getParentFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();

    }

    public void analyticsProperties () {

    }

    @Override
    public void stop () {

    }

    @Override
    public void viewCreated () {

    }

    @Override
    public void toolbarContent () {
        toolbar.setTitle(R.string.menu_balance);
    }

    @Override
    public void createView (@NonNull @NotNull View view,
                            @NonNull @NotNull LayoutInflater inflater) {
        LinearLayout layout = view.findViewById(R.id.fragment_container);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.removeAllViews();
        layout.removeAllViewsInLayout();

        MyTitle title = new MyTitle(getContext());
        title.setText(R.string.balance_title);
        layout.addView(title);

        amount = new MyTextView(getContext());
        amount.setTextSize(TypedValue.COMPLEX_UNIT_PT, 30f);
        amount.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in));
        amount.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        layout.addView(amount);

        MyTextView description = new MyTextView(getContext());
        description.setText(R.string.balance_description);
        layout.addView(description);
    }

    /**
     * Setting the new amount inside the text view {@link #amount}
     */
    private void updateAmount () {
        try {
            //TODO read data from new place
            String string = "€ " + "0,00";//String.format(Variables.LOCALE, "%.2f", person.getBalance());
            amount.setText(string);
        } catch (Exception e) {
            Log.e(TAG, "updateAmount: formatting did not work", e);
        }
    }
}
