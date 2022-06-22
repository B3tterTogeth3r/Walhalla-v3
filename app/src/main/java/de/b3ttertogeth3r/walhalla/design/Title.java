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
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import de.b3ttertogeth3r.walhalla.R;

public class Title extends androidx.appcompat.widget.AppCompatTextView {

    public Title (Context context) {
        super(context);
        design(context);
    }

    private void design (@NonNull Context context) {
        int padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                8f,
                context.getResources().getDisplayMetrics()
        );
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(/*left*/padding,
                /*top*/0,
                /*right*/(padding / 2),
                /*bottom*/0);
        setPadding(/*left*/padding,
                /*top*/padding,
                /*right*/padding,
                /*bottom*/padding);
        setTextAppearance(context, R.style.TextAppearance_AppCompat_Title);
        setLayoutParams(params);
    }

    public Title (Context context, String title) {
        super(context);
        design(context);
        setText(title);
    }

    public Title setTitle (char text) {
        this.setText(text);
        return this;
    }

    public Title setTile (int resid) {
        setText(resid);
        return this;
    }

    public Title setTitle (String text) {
        setText(text);
        return this;
    }
}
