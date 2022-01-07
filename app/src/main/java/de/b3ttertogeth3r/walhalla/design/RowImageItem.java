package de.b3ttertogeth3r.walhalla.design;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.StorageReference;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.firebase.Storage;

public class RowImageItem extends LinearLayout {
    private static final String TAG = "RowImageItem";
    private final TextView text;
    private final ImageView image;

    public RowImageItem (Context context) {
        super(context);
        int dp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 1f,
                context.getResources().getDisplayMetrics()
        );
        setOrientation(HORIZONTAL);
        text = new MyTextView(context);
        image = new ImageView(context);
        LinearLayout.LayoutParams params = new LayoutParams(
                50*dp,
                50*dp
        );
        setBackground(ContextCompat.getDrawable(context, R.drawable.border_bottom_gray));
        params.setMargins(5*dp,5*dp,5*dp,5*dp);
        addView(image, params);
        text.setPadding(0,0,0,0);
        params = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.CENTER_VERTICAL;
        addView(text, params);
    }

    public RowImageItem setText (CharSequence text) {
        this.text.setText(text);
        return this;
    }

    public RowImageItem setImage (String referencePath) {
        if (referencePath != null && !referencePath.isEmpty()) {
            StorageReference ref = Storage.downloadImage(referencePath);
            Glide.with(this)
                    .load(ref)
                    .fitCenter()
                    .into(image);
        }
        return this;
    }
}
