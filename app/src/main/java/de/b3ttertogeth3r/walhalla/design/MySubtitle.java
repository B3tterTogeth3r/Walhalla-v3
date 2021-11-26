package de.b3ttertogeth3r.walhalla.design;

import android.content.Context;
import android.util.TypedValue;
import android.widget.RelativeLayout;

import de.b3ttertogeth3r.walhalla.R;

public class MySubtitle extends androidx.appcompat.widget.AppCompatTextView {

    public MySubtitle (Context context) {
        super(context);
        int padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                8f,
                context.getResources().getDisplayMetrics()
        );
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(/*left*/padding,
                /*top*/0,
                /*right*/(padding/2),
                /*bottom*/padding);
        setPadding(/*left*/padding,
                /*top*/padding,
                /*right*/padding,
                /*bottom*/padding);
        setTextAppearance(context, R.style.TextAppearance_AppCompat_Subhead);
        setLayoutParams(params);
    }
}
