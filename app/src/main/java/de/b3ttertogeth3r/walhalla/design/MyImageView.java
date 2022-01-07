package de.b3ttertogeth3r.walhalla.design;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.StorageReference;

import de.b3ttertogeth3r.walhalla.R;

public class MyImageView extends RelativeLayout {
    private static final String TAG = "MyImageView";
    private final Context context;
    private final ImageView image;
    private final TextView description;

    public MyImageView (Context context) {
        super(context);
        int padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                16f,
                context.getResources().getDisplayMetrics()
        );
        this.context = context;
        this.image = new ImageView(context);
        this.description = new MySubtitle(context);

        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
                /* width */ ViewGroup.LayoutParams.MATCH_PARENT,
                /* height */ (10 * padding)
        );
        imageParams.addRule(Gravity.CENTER_HORIZONTAL);
        imageParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        setLayoutParams(imageParams);

        addView(image, imageParams);

        RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(
                image.getWidth(),
                LayoutParams.WRAP_CONTENT
        );
        description.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        description.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        addView(description, textParams);
        setBackground(ContextCompat.getDrawable(getContext(), R.drawable.border_round));
    }

    public MyImageView setDescription (int resId) {
        setDescription(context.getString(resId));
        return this;
    }

    public MyImageView setDescription (CharSequence text) {
        description.setBackground(ContextCompat.getDrawable(context,
                R.drawable.background_overlay));
        description.setText(text);
        return this;
    }

    public MyImageView setImage (Drawable drawable) {
        image.setImageDrawable(drawable);
        return this;
    }

    public MyImageView setImage (Bitmap bm) {
        image.setImageBitmap(bm);
        return this;
    }

    public MyImageView setImage (Uri uri) {
        Glide.with(this)
                .load(uri)
                .fitCenter()
                .placeholder(R.drawable.wappen_2017)
                .into(image);
        return this;
    }

    public MyImageView setImage (int resId) {
        image.setImageDrawable(ContextCompat.getDrawable(context, resId));
        return this;
    }

    public MyImageView setImage (StorageReference ref) {
        Glide.with(this)
                .load(ref)
                .fitCenter()
                .placeholder(R.drawable.wappen_2017)
                .into(image);
        return this;
    }

}
