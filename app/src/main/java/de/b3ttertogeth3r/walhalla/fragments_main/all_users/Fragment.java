package de.b3ttertogeth3r.walhalla.fragments_main.all_users;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableRow;

import androidx.annotation.NonNull;

import com.google.firebase.database.annotations.NotNull;

import java.util.List;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstraction.CustomFragment;
import de.b3ttertogeth3r.walhalla.design.MyTextView;
import de.b3ttertogeth3r.walhalla.design.MyTitle;
import de.b3ttertogeth3r.walhalla.firebase.Crashlytics;
import de.b3ttertogeth3r.walhalla.firebase.Functions;
import de.b3ttertogeth3r.walhalla.interfaces.CustomFirebaseCompleteListener;
import de.b3ttertogeth3r.walhalla.models.User;

/**
 * @author B3tterToegth3r
 * @version 1.0
 * @since 1.0
 */
public class Fragment extends CustomFragment implements View.OnClickListener, CustomFirebaseCompleteListener {
    private static final String TAG = "menu_all_profiles.Fragment";
    private LinearLayout layout;

    @Override
    public void authStatusChanged () {
        getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, this).commit();
    }

    @Override
    public void start () {
        Functions.findAllUsers(this);
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
        toolbar.setTitle(R.string.menu_all_profiles);
    }

    @Override
    public void createView (@NonNull @NotNull View view,
                            @NonNull @NotNull LayoutInflater inflater) {
        layout = view.findViewById(R.id.fragment_container);
    }

    @Override
    public void onClick (View v) {
    }

    @Override
    public void onFailure (Exception exception) {
        Crashlytics.log(TAG, "getting users did not work", exception);
    }

    @Override
    public void onSuccess (List<User> userList) {
        if(userList == null) {
            return;
        }
        layout.removeAllViewsInLayout();
        for(User user :userList) {
            TableRow row = new TableRow(requireContext());
            MyTitle name = new MyTitle(requireContext());
            name.setText(user.getDisplayName());
            MyTextView email = new MyTextView(requireContext());
            email.setText(user.getEmail());

            row.addView(name);
            row.addView(email);
            layout.addView(row);
        }
    }
}
