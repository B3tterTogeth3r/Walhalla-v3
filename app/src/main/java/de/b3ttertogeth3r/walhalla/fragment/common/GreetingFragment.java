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

package de.b3ttertogeth3r.walhalla.fragment.common;

import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import de.b3ttertogeth3r.walhalla.abstract_classes.Fragment;
import de.b3ttertogeth3r.walhalla.exception.NoDataException;
import de.b3ttertogeth3r.walhalla.interfaces.IFirestoreDownload;
import de.b3ttertogeth3r.walhalla.mock.FirestoreMock;
import de.b3ttertogeth3r.walhalla.object.Log;
import de.b3ttertogeth3r.walhalla.object.Text;

public class GreetingFragment extends Fragment {
    private static final String TAG = "Greeting";
    private final IFirestoreDownload download;
    private ArrayList<Text> greeting;
    private LinearLayout view;

    public GreetingFragment () {
        download = new FirestoreMock.Download();
        greeting = new ArrayList<>();
    }

    @Override
    public void start () {
        download.semesterGreeting("0")
                .setOnSuccessListener(result -> {
                if(result != null && !result.isEmpty()) {
                    greeting = result;
                    loadGreeting();
                    return;
                }
                throw new NoDataException("Greeting is empty or null");
            })
                .setOnFailListener(e -> {
                Log.e(TAG, "Loading greeting did not work", e);
            });
    }

    private void loadGreeting () {
        for(Text t : greeting) {
            de.b3ttertogeth3r.walhalla.design.LinearLayout greetingLayout = new de.b3ttertogeth3r.walhalla.design.LinearLayout(requireContext());
            for(String s : t.getValue()) {
                greetingLayout.addView(new de.b3ttertogeth3r.walhalla.design.Text(requireContext(), s));
            }
            // TODO: 01.06.22 format x and phX with image as bottom layout
            view.addView(greetingLayout);
        }
    }

    @Override
    public String analyticsProperties () {
        return TAG;
    }

    @Override
    public void stop () {

    }

    @Override
    public void viewCreated () {

    }

    @Override
    public void toolbarContent () {

    }

    @Override
    public void createView (@NonNull LinearLayout view) {
        this.view = view;
    }
}
