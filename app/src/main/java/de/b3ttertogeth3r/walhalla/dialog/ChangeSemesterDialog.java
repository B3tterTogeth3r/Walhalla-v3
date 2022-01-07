package de.b3ttertogeth3r.walhalla.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;

import de.b3ttertogeth3r.walhalla.MainActivity;
import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.enums.Kind;
import de.b3ttertogeth3r.walhalla.firebase.Crashlytics;
import de.b3ttertogeth3r.walhalla.firebase.Firestore;
import de.b3ttertogeth3r.walhalla.interfaces.SemesterChangeListener;
import de.b3ttertogeth3r.walhalla.models.Semester;

public class ChangeSemesterDialog extends DialogFragment implements DialogInterface.OnClickListener {
    private static final String TAG = "ChangeSemesterDialog";
    private final Kind kind;
    private final SemesterChangeListener listener;
    private final int start_id;
    private final Semester semester;
    private NumberPicker np_right, np_center;

    public ChangeSemesterDialog (Kind kind, SemesterChangeListener listener, Semester semester) {
        this.kind = kind;
        this.listener = listener;
        this.semester = semester;
        this.start_id = semester.getId();
    }

    public static void display (FragmentManager fragmentManager, Kind kind,
                                SemesterChangeListener listener, Semester semester) {
        try {
            ChangeSemesterDialog dialog = new ChangeSemesterDialog(kind, listener, semester);
            dialog.show(fragmentManager, TAG);
        } catch (Exception e) {
            Crashlytics.log(TAG, "Could not start dialog");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog (@Nullable Bundle savedInstanceState) {
        Activity activity = requireActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.dialog_change_semester,
                null);

        RelativeLayout layout = view.findViewById(R.id.dialog_layout);
        layout.removeAllViewsInLayout();
        Toolbar toolbar = view.findViewById(R.id.dialog_toolbar);
        toolbar.setNavigationOnClickListener(v -> dismiss());
        @SuppressLint("InflateParams")
        RelativeLayout numberPickers =
                (RelativeLayout) inflater.inflate(R.layout.dialog_item_sem_change, null);
        numberPickers.findViewById(R.id.np_left).setVisibility(View.GONE);

        np_center = numberPickers.findViewById(R.id.np_center);
        np_right = numberPickers.findViewById(R.id.np_right);
        String[] time = new String[]{getString(R.string.ws_long), getString(R.string.ss_long)};
        np_center.setDisplayedValues(time);
        np_center.setMinValue(0);
        np_center.setMaxValue(1);
        np_center.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        layout.addView(numberPickers);
        if (kind != Kind.JOINED) {
            toolbar.setTitle(R.string.dialog_semester_change);
        } else {
            toolbar.setTitle(R.string.dialog_semester_select);
        }
        String[] year = createYears();
        np_right.setMinValue(0);
        np_right.setMaxValue(year.length - 1);
        np_right.setValue(year.length - 1);
        np_right.setDisplayedValues(year);
        np_right.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        //Select also a previous joined semester
        if (kind == Kind.JOINED) {
            Log.d(TAG, "onCreateDialog: startId == " + start_id + " - kind == " + kind);
            if ((start_id % 2) == 0) {
                np_center.setValue(1);
                np_right.setValue((start_id / 2) - 1);
            } else {
                np_right.setDisplayedValues(createYearsWS());
                np_right.setValue(((int) (start_id / 2f) - 1));
            }
            np_center.setOnValueChangedListener((picker, oldVal, newVal) -> {
                if (newVal == 0) {
                    np_right.setDisplayedValues(createYearsWS());
                } else if (newVal == 1) {
                    np_right.setDisplayedValues(createYears());
                }
            });
        } else {
            if ((semester.getId() % 2) == 0) {
                np_right.setDisplayedValues(createYearsWS());
                np_right.setValue(((int) (semester.getId() / 2f) - 1));
            } else {
                np_center.setValue(1);
                np_right.setValue((semester.getId() / 2) - 1);
            }
            np_center.setOnValueChangedListener((picker, oldVal, newVal) -> {
                if (newVal == 0) {
                    np_right.setDisplayedValues(createYearsWS());
                } else if (newVal == 1) {
                    np_right.setDisplayedValues(createYears());
                }
            });
        }

        builder.setView(view);
        builder.setPositiveButton(R.string.send, this);
        builder.setNeutralButton(R.string.abort, this);

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @NonNull
    public static String[] createYears () {
        ArrayList<String> years = new ArrayList<>();
        Calendar date = Calendar.getInstance();
        int year = date.get(Calendar.YEAR) + 1;
        for (int i = 1864; i < year; i++) {
            years.add(String.valueOf(i));
        }
        return years.toArray(new String[0]);
    }

    @NonNull
    private String[] createYearsWS () {
        ArrayList<String> years = new ArrayList<>();
        Calendar date = Calendar.getInstance();
        int year = date.get(Calendar.YEAR) + 1;
        for (int i = 1864; i < year; i++) {
            String number = i + "/" + String.valueOf(i + 1).substring(2);
            years.add(number);
        }
        return years.toArray(new String[0]);
    }

    @Override
    public void onClick (DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_NEUTRAL || which == DialogInterface.BUTTON_NEGATIVE) {
            dismiss();
        } else if (which == DialogInterface.BUTTON_POSITIVE) {
            float timeInt;
            if (np_center.getValue() == 1) {
                timeInt = np_center.getValue();
            } else {
                timeInt = np_center.getValue() + 1.5f;
            }
            int yearInt = np_right.getValue();
            int semesterID = (int) ((timeInt + yearInt) * 2 - 1); // starting with semester number 0

            // only load new semester, if user changed something.
            if (semesterID != semester.getId()) {
                Firestore.getSemester(semesterID)
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                try {
                                    Semester s = documentSnapshot.toObject(Semester.class);
                                    s.setId(semesterID);
                                    try {
                                        if (kind.equals(Kind.JOINED)) {
                                            listener.joinedDone(s);
                                        } else {
                                            listener.selectorDone(s);
                                        }
                                    } catch (Exception e) {
                                        Crashlytics.log(TAG, "onClick: if-else", e);
                                    }
                                } catch (Exception error) {
                                    Crashlytics.log(TAG, "onSuccess: selected semester does not " +
                                            "exist", error);
                                    Snackbar.make(MainActivity.parentLayout,
                                            R.string.error_semester_not_exist,
                                            Snackbar.LENGTH_LONG).show();
                                }
                            } else {
                                Crashlytics.log(TAG, "onSuccess: selected semester does not exist");
                                Snackbar.make(MainActivity.parentLayout,
                                        R.string.error_semester_not_exist, Snackbar.LENGTH_LONG).show();
                            }
                        });
            }
        }
    }

}
