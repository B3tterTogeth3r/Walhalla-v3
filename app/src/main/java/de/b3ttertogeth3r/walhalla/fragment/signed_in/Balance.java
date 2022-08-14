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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstract_generic.Fragment;
import de.b3ttertogeth3r.walhalla.design.Button;
import de.b3ttertogeth3r.walhalla.design.LinearLayout;
import de.b3ttertogeth3r.walhalla.design.SideNav;
import de.b3ttertogeth3r.walhalla.design.TableLayout;
import de.b3ttertogeth3r.walhalla.design.Text;
import de.b3ttertogeth3r.walhalla.design.Title;
import de.b3ttertogeth3r.walhalla.design.Toast;
import de.b3ttertogeth3r.walhalla.exception.NoDataException;
import de.b3ttertogeth3r.walhalla.firebase.Firebase;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IAuth;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IFirestoreDownload;
import de.b3ttertogeth3r.walhalla.object.Account;
import de.b3ttertogeth3r.walhalla.object.Log;
import de.b3ttertogeth3r.walhalla.object.Movement;
import de.b3ttertogeth3r.walhalla.util.Values;

/**
 * This Fragment displays the current balance of a user and its last movements.
 *
 * @author B3tterTogeth3r
 * @see Fragment
 * @see de.b3ttertogeth3r.walhalla.firebase.Firestore Firestore
 * @see <a href="https://www.youtube.com/watch?v=UWnbPp4PedQ">Android Development Tutorial - Google
 * In App Purchase by EDMT Dev</a> (May/30/2022)
 * @see <a href="https://developers.google.com/pay/api/android/overview">Google Pay for Payments -
 * Android</a> (May/30/2022)
 * @see <a href="https://developer.android.com/google/play/billing">Android developers - Google
 * Play's Billing System</a> (May/30/2022)
 */
public class Balance extends Fragment implements View.OnClickListener {
    private static final String TAG = "Balance";
    private Button subscribe, payBill;
    private IFirestoreDownload download;
    private Account account;
    private Title balance;
    private LinearLayout movements;
    private String uid;
    private ArrayList<Movement> movementList;

    @Override
    public void constructor() {
        movementList = new ArrayList<>();
        download = Firebase.Firestore.download();
        IAuth auth = Firebase.authentication();
        if (!auth.isSignIn()) {
            Toast.makeToast(requireContext(), R.string.fui_error_session_expired).show();
            SideNav.changePage(R.string.menu_home, requireActivity().getSupportFragmentManager().beginTransaction());
            return;
        }
        uid = auth.getUser().getUid();
    }

    @Override
    public String analyticsProperties() {
        return TAG;
    }

    @Override
    public void start() {
        try {
            download.getPersonBalance(requireActivity(), uid)
                    .setOnSuccessListener(result -> {
                        account = result;
                        getMovements();
                        String price = "€ " + String.format(Values.LOCALE, "%.2f", account.getAmount()).replace(".", ",");
                        Log.i(TAG, "start: " + price);
                        balance.setText(price);
                    })
                    .setOnFailListener(e -> Log.e(TAG, "onFailureListener: Listening to the account did not work", e));
        } catch (Exception ignored) {
        }
    }

    private void getMovements() {
        download.getPersonMovements(uid)
                .setOnSuccessListener(result -> {
                    if (result == null) {
                        throw new NoDataException("User has no movements");
                    } else if (result.isEmpty()) {
                        Movement m = new Movement();
                        m.setPurpose("No movements");
                        movementList.add(m);
                        movementTable();
                        return;
                    }
                    movementList = result;
                    movementTable();
                })
                .setOnFailListener(e ->
                        Log.e(TAG, "Downloading the movements didn't work", e));
    }

    private void movementTable() {
        movements.removeAllViewsInLayout();
        movements.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        TableLayout table = new TableLayout(requireContext());
        TableRow titleRow = (TableRow) LayoutInflater.from(requireContext()).inflate(R.layout.movement_layout, null);
        Text date = titleRow.findViewById(R.id.date);
        date.setTitle()
                .setText(R.string.balance_date);
        Text income = titleRow.findViewById(R.id.income);
        income.setTitle().setText(R.string.balance_income);
        Text expense = titleRow.findViewById(R.id.expense);
        expense.setTitle().setText(R.string.balance_expense);
        Text purpose = titleRow.findViewById(R.id.purpose);
        purpose.setTitle().setText(R.string.balance_purpose);
        Text add = titleRow.findViewById(R.id.info);
        add.setTitle().setText("");
        table.addView(titleRow);

        if (!movementList.isEmpty()) {
            for (Movement m : movementList) {
                table.addView(
                        de.b3ttertogeth3r.walhalla.design.Movement.create(
                                        requireActivity(), null, m)
                                .show()
                );
            }
        }
        movements.addView(table);
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

        balance = new Title(getContext());
        view.addView(balance);

        //payButtons(view);

        //region movement list
        movements = new LinearLayout(requireContext());
        HorizontalScrollView hsv = new HorizontalScrollView(requireContext());
        hsv.addView(movements);
        view.addView(hsv);
        //endregion
    }

    @Override
    public FragmentActivity authStatusChanged(FirebaseAuth firebaseAuth) {
        return requireActivity();
    }

    /**
     * TODO Integrate Google pay billing or a different way to pay the amount
     *
     * @see <a href="https://developer.android.com/google/play/billing">Google Pay biling</a>
     */
    private void payButtons(@NonNull android.widget.LinearLayout view) {
        LinearLayout paymentButtons = new LinearLayout(requireContext());
        paymentButtons.setOrientation(android.widget.LinearLayout.HORIZONTAL);
        subscribe = new Button(getContext());
        payBill = new Button(getContext());
        subscribe.setText("Subscribe to a regular payment");
        payBill.setText("Pay bill");
        paymentButtons.addView(subscribe);
        paymentButtons.addView(payBill);
        view.addView(paymentButtons);
        subscribe.setOnClickListener(this);
        payBill.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (subscribe.equals(v)) {
            Toast.makeToast(requireContext(), R.string.error_dev).show();
        } else if (payBill.equals(v)) {
            Toast.makeToast(requireContext(), R.string.error_dev).show();
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "Fragment: Balance extends Fragment implements View.OnClickListener";
    }
}
