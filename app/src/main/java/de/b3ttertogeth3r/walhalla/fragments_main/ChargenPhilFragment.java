package de.b3ttertogeth3r.walhalla.fragments_main;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstraction.CustomFragment;
import de.b3ttertogeth3r.walhalla.design.RowChargen;
import de.b3ttertogeth3r.walhalla.firebase.Crashlytics;
import de.b3ttertogeth3r.walhalla.firebase.Firestore;
import de.b3ttertogeth3r.walhalla.interfaces.MyCompleteListener;
import de.b3ttertogeth3r.walhalla.models.Charge;
import de.b3ttertogeth3r.walhalla.utils.CacheData;

/**
 * In this fragment the chargen of the selected semester. By default the current semester gets
 * displayed. The user can change the selected semester by tapping on the top toolbar.
 *
 * @author B3tterToegth3r
 */
public class ChargenPhilFragment extends CustomFragment {
    private static final String TAG = "chargen_phil.Fragment";
    private final ArrayList<Charge> board = new ArrayList<>();
    private LinearLayout layout;


    @Override
    public void authStatusChanged () {
        getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, this).commit();
    }
    @Override
    public void start () {
        Firestore.getPhilChargen(CacheData.getChosenSemester(), new MyCompleteListener<QuerySnapshot>(){
            @Override
            public void onSuccess (QuerySnapshot querySnapshot) {
                if(querySnapshot == null || querySnapshot.isEmpty()){
                    Crashlytics.log(TAG, "finding chargen did not work");
                    return;
                }
                board.clear();
                for(DocumentSnapshot snap : querySnapshot){
                    try{
                        Charge c = snap.toObject(Charge.class);
                        if(c != null){
                            c.setId(snap.getId());
                            board.add(c);
                        }
                    } catch (Exception e){
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
    public void createView (@NonNull @NotNull View view,
                            @NonNull @NotNull LayoutInflater inflater) {
        layout = view.findViewById(R.id.fragment_container);
        updateList();
    }

    private void updateList () {
        layout.removeAllViewsInLayout();
        sortList();
        for(Charge b: board){
            layout.addView(new RowChargen(getContext(), b, de.b3ttertogeth3r.walhalla.enums.Charge.find(b.getId())));
        }

    }
    @Override
    public void viewCreated () {

    }

    @Override
    public void toolbarContent () {
        toolbar.setTitle(R.string.menu_chargen_phil);
    }

    @Override
    public void stop () {

    }

    void sortList(){
        //TODO Sort list
        //pos1: AH_X - pos2: AH_XX - pos3: AH_XXX - pos4+x: AH_HW

    }
}
