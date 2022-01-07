package de.b3ttertogeth3r.walhalla.design;

import android.content.Context;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;

import de.b3ttertogeth3r.walhalla.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfileImage extends CircleImageView {

    public MyProfileImage (Context context) {
        super(context);
        int padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                1f,
                context.getResources().getDisplayMetrics()
        );
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                (padding * 70),
                (padding * 70)
        );
        setLayoutParams(params);
        setBorderOverlay(false);
        setBorderColor(ContextCompat.getColor(context, R.color.black));
        setBorderWidth(padding);
        setImageDrawable(ContextCompat.getDrawable(context, R.drawable.wappen_2017_v2));
        setCircleBackgroundColor(ContextCompat.getColor(context, R.color.black));
    }
}
