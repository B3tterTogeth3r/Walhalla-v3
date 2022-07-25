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
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;

import de.b3ttertogeth3r.walhalla.object.Text;

public class TableRow extends android.widget.TableRow {

    public TableRow(Context context) {
        super(context);
        design(context);
    }

    public TableRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        design(context);
    }

    private void design(Context context) {
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        setPadding(0, 0, 0, 0);
        params.setMargins(0, 0, 0, 0);
        setLayoutParams(params);
    }

    public TableRow(Context context, @NonNull ArrayList<Text> textList) {
        super(context);
        design(context);
        for (Text t : textList) {
            for (String s : t.getValue()) {
                addView(new de.b3ttertogeth3r.walhalla.design.Text(context, s));
            }
        }
    }

    public TableRow(FragmentActivity context, @NonNull ArrayList<String> textList) {
        super(context);
        design(context);
        for (String s : textList) {
            addView(new de.b3ttertogeth3r.walhalla.design.Text(context, s));

        }
    }

    @NonNull
    @Override
    public String toString() {
        String string = super.toString();
        if (getChildCount() != 0) {
            string = "TableRow Children: " + getChildCount();
        }
        return string;
    }
}
