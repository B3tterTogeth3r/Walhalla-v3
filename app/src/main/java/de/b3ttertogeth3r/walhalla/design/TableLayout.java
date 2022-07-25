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

import androidx.annotation.NonNull;

import de.b3ttertogeth3r.walhalla.R;

public class TableLayout extends android.widget.TableLayout {
    private static final String TAG = "TableLayout";
    private final Context context;

    public TableLayout(Context context) {
        super(context);
        this.context = context;
        design();
    }

    private void design() {
        setId(R.id.table);

        int padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                8f,
                context.getResources().getDisplayMetrics()
        );
        android.widget.TableLayout.LayoutParams params = new android.widget.TableLayout.LayoutParams(
                android.widget.TableLayout.LayoutParams.MATCH_PARENT,
                android.widget.TableLayout.LayoutParams.WRAP_CONTENT);
        setPadding(/*left*/padding,
                /*top*/padding,
                /*right*/padding,
                /*bottom*/padding);
        setLayoutParams(params);
        setOrientation(VERTICAL);
    }

    public TableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        design();
    }

    @NonNull
    @Override
    public String toString() {
        if (getChildCount() != 0) {
            return TAG + ": " + getChildCount();
        }
        return super.toString();
    }
}
