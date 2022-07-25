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
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstract_generic.Fragment;
import de.b3ttertogeth3r.walhalla.design.SideNav;
import de.b3ttertogeth3r.walhalla.design.TableLayout;
import de.b3ttertogeth3r.walhalla.design.Toast;
import de.b3ttertogeth3r.walhalla.exception.NoDataException;
import de.b3ttertogeth3r.walhalla.firebase.Firebase;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IFirestoreDownload;
import de.b3ttertogeth3r.walhalla.interfaces.loader.OnSuccessListener;
import de.b3ttertogeth3r.walhalla.object.DrinkMovement;
import de.b3ttertogeth3r.walhalla.object.Log;

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
    int currentSemester;
    String uid;
    LinearLayout view;
    private de.b3ttertogeth3r.walhalla.design.LinearLayout movements;
    private IFirestoreDownload download;

    @Override
    public void constructor() {
        download = Firebase.firestoreDownload();
        if (!Firebase.authentication().isSignIn()) {
            Toast.makeToast(requireContext(), R.string.fui_error_session_expired).show();
            SideNav.changePage(R.string.menu_home, requireActivity().getSupportFragmentManager().beginTransaction());
        }
    }

    @Override
    public String analyticsProperties() {
        return TAG;
    }

    @Override
    public void start() {
        // TODO: 21.07.22 list all drinks of the current/selected semester grouped by kind above the table
        download.getPersonDrinkMovement(uid, currentSemester)
                .setOnSuccessListener(new OnSuccessListener<ArrayList<DrinkMovement>>() {
                    @Override
                    public void onSuccessListener(@Nullable ArrayList<DrinkMovement> result) throws Exception {
                        if (result == null || result.isEmpty()) {
                            throw new NoDataException("No drink movements found");
                        }
                        displayDrinks(result);
                    }
                })
                .setOnFailListener(e -> {
                    Log.e(TAG, "start: ", e);
                });
    }

    @Override
    public void toolbarContent() {
        toolbar.setTitle(R.string.menu_drinks);
        // TODO: 30.05.22 add menu to manage drinks, if the user is a board member
    }

    @Override
    public void createView(@NonNull LinearLayout view) {
        movements = new de.b3ttertogeth3r.walhalla.design.LinearLayout(requireContext());
        HorizontalScrollView hsv = new HorizontalScrollView(requireContext());
        hsv.addView(movements);
        view.addView(hsv);
    }

    @Override
    public FragmentActivity authStatusChanged(FirebaseAuth firebaseAuth) {
        return requireActivity();
    }

    private void displayDrinks(@NonNull ArrayList<DrinkMovement> drinkMovements) {
        movements.removeAllViewsInLayout();
        TableLayout table = new TableLayout(requireContext());
        for (DrinkMovement dm : drinkMovements) {
            table.addView(
                    de.b3ttertogeth3r.walhalla.design.Movement.create(
                                    requireActivity(), null, dm)
                            .show());
        }
        movements.addView(table);
    }

    @NonNull
    @Override
    public String toString() {
        return "Fragment: Drinks extends Fragment";
    }
}
