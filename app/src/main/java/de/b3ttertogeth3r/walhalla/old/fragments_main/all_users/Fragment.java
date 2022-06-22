/*
 * Copyright (c) 2022.
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

package de.b3ttertogeth3r.walhalla.old.fragments_main.all_users;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableRow;

import androidx.annotation.NonNull;

import com.google.firebase.database.annotations.NotNull;

import java.util.List;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.old.abstraction.CustomFragment;
import de.b3ttertogeth3r.walhalla.old.design.MyTextView;
import de.b3ttertogeth3r.walhalla.old.design.MyTitle;
import de.b3ttertogeth3r.walhalla.old.firebase.Crashlytics;
import de.b3ttertogeth3r.walhalla.old.firebase.Functions;
import de.b3ttertogeth3r.walhalla.old.interfaces.MyCompleteListener;
import de.b3ttertogeth3r.walhalla.old.models.User;

/**
 * @author B3tterToegth3r
 * @version 1.0
 * @since 1.0
 */
public class Fragment extends CustomFragment implements View.OnClickListener, MyCompleteListener<List<User>> {
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
        Crashlytics.error(TAG, "getting users did not work", exception);
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
