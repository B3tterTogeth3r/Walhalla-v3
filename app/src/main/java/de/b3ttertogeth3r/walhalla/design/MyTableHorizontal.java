package de.b3ttertogeth3r.walhalla.design;

import android.content.Context;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TableLayout;

import androidx.core.content.ContextCompat;

import de.b3ttertogeth3r.walhalla.R;

public class MyTableHorizontal extends HorizontalScrollView {
    public final TableLayout layout;

    public MyTableHorizontal (Context context) {
        super(context);
        removeAllViews();
        removeAllViewsInLayout();
        layout = new TableLayout(context);
        layout.removeAllViews();
        layout.removeAllViewsInLayout();
        layout.setId(R.id.table);
        layout.isColumnStretchable(0);
        layout.isColumnShrinkable(0);
        addView(layout);

        int padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                1f,
                context.getResources().getDisplayMetrics()
        );
        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        setPadding(/*left*/(padding * 8),
                /*top*/(padding * 8),
                /*right*/(padding * 8),
                /*bottom*/(padding * 8));
        setLayoutParams(params);
        setBackground(ContextCompat.getDrawable(context, R.drawable.border_round));
    }
}
