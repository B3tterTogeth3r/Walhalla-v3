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

package de.b3ttertogeth3r.walhalla.old.fragments_main.drinks;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.old.abstraction.CustomFragment;
import de.b3ttertogeth3r.walhalla.old.design.MyTable;
import de.b3ttertogeth3r.walhalla.old.design.MyTableRow;
import de.b3ttertogeth3r.walhalla.old.design.MyTextView;
import de.b3ttertogeth3r.walhalla.old.design.MyToast;
import de.b3ttertogeth3r.walhalla.old.enums.Page;
import de.b3ttertogeth3r.walhalla.old.firebase.Firestore;
import de.b3ttertogeth3r.walhalla.old.interfaces.MyCompleteListener;
import de.b3ttertogeth3r.walhalla.old.models.Drink;
import de.b3ttertogeth3r.walhalla.old.models.DrinkPrice;
import de.b3ttertogeth3r.walhalla.old.utils.CacheData;
import de.b3ttertogeth3r.walhalla.old.utils.Variables;

/**
 * @author B3tterToegth3r
 * @version 1.0
 * @since 1.0
 */
public class Fragment extends CustomFragment {
    private static final String TAG = "drink.Fragment";
    private static final ArrayList<Drink> drinkList = new ArrayList<>();
    private final ArrayList<DrinkPrice> currentPriceList = new ArrayList<>();
    private LinearLayout layout;

    @Override
    public void authStatusChanged () {
        getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, this).commit();
    }

    @Override
    public void start () {
        Firestore.getUserDrinks(new MyCompleteListener<QuerySnapshot>() {
            @Override
            public void onSuccess (QuerySnapshot querySnapshot) {
                drinkList.clear();
                for (DocumentSnapshot ds : querySnapshot.getDocuments()) {
                    try {
                        Drink drink = ds.toObject(Drink.class);
                        if (drink != null) {
                            drinkList.add(drink);
                        }
                    } catch (Exception ignored) {
                    }
                }
                drinkList.sort(Comparator.comparing(Drink::getDate));
                fillTable();
            }

            @Override
            public void onFailure (Exception e) {
            }
        });

        Firestore.getDrinkValues(new MyCompleteListener<QuerySnapshot>() {
            @Override
            public void onSuccess (QuerySnapshot querySnapshot) {
                Log.e(TAG, "onSuccess: task successful");
                currentPriceList.clear();
                for (DocumentSnapshot ds : querySnapshot.getDocuments()) {
                    try {
                        DrinkPrice price = ds.toObject(DrinkPrice.class);
                        if (price != null && price.isAvailable()) {
                            currentPriceList.add(price);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "onSuccess: not really successful", e);
                    }
                }
            }

            @Override
            public void onFailure (Exception e) {
            }

            @Override
            public void onFailure () {
                Log.e(TAG, "onFailure: task failed");
            }
        });
    }

    private void fillTable () {
        layout.removeAllViewsInLayout();
        MyTable table = new MyTable(requireContext());
        MyTableRow row = new MyTableRow(requireContext());
        MyTextView date = new MyTextView(requireContext());
        MyTextView amount = new MyTextView(requireContext());
        MyTextView value = new MyTextView(requireContext());
        MyTextView name = new MyTextView(requireContext());

        date.setText(R.string.drink_date);
        amount.setText(R.string.drink_amount);
        value.setText(R.string.drink_value);
        name.setText(R.string.drink_name);

        row.addView(date);
        row.addView(amount);
        row.addView(value);
        row.addView(name);

        table.addView(row);

        for (Drink drink : drinkList) {
            row = new MyTableRow(requireContext());
            date = new MyTextView(requireContext());
            amount = new MyTextView(requireContext());
            value = new MyTextView(requireContext());
            name = new MyTextView(requireContext());

            //Format date
            Calendar c = Calendar.getInstance();
            c.setTime(drink.getDate().toDate());
            String helper = c.get(Calendar.DAY_OF_MONTH) + ". " +
                    Variables.MONTHS[c.get(Calendar.MONTH)] + " " +
                    c.get(Calendar.YEAR);
            date.setText(helper);
            amount.setText(drink.getAmount());
            String price = "€ " + (drink.getAmount() + drink.getPrice());
            value.setText(price);
            name.setText(drink.getKind());

            row.addView(date);
            row.addView(amount);
            row.addView(value);
            row.addView(name);

            table.addView(row);
        }

    }

    @Override
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
        toolbar.setTitle(R.string.menu_drinks);
        toolbar.getMenu().clear();
        if (Page.BEER.canEditPage(CacheData.getCharge())) {
            //TODO add add menu with its dialogs
            toolbar.inflateMenu(R.menu.drink);
            toolbar.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                MyToast toast = new MyToast(requireContext());
                if (id == R.id.menu_send_reminder) {
                    toast.setText("Reminder not yet possible");
                } else if (id == R.id.menu_drink_fine) {
                    toast.setText("Fines can be added soon");
                } else if (id == R.id.menu_drink_invoice) {
                    toast.setText("An invoice can get added soon + currentPriceList.size():" + currentPriceList.size());
                } else if (id == R.id.menu_drink_payment) {
                    toast.setText("A payment can be made soon");
                }
                toast.show();
                return false;
            });
        }
    }

    @Override
    public void createView (@NonNull @NotNull View view,
                            @NonNull @NotNull LayoutInflater inflater) {
        layout = view.findViewById(R.id.fragment_container);
    }
}