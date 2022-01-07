package de.b3ttertogeth3r.walhalla.design;

import android.app.AlertDialog;
import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.StorageReference;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.firebase.Storage;
import de.b3ttertogeth3r.walhalla.models.Charge;
import de.b3ttertogeth3r.walhalla.utils.RemoteConfigData;

public class RowChargen extends RelativeLayout implements View.OnClickListener {
    private static final String TAG = "ChargenRow";
    private final Charge charge;
    private final de.b3ttertogeth3r.walhalla.enums.Charge kind;

    public RowChargen (Context context, @NonNull Charge charge,
                       @NonNull de.b3ttertogeth3r.walhalla.enums.Charge kind) {
        super(context);
        this.charge = charge;
        this.kind = kind;
        int padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                120f,
                context.getResources().getDisplayMetrics()
        );

        setBackground(ContextCompat.getDrawable(context, R.drawable.border_bottom_black));

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        setLayoutParams(params);
        MyLinearLayout layout = new MyLinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        RelativeLayout.LayoutParams linearParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        linearParams.addRule(ALIGN_PARENT_START);
        layout.setBackground(null);

        MyTitle title = new MyTitle(context);
        title.setText(kind.getName());
        layout.addView(title);

        MyTextView name = new MyTextView(context);
        name.setPadding(name.getPaddingLeft(), 0, 0, 0);
        name.setText(charge.getFullName());
        layout.addView(name);

        MyTextView major = new MyTextView(context);
        major.setPadding(major.getPaddingLeft(), 0, 0, 0);
        String majorStr = getContext().getString(R.string.major_short) + " " +charge.getMajor();
        major.setText(majorStr);
        layout.addView(major);

        MyTextView PoB = new MyTextView(context);
        PoB.setPadding(PoB.getPaddingLeft(), 0, 0, 0);
        String from = getContext().getString(R.string.from) + " " + charge.getPoB();
        PoB.setText(from);
        layout.addView(PoB);

        MyTextView mobile = new MyTextView(context);
        mobile.setText(charge.getMobile());
        mobile.setPadding(mobile.getPaddingLeft(), 0, 0, 0);
        layout.addView(mobile);

        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
                padding, padding);
        imageParams.addRule(ALIGN_PARENT_END);
        imageParams.setMargins(0, (padding / 12), (padding / 12), 0);

        RelativeLayout imageView = new RelativeLayout(context);
        imageView.setBackground(ContextCompat.getDrawable(context, R.drawable.border_round));
        ImageView image = new ImageView(context);
        image.setPadding(4,4,4,4);
        image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.wappen_2017));

        imageView.addView(image);
        this.addView(layout, linearParams);
        this.addView(imageView, imageParams);

        if (charge.getPicture_path() != null && !charge.getPicture_path().isEmpty()) {
            StorageReference imageRef = Storage.downloadImage(charge.getPicture_path());
            Glide.with(this)
                    .load(imageRef)
                    .placeholder(R.drawable.wappen_2017)
                    .centerCrop()
                    .into(image);
            image.setBackground(ContextCompat.getDrawable(context, R.drawable.border_round_clear));
        }
        setOnClickListener(this);
    }

    @Override
    public void onClick (View v) {
        // Create a dialog with a description of the board members job
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(kind.getName())
                .setPositiveButton(R.string.close, null);
        switch (kind) {
            case X:
                builder.setMessage(RemoteConfigData.chargen_description.get(0));
                break;
            case VX:
                builder.setMessage(RemoteConfigData.chargen_description.get(1));
                break;
            case FM:
                builder.setMessage(RemoteConfigData.chargen_description.get(2));
                break;
            case XX:
                builder.setMessage(RemoteConfigData.chargen_description.get(3));
                break;
            case XXX:
                builder.setMessage(RemoteConfigData.chargen_description.get(4));
                break;
        }
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
