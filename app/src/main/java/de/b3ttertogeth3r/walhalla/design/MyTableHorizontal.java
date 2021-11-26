package de.b3ttertogeth3r.walhalla.design;

import android.content.Context;
import android.util.TypedValue;
import android.widget.TableLayout;

import androidx.core.content.ContextCompat;

import de.b3ttertogeth3r.walhalla.R;

public class MyTableHorizontal extends TableLayout {

    public MyTableHorizontal (Context context) {
        super(context);
        setId(R.id.table);

        int padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                1f,
                context.getResources().getDisplayMetrics()
        );
        LayoutParams params = new LayoutParams(
                (500 * padding),
                LayoutParams.WRAP_CONTENT);
        setPadding(/*left*/padding*8,
                /*top*/padding*8,
                /*right*/padding*8,
                /*bottom*/padding*8);
        setLayoutParams(params);
        setBackground(ContextCompat.getDrawable(context, R.drawable.border_round));
        setOrientation(HORIZONTAL);
    }
}
