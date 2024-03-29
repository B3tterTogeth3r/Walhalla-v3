/*
 * Copyright (c) 2022-2022.
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

package de.b3ttertogeth3r.walhalla.fragment.common;

import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.auth.FirebaseAuth;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstract_generic.Fragment;

public class Settings extends Fragment {
    private static final String TAG = "Settings";

    @Override
    public String analyticsProperties() {
        return TAG;
    }

    @Override
    public void toolbarContent() {
        toolbar.setTitle(R.string.menu_settings);
    }

    @Override
    public void createView(@NonNull LinearLayout view) {
    }

    @Override
    public FragmentActivity authStatusChanged(FirebaseAuth firebaseAuth) {
        return requireActivity();
    }
}
