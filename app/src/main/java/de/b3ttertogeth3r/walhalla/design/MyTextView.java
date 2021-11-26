package de.b3ttertogeth3r.walhalla.design;

import android.content.Context;
import android.util.TypedValue;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;

import de.b3ttertogeth3r.walhalla.R;

public class MyTextView extends androidx.appcompat.widget.AppCompatTextView {

    public MyTextView (Context context) {
        super(context);
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
        //TODO Autolink web
        //TODO links clickable true
        setLinksClickable(true);
        setLinkTextColor(ContextCompat.getColor(context, R.color.colorAccent));
    }
}
