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
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.enums.Editable;
import de.b3ttertogeth3r.walhalla.interfaces.EditListener;
import de.b3ttertogeth3r.walhalla.interfaces.TextWatcher;

public class EditText extends TextInputLayout {
    private static final String TAG = "EditText";
    private final Context context;
    private final android.widget.EditText text;
    private de.b3ttertogeth3r.walhalla.enums.Editable editable = Editable.EMPTY;

    public EditText(@NonNull Context context) {
        super(context);
        this.context = context;
        this.text = new android.widget.EditText(context);
        design();
    }

    private void design() {
        this.addView(text);
        int padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                16f,
                context.getResources().getDisplayMetrics()
        );
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        setPadding(padding, (int) (0.5 * padding), padding, (int) (0.5 * padding));
        this.text.setTextAppearance(R.style.TextAppearance_AppCompat_Body1);
        setLayoutParams(params);
    }

    public EditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.text = new android.widget.EditText(context);
        design();
    }

    public EditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        this.text = new android.widget.EditText(context);
        design();
    }

    public EditText(@NonNull Context context, Editable editable) {
        super(context);
        this.context = context;
        this.text = new android.widget.EditText(context);
        design();
    }

    public EditText setEditListener(@NonNull EditListener editListener) {
        text.addTextChangedListener((TextWatcher) s -> {
            editListener.sendLiveChange(s.toString(), getEditable());
        });
        return this;
    }

    public Editable getEditable() {
        return editable;
    }

    public EditText setEditable(Editable editable) {
        this.editable = editable;
        return this;
    }

    public EditText setContent(String text) {
        this.text.setText(text);
        return this;
    }

    /**
     * @return String value or null
     */
    @Nullable
    public String getText() {
        String string = this.text.getText().toString();
        if (string.length() != 0) {
            return string;
        } else {
            return null;
        }
    }

    /**
     * adds a hint and sets the hint color and underline to colorAccent
     *
     * @param hint CharSequence of the displayed hint
     * @return this to add more
     */
    public EditText setText(CharSequence hint) {
        text.setText(hint);
        return this;
    }

    /**
     * adds a hint and sets the hint color and underline to colorAccent
     *
     * @param textHintId int of the displayed hint
     * @return this to add more
     */
    public EditText setText(int textHintId) {
        text.setText(textHintId);
        return this;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (this.text != null) {
            text.setEnabled(enabled);
            text.setClickable(enabled);
        }
    }

    public android.widget.EditText getField() {
        return text;
    }

    /**
     * adds a hint and sets the hint color and underline to colorAccent
     *
     * @param hint CharSequence of the displayed hint
     * @return this to add more
     */
    public EditText setDescription(CharSequence hint) {
        this.setHint(hint);
        this.setHintTextColor(ContextCompat.getColorStateList(context, R.color.colorAccent));
        return this;
    }

    /**
     * adds a hint and sets the hint color and underline to colorAccent
     *
     * @param textHintId int of the displayed hint
     * @return this to add more
     */
    public EditText setDescription(int textHintId) {
        this.setHintTextColor(ContextCompat.getColorStateList(context, R.color.colorAccent));
        this.setHint(textHintId);
        return this;
    }

    public EditText addTextWatcher(TextWatcher watcher) {
        this.text.addTextChangedListener(watcher);
        return this;
    }

    public void setError(boolean enabled, String message) {
        if (enabled) {
            this.setErrorEnabled(true);
            this.setErrorIconDrawable(R.drawable.ic_error_outline);
            this.setErrorIconTintList(ColorStateList.valueOf(Color.RED));
            this.setError(message);
        } else {
            this.setErrorIconDrawable(null);
            this.setError(null);
            this.setErrorEnabled(false);
        }
    }

    public void setInputType(int type) {
        this.text.setInputType(InputType.TYPE_CLASS_TEXT | type);
    }
}
