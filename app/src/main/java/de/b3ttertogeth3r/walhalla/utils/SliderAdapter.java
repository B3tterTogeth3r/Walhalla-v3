package de.b3ttertogeth3r.walhalla.utils;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.firebase.Firebase;
import de.b3ttertogeth3r.walhalla.models.Image;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.Holder> {
    private static final String TAG = "SliderAdapter";
    List<String> itemList;

    public SliderAdapter (List<String> itemList) {
        this.itemList = itemList;
    }

    @Override
    public Holder onCreateViewHolder (@NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item, null);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder (Holder viewHolder, int position) {
        String link = itemList.get(position);
        Firebase.FIRESTORE.document("Images/" + link)
                .get().addOnSuccessListener(documentSnapshot -> {
            if (!documentSnapshot.exists()) {
                Log.d(TAG, "onBindViewHolder: image with uid " + link + " not found.");
            }
            try {
                Image image = documentSnapshot.toObject(Image.class);
                image.setId(documentSnapshot.getId());
                //Log.d(TAG, "image: " + image.getLarge_path());
                Glide.with(viewHolder.imageView)
                        .load(image.getLarge_link())
                        .fitCenter()
                        .into(viewHolder.imageView);
            } catch (Exception exception) {
                Log.e(TAG, "onBindViewHolder: download exception", exception);
            }
        });
    }

    @Override
    public int getCount () {
        return itemList.size();
    }

    public static class Holder extends SliderViewAdapter.ViewHolder {
        private static final String TAG = "Holder";
        ImageView imageView;

        public Holder (View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
        }

    }
}
