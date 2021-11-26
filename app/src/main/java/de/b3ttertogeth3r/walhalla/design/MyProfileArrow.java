package de.b3ttertogeth3r.walhalla.design;

import android.content.Context;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;

import de.b3ttertogeth3r.walhalla.R;

public class MyProfileArrow extends androidx.appcompat.widget.AppCompatImageView {

    public MyProfileArrow (Context context) {
        super(context);
        int padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                8f,
                context.getResources().getDisplayMetrics()
        );
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                (padding*4),
                (padding*4));
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.addRule(RelativeLayout.ALIGN_PARENT_END);
        setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_arrow_right));
        setPadding(/*left*/padding/2,
                /*top*/padding/2,
                /*right*/0,
                /*bottom*/padding/2);
        setLayoutParams(params);
    }
}
