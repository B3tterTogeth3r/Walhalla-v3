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
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.StorageReference;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.old.firebase.Storage;


public class MyImageButton extends RelativeLayout {
    private static final String TAG = "MyImageButton";
    private final Context context;
    private final ImageView image;
    private final MyTitle text;

    public MyImageButton (Context context) {
        super(context);
        this.context = context;
        int padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                16f,
                context.getResources().getDisplayMetrics()
        );
        setBackground(ContextCompat.getDrawable(context, R.drawable.background_button));
        setPadding(padding, padding, padding, padding);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                (10 * padding));
        setLayoutParams(params);
        image = new ImageView(context);
        image.setId(R.id.image_view);
        image.setLayoutParams(params);

        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        image.setLayoutParams(imageParams);

        text = new MyTitle(context);
        RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
        );
        textParams.addRule(ALIGN_PARENT_BOTTOM);
        text.setLayoutParams(textParams);
        text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        text.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));

        this.addView(image);
        this.addView(text);
    }

    public MyImageButton setImageDrawable (Drawable drawable) {
        Glide.with(context)
                .load(drawable)
                .fitCenter()
                .into(image);
        return this;
    }

    public MyImageButton setImageBitmap(Bitmap bm){
        Glide.with(context)
                .load(bm)
                .fitCenter()
                .into(image);
        return this;
    }

    public MyImageButton setText(CharSequence text){
        this.text.setText(text);
        this.text.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.background_overlay));
        return this;
    }

    public MyImageButton setText(int resId){
        setText(context.getString(resId));
        text.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.background_overlay));
        return this;
    }

    public ImageView getImageView () {
        return image;
    }

    public MyImageButton setImage (String referencePath) {
        if (referencePath != null && !referencePath.isEmpty()) {
            StorageReference ref = Storage.downloadImage(referencePath);
            Glide.with(this)
                    .load(ref)
                    .fitCenter()
                    .into(this.image);
        }
        return this;
    }
}
