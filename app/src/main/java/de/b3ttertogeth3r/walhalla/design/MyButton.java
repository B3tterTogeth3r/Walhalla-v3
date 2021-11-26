package de.b3ttertogeth3r.walhalla.design;

import android.content.Context;
import android.util.TypedValue;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;

import de.b3ttertogeth3r.walhalla.R;


public class MyButton extends androidx.appcompat.widget.AppCompatButton {

    public MyButton (Context context) {
        super(context);
        int padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                16f,
                context.getResources().getDisplayMetrics()
        );
        setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.background_button));
        setTextColor(ContextCompat.getColor(context, R.color.whiteish));
        setPadding(padding, padding, padding, padding);
        setTextAppearance(context, R.style.TextAppearance_AppCompat_Body2);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(params);
    }
}
