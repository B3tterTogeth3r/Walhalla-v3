package de.b3ttertogeth3r.walhalla.fragments_main.program;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.models.Event;
import de.b3ttertogeth3r.walhalla.utils.Variables;

public class Entry extends ArrayAdapter<Event> {
    private static final String TAG = "Entry";
    private final Context ctx;
    private ArrayList<Event> events = new ArrayList<>();

    public Entry (Context context, ArrayList<Event> eventList) {
        super(context, R.layout.event_item, eventList);
        this.ctx = context;
        this.events = eventList;
    }

    @NonNull
    @Override
    public View getView (int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        Event event = events.get(position);

        RelativeLayout date = view.findViewById(R.id.item_event_date);
        TextView year = view.findViewById(R.id.item_event_year);
        TextView day = view.findViewById(R.id.item_event_day);
        TextView month = view.findViewById(R.id.item_event_month);
        TextView time = view.findViewById(R.id.item_event_time);
        TextView punkt = view.findViewById(R.id.item_event_punkt);
        TextView collar = view.findViewById(R.id.item_event_collar);
        TextView title = view.findViewById(R.id.item_event_title);
        TextView description = view.findViewById(R.id.item_event_description);
        Calendar begin = Calendar.getInstance();
        begin.setTime(event.getBegin().toDate());

        date.setVisibility(View.VISIBLE);
        year.setText(String.valueOf(begin.get(Calendar.YEAR)));
        day.setText(String.valueOf(begin.get(Calendar.DAY_OF_MONTH)));
        month.setText(Variables.MONTHS[begin.get(Calendar.MONTH)]);
        punkt.setText(event.getPunct());
        time.setText(getTime(begin));
        //punkt.setVisibility(View.INVISIBLE);
        collar.setText(event.getCollar());
        title.setText(event.getTitle());
        description.setText(event.getDescription());

        switch (event.getPublicity()){
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

        Animation anim = AnimationUtils.loadAnimation(ctx, R.anim.fade_in);
        try{
            view.setAnimation(anim);
            view.startAnimation(anim);
        } catch (Exception e) {
            Log.d(TAG, "getView: starting animation did not work", e);
        }

        return view;
    }

    @NonNull
    @NotNull
    private String getTime(@NonNull @NotNull Calendar calendar) {
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
        return hour + "." + minute + " " + ctx.getResources().getString(R.string.clock);
    }
}
