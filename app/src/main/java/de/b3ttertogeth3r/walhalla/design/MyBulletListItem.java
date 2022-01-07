package de.b3ttertogeth3r.walhalla.design;

import android.content.Context;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import de.b3ttertogeth3r.walhalla.R;

public class MyBulletListItem extends RelativeLayout {
    private static final String TAG = "MyBulletItem";
    private final TextView textView;

    public MyBulletListItem (Context context) {
        super(context);

        int padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                4,
                context.getResources().getDisplayMetrics()
        );
        ImageView checkBox = new ImageView(context);
        LayoutParams imageParams = new LayoutParams(
                (10 * padding), (10 * padding)
        );
        imageParams.setMarginEnd(padding);
        checkBox.setPadding(padding, padding, padding, padding);
        checkBox.setId(R.id.x_left);
        checkBox.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_circle));
        checkBox.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent));

        this.addView(checkBox, imageParams);

        textView = new TextView(context);
        LayoutParams textParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
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
