/*
 * Copyright (c) 2022.
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

package de.b3ttertogeth3r.walhalla.old.fragments_main.program;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.xw.repo.BubbleSeekBar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.old.design.MyToast;
import de.b3ttertogeth3r.walhalla.old.enums.Punctuality;
import de.b3ttertogeth3r.walhalla.old.firebase.Crashlytics;
import de.b3ttertogeth3r.walhalla.old.firebase.Firestore;
import de.b3ttertogeth3r.walhalla.old.interfaces.ChangeListener;
import de.b3ttertogeth3r.walhalla.old.interfaces.MyCompleteListener;
import de.b3ttertogeth3r.walhalla.old.interfaces.MyTextWatcher;
import de.b3ttertogeth3r.walhalla.old.interfaces.Reload;
import de.b3ttertogeth3r.walhalla.old.models.Event;

public class NewEventDialog extends DialogFragment implements ChangeListener<Event> {
    private static final String TAG = "NewEventDialog";
    public static Dialog DIALOG;
    private final ArrayList<String> punctArray;
    private Event event = new Event();
    private Toolbar toolbar;
    private RelativeLayout date, time, duration, collar;
    private Button visibleTo;
    private TextView eventVisibleToList;
    private BubbleSeekBar punctuality;
    private EditText title, description;
    private AppCompatCheckBox meetingBox;
    private Reload dismissListener = null;

    public NewEventDialog () {
        punctArray = new ArrayList<>();
        punctArray.add(0, Punctuality.ST.getShort_description());
        punctArray.add(1, Punctuality.CT.getShort_description());
        punctArray.add(2, Punctuality.ALL_DAY.toString());
        punctArray.add(3, Punctuality.INFO.toString());
    }

    @Nullable
    public static NewEventDialog show (FragmentManager manager) {
        try {
            NewEventDialog dialog = new NewEventDialog();
            dialog.show(manager, TAG);
            return dialog;
        } catch (Exception e) {
            Crashlytics.error(TAG, "Displaying dialog to create a new event did not work", e);
            return null;
        }
    }

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.event_create, container, false);
        toolbar = view.findViewById(R.id.program_details_close);
        date = view.findViewById(R.id.event_date);
        time = view.findViewById(R.id.event_time);
        duration = view.findViewById(R.id.event_duration);
        collar = view.findViewById(R.id.event_collar);
        punctuality = view.findViewById(R.id.event_punctuality);
        title = view.findViewById(R.id.event_title);
        description = view.findViewById(R.id.event_description);
        meetingBox = view.findViewById(R.id.isMeeting);
        visibleTo = view.findViewById(R.id.event_visible_to);
        eventVisibleToList = view.findViewById(R.id.event_visible_list);
        event.setPunctuality("ct");

        //region punctuality bubble seek bar
        punctuality.getConfigBuilder().hideBubble()
                .trackColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                .showSectionText()
                .showSectionMark()
                .min(0)
                .max(3)
                .seekBySection()
                .sectionTextInterval(1)
                .sectionCount(3)
                .rtl(false)
                .autoAdjustSectionMark()
                .sectionTextPosition(BubbleSeekBar.TextPosition.BELOW_SECTION_MARK)
                .secondTrackColor(ContextCompat.getColor(requireContext(),
                        R.color.colorAccentLight))
                .progress(1)
                .build();
        punctuality.setCustomSectionTextArray((sectionCount, array) -> {
            array.clear();
            for (int i = 0; i < punctArray.size(); i++) {
                array.put(i, punctArray.get(i));
            }

            return array;
        });
        //endregion
        return view;
    }

    @Override
    public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        event.setChangeListener(this);
        title.addTextChangedListener((MyTextWatcher) s -> event.setTitle(s.toString()));
        description.addTextChangedListener((MyTextWatcher) s -> event.setDescription(s.toString()));
        meetingBox.setOnClickListener(v -> event.setMeeting(meetingBox.isEnabled()));
        toolbar.setTitle(R.string.event_add);
        //remove menu x(abort)
        toolbar.getMenu().removeItem(R.id.abort);
        toolbar.setOnMenuItemClickListener(item -> {
            int clickedId = item.getItemId();
            if (clickedId == R.id.abort) {
                abort();
            } else if (clickedId == R.id.save) {
                uploadEvent();
            }
            return false;
        });
        toolbar.setNavigationOnClickListener(v -> abort());
        date.setOnClickListener(v -> {
            Calendar date = Calendar.getInstance();
            if (event.getBegin() != null) {
                date.setTime(event.getBegin().toDate());
            }
            DatePickerDialog datePicker = new DatePickerDialog(requireContext(),
                    (v1, year, month, dayOfMonth) -> {
                        date.set(Calendar.YEAR, year);
                        date.set(Calendar.MONTH, month);
                        date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        event.setBegin(new Timestamp(date.getTime()));
                        ((TextView) this.date.findViewById(R.id.event_date_text))
                                .setText(event.getBeginDateString());
                    }, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE));
            datePicker.show();
        });
        time.setOnClickListener(v -> {
            Calendar date = Calendar.getInstance();
            if (event.getBegin() != null) {
                date.setTime(event.getBegin().toDate());
            }
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                    (v1, hourOfDay, minute) -> {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        event.setBegin(new Timestamp(date.getTime()));
                        ((TextView) this.time.findViewById(R.id.event_time_text))
                                .setText(event.getBeginTimeString());
                    }, 20, 0, true);
            timePickerDialog.show();
        });
        duration.setOnClickListener(v -> {
            Log.e(TAG, "onClick: duration clicked");
            try {
                durationDialog();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        collar.setOnClickListener(v -> {
            CharSequence[] collarList = new CharSequence[]{"ho", "o", "io"};
            final String[] selectedCollar = {"o"};
            AlertDialog.Builder collarDialog = new AlertDialog.Builder(getContext());
            collarDialog.setTitle(R.string.event_collar)
                    .setSingleChoiceItems(collarList, 1, (dialog, which) -> selectedCollar[0] =
                            collarList[which].toString())
                    .setPositiveButton(R.string.ok,
                            (dialog, which) -> {
                                event.setCollar(selectedCollar[0]);
                                ((TextView) this.collar.findViewById(R.id.event_collar_text))
                                        .setText(event.getCollar());
                            })
                    .setNegativeButton(R.string.abort, null);
            AlertDialog dialog = collarDialog.create();
            dialog.show();
        });
        punctuality.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged (BubbleSeekBar bubbleSeekBar, int progress,
                                           float progressFloat, boolean fromUser) {

            }

            @Override
            public void getProgressOnActionUp (BubbleSeekBar bubbleSeekBar, int progress,
                                               float progressFloat) {
            }

            @Override
            public void getProgressOnFinally (BubbleSeekBar bubbleSeekBar, int progress,
                                              float progressFloat, boolean fromUser) {
                event.setPunctuality(punctArray.get(progress));
            }
        });
        CharSequence[] visibilityList = de.b3ttertogeth3r.walhalla.old.enums.Group.getList();
        ArrayList<String> checkedVisList = new ArrayList<>();
        if(event.getVisibleTo() != null) {
            checkedVisList.addAll(event.getVisibleTo());
        }
        visibleTo.setOnClickListener(v -> {
            AlertDialog visibleToDialog = new AlertDialog.Builder(getContext())
                    .setMultiChoiceItems(visibilityList, null,
                            (dialog, which, isChecked) -> {
                                if(isChecked) {
                                    if(!checkedVisList.contains(visibilityList[which].toString())){
                                        checkedVisList.add(visibilityList[which].toString());
                                    }
                                } else {
                                    checkedVisList.remove(visibilityList[which].toString());
                                }
                            })
                    .setPositiveButton(R.string.send, (dialog, which) -> {
                        event.setVisibleTo(checkedVisList);
                        StringBuilder stringBuilder = new StringBuilder();
                        for(String s : checkedVisList) {
                            stringBuilder.append(s).append("\n");
                        }
                        eventVisibleToList.setText(stringBuilder);
                    })
                    .setNegativeButton(R.string.abort, null).create();
            visibleToDialog.show();
        });
    }

    private void abort () {
        this.dismiss();
    }

    private void uploadEvent () {
        if (event.isValid()) {
            // TODO Upload event to the semester it is in.
            Firestore.uploadEvent(event, new MyCompleteListener<DocumentReference>() {
                @Override
                public void onSuccess (@Nullable DocumentReference result) {
                    MyToast toast = new MyToast(getContext());
                    toast.setMessage("Upload successful").show();
                    abort();
                }

                @Override
                public void onFailure (@Nullable Exception exception) {
                    MyToast toast = new MyToast(getContext());
                    toast.setMessage(exception.getMessage()).show();
                    Crashlytics.error(TAG, "upload of event failed", exception);
                }
            });
        }
    }

    private void durationDialog () {
        String[] hoursStr = new String[72];
        for (int i = 0; i < hoursStr.length; i++) {
            hoursStr[i] = (i + 1) + " h";
        }
        String[] minutesStr = new String[]{"0 min", "15 min", "30 min", "45 min"};
        RelativeLayout numberPickers =
                (RelativeLayout) getLayoutInflater().inflate(R.layout.dialog_item_sem_change, null);
        numberPickers.findViewById(R.id.np_left).setVisibility(View.GONE);
        NumberPicker left = numberPickers.findViewById(R.id.np_center);
        NumberPicker right = numberPickers.findViewById(R.id.np_right);
        left.setDisplayedValues(hoursStr);
        left.setMinValue(0);
        left.setMaxValue(71);
        left.setWrapSelectorWheel(false);
        left.setDescendantFocusability(NumberPicker.FOCUS_AFTER_DESCENDANTS);
        right.setDisplayedValues(minutesStr);
        right.setMinValue(0);
        right.setMaxValue(3);
        right.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        AlertDialog durationDialog = new AlertDialog.Builder(requireContext())
                .setView(numberPickers)
                .setTitle(R.string.event_duration_set)
                .setPositiveButton(R.string.send, (dialog, which) -> {
                    String hour = hoursStr[left.getValue()];
                    String minutes = minutesStr[right.getValue()];
                    Log.e(TAG, "onClick: h:" + hour + ", m:" + minutes);
                    // TODO: 20.01.22 something is wrong with the year and the time
                    int durationM = ((left.getValue() + 1) * 60);
                    switch (right.getValue()) {
                        case 0:
                            break;
                        case 1:
                            durationM = 15;
                            break;
                        case 2:
                            durationM = 30;
                            break;
                        case 3:
                            durationM = 45;
                            break;
                    }
                    long begin = event.getBegin().getSeconds();
                    long end = begin + (durationM * 60L);
                    event.setEnd(new Timestamp(new Date(end)));
                    Log.d(TAG, "durationDialog: " + event.getEnd());
                    ((TextView) duration.findViewById(R.id.event_duration_text))
                            .setText((hour + " & " + minutes));
                })
                .setNegativeButton(R.string.abort, null)
                .create();
        durationDialog.show();
    }

    @Override
    public void dismiss () {
        super.dismiss();
        if (dismissListener != null) {
            dismissListener.site();
        }
    }

    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }

    @Override
    public void onStart () {
        super.onStart();
        DIALOG = getDialog();
        if (DIALOG != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            Objects.requireNonNull(DIALOG.getWindow()).setLayout(width, height);
            DIALOG.getWindow().setWindowAnimations(R.style.AppTheme_Slide);
        }
    }

    @Override
    public void change (@NonNull Event change) {
        if (change.exists() && change.isValid()) {
            this.event = change;
        }
    }

    public void addOnDismissListener (Reload reload) {
        this.dismissListener = reload;
    }
}
