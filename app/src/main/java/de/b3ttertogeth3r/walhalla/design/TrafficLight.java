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

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.enums.TrafficLightColor;

public class TrafficLight extends LinearLayout {
    private static final String TAG = "TrafficLight";
    private final Context context;
    private final TrafficLightColor tlc;
    private Image light;

    public TrafficLight(Context context, @Nullable AttributeSet attrs, TrafficLightColor tlc) {
        super(context, attrs);
        this.context = context;
        this.tlc = tlc;
        design(context);
        setLight();
    }

    void design(Context context) {
        light = new Image(context, R.drawable.ic_circle);

        light.setVisibility(View.INVISIBLE);

        addView(light);
    }

    public void setLight() {
        light.setVisibility(View.VISIBLE);
        switch (tlc) {
            case GREEN:
                light.setColorFilter(ContextCompat.getColor(context, R.color.green));
                break;
            case YELLOW:
                light.setColorFilter(ContextCompat.getColor(context, R.color.yellow));
                break;
            case BLACK:
                light.setColorFilter(ContextCompat.getColor(context, R.color.black));
                break;
            case RED:
            default:
                light.setColorFilter(ContextCompat.getColor(context, R.color.red));
        }
        // Log.i(TAG, "setLight: " + tlc.name());
    }

    public TrafficLight(Context context, @Nullable AttributeSet attrs, int defStyleAttr, TrafficLightColor tlc) {
        super(context, attrs, defStyleAttr);
        this.tlc = tlc;
        this.context = context;
        design(context);
        setLight();
    }

    public TrafficLight(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, TrafficLightColor tlc) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.tlc = tlc;
        this.context = context;
        design(context);
        setLight();
    }

    public TrafficLight (@NonNull Context context, TrafficLightColor tlc) {
        super(context);
        this.tlc = tlc;
        this.context = context;
        design(context);
        setLight();
    }
}
