package de.b3ttertogeth3r.walhalla.utils;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.StorageReference;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.firebase.Firestore;
import de.b3ttertogeth3r.walhalla.firebase.Storage;
import de.b3ttertogeth3r.walhalla.interfaces.MyCompleteListener;
import de.b3ttertogeth3r.walhalla.models.Image;

public class MySliderAdapter extends SliderViewAdapter<MySliderAdapter.SliderAdapterVH> {
    private static final String TAG = "MySliderAdapter";
    protected final Context context;
    private List<String> mSliderItems = new ArrayList<>();

    public MySliderAdapter (Context context) {
        this.context = context;
    }

    public void renewItems (List<String> sliderItems) {
        this.mSliderItems = sliderItems;
        notifyDataSetChanged();
    }

    @Override
    public SliderAdapterVH onCreateViewHolder (ViewGroup parent) {
        View inflate =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder (SliderAdapterVH viewHolder, final int position) {
        String sliderItem = mSliderItems.get(position);

        Firestore.getImage(sliderItem, new MyCompleteListener<Image>() {
            @Override
            public void onSuccess (Image image) {
                if (image != null) {
                    viewHolder.textViewDescription.setText(image.getDescription());
                    StorageReference ref = Storage.downloadImage(image.getLarge_path());
                    Glide.with(viewHolder.itemView)
                            .load(ref)
                            .fitCenter()
                            .into(viewHolder.imageViewBackground);
                } else {
                    Log.e(TAG, "onSuccess: image == null");
                }
            }

            @Override
            public void onFailure (Exception exception) {
                Log.e(MySliderAdapter.TAG, "onFailure: ", exception);
            }
        });
    }

    @Override
    public int getCount () {
        //slider view count could be dynamic size
        return mSliderItems.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        ImageView imageGifContainer;
        TextView textViewDescription;

        public SliderAdapterVH (View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            imageGifContainer = itemView.findViewById(R.id.iv_gif_container);
            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
            textViewDescription.setTextSize(16);
            textViewDescription.setGravity(Gravity.CENTER);
            textViewDescription.setTextColor(ContextCompat.getColor(context, R.color.colorAccentLight));
            this.itemView = itemView;
        }
    }
}
