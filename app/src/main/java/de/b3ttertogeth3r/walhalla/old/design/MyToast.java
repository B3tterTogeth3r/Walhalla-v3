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
import android.view.Gravity;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import de.b3ttertogeth3r.walhalla.R;

public class MyToast extends Toast {
    private static final String TAG = "MyToast";
    private final MyTitle view;
    private final Context context;

    public MyToast (Context context) {
        super(context);
        this.context = context;
        view = new MyTitle(context);
        view.setBackground(ContextCompat.getDrawable(context, R.drawable.background_button));
        setView(view);
        setDuration(Toast.LENGTH_LONG);
        setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        setGravity(Gravity.BOTTOM, 0, 100);
    }

    @Override
    public void setText (int resId) {
        view.setText(context.getString(resId));
        view.setTextColor(ContextCompat.getColor(context, R.color.background));
    }

    @Override
    public void setText (CharSequence s) {
        view.setTextColor(ContextCompat.getColor(context, R.color.background));
        view.setText(s);
    }

    public MyToast setMessage(CharSequence s) {
        view.setTextColor(ContextCompat.getColor(context, R.color.background));
        view.setText(s);
        return this;
    }

    public MyToast setMessage(int resId) {
        view.setTextColor(ContextCompat.getColor(context, R.color.background));
        view.setText(resId);
        return this;
    }
}
