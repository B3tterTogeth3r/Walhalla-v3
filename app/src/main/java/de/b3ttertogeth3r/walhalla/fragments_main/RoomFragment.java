package de.b3ttertogeth3r.walhalla.fragments_main;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.google.firebase.database.annotations.NotNull;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstraction.CustomFragment;
import de.b3ttertogeth3r.walhalla.utils.Site;
import de.b3ttertogeth3r.walhalla.utils.RemoteConfigData;

/**
 * This fragment displays a description of the house and its rooms. Also pictures of the rooms and
 * links to <a href="http://wg-gesucht.de">WG-Gesucht</a> to apply for a room are displayed here.
 *
 * @author B3tterToegth3r
 * @version 1.0
 * @since 1.0
 */
public class RoomFragment extends CustomFragment {
    private static final String TAG = "room.Fragment";
    private LinearLayout layout;


    @Override
    public void authStatusChanged () {
        getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, this).commit();
    }
    @Override
    public void start () {
    }

    @Override
    public void analyticsProperties () {

    }

    @Override
    public void onResume () {
        super.onResume();
    }

    @Override
    public void stop () {

    }

    @Override
    public void viewCreated () {
        try {
            new Site(getContext(), layout, RemoteConfigData.roomsList);
        } catch (Exception ignored) {
        }
    }

    @Override
    public void toolbarContent () {
        toolbar.setTitle(R.string.menu_rooms);
    }

    @Override
    public void createView (@NonNull @NotNull View view,
                            @NonNull @NotNull LayoutInflater inflater) {
        layout = view.findViewById(R.id.fragment_container);
        layout.setOrientation(LinearLayout.VERTICAL);
    }
}
