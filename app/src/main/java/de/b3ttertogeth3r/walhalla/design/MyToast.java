package de.b3ttertogeth3r.walhalla.design;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import de.b3ttertogeth3r.walhalla.R;

public class MyToast extends Toast {
    private static final String TAG = "MyToast";
    private final MyButton view;
    private final Context context;

    public MyToast (Context context) {
        super(context);
        this.context = context;
        view = new MyButton(context);
        view.setTextColor(ContextCompat.getColor(context, R.color.white));
        setView(view);
        setDuration(Toast.LENGTH_LONG);
        setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        setGravity(Gravity.BOTTOM, 0, 100);
    }

    @Override
    public void setText (int resId) {
        view.setText(context.getString(resId));
    }

    @Override
    public void setText (CharSequence s) {
        view.setText(s);
    }


}
