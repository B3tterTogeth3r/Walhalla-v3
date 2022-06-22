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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.old.abstraction.CustomFragment;
import de.b3ttertogeth3r.walhalla.old.design.EventRow;
import de.b3ttertogeth3r.walhalla.old.design.MyTitle;
import de.b3ttertogeth3r.walhalla.old.dialog.ChangeSemesterDialog;
import de.b3ttertogeth3r.walhalla.old.dialog.DetailsDialog;
import de.b3ttertogeth3r.walhalla.old.enums.Kind;
import de.b3ttertogeth3r.walhalla.old.enums.Page;
import de.b3ttertogeth3r.walhalla.old.firebase.Crashlytics;
import de.b3ttertogeth3r.walhalla.old.firebase.Firestore;
import de.b3ttertogeth3r.walhalla.old.interfaces.Reload;
import de.b3ttertogeth3r.walhalla.old.interfaces.SemesterChangeListener;
import de.b3ttertogeth3r.walhalla.old.models.Event;
import de.b3ttertogeth3r.walhalla.old.models.Semester;
import de.b3ttertogeth3r.walhalla.old.utils.CacheData;

/**
 * In this fragment the program of the selected semester. By default the current semester gets
 * displayed. The user can change the selected semester by tapping on the top toolbar.
 *
 * @author B3tterToegth3r
 */
public class Fragment extends CustomFragment implements SemesterChangeListener {
    private static final String TAG = "program.Fragment";
    private static QuerySnapshot documentSnapshots;
    private final ArrayList<Event> eventList = new ArrayList<>();
    private LinearLayout layout, customTitle;
    private Semester semester = CacheData.getChosenSemester();

    @Override
    public void start () {
        // Log.d(TAG, "start: semester: " + semester);
        downloadSemester();
    }

    /**
     * load on start and after the user chose another semester via {@link
     * de.b3ttertogeth3r.walhalla.old.dialog.ChangeSemesterDialog ChangeSemesterDialog}
     */
    private void downloadSemester () {
        registration.add(0, Firestore.loadSemesterEvents(String.valueOf(semester.getId()))
                .addSnapshotListener((value, error) -> {
                    if (error != null || value == null || value.isEmpty()) {
                        return;
                    }
                    documentSnapshots = value;
                    try {
                        updateList();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }));
    }

    private void updateList () {
        eventList.clear();
        for (DocumentSnapshot doc : documentSnapshots) {
            Event e = doc.toObject(Event.class);
            if (e != null && e.exists()) {
                e.setId(doc.getId());
                // TODO admin user
                //  1. always show public events
                //  2. if a user is signed in:
                //  2.1 if user is admin/charge -> show everything (incl drafts)
                //  2.2 show events to its rank(-group)
                //  2.3 maybe show events to the users auth-group
                eventList.add(e);
            }
            try {
                eventList.sort((o1, o2) -> Integer.compare(o1.getBegin().compareTo(o2.getBegin())
                        , o2.getBegin().compareTo(o1.getBegin())));
            } catch (Exception error) {
                Log.e(TAG, "updateList: error while filling array", error);
            }
        }

        //Group eventList by Month and day
        ArrayList<Map<String, Object>> groupByMonth = Group.byMonths(eventList);
        layout.removeAllViewsInLayout();
        if (groupByMonth.isEmpty()) {
            Log.d(TAG, "updateList: groupByMonth list empty");
            // show text with message, that no data could be found or loaded.
            TextView noData = new MyTitle(getContext());
            noData.setText("Es konnte keine Veranstaltung geladen werden.");
            layout.addView(noData);
            return;
        }
        for (Map<String, Object> month : groupByMonth) {
            Log.d(TAG, "month:size = " + month.size());
            if (1 <= month.size()) {
                String title = (String) month.get("title");
                TextView titleTV = new MyTitle(getContext());
                titleTV.setText(title);
                layout.addView(titleTV);
                List<String> keys = new ArrayList<>(month.keySet());
                keys.remove("title");
                int keySize = keys.size();
                for (int i = 0; i < keySize; i++) {
                    Object day = month.get(keys.get(i));
                    if (day == null) {
                        return;
                    } else if (day.getClass().equals(Event.class)) {
                        Event event = (Event) day;
                        EventRow row = new EventRow(getContext(), event);
                        /* TODO: 08.02.22 fix map
                           TODO: 08.02.22 add helpers and account bound to the event, than
                             activate the listener again
                        row.setOnClickListener(v -> Details.display(getParentFragmentManager(),
                                event));*/
                        //TODO add onLongClick to edit displayed details only if user is
                        // enum.Charge.* or super-admin
                        if (Page.PROGRAM.canEditPage(CacheData.getCharge())) {
                            row.setOnLongClickListener(v -> {
                                DetailsDialog dialog = new DetailsDialog(requireContext(), event);
                                dialog.show();
                                return false;
                            });
                        }
                        layout.addView(row);
                    } else {
                        try {
                            @SuppressWarnings("unchecked")
                            Map<String, Event> eventMap =
                                    (Map<String, Event>) day;
                            //Add layout around for the border
                            LinearLayout multiEventAtOneDay = new LinearLayout(getContext());
                            multiEventAtOneDay.setOrientation(LinearLayout.VERTICAL);
                            for (String string : eventMap.keySet()) {
                                // Format the events accordingly
                                Event event = eventMap.get(string);
                                EventRow row = new EventRow(getContext(), event);
                                row.setOnClickListener(v -> Details.display(getParentFragmentManager(),
                                        event));
                                //TODO add onLongClick to edit displayed details only if
                                // user is enum.Charge.* or super-admin
                                row.setOnLongClickListener(v -> {
                                    DetailsDialog dialog = new DetailsDialog(requireContext(),
                                            event);
                                    dialog.show();
                                    return false;
                                });
                                row.isMulti();
                                multiEventAtOneDay.addView(row);
                            }
                            multiEventAtOneDay.setBackgroundResource(R.drawable.border_bottom_black);
                            layout.addView(multiEventAtOneDay);
                        } catch (Exception ex) {
                            Log.d(TAG, "formatResult: design: second try: parsing into that map " +
                                    "did not work at position " + keys.get(i), ex);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void analyticsProperties () {

    }

    @Override
    public void stop () {
        try {
            customTitle.setOnClickListener(null);
        } catch (Exception e) {
            Crashlytics.error(TAG, "Trying to disable the OnClickListener for Custom " +
                    "Title", e);
        }
    }

    @Override
    public void viewCreated () {
        try {
            updateList();
        } catch (Exception ignored) {
        }
    }

    @Override
    public void toolbarContent () {
        customTitle = toolbar.findViewById(R.id.custom_title);
        customTitle.setVisibility(View.VISIBLE);
        ((TextView) customTitle.findViewById(R.id.action_bar_title))
                .setText(CacheData.getChosenSemester().getName_long());
        customTitle.setOnClickListener(v -> {
            // add semester change dialog
            // Log.d(TAG, "toolbarContent: custom title clicked");
            ChangeSemesterDialog.display(getParentFragmentManager(), Kind.CHANGE, this, semester);
        });
        toolbar.getMenu().clear();
        if (Page.PROGRAM.canEditPage(CacheData.getCharge())) {
            toolbar.inflateMenu(R.menu.add);
            toolbar.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.add) {
                    Log.d(TAG, "toolbarContent: add an event");
                    NewEventDialog newEventDialog = NewEventDialog.show(getParentFragmentManager());
                    if (newEventDialog != null) {
                        newEventDialog.addOnDismissListener(new Reload() {
                            @Override
                            public void site () {
                                updateList();
                            }
                        });
                    }
                }
                return true;
            });
        }
    }

    @Override
    public void createView (@NonNull @NotNull View view,
                            @NonNull @NotNull LayoutInflater inflater) {
        layout = view.findViewById(R.id.fragment_container);
        Log.d(TAG, "createView: semester: " + semester.getId());
    }

    @Override
    public void authStatusChanged () {
        try {
            updateList();
        } catch (Exception ignored) {
        } finally {
            toolbarContent();
        }
    }

    @Override
    public void saveDone () {
        semester = CacheData.getChosenSemester();
        downloadSemester();
    }
}
