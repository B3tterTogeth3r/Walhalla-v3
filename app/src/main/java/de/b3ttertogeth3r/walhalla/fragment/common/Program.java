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

import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstract_generic.Fragment;
import de.b3ttertogeth3r.walhalla.abstract_generic.Touch;
import de.b3ttertogeth3r.walhalla.design.Event;
import de.b3ttertogeth3r.walhalla.design.Title;
import de.b3ttertogeth3r.walhalla.design.Toast;
import de.b3ttertogeth3r.walhalla.dialog.ChangeSemester;
import de.b3ttertogeth3r.walhalla.dialog.EventDetails;
import de.b3ttertogeth3r.walhalla.enums.DialogSize;
import de.b3ttertogeth3r.walhalla.exception.CreateDialogException;
import de.b3ttertogeth3r.walhalla.exception.NoDataException;
import de.b3ttertogeth3r.walhalla.firebase.Firebase;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IFirestoreDownload;
import de.b3ttertogeth3r.walhalla.util.Cache;
import de.b3ttertogeth3r.walhalla.util.Log;
import de.b3ttertogeth3r.walhalla.util.Values;

public class Program extends Fragment {
    private static final String TAG = "ProgramFragment";
    private IFirestoreDownload download;
    private LinearLayout view;
    private int semesterID;

    @Override
    public void constructor() {
        download = Firebase.Firestore.download();
    }

    @Override
    public String analyticsProperties() {
        return TAG;
    }

    @Override
    public void start() {
        semesterID = Cache.CACHE_DATA.getChosenSemester();
        download(semesterID);
    }

    @Override
    public void toolbarContent() {
        toolbar.setTitle("");
        customToolbar.setVisibility(View.VISIBLE);
        String title = getString(R.string.menu_program) + " " + Values.semesterList.get(semesterID).getName_short();
        customToolbarTitle.setText(title);
        customToolbar.setOnClickListener(v -> displayDialog());
        if (Cache.CACHE_DATA.isBoardMember()) {
            // TODO: 29.07.22 add menu to add new events
        }
    }

    private void displayDialog() {
        try {
            ChangeSemester.display(getParentFragmentManager(), Values.semesterList.get(semesterID))
                    .setOnSuccessListener(result -> {
                        assert result != null;
                        download(result);
                    });
        } catch (CreateDialogException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createView(@NonNull LinearLayout view) {
        this.view = view;
    }

    @Override
    public FragmentActivity authStatusChanged(FirebaseAuth firebaseAuth) {
        return requireActivity();
    }

    private void download(int semId) {
        download.getSemesterEvents(semId)
                .setOnSuccessListener(result -> {
                    semesterID = semId;
                    if (result == null) {
                        throw new NoDataException("Download failed");
                    } else if (result.isEmpty()) {
                        Log.e(TAG, "download: ", new NoDataException("The Semester has no events"));
                        view.removeAllViewsInLayout();
                        view.removeAllViews();
                        view.addView(new Title(requireContext(), getString(R.string.no_event)));
                        view.invalidate();
                        return;
                    }
                    listEvents(result);
                }).setOnFailListener(e -> {
                    view.removeAllViewsInLayout();
                    view.removeAllViews();
                    view.addView(new Title(requireContext(), getString(R.string.no_event)));
                    view.invalidate();
                    Toast.makeToast(getContext(), "Loading page content didn't work. Please try again later").show();
                    Log.e(TAG, "Loading the semester program did not work", e);
                });
    }

    void listEvents(@NonNull ArrayList<de.b3ttertogeth3r.walhalla.object.Event> eventList) {
        view.removeAllViewsInLayout();
        view.removeAllViews();
        Title semester = new Title(requireContext());
        semester.setTitle(Values.semesterList.get(semesterID).getName_long());
        view.addView(semester);
        int i = 0;
        for (de.b3ttertogeth3r.walhalla.object.Event e : eventList) {
            boolean checker = Cache.CACHE_DATA.getRank().canSee(e.getVisibility());
            if (checker) {
                try {
                    view.addView(Event.create(requireActivity(), null, e)
                            .addTouchListener(new Touch() {
                                @Override
                                public void onClick(View view) {
                                    try {
                                        FragmentManager fm = requireActivity().getSupportFragmentManager();
                                        EventDetails.display(fm, DialogSize.FULL_SCREEN, Values.semesterList.get(semesterID), e);
                                    } catch (Exception e) {
                                        Log.e("Event", "onClickListener: Opening dialog exception", e);
                                    }
                                }
                            })
                            .show());
                    i++;
                } catch (Exception exception) {
                    Log.e(TAG, "displaying event did not work", exception);
                }
                if (i == 5) {
                    i = 0;
                    AdView adView = new AdView(requireContext());
                    adView.setAdSize(AdSize.LARGE_BANNER);
                    adView.setAdUnitId(getString(R.string.adUnitId));
                    MobileAds.initialize(requireContext(), initializationStatus -> {
                    });

                    AdRequest adRequest = new AdRequest.Builder().build();
                    adView.loadAd(adRequest);
                    view.addView(adView);
                }
            }
        }
    }
}
