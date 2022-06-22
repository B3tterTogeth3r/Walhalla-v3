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
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;

import de.b3ttertogeth3r.walhalla.enums.Collar;
import de.b3ttertogeth3r.walhalla.enums.Punctuality;
import de.b3ttertogeth3r.walhalla.object.Log;
import de.b3ttertogeth3r.walhalla.util.Values;

public class TimeFrat extends LinearLayout {
    private static final String TAG = "TimeFrat";
    private final Context context;
    private Text time, col, pun;

    public TimeFrat (Context context) {
        super(context);
        this.context = context;
        design();
    }

    private void design () {
        LayoutParams layoutParams = new LayoutParams(
                inSP(), inSP()
        );
        setLayoutParams(layoutParams);
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        time = new Text(context);
        col = new Text(context);
        pun = new Text(context);
        time.setPadding(0, 0, 0, 0);
        pun.setPadding(0, 0, 0, 0);
        col.setPadding(0, 0, 0, 0);
        time.setGravity(Gravity.CENTER);
        addView(time);
        time.setVisibility(View.GONE);

        LinearLayout secondRow = new LinearLayout(context);
        LayoutParams srParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        srParams.setMargins(10, 0, 10, 0);
        secondRow.setLayoutParams(srParams);
        secondRow.setOrientation(HORIZONTAL);
        col.setLayoutParams(srParams);
        pun.setLayoutParams(srParams);
        pun.setVisibility(View.GONE);
        col.setVisibility(View.GONE);
        secondRow.addView(pun);
        secondRow.addView(col);
        addView(secondRow);
    }

    private int inSP () {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                (float) 70,
                context.getResources().getDisplayMetrics()
        );
    }

    public TimeFrat (Context context, Timestamp time) {
        super(context);
        this.context = context;
        design();
        setTime(time);
    }

    public TimeFrat setTime (Timestamp timestamp) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH.mm", Values.LOCALE);
        try {
            time.setText(timeFormat.format(timestamp.toDate()));
            time.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            Log.e(TAG, "Formatting the time did not work", e);
            time.setVisibility(View.GONE);
        }
        return this;
    }

    public TimeFrat (Context context, Timestamp timestamp, Collar collar, Punctuality punctuality) {
        super(context);
        this.context = context;
        design();
        setPunctuality(punctuality);
        setTime(timestamp);
        setCollar(collar);
        setGravity(Gravity.CENTER);
    }

    public TimeFrat setPunctuality (@NonNull Punctuality punctuality) {
        pun.setText(punctuality.toString());
        pun.setVisibility(View.VISIBLE);
        return this;
    }

    public TimeFrat setCollar (@NonNull Collar collar) {
        col.setText(collar.toString());
        col.setVisibility(View.VISIBLE);
        return this;
    }
}
