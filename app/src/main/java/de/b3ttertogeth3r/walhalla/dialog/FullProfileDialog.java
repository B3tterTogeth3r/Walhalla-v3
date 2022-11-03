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

package de.b3ttertogeth3r.walhalla.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.Timestamp;

import java.util.Calendar;

import de.b3ttertogeth3r.walhalla.MainActivity;
import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstract_generic.Dialog;
import de.b3ttertogeth3r.walhalla.design.ProfileRow;
import de.b3ttertogeth3r.walhalla.enums.DialogSize;
import de.b3ttertogeth3r.walhalla.exception.CreateDialogException;
import de.b3ttertogeth3r.walhalla.object.Person;
import de.b3ttertogeth3r.walhalla.object.Semester;
import de.b3ttertogeth3r.walhalla.util.Log;
import de.b3ttertogeth3r.walhalla.util.Values;

public class FullProfileDialog extends Dialog<Person> implements View.OnClickListener {
    private static final String TAG = "FullProfileDialog";
    private final Person person;

    public FullProfileDialog(@NonNull Person person) {
        super(DialogSize.FULL_SCREEN);
        this.person = person;
    }

    @NonNull
    public static FullProfileDialog display(FragmentManager fragmentManager, @NonNull Person person) throws CreateDialogException {
        try {
            FullProfileDialog dialog = new FullProfileDialog(person);
            dialog.show(fragmentManager, TAG);
            return dialog;
        } catch (Exception e) {
            throw new CreateDialogException("unable to create " + TAG, e);
        }
    }

    @Override
    public Person done() throws Exception {
        MainActivity.hideKeyBoard.hide();
        if (e != null) {
            throw e;
        } else {
            return person;
        }
    }

    @Override
    public void createDialog(@NonNull RelativeLayout container, @NonNull LayoutInflater inflater) {
        ScrollView layout = new ScrollView(requireContext());
        layout.addView(person.getViewEdit(requireContext(), this));
        container.addView(layout);
    }

    @Override
    public void configDialog(@NonNull AlertDialog.Builder builder) {
        builder.setPositiveButton(R.string.save, this);
        builder.setNegativeButton(R.string.abort, this);
    }

    @Override
    public void configToolbar(@NonNull Toolbar toolbar) {
        toolbar.setTitle(R.string.menu_profile);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        if (view != null) {
            try {
                switch (view.getId()) {
                    case R.id.first_name:
                        Log.i(TAG, "onClick: name");
                        ProfileEditDialog.display(getParentFragmentManager(), person.getFirst_Name())
                                .setOnSuccessListener(result -> {
                                    ((ProfileRow) view).setContent(result);
                                    person.setFirst_Name(String.valueOf(result));
                                });
                        break;
                    case R.id.last_name:
                        Log.i(TAG, "onClick: name");
                        ProfileEditDialog.display(getParentFragmentManager(), person.getLast_Name())
                                .setOnSuccessListener(((ProfileRow) view)::setContent);
                        break;
                    case R.id.mobile:
                        Log.i(TAG, "onClick: mobile");
                        ProfileEditDialog.display(getParentFragmentManager(), person.getMobile())
                                .changeInputType(InputType.TYPE_CLASS_PHONE)
                                .setOnSuccessListener(((ProfileRow) view)::setContent);
                        break;
                    case R.id.from:
                        Log.i(TAG, "onClick: from");
                        ProfileEditDialog.display(getParentFragmentManager(), person.getOrigin())
                                .setOnSuccessListener(((ProfileRow) view)::setContent);
                        break;
                    case R.id.major:
                        Log.i(TAG, "onClick: major");
                        ProfileEditDialog.display(getParentFragmentManager(), person.getMajor())
                                .setOnSuccessListener(((ProfileRow) view)::setContent);
                        break;
                    case R.id.nickname:
                        Log.i(TAG, "onClick: nickname");
                        ProfileEditDialog.display(getParentFragmentManager(), person.getNickname())
                                .setOnSuccessListener(((ProfileRow) view)::setContent);
                        break;
                    case R.id.dob:
                        Log.i(TAG, "onClick: dob");
                        // Create dialog to change the birthday
                        Calendar date = Calendar.getInstance();
                        if (person.getBirthday() != null) {
                            date.setTime(person.getBirthday().toDate());
                        }
                        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                Calendar date = Calendar.getInstance();
                                date.set(Calendar.YEAR, year);
                                date.set(Calendar.MONTH, month);
                                date.set(Calendar.DAY_OF_MONTH, day);
                                person.setBirthday(new Timestamp(date.getTime()));
                                ((ProfileRow) view).setContent(day + "." + (month + 1) + "." + year);
                            }
                        }, date.get(Calendar.YEAR), date.get(Calendar.MONTH),
                                date.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.show();
                        break;
                    case R.id.rank:
                        Log.i(TAG, "onClick: rank");
                        RankSelectDialog.display(getParentFragmentManager())
                                .setOnSuccessListener(result -> {
                                    if (result != null) {
                                        ((ProfileRow) view).setContent(result.toString());
                                        person.setRank(result);
                                    }
                                });
                        break;
                    case R.id.joined:
                        Log.i(TAG, "onClick: joined");
                        ChangeSemester.display(getParentFragmentManager(), new Semester(person.getJoined()))
                                .setOnSuccessListener(result -> {
                                    if (result == null || result == -1) {
                                        throw new NullPointerException("Semester id cannot be null or -1");
                                    }
                                    person.setJoined(result);
                                    ((ProfileRow) view).setContent(Values.semesterList.get(result).getName_long());
                                });
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                Log.e(TAG, "onClick: ", e);
            }
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
