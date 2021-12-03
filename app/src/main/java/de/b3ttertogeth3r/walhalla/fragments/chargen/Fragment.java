package de.b3ttertogeth3r.walhalla.fragments.chargen;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstraction.CustomFragment;
import de.b3ttertogeth3r.walhalla.design.ChargenRow;
import de.b3ttertogeth3r.walhalla.design.MyToast;
import de.b3ttertogeth3r.walhalla.enums.Page;
import de.b3ttertogeth3r.walhalla.firebase.Firebase;
import de.b3ttertogeth3r.walhalla.interfaces.OnGetDataListener;
import de.b3ttertogeth3r.walhalla.models.Charge;
import de.b3ttertogeth3r.walhalla.utils.CacheData;

/**
 * In this fragment the chargen of the selected semester. By default the current semester gets
 * displayed. The user can change the selected semester by tapping on the top toolbar.
 *
 * @author B3tterToegth3r
 */
public class Fragment extends CustomFragment {
    private static final String TAG = "chargen.Fragment";
    private final ArrayList<Charge> board = new ArrayList<>();
    private LinearLayout layout;

    @Override
    public void start () {
        Firebase.Firestore.getChargen(CacheData.getChosenSemester(), new OnGetDataListener() {
            @Override
            public void onSuccess (QuerySnapshot querySnapshot) {
                if (querySnapshot == null || querySnapshot.isEmpty()) {
                    Firebase.Crashlytics.log(TAG, "finding chargen did not work");
                    return;
                }
                board.clear();
                for (DocumentSnapshot snap : querySnapshot) {
                    try {
                        Charge c = snap.toObject(Charge.class);
                        if (c != null) {
                            c.setId(snap.getId());
                            board.add(c);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "onSuccess: chargen parsing didn't work", e);
                    }
                }
                updateList();
            }
        });
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
        toolbar.setTitle(R.string.menu_chargen);
        toolbar.getMenu().clear();
        if (Page.canEditPage(CacheData.getCharge())) {
            toolbar.inflateMenu(R.menu.default_edit_only);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick (MenuItem item) {
                    if (item.getItemId() == R.id.edit) {
                        MyToast toast = new MyToast(getContext());
                        toast.setText(R.string.error_dev);
                        toast.show();
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public void createView (@NonNull @NotNull View view,
                            @NonNull @NotNull LayoutInflater inflater) {
        layout = view.findViewById(R.id.fragment_container);
        updateList();
    }

    private void updateList () {
        layout.removeAllViewsInLayout();
        sortList();
        for (Charge b : board) {
            try {
                layout.addView(new ChargenRow(requireContext(), b,
                        de.b3ttertogeth3r.walhalla.enums.Charge.find(b.getId())));
            } catch (Exception e){
                Firebase.Crashlytics.log(TAG, "ChargenRow could not be created", e);
            }
        }

    }

    void sortList () {
        //TODO Sort list
        //pos1: X - pos2: VX - pos3: FM - pos4: XX - pos5: XXX

    }
}
