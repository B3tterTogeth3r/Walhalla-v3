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

package de.b3ttertogeth3r.walhalla.fragment.signed_in;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstract_generic.Fragment;
import de.b3ttertogeth3r.walhalla.abstract_generic.Touch;
import de.b3ttertogeth3r.walhalla.design.LinearLayout;
import de.b3ttertogeth3r.walhalla.design.ProfileRow;
import de.b3ttertogeth3r.walhalla.design.SideNav;
import de.b3ttertogeth3r.walhalla.design.Title;
import de.b3ttertogeth3r.walhalla.design.Toast;
import de.b3ttertogeth3r.walhalla.dialog.ChangeSemester;
import de.b3ttertogeth3r.walhalla.exception.CreateDialogException;
import de.b3ttertogeth3r.walhalla.firebase.Firebase;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IAuth;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IFirestoreDownload;
import de.b3ttertogeth3r.walhalla.object.File;
import de.b3ttertogeth3r.walhalla.object.Log;
import de.b3ttertogeth3r.walhalla.util.Cache;
import de.b3ttertogeth3r.walhalla.util.Values;

public class Transcript extends Fragment {
    private static final String TAG = "Transcript";
    private IFirestoreDownload download;
    private int semesterId;
    private LinearLayout layout;
    private ArrayList<File> fileList;
    private boolean isBoardMember = false;

    @Override
    public void constructor() {
        download = Firebase.Firestore.download();
        IAuth auth = Firebase.authentication();
        if (!auth.isSignIn()) {
            Toast.makeToast(requireContext(), R.string.fui_error_session_expired).show();
            SideNav.changePage(R.string.menu_home, requireActivity().getSupportFragmentManager().beginTransaction());
        }
        semesterId = Values.currentSemester.getId();
        isBoardMember = Cache.CACHE_DATA.isBoardMember();
    }

    @Override
    public String analyticsProperties() {
        return TAG;
    }

    @Override
    public void start() {
        download();
    }

    private void download() {
        download.getSemesterProtocols(semesterId)
                .setOnSuccessListener(result -> {
                    if (result == null || result.isEmpty()) {
                        noFiles();
                        return;
                    }
                    fileList = result;
                    showFiles();
                }).setOnFailListener(e -> Log.e(TAG, "onFailureListener: ", e));

    }

    private void noFiles() {
        layout.addView(new Title(requireContext(), R.string.transcript_no_files));
        layout.invalidate();
    }

    private void showFiles() {
        layout.removeAllViewsInLayout();
        for (File f : fileList) {
            ProfileRow row = f.getView(requireActivity());
            if (isBoardMember) {
                row.addTouchListener(new Touch() {
                    @Override
                    public void onLongClick(View view) {
                        // TODO: 29.07.22 Open dialog to edit the file.
                    }
                });
            }
            layout.addView(row);
        }
    }

    @Override
    public void toolbarContent() {
        toolbar.setTitle("");
        customToolbar.setVisibility(View.VISIBLE);
        customToolbarTitle.setText(R.string.menu_transcript);
        customToolbar.setOnClickListener(v -> displayDialog());
        if (isBoardMember) {
            // TODO: 29.07.22 add menu to upload more transcripts.
        }
    }

    private void displayDialog() {
        try {
            ChangeSemester.display(getParentFragmentManager(), Values.semesterList.get(semesterId))
                    .setOnSuccessListener(result -> {
                        assert result != null;
                        semesterId = result;
                        download();
                    });
        } catch (CreateDialogException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createView(@NonNull android.widget.LinearLayout view) {
        layout = new LinearLayout(requireContext());
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        view.addView(layout);
    }

    @Override
    public FragmentActivity authStatusChanged(FirebaseAuth firebaseAuth) {
        return requireActivity();
    }
}
