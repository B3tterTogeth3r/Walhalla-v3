/*
 * Copyright (c) 2022-2023.
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

import static de.b3ttertogeth3r.walhalla.firebase.Firebase.Firestore.download;
import static de.b3ttertogeth3r.walhalla.firebase.Firebase.Firestore.upload;
import static de.b3ttertogeth3r.walhalla.firebase.Firebase.authentication;
import static de.b3ttertogeth3r.walhalla.interfaces.RealtimeListeners.stopRealtimeListener;
import static de.b3ttertogeth3r.walhalla.util.Cache.CACHE_DATA;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstract_generic.Fragment;
import de.b3ttertogeth3r.walhalla.abstract_generic.Loader;
import de.b3ttertogeth3r.walhalla.design.Button;
import de.b3ttertogeth3r.walhalla.design.LinearLayout;
import de.b3ttertogeth3r.walhalla.design.SideNav;
import de.b3ttertogeth3r.walhalla.design.TableLayout;
import de.b3ttertogeth3r.walhalla.design.Text;
import de.b3ttertogeth3r.walhalla.design.Title;
import de.b3ttertogeth3r.walhalla.design.Toast;
import de.b3ttertogeth3r.walhalla.dialog.MovementPurposeDialog;
import de.b3ttertogeth3r.walhalla.dialog.NumericDialog;
import de.b3ttertogeth3r.walhalla.dialog.PersonSearchDialog;
import de.b3ttertogeth3r.walhalla.exception.CreateDialogException;
import de.b3ttertogeth3r.walhalla.object.Account;
import de.b3ttertogeth3r.walhalla.object.Movement;
import de.b3ttertogeth3r.walhalla.util.Log;
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
    private Account account;
    private Title balance;
    private LinearLayout movements;
    private String uid;
    private ArrayList<Movement> movementList;

    @Override
    public void constructor() {
        movementList = new ArrayList<>();
        if (!authentication().isSignIn()) {
            Toast.makeToast(requireContext(), R.string.fui_error_session_expired).show();
            SideNav.changePage(R.string.menu_home, requireActivity().getSupportFragmentManager().beginTransaction());
            return;
        }
        uid = authentication().getUser().getUid();
    }

    @Override
    public String analyticsProperties() {
        return TAG;
    }

    @Override
    public void start() {
        balance();
        getMovements();
    }

    @Override
    public void toolbarContent() {
        toolbar.setTitle(R.string.menu_balance);
        toolbar.getMenu().clear();
        if (CACHE_DATA.isBoardMember()) {
            toolbar.inflateMenu(R.menu.add);
            toolbar.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.add) {
                    AlertDialog dialog = new AlertDialog.Builder(requireContext())
                            .setTitle("Einnahme hinzufügen")
                            .setMessage("Soll eine Einnahme einem Nutzer hinzugefügt werden?")
                            .setPositiveButton(R.string.yes, (dialogInterface, i) -> showPersonDialog())
                            .setNegativeButton(R.string.no, null)
                            .create();
                    dialog.show();
                    return true;
                }
                return false;
            });
        }
    }

    @Override
    @SuppressLint("DefaultLocale")
    public void createView(@NonNull android.widget.LinearLayout view) {
        view.setOrientation(android.widget.LinearLayout.VERTICAL);

        balance = new Title(getContext());
        view.addView(balance);

        payButtons(view);

        //region movement list
        movements = new LinearLayout(requireContext());
        HorizontalScrollView hsv = new HorizontalScrollView(requireContext());
        hsv.addView(movements);
        view.addView(hsv);
        //endregion
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
    public void stop() {
        stopRealtimeListener();
    }

    @Override
    public FragmentActivity authStatusChanged(FirebaseAuth firebaseAuth) {
        return requireActivity();
    }

    /**
     * Download the movements
     */
    private void getMovements() {
        download().getPersonMovements(uid)
                .setOnSuccessListener(result -> {
                    if (result == null || result.isEmpty()) {
                        movements.removeAllViewsInLayout();
                        movements.addView(new Title(requireContext(), "You have no movements so far."));
                        Log.i(TAG, "getMovements: user has no movements so far.");
                        return;
                    }
                    movementList = result;
                    movementTable();
                })
                .setOnFailListener(e ->
                        Log.e(TAG, "Downloading the movements didn't work", e));
    }

    /**
     * Format the downloaded movements into a table and display it.
     */
    @SuppressLint("InflateParams")
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
        titleRow.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.border_bottom_black));
        table.addView(titleRow);

        if (!movementList.isEmpty()) {
            for (int i = 0; i < movementList.size(); i++) {
                Movement m = movementList.get(i);
                TableRow row = (TableRow) LayoutInflater.from(requireContext()).inflate(R.layout.movement_layout, null);
                SimpleDateFormat day = new SimpleDateFormat("dd.MM.yyyy", Values.LOCALE);
                ((Text) row.findViewById(R.id.date)).setText(day.format(m.getTime().toDate()));
                double dAmount = m.getAmount();
                String value = String.format(Values.LOCALE, "%.2f", dAmount);
                if (dAmount == 0) {
                    ((Text) row.findViewById(R.id.income)).setText("€ 0,-");
                    ((Text) row.findViewById(R.id.expense)).setText("€ 0,-");
                } else if (dAmount < 0) {
                    ((Text) row.findViewById(R.id.income)).setText("€ 0,-");
                    ((Text) row.findViewById(R.id.expense)).setText(String.format("€ %s", value));
                } else if (dAmount > 0) {
                    ((Text) row.findViewById(R.id.income)).setText(String.format("€ %s", value));
                    ((Text) row.findViewById(R.id.expense)).setText("€ 0,-");
                }
                if (m.getPurpose() != null && !m.getPurpose().isEmpty()) {
                    ((Text) row.findViewById(R.id.purpose)).setText(m.getPurpose());
                } else {
                    ((Text) row.findViewById(R.id.purpose)).setText("");
                }
                if (!m.getAdd().isEmpty()) {
                    ((Text) row.findViewById(R.id.info)).setText(m.getAdd());
                } else {
                    ((Text) row.findViewById(R.id.info)).setText(m.getAdd());
                }
                if (m.getRecipe() != null) {
                    ImageView recipe = row.findViewById(R.id.recipe);
                    recipe.setVisibility(View.VISIBLE);
                    recipe.setOnClickListener(view -> {
                        // TODO: 10.11.22 open dialog with the recipe displayed.
                    });
                }
                if (i == movementList.size() - 1) {
                    row.setBackground(null);
                }

                if (CACHE_DATA.isBoardMember()) {
                    row.setOnLongClickListener(view -> {
                        // TODO: 02.12.22 open dialog to change the the movement
                        return false;
                    });
                }
                table.addView(row);
            }
        }
        movements.addView(table);
        movements.invalidate();
    }

    private void showPersonDialog() {
        android.widget.Toast.makeText(requireContext(), "Loading person list",
                android.widget.Toast.LENGTH_SHORT).show();
        PersonSearchDialog.create(requireActivity())
                .setOnSuccessListener(result -> {
                    if (result == null) {
                        Toast.makeToast(requireContext(), R.string.fui_error_unknown).show();
                        return;
                    }
                    showAmountDialog(result.getId());
                }).setOnFailListener(e -> Toast.makeToast(requireContext(), R.string.fui_error_unknown).show());
    }

    private void showAmountDialog(String uid) throws CreateDialogException {
        NumericDialog.display(getParentFragmentManager())
                .setOnSuccessListener(result -> {
                    if (result == null) {
                        throw new NullPointerException("result of 'NumericDialog' cannot be 'null'");
                    }
                    Log.i(TAG, "showAmountDialog: Amount to add as a new Movement is " + result);
                    // Add input field to add a purpose
                    addPurpose(result, uid);
                })
                .setOnFailureListener(e -> Log.e(TAG, "showAmountDialog: Something went wrong", e));
    }

    private void addPurpose(Float amount, String uid) throws CreateDialogException {
        //TODO Why is this dialog only displayed once?
        try {
            MovementPurposeDialog.display(getChildFragmentManager(), new Loader<>())
                    .setOnSuccessListener(result -> {
                        Log.i(TAG, "addPurpose: purpose is: " + result);
                        if (result == null) {
                            result = "Einzahlung";
                        }
                        upload()
                                .addPersonMovement(uid,
                                        new Movement(Timestamp.now(), Double.valueOf(amount), "", result, null));
                        //TODO This part is working. But the synchronization and the update of the
                        // users balance in Firebase via CF does not. I'll have to fix that next.
                    })
                    .setOnFailListener(e -> {
                        if (e != null) {
                            try {
                                addPurpose(amount, uid);
                            } catch (Exception ignored) {
                            }
                        }/*
                        upload()
                                .addPersonMovement(uid,
                                        new Movement(Timestamp.now(), Double.valueOf(amount), "", "Einzahlung", null));
                                        */
                    });
        } catch (Exception e) {
            Log.e(TAG, "addPurpose: an error was found", e);
            throw e;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        balance();
        getMovements();
    }

    /**
     * Download the users balance
     */
    private void balance() {
        try {
            download().getPersonBalance(uid)
                    .setOnSuccessListener(result -> {
                        if (result == null) {
                            throw new NullPointerException();
                        }
                        account = result;
                        String price = "€ " + String.format(Values.LOCALE, "%.2f", account.getAmount()).replace(".", ",");
                        Log.i(TAG, "start: found a balance.");
                        balance.setText(price);
                        if (account.getAmount() <= 0) {
                            try {
                                payBill.setVisibility(View.GONE);
                            } catch (Exception ignored) {
                            }
                        }
                        getMovements();
                    })
                    .setOnFailListener(e -> Log.e(TAG, "onFailureListener: Listening to the account did not work", e));
        } catch (Exception e) {
            // TODO: 02.12.22 reload a bit later when activity is build and/or
            //  fragmentManager isn't doing anything anymore
            balance.setText(R.string.error_reload);
            Log.e(TAG, "Activity still running", e);
        }
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
