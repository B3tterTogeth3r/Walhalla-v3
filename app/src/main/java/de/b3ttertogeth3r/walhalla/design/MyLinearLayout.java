package de.b3ttertogeth3r.walhalla.design;

import android.content.Context;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;

import de.b3ttertogeth3r.walhalla.R;

public class MyLinearLayout extends LinearLayout {

    public MyLinearLayout (Context context) {
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
                /*top*/padding,
                /*right*/(padding),
                /*bottom*/padding);
        setPadding(/*left*/padding,
                /*top*/padding,
                /*right*/padding,
                /*bottom*/padding);
        setLayoutParams(params);
        setOrientation(VERTICAL);
        setBackground(ContextCompat.getDrawable(context, R.drawable.border_round));
    }
}
