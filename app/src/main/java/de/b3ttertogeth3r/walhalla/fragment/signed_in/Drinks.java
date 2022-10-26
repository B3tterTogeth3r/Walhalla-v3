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

import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstract_generic.Fragment;
import de.b3ttertogeth3r.walhalla.design.SideNav;
import de.b3ttertogeth3r.walhalla.design.TableLayout;
import de.b3ttertogeth3r.walhalla.design.Title;
import de.b3ttertogeth3r.walhalla.design.Toast;
import de.b3ttertogeth3r.walhalla.exception.NoDataException;
import de.b3ttertogeth3r.walhalla.firebase.Firebase;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IAuth;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IFirestoreDownload;
import de.b3ttertogeth3r.walhalla.object.DrinkMovement;
import de.b3ttertogeth3r.walhalla.util.Log;
import de.b3ttertogeth3r.walhalla.util.Values;

/**
 * <h1>This Fragment should only be accessible, if a user is signed in</h1>
 * <br>This Fragment is to display the users drinks of the current semester, so the user can
 * see how many drinks he consumed.
 *
 * @author B3tterTogeth3r
 * @version 1.0
 * @since 1.0
 */
public class Drinks extends Fragment {
    private static final String TAG = "Drinks";
    private String uid;
    private de.b3ttertogeth3r.walhalla.design.LinearLayout movements;
    private de.b3ttertogeth3r.walhalla.design.LinearLayout groupTable;
    private IFirestoreDownload download;

    @Override
    public void constructor() {
        download = Firebase.Firestore.download();
        IAuth auth = Firebase.authentication();
        if (!auth.isSignIn()) {
            Toast.makeToast(requireContext(), R.string.fui_error_session_expired).show();
            SideNav.changePage(R.string.menu_home, requireActivity().getSupportFragmentManager().beginTransaction());
        }
        uid = auth.getUser().getUid();
    }

    @Override
    public String analyticsProperties() {
        return TAG;
    }

    @Override
    public void start() {
        download.getPersonDrinkMovement(uid, Values.currentSemester.getId())
                .setOnSuccessListener(result -> {
                    if (result == null || result.isEmpty()) {
                        throw new NoDataException("No drink movements found");
                    }
                    displayDrinksGroup(result);
                    displayDrinks(result);
                })
                .setOnFailListener(e -> Log.e(TAG, "start: ", e));
    }

    @SuppressWarnings("ConstantConditions")
    private void displayDrinksGroup(@NonNull ArrayList<DrinkMovement> result) {
        Map<String, Float> groupList = new HashMap<>();
        for (DrinkMovement dm : result) {
            String name = dm.getViewString();
            int amount = dm.getAmount();
            float price = dm.getPrice();
            if (groupList.containsKey(name)) {
                float current = groupList.getOrDefault(name, 0f) + (amount * price);
                groupList.replace(name, current);
            } else {
                groupList.put(name, (amount * price));
            }
        }
        groupTable.removeAllViewsInLayout();
        groupTable.addView(new Title(requireContext(), R.string.drink_total));
        TableLayout table = new TableLayout(requireContext());
        for (String key : groupList.keySet()) {
            float value = groupList.getOrDefault(key, 0f);
            String valueS = "€ " + String.format(Values.LOCALE, "%.2f", value)
                    .replace(".", ",");
            table.addView(de.b3ttertogeth3r.walhalla.design.Movement.create(
                            requireActivity(), null, key, valueS)
                    .show());
        }
        groupTable.addView(table);
    }

    private void displayDrinks(@NonNull ArrayList<DrinkMovement> drinkMovements) {
        movements.removeAllViewsInLayout();
        movements.addView(new Title(requireContext(), R.string.drink_total_detail));
        TableLayout table = new TableLayout(requireContext());
        for (DrinkMovement dm : drinkMovements) {
            table.addView(
                    de.b3ttertogeth3r.walhalla.design.Movement.create(
                                    requireActivity(), null, dm)
                            .show());
        }
        movements.addView(table);
    }

    @Override
    public void toolbarContent() {
        toolbar.setTitle(R.string.menu_drinks);
        // TODO: 30.05.22 add menu to manage drinks, if the user is a board member
    }

    @Override
    public void createView(@NonNull LinearLayout view) {
        view.setOrientation(LinearLayout.VERTICAL);
        groupTable = new de.b3ttertogeth3r.walhalla.design.LinearLayout(requireContext());
        HorizontalScrollView hsv = new HorizontalScrollView(requireContext());
        hsv.addView(groupTable);
        view.addView(hsv);
        movements = new de.b3ttertogeth3r.walhalla.design.LinearLayout(requireContext());
        HorizontalScrollView hsv2 = new HorizontalScrollView(requireContext());
        hsv2.addView(movements);
        view.addView(hsv2);
    }

    @Override
    public FragmentActivity authStatusChanged(FirebaseAuth firebaseAuth) {
        return requireActivity();
    }

    @NonNull
    @Override
    public String toString() {
        return "Fragment: Drinks extends Fragment";
    }
}
