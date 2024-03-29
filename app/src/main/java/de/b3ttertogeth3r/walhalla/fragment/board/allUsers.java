/*
 * Copyright (c) 2022-2023.
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

package de.b3ttertogeth3r.walhalla.fragment.board;

import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstract_generic.Fragment;
import de.b3ttertogeth3r.walhalla.abstract_generic.Touch;
import de.b3ttertogeth3r.walhalla.design.TableLayout;
import de.b3ttertogeth3r.walhalla.design.Title;
import de.b3ttertogeth3r.walhalla.design.Toast;
import de.b3ttertogeth3r.walhalla.dialog.FullProfileDialog;
import de.b3ttertogeth3r.walhalla.dialog.InfoDialog;
import de.b3ttertogeth3r.walhalla.exception.NoDataException;
import de.b3ttertogeth3r.walhalla.firebase.Firebase;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IFirestoreDownload;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IFirestoreUpload;
import de.b3ttertogeth3r.walhalla.object.PersonLight;
import de.b3ttertogeth3r.walhalla.util.Log;

public class allUsers extends Fragment {
    private static final String TAG = "allUsers";
    private IFirestoreDownload download;
    private IFirestoreUpload upload;
    private ArrayList<PersonLight> personList;
    private de.b3ttertogeth3r.walhalla.design.LinearLayout personView;

    @Override
    public void constructor() {
        download = Firebase.Firestore.download();
        upload = Firebase.Firestore.upload();
        personList = new ArrayList<>();
    }

    @Override
    public String analyticsProperties() {
        return TAG;
    }

    @Override
    public void start() {
        download.getPersonList()
                .setOnSuccessListener(result -> {
                    if (result == null || result.isEmpty()) {
                        throw new NoDataException("No persons found");
                    }
                    personList = result;
                    fillList();
                }).setOnFailListener(e -> Log.e(TAG, "start: download person list error found.", e));
    }

    @Override
    public void toolbarContent() {
        toolbar.setTitle(R.string.menu_kartei);
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.info);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.info) {
                InfoDialog dialog = new InfoDialog(requireContext().getString(R.string.traffic_explanation));
                dialog.show(getChildFragmentManager(), TAG);
            }
            return false;
        });
    }

    @Override
    public void createView(@NonNull LinearLayout view) {
        personView = new de.b3ttertogeth3r.walhalla.design.LinearLayout(requireContext());
        personView.setOrientation(LinearLayout.VERTICAL);
        view.addView(personView);
    }

    @Override
    public FragmentActivity authStatusChanged(FirebaseAuth firebaseAuth) {
        return requireActivity();
    }

    private void fillList() {
        if (personList.isEmpty()) {
            personView.addView(new Title(requireContext(), "No Persons found."));
            return;
        }
        personView.removeAllViewsInLayout();
        TableLayout table = new TableLayout(requireContext());
        for (PersonLight p : personList) {
            View row = p.getView(requireContext());
            table.addView(row);
            row.setOnTouchListener(new Touch() {
                @Override
                public void onClick(View view) {
                    /* Download the person from the database and then
                     * Open full screen dialog with the ability to edit this person
                     */
                    Firebase.Firestore.download().person(p.getId())
                            .setOnSuccessListener(res -> {
                                if (res == null) {
                                    throw new NoDataException("Downloading person with uid " + p.getId() + " didn't work");
                                }
                                FullProfileDialog.display(getChildFragmentManager(), res)
                                        .setOnSuccessListener(person -> {
                                            // Upload changes, if there are any
                                            if (person != null && person != res && person.validate()) {
                                                upload.setPerson(person).setOnSuccessListener(result -> {
                                                            if (result == null || !result) {
                                                                throw new NullPointerException();
                                                            }
                                                            Toast.makeToast(requireContext(), R.string.upload_complete).show();
                                                        })
                                                        .setOnFailListener(e -> {
                                                            Toast.makeToast(requireContext(), e.getMessage()).show();
                                                            Log.e(TAG, "FullProfileDialog: onSuccessListener: uploadPerson: onFailureListener", e);
                                                        });
                                            }
                                        })
                                        .setOnFailureListener(e -> Log.e(TAG, "onFailureListener: ", e));
                            }).setOnFailListener(e -> Log.e(TAG, "onClick: ", e));
                }
            });
        }
        Log.i(TAG, "fillList: " + table.getChildCount());
        personView.addView(table);
    }
}
