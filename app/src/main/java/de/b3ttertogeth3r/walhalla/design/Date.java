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

package de.b3ttertogeth3r.walhalla.design;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.object.Log;
import de.b3ttertogeth3r.walhalla.util.Values;

public class Date extends RelativeLayout {
    private static final String TAG = "Date";
    private final Context context;
    private Text year, month;
    private Text day;

    public Date(Context context) {
        super(context);
        this.context = context;
        design();
    }

    public Date(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        design();
    }

    public Date(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        design();
    }

    public Date(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        design();
    }

    private void design() {
        day.setId(R.id.day);
        year.setId(R.id.year);
        month.setId(R.id.month);
        LayoutParams params = new LayoutParams(inSP(80), inSP(80));
        params.addRule(CENTER_VERTICAL);
        setLayoutParams(params);

        LayoutParams yearParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        yearParams.setMargins(0, inSP(4), 0, inSP(4));
        yearParams.addRule(ABOVE, day.getId());
        year.setPadding(0,0,0,0);
        year.setLayoutParams(yearParams);
        year.setGravity(Gravity.CENTER);

        year.setTextColor(ContextCompat.getColor(context, R.color.white));
        year.setBackgroundColor(ContextCompat.getColor(context, R.color.black));

        LayoutParams dayParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dayParams.addRule(CENTER_IN_PARENT);
        dayParams.setMargins(0, 0, 0, 0);
        day.setLayoutParams(dayParams);
        day.setTextSize(inSP(7));
        day.setPadding(0, 0, 0, 0);
        day.setGravity(Gravity.CENTER);
        day.setTextAlignment(TEXT_ALIGNMENT_CENTER);

        LayoutParams monthParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                inSP(20));
        monthParams.addRule(CENTER_HORIZONTAL);
        month.setPadding(0,0,0,0);
        monthParams.addRule(BELOW, day.getId());
        monthParams.setMargins(0, inSP(2), 0, inSP(2));
        //monthParams.setMargins(0,0,0,0);
        month.setLayoutParams(monthParams);
        month.setGravity(Gravity.CENTER);
        //month.setTextSize(inSP(2));
        //month.setBackground(ContextCompat.getDrawable(context, R.drawable.border_round));
        month.setTextColor(ContextCompat.getColor(context, R.color.black));

        setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
    }

    private int inSP(float value) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                value,
                context.getResources().getDisplayMetrics()
        );
    }

    public Date(Context context, Timestamp timestamp) {
        super(context);
        this.context = context;
        addFields();
        setTime(timestamp);
        design();
        setVisibility(View.VISIBLE);
    }

    private void addFields() {
        year = new Text(context);
        day = new Text(context);
        month = new Text(context);

        addView(year);
        addView(day);
        addView(month);

    }

    public Date setTime(@NonNull Timestamp timestamp) {
        try {
            java.util.Date d = timestamp.toDate();
            SimpleDateFormat day = new SimpleDateFormat("dd", Values.LOCALE);
            this.day.setText(day.format(d));
            day = new SimpleDateFormat("MM", Values.LOCALE);
            int i = Integer.parseInt(day.format(d));
            this.month.setText(Values.MONTHS[i]);
            day = new SimpleDateFormat("yyyy", Values.LOCALE);
            this.year.setText(day.format(d));
        } catch (Exception e) {
            Log.e(TAG, "Displaying the date didn't work properly. ", e);
        }
        return this;
    }
}
