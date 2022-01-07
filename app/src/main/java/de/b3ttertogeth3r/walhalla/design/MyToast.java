package de.b3ttertogeth3r.walhalla.design;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import de.b3ttertogeth3r.walhalla.R;

public class MyToast extends Toast {
    private static final String TAG = "MyToast";
    private final MyTitle view;
    private final Context context;

    public MyToast (Context context) {
        super(context);
        this.context = context;
        view = new MyTitle(context);
        view.setBackground(ContextCompat.getDrawable(context, R.drawable.background_button));
        setView(view);
        setDuration(Toast.LENGTH_LONG);
        setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        setGravity(Gravity.BOTTOM, 0, 100);
    }

    @Override
    public void setText (int resId) {
        view.setText(context.getString(resId));
        view.setTextColor(ContextCompat.getColor(context, R.color.background));
    }

    @Override
    public void setText (CharSequence s) {
        view.setTextColor(ContextCompat.getColor(context, R.color.background));
        view.setText(s);
    }

    public MyToast setMessage(CharSequence s) {
        view.setTextColor(ContextCompat.getColor(context, R.color.background));
        view.setText(s);
        return this;
    }

    public MyToast setMessage(int resId) {
        view.setTextColor(ContextCompat.getColor(context, R.color.background));
        view.setText(resId);
        return this;
    }
}
