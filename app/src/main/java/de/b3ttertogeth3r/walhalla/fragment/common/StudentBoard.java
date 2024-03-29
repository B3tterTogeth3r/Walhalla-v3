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

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstract_generic.Fragment;
import de.b3ttertogeth3r.walhalla.design.AdView;
import de.b3ttertogeth3r.walhalla.design.Title;
import de.b3ttertogeth3r.walhalla.design.Toast;
import de.b3ttertogeth3r.walhalla.dialog.ChangeSemester;
import de.b3ttertogeth3r.walhalla.enums.Rank;
import de.b3ttertogeth3r.walhalla.exception.CreateDialogException;
import de.b3ttertogeth3r.walhalla.exception.NoDataException;
import de.b3ttertogeth3r.walhalla.firebase.Firebase;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IFirestoreDownload;
import de.b3ttertogeth3r.walhalla.object.BoardMember;
import de.b3ttertogeth3r.walhalla.object.Semester;
import de.b3ttertogeth3r.walhalla.util.Cache;
import de.b3ttertogeth3r.walhalla.util.Log;
import de.b3ttertogeth3r.walhalla.util.Values;

public class StudentBoard extends Fragment {
    private static final String TAG = "StudentBoard";
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
        String title = getString(R.string.menu_chargen) + " " + Values.semesterList.get(semesterID).getName_short();
        customToolbarTitle.setText(title);
        customToolbar.setOnClickListener(v -> {
            try {
                ChangeSemester.display(getParentFragmentManager(),
                                new Semester(semesterID))
                        .setOnSuccessListener(result -> {
                            assert result != null;
                            download(result);
                            this.semesterID = result;
                        });
            } catch (CreateDialogException e) {
                Log.e(TAG, "toolbarContent: ", e);
            }
        });
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
        download.getSemesterBoard(semId, Rank.ACTIVE)
                .setOnSuccessListener(result -> {
                    if (result == null) {
                        throw new NullPointerException("Result list is null");
                    } else if (result.isEmpty()) {
                        Log.w(TAG, "download: ", new NoDataException("The Semester has no student board"));
                        view.removeAllViewsInLayout();
                        view.removeAllViews();
                        view.addView(new Title(requireContext(), getString(R.string.no_board)));
                        view.invalidate();
                        return;
                    }
                    listMembers(result);
                })
                .setOnFailListener(e -> {
                    Toast.makeToast(getContext(), "Loading page content didn't work. Pleas try again later").show();
                    Log.e(TAG, "Loading the board members did not work", e);
                });

    }

    private void listMembers(@NonNull ArrayList<BoardMember> boardList) {
        view.removeAllViewsInLayout();
        view.removeAllViews();
        for (BoardMember bm : boardList) {
            view.addView(bm.getView(requireActivity()));
        }
        view.addView(new AdView(requireContext()));
    }
}
