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

import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstract_generic.Fragment;
import de.b3ttertogeth3r.walhalla.abstract_generic.Touch;
import de.b3ttertogeth3r.walhalla.design.Event;
import de.b3ttertogeth3r.walhalla.design.SideNav;
import de.b3ttertogeth3r.walhalla.design.Toast;
import de.b3ttertogeth3r.walhalla.exception.NoDataException;
import de.b3ttertogeth3r.walhalla.firebase.Firebase;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IAuth;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IFirestoreDownload;
import de.b3ttertogeth3r.walhalla.object.Chore;
import de.b3ttertogeth3r.walhalla.util.Log;

public class Chores extends Fragment {
    private static final String TAG = "Chores";
    private final boolean showDoneChores;
    private IFirestoreDownload download;
    private String uid;
    private LinearLayout layout;

    public Chores(boolean showDoneChores) {
        super();
        this.showDoneChores = showDoneChores;
    }

    @Override
    public void constructor() {
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
        download.getPersonChores(uid, showDoneChores)
                .setOnSuccessListener(result -> {
                    if (result == null || result.isEmpty()) {
                        throw new NoDataException("User has no Chores");
                    }
                    loadChores(result);
                })
                .setOnFailListener(e -> {
                    Chore c = new Chore();
                    c.setEvent(getString(R.string.chore_none));
                });
    }

    private void loadChores(@NonNull ArrayList<Chore> result) {
        try {
            layout.removeAllViewsInLayout();
            for (Chore c : result) {
                layout.addView(
                        Event.create(requireActivity(), null, c, false)
                                .addTouchListener(new Touch() {

                                }).show());

            }
        } catch (Exception e) {
            Log.e(TAG, "loadChores: " + e.getMessage());
        }
    }

    @Override
    public void toolbarContent() {
        toolbar.getMenu().clear();
        toolbar.setTitle(R.string.menu_chores);
        toolbar.inflateMenu(R.menu.chores);
        Menu menu = toolbar.getMenu();

        MenuItem checkable = menu.findItem(R.id.app_bar_switch);
        checkable.setActionView(R.layout.switch_item);
        SwitchCompat sw = menu.findItem(R.id.app_bar_switch).getActionView().findViewById(R.id.switch2);
        sw.setChecked(showDoneChores);
        sw.setOnCheckedChangeListener((buttonView, isChecked) -> {
            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new Chores(isChecked))
                    .commit();
        });
    }

    @Override
    public void createView(@NonNull LinearLayout view) {
        layout = view;
        layout.setOrientation(LinearLayout.VERTICAL);
    }

    @Override
    public FragmentActivity authStatusChanged(FirebaseAuth firebaseAuth) {
        return requireActivity();
    }
}
