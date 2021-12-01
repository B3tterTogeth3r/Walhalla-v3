package de.b3ttertogeth3r.walhalla.design;

import android.content.Context;
import android.util.TypedValue;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;

import de.b3ttertogeth3r.walhalla.enums.Editable;
import de.b3ttertogeth3r.walhalla.interfaces.EditListener;

public class MyNumberPicker extends NumberPicker {
    private String[] values;
    private EditListener listener;
    private Editable editable;
    private Context context;

    public MyNumberPicker (Context context, String[] values, EditListener listener, Editable editable){
        super(context);
        this.context= context;
        this.values = values;
        this.listener = listener;
        this.editable = editable;
        design();
    }

    public MyNumberPicker (Context context, String[] values) {
        super(context);
        design();
    }

    private void design(){
        int padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                8f,
                context.getResources().getDisplayMetrics()
        );
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(/*left*/padding,
                /*top*/(4 * padding),
                /*right*/(padding / 2),
                /*bottom*/(4 * padding));
        setPadding(/*left*/padding,
                /*top*/padding,
                /*right*/padding,
                /*bottom*/padding);
        setLayoutParams(params);
        setOrientation(VERTICAL);
        setEnabled(true);
        setClickable(true);
        setDisplayedValues(values);
        setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        setMinValue(0);
        setMaxValue(values.length - 1);
        if (listener != null) {
            enableListener();
        }
    }

    public String getResult(){
        return values[getValue()];
    }

    private void enableListener () {
        setOnValueChangedListener(new OnValueChangeListener() {
            @Override
            public void onValueChange (NumberPicker picker, int oldVal, int newVal) {
                String result = values[newVal];
                listener.sendLiveChange(result, Editable.DAY);
            }
        });
    }

}
