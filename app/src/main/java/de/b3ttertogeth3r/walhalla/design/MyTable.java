package de.b3ttertogeth3r.walhalla.design;

import android.content.Context;
import android.util.TypedValue;
import android.widget.TableLayout;

import de.b3ttertogeth3r.walhalla.R;

public class MyTable extends TableLayout {

    public MyTable (Context context) {
        super(context);
        setId(R.id.table);

        int padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                8f,
                context.getResources().getDisplayMetrics()
        );
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT);
        setPadding(/*left*/padding,
                /*top*/padding,
                /*right*/padding,
                /*bottom*/padding);
        setLayoutParams(params);
        setOrientation(VERTICAL);
    }
}
