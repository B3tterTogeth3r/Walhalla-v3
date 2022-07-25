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
import de.b3ttertogeth3r.walhalla.design.Toast;
import de.b3ttertogeth3r.walhalla.dialog.ChangeSemester;
import de.b3ttertogeth3r.walhalla.enums.Rank;
import de.b3ttertogeth3r.walhalla.exception.CreateDialogException;
import de.b3ttertogeth3r.walhalla.firebase.Firebase;
import de.b3ttertogeth3r.walhalla.firebase.RemoteConfig;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IFirestoreDownload;
import de.b3ttertogeth3r.walhalla.object.BoardMember;
import de.b3ttertogeth3r.walhalla.object.Log;
import de.b3ttertogeth3r.walhalla.object.Semester;

public class StudentBoard extends Fragment {
    private static final String TAG = "StudentBoard";
    private IFirestoreDownload download;
    private LinearLayout view;

    @Override
    public void constructor() {
        download = Firebase.firestoreDownload();
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
        customToolbarTitle.setText(R.string.menu_chargen);
        customToolbar.setOnClickListener(v -> {
            try {
                ChangeSemester.display(getParentFragmentManager(),
                                new Semester(Firebase.remoteConfig().getInt(RemoteConfig.CURRENT_SEMESTER)))
                        .setOnSuccessListener(result -> {
                            assert result != null;
                            download(result);
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
        download.board(Rank.ACTIVE, String.valueOf(semId))
                .setOnSuccessListener(result -> {
                    if (result == null) {
                        throw new NullPointerException("Result list is null");
                    } else if (result.isEmpty()) {
                        // TODO: 31.05.22 add text with content like "this semester did't
                        //  have a board" or so
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
