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
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstract_generic.Touch;

@SuppressWarnings("UnusedReturnValue")
public class Button extends RelativeLayout {
    private static LayoutParams params;
    private Subtitle1 text;
    private ImageView icon;

    public Button(Context context) {
        super(context);
        design(context);
    }

    public void design(@NonNull Context context) {
        int padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                16f,
                context.getResources().getDisplayMetrics()
        );
        icon = new ImageView(context);
        text = new Subtitle1(context);
        RelativeLayout.LayoutParams textParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        textParams.addRule(CENTER_IN_PARENT);
        setBackground(ContextCompat.getDrawable(context, R.drawable.background_button));
        params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        padding = (padding / 2);
        params.setMargins(padding, padding, padding, padding);
        setLayoutParams(params);
        text.setGravity(Gravity.CENTER);
        text.setId(R.id.text);
        LayoutParams imageParams = new LayoutParams((padding / 2), (padding / 2));
        imageParams.addRule(LEFT_OF, text.getId());
        icon.setVisibility(View.GONE);
        addView(text, textParams);
        addView(icon, imageParams);
    }

    public Button(Context context, AttributeSet attrs) {
        super(context, attrs);
        design(context);
    }

    public Button(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        design(context);
    }

    public Button(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        design(context);
    }
    /*
    public Button (Context context, String text, String link) {
        super(context);
        design(context);
        setText(text);
        setLink(link);
    }

    private void setLink (String link) {

    }*/

    public Button(Context context, String text) {
        super(context);
        design(context);
        setText(text);
    }

    public Button setText(CharSequence text) {
        this.text.setText(text);
        this.text.setTextColor(ContextCompat.getColor(getContext(), R.color.background));
        return this;
    }

    public static LayoutParams getParams() {
        return params;
    }

    public Button setIconDrawable(Drawable drawable) {
        icon.setImageDrawable(drawable);
        icon.setVisibility(View.VISIBLE);
        return this;
    }

    public Button setText(int resid) {
        this.text.setText(resid);
        this.text.setTextColor(ContextCompat.getColor(getContext(), R.color.background));
        return this;
    }

    @Override
    public void setEnabled(boolean enabled) {
        try {
            if (enabled) {
                setAlpha(1f);
                setBackgroundTintList(null);
                setBackgroundTintMode(null);
                setBackground(ContextCompat.getDrawable(getContext(),
                        R.drawable.background_button));
            } else {
                setOnClickListener(null);
                setAlpha(0.5f);
                setBackgroundTintList(ContextCompat.getColorStateList(getContext(),
                        R.color.darkGray));
                setBackgroundTintMode(PorterDuff.Mode.DARKEN);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Button addTouchListener(Touch touch) {
        setOnTouchListener(touch);
        return this;
    }
}
