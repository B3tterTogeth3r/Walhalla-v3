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

package de.b3ttertogeth3r.walhalla.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.design.Image;
import de.b3ttertogeth3r.walhalla.design.LinearLayout;
import de.b3ttertogeth3r.walhalla.enums.TrafficLightColor;

public class TrafficLight extends LinearLayout {
    private static final String TAG = "TrafficLight";
    private final TrafficLightColor tlc;
    Image red, yellow, green;

    public TrafficLight(Context context, @Nullable AttributeSet attrs, TrafficLightColor tlc) {
        super(context, attrs);
        this.tlc = tlc;
        design(context);
        setLight();
    }

    public TrafficLight(Context context, @Nullable AttributeSet attrs, int defStyleAttr, TrafficLightColor tlc) {
        super(context, attrs, defStyleAttr);
        this.tlc = tlc;
        design(context);
        setLight();
    }

    public TrafficLight(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, TrafficLightColor tlc) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.tlc = tlc;
        design(context);
        setLight();
    }

    void design (Context context) {
        setOrientation(VERTICAL);
        red = new Image(context, R.drawable.ic_circle);
        yellow = new Image(context, R.drawable.ic_circle);
        green = new Image(context, R.drawable.ic_circle);

        red.setColorFilter(ContextCompat.getColor(context, R.color.red));
        yellow.setColorFilter(ContextCompat.getColor(context, R.color.yellow));
        green.setColorFilter(ContextCompat.getColor(context, R.color.green));

        red.setVisibility(View.INVISIBLE);
        yellow.setVisibility(View.INVISIBLE);
        green.setVisibility(View.INVISIBLE);

        addView(red);
        addView(yellow);
        addView(green);
    }

    public TrafficLight (@NonNull Context context, TrafficLightColor tlc) {
        super(context);
        this.tlc = tlc;
        design(context);
        setLight();
    }

    public void setLight () {
        switch (tlc) {
            case GREEN:
                red.setVisibility(View.INVISIBLE);
                yellow.setVisibility(View.INVISIBLE);
                green.setVisibility(View.VISIBLE);
                break;
            case YELLOW:
                red.setVisibility(View.INVISIBLE);
                yellow.setVisibility(View.VISIBLE);
                green.setVisibility(View.INVISIBLE);
                break;
            case RED:
            default:
                green.setVisibility(View.INVISIBLE);
                yellow.setVisibility(View.INVISIBLE);
                red.setVisibility(View.VISIBLE);
        }
    }
}
