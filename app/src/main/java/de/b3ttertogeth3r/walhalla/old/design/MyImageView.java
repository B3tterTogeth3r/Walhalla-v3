/*
 * Copyright (c) 2022.
 *
 * Licensed under the Apace License, Version 2.0 (the "Licence"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 *  License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 *  either express or implied. See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package de.b3ttertogeth3r.walhalla.old.design;

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

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.StorageReference;

import de.b3ttertogeth3r.walhalla.R;

public class MyImageView extends RelativeLayout {
    private static final String TAG = "MyImageView";
    private Context context;
    private ImageView image;
    private TextView description;

    public MyImageView(Context context){
        super(context);
        int padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                16f,
                context.getResources().getDisplayMetrics()
        );
        design(context, ViewGroup.LayoutParams.MATCH_PARENT, (10 * padding));
    }

    private void design (@NonNull Context context, int width, int height) {
        int padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                16f,
                context.getResources().getDisplayMetrics()
        );
        this.context = context;
        this.image = new ImageView(context);
        this.description = new MySubtitle(context);

        if(height == 0) {
            height = (10*padding);
        }
        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
                /* width */ width,
                /* height */ height
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

    public MyImageView (Context context, int width, int height) {
        super(context);
        int padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                16f,
                context.getResources().getDisplayMetrics()
        );
        design(context, width, height);
    }

    public MyImageView setDescription (int resId) {
        description.setBackground(ContextCompat.getDrawable(context,
                R.drawable.background_overlay));
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
        image(drawable);
        return this;
    }

    public MyImageView setImage (Bitmap bm) {
        image(bm);
        return this;
    }

    public MyImageView setImage (Uri uri) {
        image(uri);
        return this;
    }

    public MyImageView setImage (int resId) {
        image(resId);
        return this;
    }

    public MyImageView setImage (StorageReference ref) {
        image(ref);
        return this;
    }

    private void image(Object load){
        Glide.with(this)
                .load(load)
                .fitCenter()
                .placeholder(R.drawable.wappen_2017)
                .into(image);
    }

    public void setWidth (int buttonWidth) {
        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = buttonWidth;
        // image.setMaxWidth(buttonWidth);
        setLayoutParams(params);
    }
}
