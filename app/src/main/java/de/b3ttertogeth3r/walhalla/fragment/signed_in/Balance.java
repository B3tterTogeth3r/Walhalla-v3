/*
 * Copyright (c) 2022-2022.
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

package de.b3ttertogeth3r.walhalla.fragment.signed_in;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstract_classes.Fragment;
import de.b3ttertogeth3r.walhalla.abstract_classes.Loader;
import de.b3ttertogeth3r.walhalla.design.Button;
import de.b3ttertogeth3r.walhalla.design.LinearLayout;
import de.b3ttertogeth3r.walhalla.design.Title;
import de.b3ttertogeth3r.walhalla.design.Toast;
import de.b3ttertogeth3r.walhalla.firebase.Firebase;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IFirestoreDownload;
import de.b3ttertogeth3r.walhalla.object.Account;
import de.b3ttertogeth3r.walhalla.object.Log;
import de.b3ttertogeth3r.walhalla.object.Movement;
import de.b3ttertogeth3r.walhalla.util.ToastList;

/**
 * @author B3tterTogeth3r
 * @see Fragment
 * @see de.b3ttertogeth3r.walhalla.old.firebase.Firestore Firestore
 * @see <a href="https://www.youtube.com/watch?v=UWnbPp4PedQ">Android Development Tutorial - Google
 * In App Purchase by EDMT Dev</a> (May/30/2022)
 * @see <a href="https://developers.google.com/pay/api/android/overview">Google Pay for Payments ->
 * Android</a> (May/30/2022)
 * @see <a href="https://developer.android.com/google/play/billing">Android developers -> Google
 * Play's Billing System</a> (May/30/2022)
 */
public class Balance extends Fragment implements View.OnClickListener {
    private static final String TAG = "Balance";
    private Button subscribe, payBill;
    private IFirestoreDownload download;
    private Account account;
    private Title balance;
    private TableLayout movements;

    @Override
    public void constructor() {
        download = Firebase.firestoreDownload();
    }

    @Override
    public String analyticsProperties() {
        return TAG;
    }

    @Override
    public void start() {
        registration.add(download.listenPersonBalance("1", new Loader<Account>(false) {
            @Override
            public void onSuccessListener(@Nullable Account result) {
                account = result;
                getMovements();
                @SuppressLint("DefaultLocale")
                String price = "€ " + String.format("%.2f", account.getAmount()).replace(".", ",");
                balance.setText(price);
            }

            @Override
            public void onFailureListener(Exception e) {
                Log.e(TAG, "Listening to the account did not work", e);
            }
        }));
    }

    private void getMovements() {
        download.getPersonMovements("1")
                .setOnSuccessListener(result -> {
                    if (result == null) {
                        // TODO: 15.06.22 find where exceptions in here are thrown to
                        return;
                    }
                    // movementList = new ArrayList<>(result);
                    addMovementsToDesign(result);
                })
                .setOnFailListener(e ->
                        Log.e(TAG, "Downloading the movements didn't work", e));
    }

    private void addMovementsToDesign(@NonNull ArrayList<Movement> result) {
        // TODO: 07.06.22 figure out why nothing is displayed... The list has the supposed size
        movements.removeAllViews();
        movements.removeAllViewsInLayout();
        for (Movement m : result) {
            movements.addView(m.getView(getContext()));
        }
        movements.invalidate();
    }

    @Override
    public void toolbarContent() {
        toolbar.setTitle(R.string.menu_balance);
    }

    @Override
    @SuppressLint("DefaultLocale")
    public void createView(@NonNull android.widget.LinearLayout view) {
        view.setOrientation(android.widget.LinearLayout.VERTICAL);
        //region balance
        balance = new Title(getContext());
        String price = "€ " + String.format("%.2f", 0f).replace(".", ",");
        balance.setText(price);
        view.addView(balance);
        //endregion

        //region payment buttons
        LinearLayout paymentButtons = new LinearLayout(requireContext());
        paymentButtons.setOrientation(android.widget.LinearLayout.HORIZONTAL);
        subscribe = new Button(getContext());
        payBill = new Button(getContext());
        subscribe.setText("Subscribe to a regular payment");
        payBill.setText("Pay bill");
        paymentButtons.addView(subscribe);
        paymentButtons.addView(payBill);
        view.addView(paymentButtons);
        //endregion

        //region movement list
        movements = new TableLayout(getContext());
        view.addView(movements);
        //endregion

        //region advertisement
        // a banner ad on the bottom of the screen
        // TODO: 30.05.22 add app to GoogleAdMob
        //endregion
    }

    @Override
    public void viewCreated() {
        subscribe.setOnClickListener(this);
        payBill.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO: 30.05.22 implement in app payments
        ToastList.addToast(Toast.makeToast(getContext(), R.string.error_dev));
        if (subscribe.equals(v)) {

        } else if (payBill.equals(v)) {

        }
    }
}
