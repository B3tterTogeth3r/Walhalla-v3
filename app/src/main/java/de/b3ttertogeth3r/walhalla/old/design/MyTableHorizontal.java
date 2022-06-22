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
import android.widget.HorizontalScrollView;
import android.widget.TableLayout;

import androidx.core.content.ContextCompat;

import de.b3ttertogeth3r.walhalla.R;

public class MyTableHorizontal extends HorizontalScrollView {
    public final TableLayout layout;

    public MyTableHorizontal (Context context) {
        super(context);
        removeAllViews();
        removeAllViewsInLayout();
        layout = new TableLayout(context);
        layout.removeAllViews();
        layout.removeAllViewsInLayout();
        layout.setId(R.id.table);
        layout.isColumnStretchable(0);
        layout.isColumnShrinkable(0);
        addView(layout);

        int padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                1f,
                context.getResources().getDisplayMetrics()
        );
        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        setPadding(/*left*/(padding * 8),
                /*top*/(padding * 8),
                /*right*/(padding * 8),
                /*bottom*/(padding * 8));
        setLayoutParams(params);
        setBackground(ContextCompat.getDrawable(context, R.drawable.border_round));
    }
}
