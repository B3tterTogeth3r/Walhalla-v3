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

import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstract_classes.Fragment;
import de.b3ttertogeth3r.walhalla.abstract_classes.Loader;
import de.b3ttertogeth3r.walhalla.abstract_classes.TouchListener;
import de.b3ttertogeth3r.walhalla.design.DEvent;
import de.b3ttertogeth3r.walhalla.design.Toast;
import de.b3ttertogeth3r.walhalla.dialog.ChangeSemester;
import de.b3ttertogeth3r.walhalla.dialog.EventDetails;
import de.b3ttertogeth3r.walhalla.enums.DialogSize;
import de.b3ttertogeth3r.walhalla.interfaces.IFirestoreDownload;
import de.b3ttertogeth3r.walhalla.mock.FirestoreMock;
import de.b3ttertogeth3r.walhalla.object.Event;
import de.b3ttertogeth3r.walhalla.object.Log;
import de.b3ttertogeth3r.walhalla.object.Semester;

public class Program extends Fragment {
    private static final String TAG = "ProgramFragment";
    private final IFirestoreDownload download;
    private LinearLayout view;

    public Program() {
        this.download = new FirestoreMock.Download();
    }

    @Override
    public void start() {
        download(1);
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
        toolbar.setTitle("");
        customToolbar.setVisibility(View.VISIBLE);
        customToolbarTitle.setText(R.string.menu_program);
        customToolbar.setOnClickListener(v ->
                ChangeSemester.display(getParentFragmentManager(), DialogSize.WRAP_CONTENT,
                        new Semester(), new Loader<Integer>() {
                            @Override
                            public void onSuccessListener(Integer result) {
                                Log.d(TAG, "onSuccessListener() returned: " + result);
                                download(result);
                            }

                            @Override
                            public void onFailureListener(Exception e) {

                            }
                        })
        );
    }

    @Override
    public void createView(@NonNull LinearLayout view) {
        this.view = view;
    }

    private void download(int semId) {
        download.semesterEvents(String.valueOf(semId))
                .setOnSuccessListener(result -> {
                    if (result == null) {
                        // onFailureListener(new NullPointerException("Download failed"));
                        return;
                    } else if (result.isEmpty()) {
                        // TODO: 31.05.22 add text with content like "this semester didn't have any
                        //  events or so
                        // onFailureListener(new NoDataException("The semester has no events"));
                        return;
                    }
                    listEvents(result);
                }).setOnFailListener(e -> {
                    Toast.makeToast(getContext(), "Loading page content didn't work. Please try again later").show();
                    Log.e(TAG, "Loading the board members did not work", e);
                });
    }

    void listEvents(@NonNull ArrayList<Event> eventList) {
        view.removeAllViewsInLayout();
        view.removeAllViews();
        int i = 0;
        for (Event e : eventList) {
            view.addView(DEvent.create(requireContext(), null, e)
                    .addTouchListener(new TouchListener<Event>(e) {
                        @Override
                        public void onClick(Event event, View view) {
                            try {
                                FragmentManager fm = (requireActivity()).getSupportFragmentManager();
                                EventDetails.display(fm, DialogSize.FULL_SCREEN, event, new Loader<Void>() {
                                    @Override
                                    public void onSuccessListener(@Nullable Void result) {

                                    }
                                });
                            } catch (Exception e) {
                                Log.e("Event", "onClickListener: Fragment manager not found", e);
                            }
                        }
                    })
                    .show());
            i++;
            if (i == 5) {
                // TODO: 31.05.22 group by month and after every month there is an advert or like this
                i = 0;
                /*
                AdView mAdView = new AdView(requireContext());
                mAdView.setAdSize(AdSize.BANNER);
                mAdView.setAdUnitId(Values.AD_UNIT_ID);
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);
                view.addView(mAdView);*/
            }
        }
    }
}
