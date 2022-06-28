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
import android.view.Gravity;
import android.view.View;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import de.b3ttertogeth3r.walhalla.R;

public class Toast extends android.widget.Toast {
    private static final String TAG = "Toast";
    public static final int LENGTH_LONG = 1;
    public static final int LENGTH_SHORT = 0;
    private final Context context;
    private Title view;

    public Toast (Context context) {
        super(context);
        this.context = context;
        design();
    }

    @Override
    public void addCallback(@NonNull Callback callback) {

    }

    private void design () {
        view = new Title(context);
        view.setGravity(Gravity.CENTER);
        view.setBackground(ContextCompat.getDrawable(context, R.drawable.background_toast));
        setView(view);
        setDuration(android.widget.Toast.LENGTH_LONG);
        setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        setGravity(Gravity.BOTTOM, 0, 100);
    }

    @Deprecated
    @Override
    public void setView (View view) {
        super.setView(view);
    }

    @Override
    public void setDuration (@Duration int duration) {
        super.setDuration(duration);
    }

    public Toast setMessage (CharSequence s) {
        view.setTextColor(ContextCompat.getColor(context, R.color.background));
        view.setText(s);
        return this;
    }

    public Toast setMessage (int resId) {
        view.setTextColor(ContextCompat.getColor(context, R.color.background));
        view.setText(resId);
        return this;
    }

    @IntDef(value = {
            LENGTH_SHORT,
            LENGTH_LONG
    })
    public @interface Duration {}

    @NonNull
    public static Toast makeToast(Context context, CharSequence text) {
        Toast t = new Toast(context);
        t.setMessage(text);
        return t;
    }

    @NonNull
    public static Toast makeToast(Context context, int resId) {
        Toast t = new Toast(context);
        t.setMessage(resId);
        return t;
    }
}
