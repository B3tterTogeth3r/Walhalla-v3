package de.b3ttertogeth3r.walhalla.design;

import android.content.Context;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import de.b3ttertogeth3r.walhalla.R;

public class MyTextView extends androidx.appcompat.widget.AppCompatTextView {

    public MyTextView (@NonNull Context context) {
        super(context);
        design(context);
    }

    public MyTextView (@NonNull Context context, String text) {
        super(context);
        design(context);
        setText(text);
    }

    @Override
    public String getText () {
        return super.getText().toString();
    }

    private void design (@NonNull Context context) {
        int padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                16f,
                context.getResources().getDisplayMetrics()
        );
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        setPadding(padding, (int) (0.5 * padding), padding, padding);
        setTextAppearance(context, R.style.TextAppearance_AppCompat_Body1);
        setLayoutParams(params);
        setLinksClickable(true);
        setLinkTextColor(ContextCompat.getColor(context, R.color.colorAccent));
    }
}
