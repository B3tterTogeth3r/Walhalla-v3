package de.b3ttertogeth3r.walhalla.fragments_main;

import static de.b3ttertogeth3r.walhalla.enums.Charge.FM;
import static de.b3ttertogeth3r.walhalla.enums.Charge.NONE;
import static de.b3ttertogeth3r.walhalla.enums.Charge.VX;
import static de.b3ttertogeth3r.walhalla.enums.Charge.X;
import static de.b3ttertogeth3r.walhalla.enums.Charge.XX;
import static de.b3ttertogeth3r.walhalla.enums.Charge.XXX;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstraction.CustomFragment;
import de.b3ttertogeth3r.walhalla.design.MyTextView;
import de.b3ttertogeth3r.walhalla.design.MyToast;
import de.b3ttertogeth3r.walhalla.design.RowChargen;
import de.b3ttertogeth3r.walhalla.dialog.EditChargeDialog;
import de.b3ttertogeth3r.walhalla.enums.Page;
import de.b3ttertogeth3r.walhalla.firebase.Crashlytics;
import de.b3ttertogeth3r.walhalla.firebase.Firestore;
import de.b3ttertogeth3r.walhalla.interfaces.MyCompleteListener;
import de.b3ttertogeth3r.walhalla.models.Charge;
import de.b3ttertogeth3r.walhalla.utils.CacheData;
import de.b3ttertogeth3r.walhalla.utils.RemoteConfigData;

/**
 * In this fragment the chargen of the selected semester. By default the current semester gets
 * displayed. The user can change the selected semester by tapping on the top toolbar.
 *
 * @author B3tterToegth3r
 */
public class ChargenFragment extends CustomFragment {
    private static final String TAG = "chargen.Fragment";
    private ArrayList<Charge> board = new ArrayList<>(Arrays.asList(new Charge(), new Charge(),
            new Charge(), new Charge(), new Charge()));
    private LinearLayout layout;

    @Override
    public void start () {
        Firestore.getChargen(CacheData.getChosenSemester(), new MyCompleteListener<QuerySnapshot>() {
            @Override
            public void onSuccess (QuerySnapshot querySnapshot) {
                if (querySnapshot == null || querySnapshot.isEmpty()) {
                    Crashlytics.error(TAG, "finding chargen did not work");
                    return;
                }
                board = new ArrayList<>(Arrays.asList(new Charge(), new Charge(), new Charge(),
                        new Charge(), new Charge()));
                for (DocumentSnapshot snap : querySnapshot) {
                    try {
                        Charge c = snap.toObject(Charge.class);
                        if (c != null) {
                            c.setId(snap.getId());
                            de.b3ttertogeth3r.walhalla.enums.Charge charge =
                                    de.b3ttertogeth3r.walhalla.enums.Charge.find(c.getId());
                            // order all board members in the right way
                            switch (charge) {
                                case X:
                                    board.set(0, c);
                                    break;
                                case VX:
                                    board.set(1, c);
                                    break;
                                case FM:
                                    board.set(2, c);
                                    break;
                                case XX:
                                    board.set(3, c);
                                    break;
                                case XXX:
                                    board.set(4, c);
                                    break;
                            }
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "onSuccess: chargen parsing didn't work", e);
                    }
                }
                updateList();
            }

            @Override
            public void onFailure (Exception e) {
            }
        });
    }

    @Override
    public void analyticsProperties () {

    }

    @Override
    public void authStatusChanged () {
        getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, this).commit();
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
        if (Page.CHARGEN.canEditPage(CacheData.getCharge())) {
            toolbar.inflateMenu(R.menu.default_edit_only);
            toolbar.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.edit) {
                    MyToast toast = new MyToast(getContext());
                    toast.setText(R.string.error_dev);
                    toast.show();

                    AlertDialog dialog = getEditDialog();
                    dialog.show();
                }
                return false;
            });
        }
    }

    @NonNull
    private AlertDialog getEditDialog () {
        String[] active_board_members = new String[]{X.getName(), VX.getName(), FM.getName(),
                XX.getName(), XXX.getName()};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(R.string.dialog_charge_choose)
                .setNegativeButton(R.string.abort, null)
                .setItems(active_board_members, (dialog, which) -> {
                    // Log.d(TAG, "getEditDialog: which = " + which);
                    Charge charge = new Charge();
                    de.b3ttertogeth3r.walhalla.enums.Charge selected =
                            de.b3ttertogeth3r.walhalla.enums.Charge.find(active_board_members[which]);
                    try {
                        switch (selected) {
                            case X:
                                charge = board.get(0);
                                break;
                            case VX:
                                charge = board.get(1);
                                break;
                            case FM:
                                charge = board.get(2);
                                break;
                            case XX:
                                charge = board.get(3);
                                break;
                            case XXX:
                                charge = board.get(4);
                                break;
                        }
                    } catch (Exception e) {
                        charge.setId(active_board_members[which]);
                    }
                    EditChargeDialog.display(getParentFragmentManager(), selected, charge);
                });
        return builder.create();
    }

    @Override
    public void createView (@NonNull @NotNull View view,
                            @NonNull @NotNull LayoutInflater inflater) {
        layout = view.findViewById(R.id.fragment_container);
        updateList();
    }

    private void updateList () {
        layout.removeAllViewsInLayout();
        layout.addView(new MyTextView(requireContext(),
                RemoteConfigData.chargen_description.get(5)));
        for (Charge b : board) {
            try {
                de.b3ttertogeth3r.walhalla.enums.Charge selected =
                        de.b3ttertogeth3r.walhalla.enums.Charge.find(b.getId());
                if (selected != NONE) {
                    layout.addView(new RowChargen(requireContext(), b, selected));
                }
            } catch (Exception e) {
                Crashlytics.error(TAG, "ChargenRow could not be created", e);
            }
        }

    }
}
