package de.b3ttertogeth3r.walhalla.design;

import android.content.Context;
import android.util.TypedValue;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.enums.Kind;

public class MyTableRow extends TableLayout {

    public MyTableRow (Context context) {
        super(context);
        design(context);
    }

    public MyTableRow (Context context, Kind kind) {
        super(context);
        design(context);
        if (kind == Kind.PROFILE) {

            int padding = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    1f,
                    context.getResources().getDisplayMetrics()
            );
            LayoutParams params = new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT);
            params.setMargins(0, (4 * padding), 0, 0);
            setLayoutParams(params);
        }
    }

    private void design (@NonNull Context context) {

        int padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                1f,
                context.getResources().getDisplayMetrics()
        );
        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT,
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
