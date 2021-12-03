package de.b3ttertogeth3r.walhalla.design;

import android.app.AlertDialog;
import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.StorageReference;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.enums.Page;
import de.b3ttertogeth3r.walhalla.firebase.Firebase;
import de.b3ttertogeth3r.walhalla.models.Charge;
import de.b3ttertogeth3r.walhalla.utils.CacheData;

public class ChargenRow extends RelativeLayout implements View.OnClickListener {
    private static final String TAG = "ChargenRow";
    private final Charge charge;

    public ChargenRow (Context context, @NonNull Charge charge, @NonNull de.b3ttertogeth3r.walhalla.enums.Charge kind) {
        super(context);
        this.charge = charge;
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
        name.setPadding(0,0,0,0);
        name.setText(charge.getFullName());
        layout.addView(name);

        MyTextView major = new MyTextView(context);
        major.setPadding(0,0,0,0);
        major.setText(charge.getMajor());
        layout.addView(major);

        MyTextView PoB  = new MyTextView(context);
        PoB.setPadding(0,0,0,0);
        PoB.setText(charge.getPoB());
        layout.addView(PoB);

        String addressStr = charge.getAddressStr();
        if(!addressStr.isEmpty()){
            MyTextView address = new MyTextView(context);
            address.setPadding(0,0,0,0);
            address.setText(addressStr);
            layout.addView(address);
        }

        MyTextView mobile = new MyTextView(context);
        mobile.setText(charge.getMobile());
        mobile.setPadding(0,0,0,0);
        layout.addView(mobile);

        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
                padding, padding);
        imageParams.addRule(ALIGN_PARENT_END);

        ImageView image = new ImageView(context);
        image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.wappen_2017));

        this.addView(layout, linearParams);
        this.addView(image, imageParams);

        if(charge.getPicture_path() != null && !charge.getPicture_path().isEmpty()) {
            StorageReference imageRef = Firebase.Storage.downloadImage(charge.getPicture_path());
            Glide.with(this)
                    .load(imageRef)
                    .fitCenter()
                    .into(image);
        }
        setOnClickListener(this);
    }

    @Override
    public void onClick (View v) {
        //TODO Create a send message dialog to send a message to the charge
        MyToast toast = new MyToast(getContext());
        toast.setText(R.string.error_dev);
        toast.show();
    }
}
