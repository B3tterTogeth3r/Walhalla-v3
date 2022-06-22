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

import static de.b3ttertogeth3r.walhalla.old.enums.Editable.EMPTY;

import android.content.Context;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.old.enums.PasswordStrength;
import de.b3ttertogeth3r.walhalla.old.interfaces.EditListener;
import de.b3ttertogeth3r.walhalla.old.interfaces.MyTextWatcher;

/**
 * My Design of an edit text view where the hint is always visible
 *
 * @author B3tterTogeth3r
 * @author
 * <a href="https://www.tutorialkart.com/kotlin-android/android-floating-label-in-edittext-using-textinputlayout-example/">TutorialKart</a>
 * @version 2.0
 * @since 1.1
 */
@SuppressWarnings("UnusedReturnValue")
public class MyEditText extends TextInputLayout {
    private static final String TAG = "MyEditText";
    private final Context context;
    private final EditText text;
    private de.b3ttertogeth3r.walhalla.old.enums.Editable editable = EMPTY;

    /**
     * Create a new Edit Text
     *
     * @param context
     *         context of the view, in which the field is displayed
     * @since 1.0
     */
    public MyEditText (Context context) {
        super(context);
        this.context = context;
        this.text = new EditText(context);
        design();
    }

    private void design () {
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

    /**
     * Create a new Edit Text with a live listener
     *
     * @param context
     *         context of the view, in which the field is displayed
     * @param listener
     *         EditListener to listen to real time changes
     * @since 1.11
     */
    public MyEditText (Context context, EditListener listener) {
        super(context);
        this.context = context;
        this.text = new EditText(context);
        this.text.addTextChangedListener((MyTextWatcher) s -> {
            if (listener != null) {
                listener.sendLiveChange(s.toString(), getEditable());
            }
        });
        design();
    }

    /**
     * @return Editable to find the specific edited field
     */
    public de.b3ttertogeth3r.walhalla.old.enums.Editable getEditable () {
        return editable;
    }

    /**
     * @param editable
     *         Editable to edit a specific field
     * @return this to add more
     */
    public MyEditText setEditable (de.b3ttertogeth3r.walhalla.old.enums.Editable editable) {
        this.editable = editable;
        return this;
    }

    /**
     * @param text
     *         String to set into the editable field
     * @return this to add more
     */
    public MyEditText setContent (String text) {
        this.text.setText(text);
        return this;
    }

    /**
     * @return String value or null
     */
    @Nullable
    public String getString () {
        return getText();
    }

    /**
     * @return String value or null
     */
    @Nullable
    public String getText () {
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
     * @param hint
     *         CharSequence of the displayed hint
     * @return this to add more
     */
    public MyEditText setDescription (CharSequence hint) {
        this.setHint(hint);
        this.setHintTextColor(ContextCompat.getColorStateList(context, R.color.colorAccent));
        return this;
    }

    /**
     * adds a hint and sets the hint color and underline to colorAccent
     *
     * @param textHintId
     *         int of the displayed hint
     * @return this to add more
     */
    public MyEditText setDescription (int textHintId) {
        this.setHintTextColor(ContextCompat.getColorStateList(context, R.color.colorAccent));
        this.setHint(textHintId);
        return this;
    }

    /**
     * sets the text appearance of the editable text
     *
     * @param resId
     *         int of the text appearance
     * @return this to add more
     */
    public MyEditText setTextAppearance (int resId) {
        this.text.setTextAppearance(resId);
        return this;
    }

    /**
     * sets the input type of the ediable text
     *
     * @param type
     *         int of the input type
     * @return this to add more
     */
    public MyEditText setInputType (int type) {
        this.text.setInputType(type);
        return this;
    }

    public MyEditText addTextWatcher (MyTextWatcher watcher) {
        this.text.addTextChangedListener(watcher);
        return this;
    }

    public void error (PasswordStrength strength) {
        if (strength != null) {
            try {
                this.setErrorEnabled(true);
                this.setErrorIconDrawable(R.drawable.ic_error_outline);
                this.setErrorIconTintList(ContextCompat.getColorStateList(context,
                        strength.getColor()));
                String result =
                        strength.getText(context) + "\n" + strength.getRequireDescription(context);
                this.setError(result); //TODO Add password description
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                this.setErrorEnabled(true);
                this.setErrorTextColor(ContextCompat.getColorStateList(context, R.color.green));
                this.setErrorIconOnClickListener(null);
                this.setErrorIconDrawable(R.drawable.ic_check_circle);
                this.setErrorIconTintList(ContextCompat.getColorStateList(context, R.color.green));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setEnabled (boolean enabled) {
        super.setEnabled(enabled);
        if (this.text != null) {
            text.setEnabled(enabled);
            text.setClickable(enabled);
        }
    }

    public EditText getField () {
        return text;
    }
}
