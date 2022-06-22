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

package de.b3ttertogeth3r.walhalla.old.design;

import android.content.Context;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import de.b3ttertogeth3r.walhalla.R;

public class MyBulletListItem extends RelativeLayout {
    private static final String TAG = "MyBulletItem";
    private final TextView textView;

    public MyBulletListItem (Context context) {
        super(context);

        int padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                4,
                context.getResources().getDisplayMetrics()
        );
        ImageView checkBox = new ImageView(context);
        LayoutParams imageParams = new LayoutParams(
                (10 * padding), (10 * padding)
        );
        imageParams.setMarginEnd(padding);
        checkBox.setPadding(padding, padding, padding, padding);
        checkBox.setId(R.id.x_left);
        checkBox.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_circle));
        checkBox.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent));

        this.addView(checkBox, imageParams);

        textView = new TextView(context);
        LayoutParams textParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
        );
        textParams.addRule(RIGHT_OF, checkBox.getId());
        textParams.addRule(CENTER_VERTICAL);
        textView.setTextAppearance(R.style.TextAppearance_AppCompat_Button);
        textView.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));

        this.addView(textView, textParams);

    }

    public void setText(String text){
        textView.setText(text);
    }
}
