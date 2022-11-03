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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.Calendar;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstract_generic.Dialog;
import de.b3ttertogeth3r.walhalla.annotation.SemesterRange;
import de.b3ttertogeth3r.walhalla.enums.DialogSize;
import de.b3ttertogeth3r.walhalla.exception.CreateDialogException;
import de.b3ttertogeth3r.walhalla.object.Semester;

public class ChangeSemester extends Dialog<Integer> {
    private static final String TAG = "ChangeSemester";
    private final Semester semester;
    private NumberPicker np_year, np_sem_kind;

    public ChangeSemester(Semester semester) {
        super(DialogSize.WRAP_CONTENT);
        this.semester = semester;
    }

    @NonNull
    public static ChangeSemester display(FragmentManager fragmentManager,
                                         Semester semester) throws CreateDialogException {
        try {
            ChangeSemester dialog = new ChangeSemester(semester);
            dialog.show(fragmentManager, TAG);
            return dialog;
        } catch (Exception e) {
            throw new CreateDialogException("unable to create dialog", e);
        }
    }

    @Override
    @SemesterRange
    public Integer done() {
        float time;
        if (np_sem_kind.getValue() == 1) {
            time = np_sem_kind.getValue();
        } else {
            time = np_sem_kind.getValue() + 1.5f;
        }
        int year = np_year.getValue();

        //somehow it is right this way, I forgot why :-)
        return (int) ((time + year) * 2 - 2);
    }

    @Override
    public void createDialog(@NonNull RelativeLayout container, @NonNull LayoutInflater inflater) {

        @SuppressLint("InflateParams") RelativeLayout numberPickers =
                (RelativeLayout) inflater.inflate(R.layout.dialog_item_sem_change, null);
        numberPickers.findViewById(R.id.np_left).setVisibility(View.GONE);

        np_sem_kind = numberPickers.findViewById(R.id.np_center);
        np_year = numberPickers.findViewById(R.id.np_right);
        String[] time = new String[]{getString(R.string.ws_long), getString(R.string.ss_long)};
        np_sem_kind.setDisplayedValues(time);
        np_sem_kind.setMinValue(0);
        np_sem_kind.setMaxValue(1);
        np_sem_kind.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        String[] year = createYears();
        np_year.setMinValue(0);
        np_year.setMaxValue(year.length - 1);
        np_year.setValue(year.length - 1);
        np_year.setDisplayedValues(year);
        np_year.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        int semId;
        try {
            semId = semester.getId();
        } catch (Exception npe) {
            semId = 0;
        }
        if ((semId % 2) == 0) {
            np_year.setDisplayedValues(createYearsWS());
            np_year.setValue(((int) (semId / 2f)));
        } else {
            np_sem_kind.setValue(1);
            np_year.setValue((semId / 2));
        }
        np_sem_kind.setOnValueChangedListener((picker, oldVal, newVal) -> {
            if (newVal == 0) {
                np_year.setDisplayedValues(createYearsWS());
            } else if (newVal == 1) {
                np_year.setDisplayedValues(createYears());
            }
        });

        container.addView(numberPickers);
    }

    @Override
    public void configDialog(@NonNull AlertDialog.Builder builder) {
        builder.setPositiveButton(R.string.send, this);
        builder.setNegativeButton(R.string.abort, null);
        builder.setCancelable(false);
    }

    @Override
    public void configToolbar(@NonNull Toolbar toolbar) {
        toolbar.setTitle(R.string.dialog_semester_select);
    }

    @NonNull
    private String[] createYears() {
        ArrayList<String> years = new ArrayList<>();
        Calendar date = Calendar.getInstance();
        int year = date.get(Calendar.YEAR) + 1;
        for (int i = 1864; i < year; i++) {
            years.add(String.valueOf(i));
        }
        return years.toArray(new String[0]);
    }


    @NonNull
    private String[] createYearsWS() {
        ArrayList<String> years = new ArrayList<>();
        Calendar date = Calendar.getInstance();
        int year = date.get(Calendar.YEAR) + 1;
        for (int i = 1864; i < year; i++) {
            String number = i + "/" + String.valueOf(i + 1).substring(2);
            years.add(number);
        }
        return years.toArray(new String[0]);
    }
}
