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
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import de.b3ttertogeth3r.walhalla.R;

public class Text extends AppCompatTextView {

    public Text (Context context) {
        super(context);
        design(context);
    }

    public Text(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        design(context);
    }

    public Text(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        design(context);
    }

    private void design (@NonNull Context context) {
        int padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                16f,
                context.getResources().getDisplayMetrics()
        );
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        setPadding(padding, (int) (0.5 * padding), padding, padding);
        setTextAppearance(context, R.style.TextAppearance_AppCompat_Body1);
        setLayoutParams(params);
        setLinksClickable(true);
        setLinkTextColor(ContextCompat.getColor(context, R.color.colorAccent));
    }

    public Text (Context context, String text) {
        super(context);
        design(context);
        setText(text);
    }

}
