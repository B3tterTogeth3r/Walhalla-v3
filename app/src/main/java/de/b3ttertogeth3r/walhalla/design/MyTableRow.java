package de.b3ttertogeth3r.walhalla.design;

import android.content.Context;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import de.b3ttertogeth3r.walhalla.R;

public class MyTableRow extends TableRow {

    public MyTableRow (Context context) {
        super(context);
        design(context);
    }

    private void design (@NonNull Context context) {
        LayoutParams params = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);

        params.setMargins(0, 0, 0, 0);

        setPadding(/*left*/0,
                /*top*/0,
                /*right*/0,
                /*bottom*/0);

        setLayoutParams(params);
        setBackground(ContextCompat.getDrawable(context, R.drawable.border_bottom_black));
    }

}
