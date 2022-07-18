/*
 * Copyright (c) 2022-2022.
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

package de.b3ttertogeth3r.walhalla.design;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;

public class Image extends androidx.appcompat.widget.AppCompatImageView {
    private static final String TAG = "Image";
    private final Context context;

    public Image (Context context) {
        super(context);
        this.context = context;
    }

    public Image (Context context, int resId) {
        super(context);
        this.context = context;
        setImage(resId);
    }

    public Image (Context context, Drawable drawable) {
        super(context);
        this.context = context;
        setImage(drawable);
    }

    public Image (Context context, DocumentReference ref) {
        super(context);
        this.context = context;
        setImage(ref);
    }

    public Image (Context context, String refString) {
        super(context);
        this.context = context;
        //setImage(refString);
    }

    public Image setImage (int resId) {
        Glide.with(context)
                .load(resId)
                .fitCenter()
                .into(this);
        return this;
    }

    public Image setImage (DocumentReference ref) {
        Glide.with(context)
                .load(ref)
                .fitCenter()
                .into(this);
        return this;
    }

    public Image setImage (Drawable drawable) {
        Glide.with(context)
                .load(drawable)
                .fitCenter()
                .into(this);
        return this;
    }
}
