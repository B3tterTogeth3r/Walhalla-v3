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
import de.b3ttertogeth3r.walhalla.firebase.RemoteConfig;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IFirestoreDownload;
import de.b3ttertogeth3r.walhalla.object.Log;
import de.b3ttertogeth3r.walhalla.object.Semester;

public class Program extends Fragment {
    private static final String TAG = "ProgramFragment";
    private IFirestoreDownload download;
    private LinearLayout view;

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
        download(1);
    }

    @Override
    public void toolbarContent() {
        toolbar.setTitle("");
        customToolbar.setVisibility(View.VISIBLE);
        customToolbarTitle.setText(R.string.menu_program);
        customToolbar.setOnClickListener(v ->
                {
                    try {
                        ChangeSemester.display(getParentFragmentManager(),
                                        new Semester(Firebase.remoteConfig().getInt(RemoteConfig.CURRENT_SEMESTER)))
                                .setOnSuccessListener(result -> {
                                    assert result != null;
                                    download(result);
                                });
                    } catch (CreateDialogException e) {
                        e.printStackTrace();
                    }
                }
        );
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
                    Toast.makeToast(getContext(), "Loading page content didn't work. Please try again later").show();
                    Log.e(TAG, "Loading the semester program did not work", e);
                });
    }

    void listEvents(@NonNull ArrayList<de.b3ttertogeth3r.walhalla.object.Event> eventList) {
        view.removeAllViewsInLayout();
        view.removeAllViews();
        int i = 0;
        for (de.b3ttertogeth3r.walhalla.object.Event e : eventList) {
            view.addView(Event.create(requireActivity(), null, e)
                    .addTouchListener(new Touch() {
                        @Override
                        public void onClick(View view) {
                            try {
                                FragmentManager fm = requireActivity().getSupportFragmentManager();
                                EventDetails.display(fm, DialogSize.FULL_SCREEN, e);
                            } catch (Exception e) {
                                Log.e("Event", "onClickListener: Opening dialog exception", e);
                            }
                        }
                    })
                    .show());
            i++;
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
