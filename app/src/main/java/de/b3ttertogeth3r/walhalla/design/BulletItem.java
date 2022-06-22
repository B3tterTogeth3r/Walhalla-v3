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
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import de.b3ttertogeth3r.walhalla.R;

public class BulletItem extends RelativeLayout {
    private TextView textView;

    public BulletItem(Context context, String text) {
        super(context);
        design(context);
        setText(text);
    }

    private void design(@NonNull Context context) {
        this.setLayoutParams(new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        int padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                4,
                context.getResources().getDisplayMetrics()
        );
        ImageView checkBox = new ImageView(context);
        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
                (10 * padding), ViewGroup.LayoutParams.WRAP_CONTENT
        );
        imageParams.setMarginEnd(padding);
        checkBox.setPadding(padding, padding, padding, padding);
        checkBox.setId(R.id.x_left);
        checkBox.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.check_box_icon));
        checkBox.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent));

        this.addView(checkBox, imageParams);

        textView = new TextView(context);
        RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        textParams.addRule(RIGHT_OF, checkBox.getId());
        textParams.addRule(CENTER_VERTICAL);
        textView.setTextAppearance(R.style.TextAppearance_AppCompat_Button);
        textView.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));

        this.addView(textView, textParams);
    }

    public BulletItem setText(String text) {
        textView.setText(text);
        return this;
    }

    public BulletItem(Context context) {
        super(context);
        design(context);
    }
}