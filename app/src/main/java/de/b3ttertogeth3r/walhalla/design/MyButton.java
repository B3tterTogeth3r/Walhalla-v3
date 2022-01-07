package de.b3ttertogeth3r.walhalla.design;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import de.b3ttertogeth3r.walhalla.R;


public class MyButton extends RelativeLayout {
    private final MySubtitle text;

    public MyButton (Context context) {
        super(context);
        int padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                16f,
                context.getResources().getDisplayMetrics()
        );
        text = new MySubtitle(context);
        RelativeLayout.LayoutParams textParams = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
        );
        textParams.addRule(CENTER_IN_PARENT);
        setBackground(ContextCompat.getDrawable(context, R.drawable.background_button));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        padding = (padding / 2);
        params.setMargins(padding, padding, padding, padding);
        setLayoutParams(params);
        text.setGravity(Gravity.CENTER);
        addView(text, textParams);
    }

    public void setText (CharSequence text) {
        this.text.setText(text);
        this.text.setTextColor(ContextCompat.getColor(getContext(), R.color.background));
    }

    public void setText(int resid) {
        this.text.setText(resid);
        this.text.setTextColor(ContextCompat.getColor(getContext(), R.color.background));
    }

    @Override
    public void setEnabled (boolean enabled) {
        try{
            if(enabled){
                setAlpha(1f);
                setBackgroundTintList(null);
                setBackgroundTintMode(null);
                setBackground(ContextCompat.getDrawable(getContext(), R.drawable.background_button));
            } else {
                setOnClickListener(null);
                setAlpha(0.5f);
                setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.darkGray));
                setBackgroundTintMode(PorterDuff.Mode.DARKEN);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
