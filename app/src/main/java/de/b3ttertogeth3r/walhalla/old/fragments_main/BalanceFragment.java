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

package de.b3ttertogeth3r.walhalla.old.fragments_main;

import static de.b3ttertogeth3r.walhalla.old.firebase.Firestore.FIRESTORE;

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
import de.b3ttertogeth3r.walhalla.old.abstraction.CustomFragment;
import de.b3ttertogeth3r.walhalla.old.design.MyTextView;
import de.b3ttertogeth3r.walhalla.old.design.MyTitle;
import de.b3ttertogeth3r.walhalla.old.firebase.Analytics;
import de.b3ttertogeth3r.walhalla.old.firebase.Authentication;
import de.b3ttertogeth3r.walhalla.old.firebase.Crashlytics;
import de.b3ttertogeth3r.walhalla.old.models.Person;

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
