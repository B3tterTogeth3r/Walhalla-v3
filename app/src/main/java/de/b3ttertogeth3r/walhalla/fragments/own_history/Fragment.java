package de.b3ttertogeth3r.walhalla.fragments.own_history;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.annotations.NotNull;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstraction.CustomFragment;

/**
 * @author B3tterToegth3r
 * @version 1.0
 * @since 1.0
 */
public class Fragment extends CustomFragment {
    private static final String TAG = "room.own_history";

    @Override
    public void start () {

    }

    @Override
    public void analyticsProperties () {

    }

    @Override
    public void createView (@NonNull @NotNull View view,
                            @NonNull @NotNull LayoutInflater inflater) {
        LinearLayout layout = view.findViewById(R.id.fragment_container);
        TextView title = new TextView(requireContext());
        title.setText(R.string.menu_more_history);
        layout.addView(title);
    }

    @Override
    public void viewCreated () {

    }

    @Override
    public void toolbarContent () {
        toolbar.setTitle(R.string.menu_more_history);
    }

    @Override
    public void stop () {

    }
}
