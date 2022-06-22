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

package de.b3ttertogeth3r.walhalla.fragment.signed_in;

import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstract_classes.Fragment;

/**
 * <h1>This Fragment should only be accessible, if a user is signed in</h1>
 * <br>This Fragment is to display the users drinks of the current semester, so the user can
 * see how many drinks he consumed.
 *
 * @author B3tterTogeth3r
 * @version 1.0
 * @since 1.0
 */
public class Drinks extends Fragment {
    private static final String TAG = "Drinks";

    @Override
    public void start() {
        //load movements where purpose is "Beer_Invoice"
        // TODO: 07.06.2022 or create drinks with its own class

    }

    @Override
    public String analyticsProperties() {
        return TAG;
    }

    @Override
    public void stop() {

    }

    @Override
    public void viewCreated() {

    }

    @Override
    public void toolbarContent() {
        toolbar.setTitle(R.string.menu_drinks);
        // TODO: 30.05.22 add menu to manage drinks, if the user is a board member
    }

    @Override
    public void createView(@NonNull LinearLayout view) {

    }
}
