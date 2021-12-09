package de.b3ttertogeth3r.walhalla.design;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;

import com.smarteist.autoimageslider.IndicatorView.PageIndicatorView;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.List;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.utils.MySliderAdapter;

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
        setPageIndicatorView(new MyPageIndicatorView(context));

        int padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                8f,
                context.getResources().getDisplayMetrics()
        );
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                (padding / 8 * 300));
        setPadding(padding, padding, padding, padding);
        setLayoutParams(params);

        setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using
        // IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE
        // or SCALE_DOWN or SLIDE and SWAP!!
        setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        setIndicatorEnabled(true);
        setIndicatorVisibility(true);
        setIndicatorSelectedColor(context.getColor(R.color.colorAccent));
        setIndicatorUnselectedColor(context.getColor(R.color.colorPrimaryDark));
        setScrollTimeInSec(10); //set scroll delay in seconds

        setSliderAdapter(adapter, true);

        startAutoCycle();
    }

}
