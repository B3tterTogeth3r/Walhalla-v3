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

import java.util.ArrayList;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstract_classes.Fragment;
import de.b3ttertogeth3r.walhalla.firebase.Firebase;
import de.b3ttertogeth3r.walhalla.firebase.RemoteConfig;
import de.b3ttertogeth3r.walhalla.object.Log;
import de.b3ttertogeth3r.walhalla.object.Text;
import de.b3ttertogeth3r.walhalla.util.FormatJSON;
import de.b3ttertogeth3r.walhalla.util.Paragraph;

public class Fraternities_city extends Fragment {
    private static final String TAG = "Fraternities_city";
    private LinearLayout view;

    @Override
    public void start() {
        new FormatJSON(Firebase.remoteConfig().getString(RemoteConfig.FRATERNITY_CITY))
                .setOnSuccessListener(this::fillView)
                .setOnFailListener(e -> Log.e(TAG, "onFailureListener: ", e))
                .start();
    }

    private void fillView(ArrayList<Paragraph<Text>> result) {
        if (result == null || result.isEmpty() || view == null) {
            return;
        }
        for (Paragraph<Text> l : result) {
            de.b3ttertogeth3r.walhalla.design.LinearLayout layout = new de.b3ttertogeth3r.walhalla.design.LinearLayout(requireContext());
            for (Text t : l) {
                layout.addView(t.getView(requireActivity()));
            }
            view.addView(layout);
        }
    }

    @Override
    public String analyticsProperties() {
        return TAG;
    }

    @Override
    public void toolbarContent() {
        toolbar.setTitle(R.string.menu_more_frat_wue);
    }

    @Override
    public void createView(@NonNull LinearLayout view) {
        this.view = view;
    }
}
