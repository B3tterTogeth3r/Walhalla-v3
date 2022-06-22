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
import android.util.TypedValue;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;

import de.b3ttertogeth3r.walhalla.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfileImage extends CircleImageView {

    public MyProfileImage (Context context) {
        super(context);
        int padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                1f,
                context.getResources().getDisplayMetrics()
        );
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                (padding * 70),
                (padding * 70)
        );
        setLayoutParams(params);
        setBorderOverlay(false);
        setBorderColor(ContextCompat.getColor(context, R.color.black));
        setBorderWidth(padding);
        setImageDrawable(ContextCompat.getDrawable(context, R.drawable.wappen_2017_v2));
        setCircleBackgroundColor(ContextCompat.getColor(context, R.color.black));
    }
}
