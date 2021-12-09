package de.b3ttertogeth3r.walhalla.fragments.program;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstraction.CustomFragment;
import de.b3ttertogeth3r.walhalla.design.MyTitle;
import de.b3ttertogeth3r.walhalla.dialog.ChangeSemesterDialog;
import de.b3ttertogeth3r.walhalla.dialog.DetailsDialog;
import de.b3ttertogeth3r.walhalla.enums.Kind;
import de.b3ttertogeth3r.walhalla.firebase.Firebase;
import de.b3ttertogeth3r.walhalla.firebase.Firebase.Firestore;
import de.b3ttertogeth3r.walhalla.interfaces.SemesterChangeListener;
import de.b3ttertogeth3r.walhalla.models.Event;
import de.b3ttertogeth3r.walhalla.models.Semester;
import de.b3ttertogeth3r.walhalla.utils.CacheData;
import de.b3ttertogeth3r.walhalla.utils.Variables;

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
    private String publicity = "public";
    private Semester semester = CacheData.getChosenSemester();

    @Override
    public void start () {
        // Log.d(TAG, "start: semester: " + semester);
        downloadSemester();
    }

    /**
     * load on start and after the user chose another semester via {@link de.b3ttertogeth3r.walhalla.dialog.ChangeSemesterDialog ChangeSemesterDialog}
     */
    private void downloadSemester () {
        registration.add(0, Firestore.loadSemesterEvents(String.valueOf(semester.getId()))
                .addSnapshotListener((value, error) -> {
                    if (error != null || value == null || value.isEmpty()) {
                        return;
                    }
                    // Log.d(TAG, "start: " + value.size());
                    Fragment.documentSnapshots = value;
                    try {
                        updateList();
                    } catch (Exception e) {
                        Firebase.Crashlytics.log(TAG, e);
                    }
                }));
    }

    private void updateList () {
        if (documentSnapshots.isEmpty()) {
            return;
        }
        eventList.clear();
        for (DocumentSnapshot doc : documentSnapshots) {
            Event e = doc.toObject(Event.class);
            if (e != null && e.exists()) {
                e.setId(doc.getId());
                //TODO admin user
                /*
                if(user == admin || user == Charge.*) {
                    eventList.add(e);
                } else{
                 */
                if (publicity.equals(e.getPublicity())) {
                    eventList.add(e);
                }
                //}
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
            //TODO show text with message, that no data could be found or loaded.
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
                    try {
                        Event event = (Event) month.get(keys.get(i));
                        View view = fillEventItem(event,
                                getLayoutInflater().inflate(R.layout.event_item, null));
                        view.setOnClickListener(v -> Details.display(getParentFragmentManager(),
                                event));
                        //Maybe an onLongClick to edit displayed details only if user is Charge.*
                        // or super-admin
                        view.setOnLongClickListener(v -> {
                            DetailsDialog dialog = new DetailsDialog(requireContext(), event);
                            dialog.show();
                            return false;
                        });
                        view.setBackgroundResource(R.drawable.border_bottom_black);
                        layout.addView(view);
                    } catch (ClassCastException cce) {
                        Log.e(TAG, "updateList: ClassCastException", cce);
                    } catch (Exception e) {
                        if (e.getClass() != ClassCastException.class) {
                            Log.d(TAG, "formatResult: design: first try: exception: ", e);
                        }
                        try {
                            Map<String, Event> eventMap =
                                    (Map<String, Event>) month.get(keys.get(i));
                            //Add layout around for the border
                            LinearLayout multiEventAtOneDay = new LinearLayout(getContext());
                            multiEventAtOneDay.setOrientation(LinearLayout.VERTICAL);
                            for (String string : eventMap.keySet()) {
                                // Format the events accordingly
                                Event sameDay = eventMap.get(string);
                                View view = fillEventItem(sameDay,
                                        getLayoutInflater().inflate(R.layout.event_item, null));
                                view.setOnClickListener(v -> Details.display(getParentFragmentManager(), sameDay));
                                view.setBackgroundResource(R.drawable.border_bottom_gray);
                                multiEventAtOneDay.addView(view);
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

    @NonNull
    @org.jetbrains.annotations.Contract("_, _ -> param2")
    private View fillEventItem (Event event, @NonNull View view) {
        RelativeLayout date = view.findViewById(R.id.item_event_date);
        TextView year = view.findViewById(R.id.item_event_year);
        TextView day = view.findViewById(R.id.item_event_day);
        TextView month = view.findViewById(R.id.item_event_month);
        TextView time = view.findViewById(R.id.item_event_time);
        TextView punkt = view.findViewById(R.id.item_event_punkt);
        TextView collar = view.findViewById(R.id.item_event_collar);
        TextView title = view.findViewById(R.id.item_event_title);
        TextView description = view.findViewById(R.id.item_event_description);

        //Switch between the kinds of events
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(event.getBegin().toDate());
            if (event.getPunct().contains("after") || event.getPunct().contains("ansch")) {
                date.setVisibility(View.INVISIBLE);
                time.setText(R.string.program_later);
                collar.setText(event.getCollar());
                punkt.setVisibility(View.GONE);
                title.setText(event.getTitle());
                description.setText(event.getDescription());
            }
            //TODO Make that also possible for events at the same day depending on .getStart()
            else if (event.getPunct().contains("later")) {
                date.setVisibility(View.INVISIBLE);
                time.setText(getTime(calendar));
                punkt.setVisibility(View.GONE);
                collar.setVisibility(View.GONE);
                title.setText(event.getTitle());
                description.setText(event.getDescription());
            }
            //TODO Make that also possible for events at the same day depending on .getStart()
            else if (event.getPunct().contains("total")) {
                //what to show?
                date.setVisibility(View.VISIBLE);
                year.setText(String.valueOf(calendar.get(Calendar.YEAR)));
                day.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
                month.setText(Variables.MONTHS[calendar.get(Calendar.MONTH)]);
                time.setText(getTime(calendar));
                collar.setVisibility(View.GONE);
                punkt.setVisibility(View.GONE);
                title.setText(event.getTitle());
                description.setText(event.getDescription());
            } else if (event.getPunct().contains("info")) {
                //TODO what to show?
                date.setVisibility(View.GONE);
                time.setVisibility(View.GONE);
                collar.setVisibility(View.GONE);
                punkt.setVisibility(View.GONE);
                title.setVisibility(View.GONE);
                String helper = event.getTitle() + " " + event.getDescription();
                description.setText(helper);
            } else {
                date.setVisibility(View.VISIBLE);
                year.setText(String.valueOf(calendar.get(Calendar.YEAR)));
                day.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
                month.setText(Variables.MONTHS[calendar.get(Calendar.MONTH)]);
                punkt.setText(event.getPunct());
                time.setText(getTime(calendar));
                collar.setText(event.getCollar());
                title.setText(event.getTitle());
                description.setText(event.getDescription());
            }

            switch (event.getPublicity()) {
                case "public":
                    view.setAlpha(1f);
                    break;
                case "internal":
                    view.setAlpha(0.75f);
                    break;
                case "draft":
                    view.setAlpha(0.5f);
                    break;
                default:
                    break;
            }
        } catch (Exception format) {
            Log.e(TAG, "Something went wrong while formatting data", format);
        }

        Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        try {
            view.startAnimation(anim);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    @NonNull
    @NotNull
    private String getTime (@NonNull @NotNull Calendar calendar) {
        float hourFL = calendar.get(Calendar.HOUR_OF_DAY);
        float minuteFL = calendar.get(Calendar.MINUTE);
        String minute, hour;
        if (hourFL < 10) {
            hour = "0" + String.format(Variables.LOCALE, "%.0f", hourFL);
        } else {
            hour = String.format(Variables.LOCALE, "%.0f", hourFL);
        }
        if (minuteFL < 10) {
            minute = "0" + String.format(Variables.LOCALE, "%.0f", minuteFL);
        } else {
            minute = String.format(Variables.LOCALE, "%.0f", minuteFL);
        }
        return hour + "." + minute + " " + getResources().getString(R.string.clock);
    }

    @Override
    public void analyticsProperties () {

    }

    @Override
    public void stop () {
        try {
            customTitle.setOnClickListener(null);
        } catch (Exception e) {
            Firebase.Crashlytics.log(TAG, "Trying to disable the OnClickListener for Custom " +
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
        ((TextView) customTitle.findViewById(R.id.action_bar_title)).setText(R.string.menu_program);
        customTitle.setOnClickListener(v -> {
            //TODO add semester change dialog
            Log.d(TAG, "toolbarContent: custom title clicked");
            ChangeSemesterDialog.display(getParentFragmentManager(), Kind.CHANGE, this, semester);
        });
    }

    @Override
    public void createView (@NonNull @NotNull View view,
                            @NonNull @NotNull LayoutInflater inflater) {
        layout = view.findViewById(R.id.fragment_container);
        Log.d(TAG, "createView: semester: " + semester.getId());
    }

    @Override
    public void saveDone () {
        semester = CacheData.getChosenSemester();
        downloadSemester();
    }
}
