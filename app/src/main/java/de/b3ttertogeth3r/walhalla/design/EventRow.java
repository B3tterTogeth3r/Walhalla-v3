package de.b3ttertogeth3r.walhalla.design;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.annotations.NotNull;

import java.util.Calendar;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.enums.Rank;
import de.b3ttertogeth3r.walhalla.models.Event;
import de.b3ttertogeth3r.walhalla.utils.CacheData;
import de.b3ttertogeth3r.walhalla.utils.Variables;

public class EventRow extends RelativeLayout {
    private static final String TAG = "EventRow";
    private final Context ctx;
    private final Event event;
    private final RelativeLayout layoutDate;
    private final LinearLayout layoutTime, layoutTitle, collarLayout;
    private final TextView year, day, month;
    private final MyTextView time, collar, punctuality;
    private final MyTitle title;
    private final MySubtitle description;

    // TODO: 20.01.22 the design is really faulty...
    public EventRow (Context ctx, Event event) {
        super(ctx);
        this.ctx = ctx;
        this.event = event;
        layoutDate = new RelativeLayout(ctx);
        layoutTime = new LinearLayout(ctx);
        layoutTitle = new LinearLayout(ctx);
        collarLayout = new LinearLayout(ctx);
        year = new TextView(ctx);
        day = new TextView(ctx);
        month = new TextView(ctx);
        time = new MyTextView(ctx);
        collar = new MyTextView(ctx);
        punctuality = new MyTextView(ctx);
        title = new MyTitle(ctx);
        description = new MySubtitle(ctx);
        design();
        addToLayout();
        setValues();
    }

    private void design () {
        setPadding(inSP(4), inSP(4), inSP(4), inSP(4));
        setBackgroundResource(R.drawable.border_bottom_black);

        //region setting IDs
        layoutDate.setId(R.id.layoutDate);
        layoutTime.setId(R.id.layoutTime);
        layoutTitle.setId(R.id.layoutTitle);
        year.setId(R.id.year);
        day.setId(R.id.day);
        month.setId(R.id.month);
        time.setId(R.id.time);
        collar.setId(R.id.collar);
        punctuality.setId(R.id.punctuality);
        title.setId(R.id.title);
        description.setId(R.id.description);
        //endregion

        //region date
        LayoutParams dateParams = new LayoutParams(inSP(80), inSP(80));
        dateParams.addRule(CENTER_VERTICAL);
        layoutDate.setLayoutParams(dateParams);
        layoutDate.setBackgroundColor(ContextCompat.getColor(ctx, R.color.colorPrimary));

        LayoutParams yearParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        yearParams.setMargins(0, inSP(4), 0, inSP(4));
        yearParams.addRule(ABOVE, day.getId());
        year.setLayoutParams(yearParams);
        year.setGravity(Gravity.CENTER);
        year.setTextColor(ContextCompat.getColor(ctx, R.color.white));
        year.setBackgroundColor(ContextCompat.getColor(ctx, R.color.black));

        LayoutParams dayParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dayParams.addRule(CENTER_IN_PARENT);
        day.setLayoutParams(dayParams);
        day.setTextSize(inSP(8));
        day.setGravity(Gravity.CENTER);
        day.setTextAlignment(TEXT_ALIGNMENT_CENTER);

        LayoutParams monthParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        monthParams.addRule(CENTER_HORIZONTAL);
        monthParams.addRule(BELOW, day.getId());
        monthParams.setMargins(0, inSP(2), 0, 0);
        month.setLayoutParams(monthParams);
        month.setGravity(Gravity.CENTER);
        //endregion

        //region time
        LayoutParams layoutTimeParams = new LayoutParams(
                inSP(80),
                LayoutParams.WRAP_CONTENT);
        layoutTimeParams.setMargins(inSP(8), inSP(8), inSP(8), inSP(8));
        layoutTimeParams.addRule(END_OF, layoutDate.getId());
        layoutTimeParams.addRule(CENTER_VERTICAL);
        layoutTime.setLayoutParams(layoutTimeParams);
        layoutTime.setOrientation(LinearLayout.VERTICAL);
        time.setPadding(0,0,0,0);
        punctuality.setPadding(0,0,0,0);
        collar.setPadding(0,0,0,0);

        LayoutParams collarParams = new LayoutParams(inSP(70),
                ViewGroup.LayoutParams.WRAP_CONTENT);
        collarParams.addRule(CENTER_HORIZONTAL);
        collarLayout.setLayoutParams(collarParams);
        collarLayout.setOrientation(LinearLayout.HORIZONTAL);
        //endregion

        //region title and description
        LayoutParams layoutTitleParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutTitleParams.addRule(END_OF, layoutTime.getId());
        layoutTitleParams.setMargins(inSP(4), inSP(4), inSP(4), inSP(4));
        layoutTitleParams.addRule(CENTER_HORIZONTAL);
        layoutTitle.setLayoutParams(layoutTitleParams);
        layoutTitle.setOrientation(LinearLayout.VERTICAL);
        title.setPadding(0,0,0,0);
        description.setPadding(0,0,0,0);
        //endregion
    }

    private void addToLayout () {
        layoutDate.addView(year);
        layoutDate.addView(day);
        layoutDate.addView(month);
        layoutTime.addView(time);
        collarLayout.setOrientation(LinearLayout.HORIZONTAL);
        collarLayout.setGravity(CENTER_HORIZONTAL);
        collarLayout.addView(collar);
        collarLayout.addView(punctuality);
        layoutTime.addView(collarLayout);
        layoutTitle.addView(title);
        layoutTitle.addView(description);

        this.addView(layoutDate);
        this.addView(layoutTime);
        this.addView(layoutTitle);
    }

    private void setValues () {
        //Switch between the kinds of events
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(event.getBegin().toDate());
            if (event.getPunctuality().contains("after") || event.getPunctuality().contains(
                    "ansch")) {
                layoutDate.setVisibility(View.INVISIBLE);
                time.setText(R.string.program_later);
                collar.setText(event.getCollar());
                punctuality.setVisibility(View.GONE);
                title.setText(event.getTitle());
                description.setText(event.getDescription());
            }
            //TODO Make that also possible for events at the same day depending on .getStart()
            else if (event.getPunctuality().contains("later")) {
                layoutDate.setVisibility(View.INVISIBLE);
                time.setText(getTime(calendar));
                punctuality.setVisibility(View.GONE);
                collar.setVisibility(View.GONE);
                title.setText(event.getTitle());
                description.setText(event.getDescription());
            }
            //TODO Make that also possible for events at the same day depending on .getStart()
            else if (event.getPunctuality().contains("total")) {
                //what to show?
                layoutDate.setVisibility(View.VISIBLE);
                year.setText(String.valueOf(calendar.get(Calendar.YEAR)));
                day.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
                month.setText(Variables.MONTHS[calendar.get(Calendar.MONTH)]);
                time.setText(getTime(calendar));
                collar.setVisibility(View.GONE);
                punctuality.setVisibility(View.GONE);
                title.setText(event.getTitle());
                description.setText(event.getDescription());
            } else if (event.getPunctuality().contains("info")) {
                //TODO what to show?
                layoutDate.setVisibility(View.GONE);
                time.setVisibility(View.GONE);
                collar.setVisibility(View.GONE);
                punctuality.setVisibility(View.GONE);
                title.setVisibility(View.GONE);
                String helper = event.getTitle() + " " + event.getDescription();
                description.setText(helper);
            } else {
                layoutDate.setVisibility(View.VISIBLE);
                year.setText(String.valueOf(calendar.get(Calendar.YEAR)));
                day.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
                month.setText(Variables.MONTHS[calendar.get(Calendar.MONTH)]);
                punctuality.setVisibility(View.VISIBLE);
                punctuality.setText(event.getPunctuality());
                time.setText(getTime(calendar));
                collar.setText(event.getCollar());
                title.setText(event.getTitle());
                description.setText(event.getDescription());
            }

            String rank = CacheData.getUser().getRank();
            for (String s : event.getVisibleTo()) {
                if (rank.equals(s)) {
                    //TODO display event
                    switch (Rank.find(rank).getGroup()) {
                        case ACTIVE:
                            //TODO change backgroundColor of date
                        case PHILISTINES:
                            //TODO change backgroundColor of date
                            setAlpha(.75f);
                            break;
                        case DRAFT:
                            setAlpha(0.5f);
                            break;
                        case PUBLIC:
                        default:
                            setAlpha(1f);
                            break;
                    }
                    break;
                }
            }
        } catch (Exception format) {
            Log.e(TAG, "Something went wrong while formatting data", format);
        }

        Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        try {
            startAnimation(anim);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int inSP (float value) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                value,
                ctx.getResources().getDisplayMetrics()
        );
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

    public void isMulti () {
        setBackgroundResource(R.drawable.border_bottom_gray);
        layoutDate.setVisibility(View.INVISIBLE);
    }
}
