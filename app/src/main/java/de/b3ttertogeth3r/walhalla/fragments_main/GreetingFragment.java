package de.b3ttertogeth3r.walhalla.fragments_main;

import static de.b3ttertogeth3r.walhalla.firebase.Firestore.FIRESTORE;

import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstraction.CustomFragment;
import de.b3ttertogeth3r.walhalla.enums.Display;
import de.b3ttertogeth3r.walhalla.enums.Page;
import de.b3ttertogeth3r.walhalla.models.Paragraph;
import de.b3ttertogeth3r.walhalla.utils.CacheData;
import de.b3ttertogeth3r.walhalla.utils.Site;

/**
 * In this fragment the program of the selected semester. By default the current semester gets
 * displayed. The user can change the selected semester by tapping on the top toolbar.
 *
 * @author B3tterToegth3r
 */
public class GreetingFragment extends CustomFragment {
    private static final String TAG = "greeting.Fragment";
    private ArrayList<ArrayList<Paragraph>> greetingList = new ArrayList<>();
    private LinearLayout layout;
    private boolean edit = false;
    private Display display = Display.SHOW;


    @Override
    public void authStatusChanged () {
        getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, this).commit();
    }
    @Override
    public void start () {
        findGreeting();
    }

    private void findGreeting () {
        String sem_id = "" + 316;
        FIRESTORE.collection("Semester/" + sem_id + "/Greeting")
                .get().addOnSuccessListener(documentSnapshots -> {
            if (documentSnapshots.isEmpty()) {
                Log.d(TAG, "findGreeting: invalid semester");
            }
            ArrayList<Paragraph> paragraphList = new ArrayList<>();
            for (QueryDocumentSnapshot one : documentSnapshots) {
                try {
                    Paragraph p = one.toObject(Paragraph.class);
                    p.setId(one.getId());
                    paragraphList.add(p);
                } catch (Exception e) {
                    Log.e(TAG, "onSuccess: parsing error", e);
                }
            }
            greetingList.add(0, paragraphList);
            new Site(requireContext(), layout, greetingList);
        }).addOnFailureListener(e -> Log.d(TAG, "findGreeting: invalid semester"));
    }

    @Override
    public void analyticsProperties () {

    }

    @Override
    public void stop () {

    }

    @Override
    public void viewCreated () {
        try {
            new Site(requireContext(), layout, greetingList);
        } catch (Exception ignored) {
        }
    }

    @Override
    public void toolbarContent () {
        toolbar.setTitle(R.string.menu_greeting);
        toolbar.getMenu().clear();
        // only visible if signed in user is charge or super-admin
        if (Page.GREETING.canEditPage(CacheData.getCharge())) {
            toolbar.inflateMenu(R.menu.default_edit_only);
            toolbar.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.edit) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                    AlertDialog dialog = dialogBuilder
                            .setIcon(R.drawable.ic_edit)
                            .setCancelable(true)
                            .setPositiveButton(R.string.yes,
                                    (dialog12, which) -> {
                                        //reload display now with editable fields
                                        try {
                                            layout.removeAllViews();
                                            layout.removeAllViewsInLayout();
                                            toolbar.getMenu().clear();
                                            toolbar.inflateMenu(R.menu.default_save_abort);
                                            display = Display.EDIT;
                                            new Site(requireContext(), layout, greetingList);
                                        } catch (Exception exception) {
                                            Log.e(TAG, "toolbarContent: trying to edit fragment " +
                                                    "content", exception);
                                        }
                                    })
                            .setNeutralButton(R.string.no, (dialog1, which) -> {
                                Toast.makeText(getContext(), R.string.aborted,
                                        Toast.LENGTH_SHORT).show();
                                dialog1.dismiss();
                            })
                            .setTitle(R.string.edit)
                            .setMessage(R.string.edit_shure)
                            .create();
                    dialog.show();
                } else if (item.getItemId() == R.id.abort) {
                    layout.removeAllViews();
                    layout.removeAllViewsInLayout();
                    toolbar.getMenu().clear();
                    toolbar.inflateMenu(R.menu.default_edit_only);
                    display = Display.SHOW;
                } else if (item.getItemId() == R.id.save) {
                    layout.removeAllViews();
                    layout.removeAllViewsInLayout();
                    toolbar.getMenu().clear();
                    toolbar.inflateMenu(R.menu.default_edit_only);
                    display = Display.SHOW;
                    //TODO upload data to firebase
                    //Firebase.Firestore.upload(FirestorePath.ROOMS.getDocument(), paragraphs);
                }
                return true;
            });
        }
    }

    @Override
    public void createView (@NonNull @NotNull View view,
                            @NonNull @NotNull LayoutInflater inflater) {
        layout = view.findViewById(R.id.fragment_container);
        layout.setOrientation(LinearLayout.VERTICAL);
    }
}
