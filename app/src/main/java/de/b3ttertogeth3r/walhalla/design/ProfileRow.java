package de.b3ttertogeth3r.walhalla.design;

import static android.widget.LinearLayout.HORIZONTAL;

import android.content.Context;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class ProfileRow extends RelativeLayout {
    private static final String TAG = "ProfileRow";
    private final MyTextView title, value;

    public ProfileRow (Context context) {
        super(context);
        int padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                8f,
                context.getResources().getDisplayMetrics()
        );
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setLayoutDirection(HORIZONTAL);
        setLayoutParams(params);
        title = new MyTextView(context);
        value = new MyTextView(context);
        MyProfileArrow image = new MyProfileArrow(getContext());

        params = new LayoutParams(value.getLayoutParams());
        params.setMarginStart(((int) 17.5 * padding));
        params.setMarginEnd(4 * padding);

        addView(title);
        addView(value, params);
        addView(image);
    }

    public String getValueText () {
        return this.value.getText().toString();
    }

    public void setValueText (String value) {
        this.value.setText(value);
    }

    public String getTitleText () {
        return this.title.getText().toString();
    }

    public void setTitleText (String value) {
        this.title.setText(value);
    }

    public void setTitleText(int resId){
        this.title.setText(resId);
    }
}
