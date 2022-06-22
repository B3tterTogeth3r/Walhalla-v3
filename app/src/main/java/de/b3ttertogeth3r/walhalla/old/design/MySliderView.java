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
import android.util.TypedValue;
import android.view.ViewGroup;

import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.List;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.old.utils.MySliderAdapter;

public class MySliderView extends SliderView {
    private final Context context;

    public MySliderView (Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public MySliderView (Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public MySliderView (Context context) {
        super(context);
        this.context = context;
    }

    public void design (List<String> list) {
        MySliderAdapter adapter = new MySliderAdapter(context);
        adapter.renewItems(list);

        int padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                8f,
                context.getResources().getDisplayMetrics()
        );
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                (padding / 8 * 300));
        setPadding(padding, padding, padding, padding);
        setLayoutParams(params);

        // setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using
        // IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE
        // or SCALE_DOWN or SLIDE and SWAP!!
        setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        setIndicatorEnabled(true);
        setIndicatorVisibility(true);
        setIndicatorSelectedColor(context.getColor(R.color.colorAccentLight));
        setIndicatorUnselectedColor(context.getColor(R.color.colorPrimaryDark));
        setSliderAdapter(adapter, true);
        setScrollTimeInSec(10); //set scroll delay in seconds
    }
}
