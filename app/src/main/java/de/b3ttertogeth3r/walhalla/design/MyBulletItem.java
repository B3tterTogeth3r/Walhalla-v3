package de.b3ttertogeth3r.walhalla.design;

import android.content.Context;
import android.util.TypedValue;
import android.view.Display;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import de.b3ttertogeth3r.walhalla.R;

public class MyBulletItem extends RelativeLayout {
    private static final String TAG = "MyBulletItem";
    private final TextView textView;

    public MyBulletItem (Context context) {
        super(context);

        int padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                4,
                context.getResources().getDisplayMetrics()
        );
        ImageView checkBox = new ImageView(context);
        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
                (10 * padding), (10 * padding)
        );
        imageParams.setMarginEnd(padding);
        checkBox.setPadding(padding, padding, padding, padding);
        checkBox.setId(R.id.x_left);
        checkBox.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.check_box_icon));
        checkBox.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent));

        this.addView(checkBox, imageParams);

        textView = new TextView(context);
        RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        textParams.addRule(RIGHT_OF, checkBox.getId());
        textParams.addRule(CENTER_VERTICAL);
        textView.setTextAppearance(R.style.TextAppearance_AppCompat_Button);
        textView.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));

        this.addView(textView, textParams);

    }

    public void setText(String text){
        textView.setText(text);
    }
}
