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
import android.text.Editable;
import android.text.PrecomputedText;
import android.text.Spannable;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import de.b3ttertogeth3r.walhalla.R;

@SuppressWarnings("UnusedReturnValue")
public class RowProfile extends RelativeLayout {
    private static final String TAG = "ProfileRow";
    private final MyTextView title, value;
    private final int padding;
    private LayoutParams params;
    private boolean isChanged = false;

    public RowProfile (Context context) {
        super(context);
        padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                8f,
                context.getResources().getDisplayMetrics()
        );
        params = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
        );
        setLayoutParams(params);
        title = new MyTextView(context);
        value = new MyTextView(context);
        MyProfileArrow image = new MyProfileArrow(getContext());
        setBackground(ContextCompat.getDrawable(context, R.drawable.border_bottom_black));

        params = new LayoutParams(title.getLayoutParams());
        params.addRule(CENTER_VERTICAL);

        addView(title);
        params = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        params.setMarginStart(((int) 18.5 * padding));
        params.setMarginEnd(padding);
        params.addRule(CENTER_VERTICAL);

        addView(value, params);
        addView(image);
    }

    /**
     * Return the text that TextView is displaying. If {@link #setValueText(CharSequence)} was called
     * with an argument of {@link android.widget.TextView.BufferType#SPANNABLE BufferType.SPANNABLE}
     * or {@link android.widget.TextView.BufferType#EDITABLE BufferType.EDITABLE}, you can cast
     * the return value from this method to Spannable or Editable, respectively.
     *
     * <p>The content of the return value should not be modified. If you want a modifiable one, you
     * should make your own copy first.</p>
     *
     * @return The text displayed by the text view.
     */
    public String getValueText () {
        return this.value.getText();
    }


    /**
     * Sets the text to be displayed. TextView <em>does not</em> accept
     * HTML-like formatting, which you can do with text strings in XML resource files.
     * To style your strings, attach android.text.style.* objects to a
     * {@link android.text.SpannableString}, or see the
     * <a href="{@docRoot}guide/topics/resources/available-resources.html#stringresources">
     * Available Resource Types</a> documentation for an example of setting
     * formatted text in the XML resource file.
     * <p/>
     * When required, TextView will use {@link android.text.Spannable.Factory} to create final or
     * intermediate {@link Spannable Spannables}. Likewise it will use
     * {@link android.text.Editable.Factory} to create final or intermediate
     * {@link Editable Editables}.
     *
     * If the passed text is a {@link PrecomputedText} but the parameters used to create the
     * PrecomputedText mismatches with this TextView, IllegalArgumentException is thrown. To ensure
     * the parameters match, you can call {@link TextView#setTextMetricsParams} before calling this.
     *
     * @param text text to be displayed
     *
     * @throws IllegalArgumentException if the passed text is a {@link PrecomputedText} but the
     *                                  parameters used to create the PrecomputedText mismatches
     *                                  with this TextView.
     */
    public RowProfile setValueText (CharSequence text) {
        this.value.setText(text);
        return this;
    }

    /**
     * Sets the text to be displayed. TextView <em>does not</em> accept
     * HTML-like formatting, which you can do with text strings in XML resource files.
     * To style your strings, attach android.text.style.* objects to a
     * {@link android.text.SpannableString}, or see the
     * <a href="{@docRoot}guide/topics/resources/available-resources.html#stringresources">
     * Available Resource Types</a> documentation for an example of setting
     * formatted text in the XML resource file.
     * <p/>
     * When required, TextView will use {@link android.text.Spannable.Factory} to create final or
     * intermediate {@link Spannable Spannables}. Likewise it will use
     * {@link android.text.Editable.Factory} to create final or intermediate
     * {@link Editable Editables}.
     *
     * If the passed text is a {@link PrecomputedText} but the parameters used to create the
     * PrecomputedText mismatches with this TextView, IllegalArgumentException is thrown. To ensure
     * the parameters match, you can call {@link TextView#setTextMetricsParams} before calling this.
     *
     * @param s text to be added to the already contained text
     *
     * @throws IllegalArgumentException if the passed text is a {@link PrecomputedText} but the
     *                                  parameters used to create the PrecomputedText mismatches
     *                                  with this TextView.
     */
    public RowProfile addToTitle (CharSequence s) {
        String complete = getTitleText() + " " + s;
        setTitleText(complete);
        return this;
    }

    /**
     * Return the text that TextView is displaying. If {@link #setTitleText(CharSequence)} was called
     * with an argument of {@link android.widget.TextView.BufferType#SPANNABLE BufferType.SPANNABLE}
     * or {@link android.widget.TextView.BufferType#EDITABLE BufferType.EDITABLE}, you can cast
     * the return value from this method to Spannable or Editable, respectively.
     *
     * <p>The content of the return value should not be modified. If you want a modifiable one, you
     * should make your own copy first.</p>
     *
     * @return The text displayed by the text view.
     * @attr ref android.R.styleable#TextView_text
     */
    public String getTitleText () {
        return this.title.getText();
    }

    /**
     * Sets the text to be displayed. TextView <em>does not</em> accept
     * HTML-like formatting, which you can do with text strings in XML resource files.
     * To style your strings, attach android.text.style.* objects to a
     * {@link android.text.SpannableString}, or see the
     * <a href="{@docRoot}guide/topics/resources/available-resources.html#stringresources">
     * Available Resource Types</a> documentation for an example of setting
     * formatted text in the XML resource file.
     * <p/>
     * When required, TextView will use {@link android.text.Spannable.Factory} to create final or
     * intermediate {@link Spannable Spannables}. Likewise it will use
     * {@link android.text.Editable.Factory} to create final or intermediate
     * {@link Editable Editables}.
     *
     * If the passed text is a {@link PrecomputedText} but the parameters used to create the
     * PrecomputedText mismatches with this TextView, IllegalArgumentException is thrown. To ensure
     * the parameters match, you can call {@link TextView#setTextMetricsParams} before calling this.
     *
     * @param text text to be displayed
     *
     * @throws IllegalArgumentException if the passed text is a {@link PrecomputedText} but the
     *                                  parameters used to create the PrecomputedText mismatches
     *                                  with this TextView.
     */
    public RowProfile setTitleText (CharSequence text) {
        this.title.setText(text);
        return this;
    }

    /**
     * Sets the text to be displayed using a string resource identifier.
     *
     * @param resid the resource identifier of the string resource to be displayed
     *
     * @see #setTitleText(CharSequence)
     *
     * @attr ref android.R.styleable#TextView_text
     */
    public RowProfile setTitleText (int resid) {
        this.title.setText(resid);
        return this;
    }

    /**
     * change the value view into another
     *
     * @param view
     *         the view to be set as the "value"
     * @return this to add/edit mroe
     */
    public RowProfile replaceValue (View view) {
        isChanged = !isChanged;
        params.setMargins(params.getMarginStart(), padding, params.getMarginEnd(), padding);
        this.addView(view, 1, params);
        this.removeView(value);
        return this;
    }
}
