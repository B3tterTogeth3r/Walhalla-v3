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
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstract_generic.Touch;
import de.b3ttertogeth3r.walhalla.enums.TrafficLightColor;
import de.b3ttertogeth3r.walhalla.util.TrafficLight;

public class ProfileRow extends RelativeLayout {
    private static final String TAG = "ProfileRow";
    private final Text title, content;
    private final ImageView arrow;
    private TrafficLight trafficLight;

    public ProfileRow(Context context, String full_name, String nickname, TrafficLightColor trafficLightColor) {
        super(context);
        this.title = new Text(context, full_name);
        this.content = new Text(context, nickname);
        trafficLight = new TrafficLight(context, trafficLightColor);
        this.arrow = new ImageView(context);
        designAll();
        addView(title);
        addView(content);
        addView(trafficLight);
        addView(arrow);
    }

    public ProfileRow(Context context, boolean isEdit) {
        super(context);
        this.title = new Text(context);
        this.content = new Text(context);
        this.arrow = new ImageView(context);
        design();
        addView(title);
        addView(content);
        if (isEdit) {
            addView(arrow);
        }
    }

    private void designAll() {
        int padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                8f,
                getContext().getResources().getDisplayMetrics()
        );
        LayoutParams lightColors = new LayoutParams(
                (padding * 3),
                (padding * 3)
        );
        lightColors.addRule(ALIGN_PARENT_LEFT);
        lightColors.addRule(CENTER_VERTICAL);
        trafficLight.setLayoutParams(lightColors);
        trafficLight.setId(R.id.x_left);
        trafficLight.setGravity(Gravity.CENTER_VERTICAL);

        LayoutParams titleParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        titleParams.addRule(ALIGN_PARENT_LEFT);
        titleParams.addRule(RIGHT_OF, trafficLight.getId());
        titleParams.addRule(CENTER_VERTICAL);
        titleParams.setMargins((padding * 2), 0, 0, 0);
        this.title.setLayoutParams(titleParams);

        LayoutParams arrowParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        arrowParams.addRule(ALIGN_PARENT_RIGHT);
        arrowParams.addRule(CENTER_VERTICAL);
        this.arrow.setLayoutParams(arrowParams);
        this.arrow.setId(R.id.resend_barrier);
        this.arrow.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_arrow_right));

        LayoutParams contentParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        contentParams.addRule(LEFT_OF, arrow.getId());
        contentParams.addRule(ALIGN_PARENT_RIGHT);
        contentParams.addRule(Gravity.CENTER);
        contentParams.setMargins(0, 0, (2 * padding), 0);
        this.content.setLayoutParams(contentParams);

        this.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.border_bottom_black));
    }

    private void design() {
        int padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                8f,
                getContext().getResources().getDisplayMetrics()
        );
        LayoutParams titleParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        titleParams.addRule(ALIGN_PARENT_LEFT);
        titleParams.addRule(CENTER_VERTICAL);
        this.title.setLayoutParams(titleParams);

        LayoutParams arrowParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        arrowParams.addRule(ALIGN_PARENT_RIGHT);
        arrowParams.addRule(CENTER_VERTICAL);
        this.arrow.setLayoutParams(arrowParams);
        this.arrow.setId(R.id.resend_barrier);
        this.arrow.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_arrow_right));

        LayoutParams contentParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        contentParams.addRule(LEFT_OF, arrow.getId());
        contentParams.addRule(ALIGN_PARENT_RIGHT);
        contentParams.addRule(Gravity.CENTER);
        contentParams.setMargins(0, 0, (2 * padding), 0);
        this.content.setLayoutParams(contentParams);

        this.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.border_bottom_black));
    }

    public ProfileRow(Context context) {
        super(context);
        this.title = new Text(context);
        this.content = new Text(context);
        this.arrow = new ImageView(context);
        design();
        addView(title);
        addView(content);
        addView(arrow);
    }

    public ProfileRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.title = new Text(context);
        this.content = new Text(context);
        this.arrow = new ImageView(context);
        design();
        addView(title);
        addView(content);
        addView(arrow);
    }

    public ProfileRow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.title = new Text(context);
        this.content = new Text(context);
        this.arrow = new ImageView(context);
        design();
        addView(title);
        addView(content);
        addView(arrow);
    }

    public ProfileRow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.title = new Text(context);
        this.content = new Text(context);
        this.arrow = new ImageView(context);
        design();
        addView(title);
        addView(content);
        addView(arrow);
    }

    public ProfileRow setTitle(CharSequence title) {
        this.title.setText(title);
        return this;
    }

    public ProfileRow setTitle(int resId) {
        this.title.setText(resId);
        return this;
    }

    public CharSequence getContent() {
        return this.content.getText();
    }

    public ProfileRow setContent(CharSequence title) {
        this.content.setText(title);
        return this;
    }

    public ProfileRow setContent(int resId) {
        this.content.setText(resId);
        return this;
    }

    public ProfileRow addTouchListener(Touch touch) {
        if (getRootView() != null) {
            setOnTouchListener(touch);
        }
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        try {
            return this.title.getText().toString();
        } catch (Exception e) {
            return "ProfileRow has no title";
        }
    }
}
