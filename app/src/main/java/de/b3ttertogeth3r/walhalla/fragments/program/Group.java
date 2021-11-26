package de.b3ttertogeth3r.walhalla.fragments.program;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.b3ttertogeth3r.walhalla.App;
import de.b3ttertogeth3r.walhalla.models.Event;
import de.b3ttertogeth3r.walhalla.models.Semester;
import de.b3ttertogeth3r.walhalla.utils.CacheData;
import de.b3ttertogeth3r.walhalla.utils.Variables;

public class Group {
    private final static String TAG = "groupByMonths";

    @NonNull
    @NotNull
    public static ArrayList<Map<String, Object>> byMonths(@NonNull @NotNull ArrayList<Event> eventList) {
        ArrayList<Map<String, Object>> groupedList = new ArrayList<>();
        // Log.e(TAG, "byMonths: eventList.size = " + eventList.size());
        if(eventList.size() == 1) {
            Event e = eventList.get(0);
            Map<String, Object> empty = new HashMap<>();
            empty.put("title", e.getTitle());
            groupedList.add(empty);
            return groupedList;
        }
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        Semester semester = CacheData.getChosenSemester();
        start.setTime(semester.getBegin().toDate());
        end.setTime(semester.getEnd().toDate());
        //Find months from start to end including this two
        while (start.before(end)) {
            int monthNo = start.get(Calendar.MONTH);
            ArrayList<Event> events = new ArrayList<>();
            for (Event e : eventList) {
                if (e.getMonth() == monthNo) {
                    events.add(e);
                    try {
                        events.sort((o1, o2) -> Integer.compare(o1.getBegin().compareTo(o2.getBegin()), o2.getBegin().compareTo(o1.getBegin())));
                    } catch (Exception ignored) {
                    }
                }
            }
            //Group events
            Map<String, Object> month = new HashMap<>(byDate(events));
            month.put("title", Variables.MONTHS[start.get(Calendar.MONTH)]);
            groupedList.add(month);
            start.add(Calendar.MONTH, 1);
        }
        Log.d(TAG, "byMonths: groupedList = " + groupedList.size());
        return groupedList;
    }

    @NonNull
    @NotNull
    private static Map<String, Object> byDate(@NonNull @NotNull ArrayList<Event> events) {
        Map<String, Object> result = new HashMap<>();
        int size = events.size();
        for (int i = 0; i < size; i++) {
            int current = i + 1;
            if (current < events.size() && events.get(i).getDayOfYearStart() == events.get(current).getDayOfYearStart()) {
                //TODO Add multi day events as a possible "head event"
                Map<String, Object> more = new HashMap<>();
                more.put(String.valueOf(i), events.get(i));
                while (current < events.size() && events.get(i).getDayOfYearStart() == events.get(current).getDayOfYearStart()) {
                    more.put(String.valueOf(current), events.get(current));
                    current++;
                }
                result.put(String.valueOf(i), more);
                i = current - 1;
            } else {
                result.put(String.valueOf(i), events.get(i));
            }
        }
        return result;
    }
}
