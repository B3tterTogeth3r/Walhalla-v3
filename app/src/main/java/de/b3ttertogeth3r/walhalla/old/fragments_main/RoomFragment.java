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

package de.b3ttertogeth3r.walhalla.old.fragments_main;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.google.firebase.database.annotations.NotNull;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.old.abstraction.CustomFragment;
import de.b3ttertogeth3r.walhalla.old.utils.RemoteConfigData;
import de.b3ttertogeth3r.walhalla.old.utils.Site;

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