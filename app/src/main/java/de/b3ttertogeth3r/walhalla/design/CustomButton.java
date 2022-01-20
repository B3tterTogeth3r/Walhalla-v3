package de.b3ttertogeth3r.walhalla.design;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import de.b3ttertogeth3r.walhalla.R;

public class CustomButton extends RelativeLayout {
    private final MySubtitle text;
    private final ImageView image;

    public CustomButton (@NonNull Builder builder) {
        super(builder.context);
        text = new MySubtitle(builder.context);
        image = new ImageView(builder.context);
        int padding = Builder.oneDP * 8;
        LayoutParams params = new LayoutParams(
                builder.width,
                builder.height
        );
        params.setMargins(builder.marginLeft, builder.marginTop, builder.marginRight,
                builder.marginBottom);
        setLayoutParams(params);
        setPadding(builder.paddingLeft, builder.paddingTop, builder.paddingRight, builder.paddingBottom);
        setBackground(ContextCompat.getDrawable(builder.context, R.drawable.background_button));

        if(!builder.text.toString().isEmpty()) {
            RelativeLayout.LayoutParams textParams = new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT
            );
            textParams.addRule(CENTER_VERTICAL);
            text.setGravity(Gravity.CENTER);
            text.setId(R.id.text);
            addView(text, textParams);
        }
        if(builder.drawable != null) {
            LayoutParams imageParams = new LayoutParams(padding, padding);
            imageParams.addRule(ABOVE, text.getId());
            image.setVisibility(View.VISIBLE);
            addView(image, imageParams);
        }
    }

    public static class Builder {
        //region values
        /**
         * Special value for the height or width requested by a View.
         * WRAP_CONTENT means that the view wants to be just large enough to fit
         * its own internal content, taking its own padding into account.
         */
        public static final int WRAP_CONTENT = -2;
        /**
         * Special value for the height or width requested by a View.
         * MATCH_PARENT means that the view wants to be as big as its parent,
         * minus the parent's padding, if any. Introduced in API Level 8.
         */
        public static final int MATCH_PARENT = -1;
        public static int oneDP;
        private Context context;
        private int width, height;
        private int marginTop, marginLeft, marginBottom, marginRight;
        private int paddingTop, paddingLeft, paddingBottom, paddingRight;
        private Drawable drawable;
        private CharSequence text;
        private int textColor;
        private boolean enabled;
        //endregion

        public Builder (@NonNull Context context) {
            this.context = context;
            this.height = WRAP_CONTENT;
            this.width = MATCH_PARENT;
            oneDP = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    1f,
                    context.getResources().getDisplayMetrics()
            );
        }

        //region setter

        /**
         * @param height
         *         the height, either {@link #WRAP_CONTENT},
         *         or {@link #MATCH_PARENT} or a fixed size in pixels
         */
        public Builder setHeight (int height) {
            this.height = height;
            return this;
        }

        /**
         * @param width
         *         the width, either {@link #WRAP_CONTENT},
         *         {@link #MATCH_PARENT}, or a fixed size in pixels
         */
        public Builder setWidth (int width) {
            this.width = width;
            return this;
        }

        /**
         * Sets the margins, in pixels. Left and right margins may be
         * overridden by {@link android.view.View#requestLayout()} depending on layout direction.
         * Margin values are set positive.
         *
         * @param left
         *         the left margin size
         * @param top
         *         the top margin size
         * @param right
         *         the right margin size
         * @param bottom
         *         the bottom margin size
         * @return this Builder to continue building
         */
        public Builder setMargin (int top, int left, int bottom, int right) {
            this.marginTop = Math.abs(top);
            this.marginBottom = Math.abs(bottom);
            this.marginRight = Math.abs(right);
            this.marginLeft = Math.abs(left);
            return this;
        }

        /**
         * Sets the padding.
         *
         * @param left
         *         the left padding in pixels
         * @param top
         *         the top padding in pixels
         * @param right
         *         the right padding in pixels
         * @param bottom
         *         the bottom padding in pixels
         * @return this Builder to continue building
         */
        public Builder setPadding (int top, int left, int bottom, int right) {
            this.paddingTop = Math.abs(top);
            this.paddingRight = Math.abs(right);
            this.paddingLeft = Math.abs(left);
            this.paddingBottom = Math.abs(bottom);
            return this;
        }

        public Builder setIconDrawable (Drawable drawable) {
            this.drawable = drawable;
            return this;
        }

        public Builder setIconRes (int resId) {
            this.drawable = ContextCompat.getDrawable(context, resId);
            return this;
        }

        public Builder setText (CharSequence text) {
            this.text = text;
            this.textColor = ContextCompat.getColor(context, R.color.background);
            return this;
        }

        public Builder setText (int resId) {
            this.text = context.getString(resId);
            this.textColor = ContextCompat.getColor(context, R.color.background);
            return this;
        }

        public Builder setEnabled (boolean enabled) {
            this.enabled = enabled;
            return this;
        }
        //endregion

        public CustomButton build () {
            return new CustomButton(this);
        }
    }
}
