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

package de.b3ttertogeth3r.walhalla.design;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstract_generic.Touch;
import de.b3ttertogeth3r.walhalla.enums.Collar;
import de.b3ttertogeth3r.walhalla.enums.Punctuality;
import de.b3ttertogeth3r.walhalla.object.Chore;
import de.b3ttertogeth3r.walhalla.util.Values;

public class Event {
    private static final String TAG = "DEvent";
    private Timestamp timestamp;
    private TextView year, day, month, time, col, pun, description;
    private Title title;
    private RelativeLayout view;
    private static Event dEvent;

    private Event() {
    }

    public Event(@NonNull Context context, @Nullable ViewGroup root) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = (RelativeLayout) inflater.inflate(R.layout.event_item, root);
        year = view.findViewById(R.id.item_event_year);
        month = view.findViewById(R.id.item_event_month);
        day = view.findViewById(R.id.item_event_day);
        time = view.findViewById(R.id.item_event_time);
        col = view.findViewById(R.id.item_event_collar);
        pun = view.findViewById(R.id.item_event_pun);
        LinearLayout titleLayout = view.findViewById(R.id.item_event_title_layout);
        description = new TextView(context);
        title = new Title(context);
        titleLayout.addView(title);
        titleLayout.addView(description);
    }

    public RelativeLayout show() {
        return view;
    }

    @NonNull
    public static Event create(@NonNull FragmentActivity context, @Nullable ViewGroup root, @NonNull de.b3ttertogeth3r.walhalla.object.Event event) {
        dEvent = new Event(context, root);
        dEvent.setTime(event.getTime());
        dEvent.setCol(event.getCollar());
        dEvent.setPun(event.getPunctuality());
        dEvent.setDescription(event.getDescription());
        dEvent.setTitle(event.getTitle());
        return dEvent;
    }

    @NonNull
    public static Event create(@NonNull FragmentActivity context, @Nullable ViewGroup root, @NonNull Chore chore, boolean isEvent) {
        dEvent = new Event(context, root);
        dEvent.setTime(chore.getTime());
        dEvent.setDescription(chore.getChore().toString());
        if (isEvent) {
            dEvent.setTitle(chore.getPerson());
        } else {
            dEvent.setTitle(chore.getEvent());
        }
        if (chore.isDone()) {
            dEvent.view.setAlpha(0.5f);
        }
        dEvent.view.findViewById(R.id.item_event_time_layout).setVisibility(View.GONE);
        return dEvent;
    }

    public String getYear() {
        return year.getText().toString();
    }

    public Timestamp getTime() {
        return timestamp;
    }

    public void setTime(@NonNull Timestamp timestamp) {
        this.timestamp = timestamp;
        java.util.Date d = timestamp.toDate();
        SimpleDateFormat day = new SimpleDateFormat("dd", Values.LOCALE);
        this.day.setText(day.format(d));
        day = new SimpleDateFormat("MM", Values.LOCALE);
        int i = Integer.parseInt(day.format(d));
        this.month.setText(Values.MONTHS[i]);
        day = new SimpleDateFormat("yyyy", Values.LOCALE);
        this.year.setText(day.format(d));
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Values.LOCALE);
        time.setText(timeFormat.format(timestamp.toDate()));
        time.setVisibility(View.VISIBLE);
    }

    public String getCol() {
        return col.getText().toString();
    }

    public void setCol(@NonNull Collar col) {
        this.col.setText(col.toString());
    }

    public String getPun() {
        return pun.getText().toString();
    }

    public void setPun(@NonNull Punctuality pun) {
        this.pun.setText(pun.toString());
    }

    public String getDescription() {
        return description.getText().toString();
    }

    public void setDescription(String description) {
        this.description.setText(description);
    }

    public String getTitle() {
        return title.getText().toString();
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }


    @SuppressWarnings("rawtypes")
    @SuppressLint("ClickableViewAccessibility")
    public Event addTouchListener(Touch touch) {
        if (dEvent != null && view != null) {
            view.setOnTouchListener(touch);
            return dEvent;
        }
        return null;
    }
}
