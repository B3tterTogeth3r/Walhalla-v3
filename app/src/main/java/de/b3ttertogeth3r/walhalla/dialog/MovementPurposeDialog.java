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

package de.b3ttertogeth3r.walhalla.dialog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstract_generic.Loader;
import de.b3ttertogeth3r.walhalla.exception.CreateDialogException;
import de.b3ttertogeth3r.walhalla.exception.NoDataException;
import de.b3ttertogeth3r.walhalla.firebase.Firebase;
import de.b3ttertogeth3r.walhalla.firebase.Firebase.Firestore;
import de.b3ttertogeth3r.walhalla.firebase.Firebase.Realtime;
import de.b3ttertogeth3r.walhalla.object.Event;
import de.b3ttertogeth3r.walhalla.util.Log;
import de.b3ttertogeth3r.walhalla.util.Values;

/**
 * This class is for the user to select an event name or another purpose type
 * to group the movements by at an audit at the event of every semester.
 *
 * @author B3tterTogeth3r
 * @version 1.0
 * @see de.b3ttertogeth3r.walhalla.abstract_generic.Dialog Dialog <code>super class</code>
 * @since 10.09.2022
 */
public class MovementPurposeDialog extends DialogFragment implements DialogInterface.OnClickListener {
    private static final String TAG = "MovementPurposeDialog";
    private static Loader<String> loader;
    private static FragmentManager fragmentManager;
    private final CharSequence[] purposeList;
    private CharSequence result;

    public MovementPurposeDialog(CharSequence[] purposeList) {
        this.purposeList = purposeList;
    }

    @NonNull
    public static Loader<String> display(FragmentManager fragmentManager, Loader<String> loader) throws CreateDialogException {
        try {
            downloadData().setOnSuccessListener(result -> {
                MovementPurposeDialog dialog = new MovementPurposeDialog(result);
                dialog.show(fragmentManager, TAG);
            }).setOnFailListener(loader::done);
            MovementPurposeDialog.loader = loader;
            MovementPurposeDialog.fragmentManager = fragmentManager;
            return loader;
        } catch (Exception e) {
            throw new CreateDialogException("Creating MovementPurposeDialog didn't work", e);
        }
    }

    @NonNull
    private static Loader<CharSequence[]> downloadData() {
        Loader<CharSequence[]> loader = new Loader<>();
        ArrayList<String> purposeList = new ArrayList<>();
        Realtime.download()
                .getPurposeList()
                .setOnSuccessListener(result -> {
                    if (result != null && !result.isEmpty()) {
                        purposeList.addAll(result);
                    }
                    downloadEvents(purposeList, loader);
                })
                .setOnFailListener(loader::done);
        return loader;
    }

    private static void downloadEvents(ArrayList<String> purposeList, @NonNull Loader<CharSequence[]> loader) {
        Firestore.download()
                .getSemesterEvents(Values.currentSemester.getId())
                .setOnFailListener(loader::done)
                .setOnSuccessListener(result1 -> {
                    if (result1 != null && !result1.isEmpty()) {
                        for (Event e : result1) {
                            purposeList.add(e.getTitle());
                        }
                    }
                    CharSequence[] list = new CharSequence[purposeList.size()];
                    for (int i = 0; i < purposeList.size(); i++) {
                        list[i] = purposeList.get(i);
                    }
                    loader.done(list);
                });
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        switch (i) {
            case DialogInterface.BUTTON_NEUTRAL:
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                loader.done();
                dialogInterface.dismiss();
                break;
            case DialogInterface.BUTTON_POSITIVE:
                loader.done(result.toString());
                dialogInterface.dismiss();
                break;
        }
    }

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle s) {
        // TODO: 19.11.22 buttons are not displayed. WHY???
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setNegativeButton(R.string.abort, this)
                .setPositiveButton(R.string.send, this)
                .setNeutralButton(R.string.add, (dialogInterface, i) -> {
                    //open dialog to add a purpose. on dismiss of the created dialog,
                    // recreate this dialog or reload the SingleChoiceItems.
                    try {
                        ProfileEditDialog.display(getParentFragmentManager(), "")
                                .setOnSuccessListener(result -> {
                                    if (result != null) {
                                        Log.i(TAG, "configDialog: ProfileEditDialog: result = " + result);
                                        Firebase.Realtime.upload()
                                                .newPurpose(result.toString())
                                                .setOnSuccessListener(result1 -> restart())
                                                .setOnFailListener(e -> restart());
                                        return;
                                    }
                                    throw new NoDataException("result is empty");
                                }).setOnFailureListener(e1 -> {
                                    // TODO: 10.11.22 toast error and still reopen dialog.
                                    Log.e(TAG, "configDialog: ProfileEditDialog: error found", e1);
                                    restart();
                                });
                    } catch (CreateDialogException ex) {
                        ex.printStackTrace();

                    }
                })
                .setSingleChoiceItems(purposeList, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        result = purposeList[i];
                    }
                })
                .setTitle(R.string.movement_purpose_dialog);

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    private void restart() {
        loader.done(new Exception("testing"));
    }
}
