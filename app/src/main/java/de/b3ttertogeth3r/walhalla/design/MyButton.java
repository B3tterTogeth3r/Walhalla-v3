package de.b3ttertogeth3r.walhalla.design;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;

import de.b3ttertogeth3r.walhalla.R;


public class MyButton extends RelativeLayout {
    private final MySubtitle text;
    private final ImageView icon;

    public MyButton (Context context) {
        super(context);
        int padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                16f,
                context.getResources().getDisplayMetrics()
        );
        icon = new ImageView(context);
        text = new MySubtitle(context);
        RelativeLayout.LayoutParams textParams = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
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
        text.setId(R.id.text);
        LayoutParams imageParams = new LayoutParams((padding / 2), (padding / 2));
        imageParams.addRule(LEFT_OF, text.getId());
        icon.setVisibility(View.GONE);
        addView(text, textParams);
        addView(icon, imageParams);
    }

    public MyButton setIconDrawable (Drawable drawable) {
        icon.setImageDrawable(drawable);
        icon.setVisibility(View.VISIBLE);
        return this;
    }

    public void setText (CharSequence text) {
        this.text.setText(text);
        this.text.setTextColor(ContextCompat.getColor(getContext(), R.color.background));
    }

    public MyButton setText(int resid) {
        this.text.setText(resid);
        this.text.setTextColor(ContextCompat.getColor(getContext(), R.color.background));
        return this;
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
