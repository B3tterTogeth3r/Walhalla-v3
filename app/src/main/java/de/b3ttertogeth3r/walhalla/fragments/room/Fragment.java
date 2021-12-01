package de.b3ttertogeth3r.walhalla.fragments.room;

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
import de.b3ttertogeth3r.walhalla.enums.Charge;
import de.b3ttertogeth3r.walhalla.enums.Display;
import de.b3ttertogeth3r.walhalla.enums.Page;
import de.b3ttertogeth3r.walhalla.firebase.Firebase;
import de.b3ttertogeth3r.walhalla.firebase.Firebase.Crashlytics;
import de.b3ttertogeth3r.walhalla.models.Paragraph;
import de.b3ttertogeth3r.walhalla.utils.CacheData;
import de.b3ttertogeth3r.walhalla.utils.Site;

/**
 * This fragment displays a description of the house and its rooms. Also pictures of the rooms and
 * links to <a href="http://wg-gesucht.de">WG-Gesucht</a> to apply for a room are displayed here.
 *
 * @author B3tterToegth3r
 * @version 1.0
 * @since 1.0
 */
public class Fragment extends CustomFragment {
    private static final String TAG = "room.Fragment";
    int paragraphNumber = 1;
    private final ArrayList<ArrayList<Paragraph>> paragraphs = new ArrayList<>();
    private LinearLayout layout;
    private boolean edit = false;
    private Display display = Display.SHOW;

    @Override
    public void start () {
        findParagraphs();
    }

    private void findParagraphs(){
        String paragraph = "paragraph_" + paragraphNumber;
        Firebase.FIRESTORE.collection("Sites/rooms/" + paragraph).orderBy("position")
                .get().addOnSuccessListener(documentSnapshots -> {
                    if(documentSnapshots.isEmpty()){
                        Log.d(TAG, "onFailure: " + paragraph + " doesn't exist");
                        try {
                            new Site(getContext(), layout, paragraphs, display);
                        } catch (Exception ignored){
                        }
                        return;
                    }
                    Log.d(TAG, "onSuccess: " + paragraph + " exists");

                    ArrayList<Paragraph> paragraphList = new ArrayList<>();
                    for (QueryDocumentSnapshot one : documentSnapshots) {
                        try {
                            Paragraph p = one.toObject(Paragraph.class);
                            p.setId(one.getId());
                            paragraphList.add(p);
                        } catch (Exception e) {
                            Crashlytics.log(TAG, "onSuccess: parsing error", e);
                        }
                    }
                    paragraphs.add(paragraphNumber-1, paragraphList);
                    paragraphNumber++;
                    findParagraphs();
                }).addOnFailureListener(e -> Log.d(TAG, "onFailure: #" + paragraph + " doesn't exist"));
    }

    @Override
    public void analyticsProperties () {

    }

    @Override
    public void onResume () {
        if (edit) {
            display = Display.EDIT;
        } else {
            display = Display.SHOW;
        }
        super.onResume();
    }

    @Override
    public void stop () {

    }

    @Override
    public void viewCreated () {
        try {
            new Site(requireContext(), layout, paragraphs, display);
        } catch (Exception ignored) {
        }
    }

    @Override
    public void toolbarContent () {
        toolbar.setTitle(R.string.menu_rooms);
        toolbar.getMenu().clear();
        if (Page.ROOM.canEditPage(CacheData.getCharge())) {
            // only visible if signed in user is charge or super-admin
            toolbar.inflateMenu(R.menu.default_edit_only);
            toolbar.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.edit) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                    AlertDialog dialog = dialogBuilder.setIcon(R.drawable.ic_edit)
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
                                            new Site(requireContext(), layout, paragraphs, display);
                                        } catch (Exception exception) {
                                            Log.e(TAG, "toolbarContent: trying to edit fragment " +
                                                    "content", exception);
                                        }
                                    }).setNeutralButton(R.string.no, (dialog1, which) -> {
                                Toast.makeText(getContext(), R.string.aborted,
                                        Toast.LENGTH_SHORT).show();
                                dialog1.dismiss();
                            }).setTitle(R.string.edit).setMessage(R.string.edit_shure).create();
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
