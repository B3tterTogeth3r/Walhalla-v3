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
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import com.smarteist.autoimageslider.IndicatorView.PageIndicatorView;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.IndicatorView.draw.data.Orientation;

import de.b3ttertogeth3r.walhalla.R;

public class MyPageIndicatorView extends PageIndicatorView {
    private static final String TAG = "MyPageIndicatorView";
    private final Context context;

    public MyPageIndicatorView (Context context) {
        super(context);
        this.context = context;
        design();
    }

    public MyPageIndicatorView (Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        design();
    }

    public MyPageIndicatorView (Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        design();
    }

    public MyPageIndicatorView (Context context, AttributeSet attrs, int defStyleAttr,
                                int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        design();
    }
    private void design() {
        setVisibility(View.VISIBLE);
        setOrientation(Orientation.HORIZONTAL);
        setSelectedColor(context.getColor(R.color.colorAccent));
        setUnselectedColor(context.getColor(R.color.colorPrimaryDark));
        setEnabled(true);
        setAutoVisibility(true);
        setAnimationType(IndicatorAnimationType.SLIDE);
        setAnimationDuration(2);
        invalidate();
        setForegroundGravity(Gravity.BOTTOM);
    }




}
