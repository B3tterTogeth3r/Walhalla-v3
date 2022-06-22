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
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import de.b3ttertogeth3r.walhalla.R;

public class MyTableRow extends TableRow {

    public MyTableRow (Context context) {
        super(context);
        design(context);
    }

    private void design (@NonNull Context context) {
        LayoutParams params = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);

        params.setMargins(0, 0, 0, 0);

        setPadding(/*left*/0,
                /*top*/0,
                /*right*/0,
                /*bottom*/0);

        setLayoutParams(params);
        setBackground(ContextCompat.getDrawable(context, R.drawable.border_bottom_black));
    }

}
