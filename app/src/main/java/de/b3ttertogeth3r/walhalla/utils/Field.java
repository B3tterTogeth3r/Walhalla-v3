package de.b3ttertogeth3r.walhalla.utils;

import static de.b3ttertogeth3r.walhalla.enums.Design.TABLE_TITLE;

import android.content.Context;
import android.os.OperationCanceledException;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.annotations.NotNull;

import org.jetbrains.annotations.Contract;

import java.util.List;
import java.util.Map;

import de.b3ttertogeth3r.walhalla.MainActivity;
import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.annos.Designer;
import de.b3ttertogeth3r.walhalla.design.MyBulletItem;
import de.b3ttertogeth3r.walhalla.design.MyBulletListItem;
import de.b3ttertogeth3r.walhalla.design.MyButton;
import de.b3ttertogeth3r.walhalla.design.MySliderView;
import de.b3ttertogeth3r.walhalla.design.MySubtitle;
import de.b3ttertogeth3r.walhalla.design.MySubtitle2;
import de.b3ttertogeth3r.walhalla.design.MyTable;
import de.b3ttertogeth3r.walhalla.design.MyTextView;
import de.b3ttertogeth3r.walhalla.design.MyTitle;
import de.b3ttertogeth3r.walhalla.enums.Design;
import de.b3ttertogeth3r.walhalla.firebase.Crashlytics;
import de.b3ttertogeth3r.walhalla.firebase.Firestore;
import de.b3ttertogeth3r.walhalla.firebase.Storage;
import de.b3ttertogeth3r.walhalla.models.Image;
import de.b3ttertogeth3r.walhalla.models.Paragraph;

/**
 * @author B3tterTogeth3r
 * @version 1.0
 * @see LayoutInflater
 * @see Map
 * @see Design
 * @since 1.0
 */
public class Field {
    private static final String TAG = "Field";
    private final Context context;
    private Paragraph paragraph;
    private List<Paragraph> paragraphList;

    public Field (Context context, Paragraph paragraph) {
        this.paragraph = paragraph;
        this.context = context;
    }

    public Field (Context context, List<Paragraph> paragraphList) {
        this.context = context;
        this.paragraphList = paragraphList;
    }

    public View design (@Designer Design design) {
        try {
            switch (design) {
                case TEXT:
                    return text();
                case IMAGE:
                    return image();
                case TABLE:
                    return table();
                case TITLE:
                    return title();
                case BUTTON:
                    return button();
                case SUBTITLE1:
                    return subtitle1();
                case SUBTITLE2:
                    return subtitle2();
                case LIST_BULLET:
                    return listBullet();
                case LIST_CHECKED:
                    return listChecked();
                case LINK:
                    return link();
                case BLOCK:
                    return block();
                default:
                    return null;
            }
        } catch (Exception e) {
            Crashlytics.log(TAG, "design error", e);
            return null;
        }
    }

    /**
     * The pre-checked object has a key called {@code text}, which should be formatted in this way:
     * <p>
     * {@code {text: (String)value}}
     * <p>
     * Displays a textview the the formatted style for a text.
     *
     * @return the created view.
     * @see TextView
     * @since 1.0
     */
    @NonNull
    private View text () {
        LinearLayout layout = new LinearLayout(context);
        layout.removeAllViewsInLayout();
        layout.removeAllViews();
        for (String s : paragraph.getValue()) {
            TextView text = new MyTextView(context);
            text.setText(s);
            layout.addView(text);
        }
        return layout;
    }

    /**
     * in a slide show show all the images. the images uid are saved in the array of the {@link
     * #paragraph}, so the function has to load the image from the "Images" collection, where the
     * "isVisible" boolean is "true".
     *
     * @return the created view
     * @throws OperationCanceledException
     *         If given object list is not formatted properly
     * @see Image
     * @see Firestore Firebase Cloud Firestore
     * @see Storage Firebase Storage
     * @see <a href="https://github.com/smarteist/Android-Image-Slider">Android Image Slide</a>
     * @since 1.0
     */
    @NonNull
    @Contract(" -> new")
    private View image () {
        // ImageSlideView
        // The values is a list of uids of the images in Firestore / Images / uid
        Log.e(TAG, "image: " + paragraph.getValue());
        MySliderView sliderView = new MySliderView(context);
        sliderView.design(paragraph.getValue());
        return sliderView;
    }

    /**
     * create design and format all its values
     *
     * @return the created view.
     * @since 1.0
     */
    @NonNull
    private View table () {
        HorizontalScrollView hcv = new HorizontalScrollView(context);
        TableLayout table = new TableLayout(context);
        table.removeAllViews();
        table.removeAllViewsInLayout();
        for (Paragraph p : paragraphList) {
            TableRow row = new TableRow(context);
            row.setBackground(ContextCompat.getDrawable(context, R.drawable.border_bottom_black));
            for (String s : p.getValue()) {
                TextView title = new TextView(context);
                int padding = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        8f,
                        context.getResources().getDisplayMetrics()
                );
                if (p.getKind().equals(TABLE_TITLE.toString())) {
                    title.setPadding(/*left*/padding,
                            /*top*/padding,
                            /*right*/padding*2,
                            /*bottom*/padding);
                    title.setTextAppearance(R.style.TextAppearance_AppCompat_Subhead);
                } else {
                    title.setPadding(padding, padding, padding, padding);
                    title.setTextAppearance(R.style.TextAppearance_AppCompat_Body1);
                    title.setGravity(Gravity.START);
                }
                title.setText(s);
                row.addView(title);
            }
            table.addView(row);
        }
        hcv.addView(table);
        return hcv;
    }

    /**
     * The pre-checked object has a key called {@code title}, which should be formatted in this way:
     * <p>
     * {@code {title: "value"}}
     * <p>
     * Displays a textview the the formatted style for a title.
     *
     * @return the created view.
     * @since 1.0
     */
    @NonNull
    private View title () {
        MyTitle title = new MyTitle(context);
        title.setText(paragraph.getValue().get(0));
        return title;
    }

    /**
     * create design and format all its values
     *
     * @return The created view.
     * @see Button
     * @see ImageButton
     * @since 1.0
     */
    @NonNull
    private View button () throws OperationCanceledException {
        MyButton button = new MyButton(context);
        button.setText(paragraph.getValue().get(0));
        int padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                16f,
                context.getResources().getDisplayMetrics()
        );
        button.setPadding(padding, padding, padding, padding);
        button.setOnClickListener(v -> MainActivity.externalListener.email());
        return button;
    }

    /**
     * The pre-checked object has a key called {@code subtitle1}, which should be formatted in this
     * way:
     * <p>
     * {@code {subtitle1: "value"}}
     * <p>
     * Displays a textview the the formatted style for a first subtitle.
     *
     * @return the created view.
     * @since 1.0
     */
    @NonNull
    @NotNull
    private View subtitle1 () {
        TextView sub1 = new MySubtitle(context);
        sub1.setText(paragraph.getValue().get(0));
        return sub1;

    }

    /**
     * The pre-checked object has a key called {@code subtitle2}, which should be formatted in this
     * way:
     * <p>
     * {@code {subtitle2: "value"}}
     * <p>
     * Displays a textview the the formatted style for a second subtitle.
     *
     * @return the created view.
     * @since 1.0
     */
    @NonNull
    private View subtitle2 () {
        TextView sub2 = new MySubtitle2(context);
        sub2.setText(paragraph.getValue().get(0));
        return sub2;

    }

    /**
     * The pre-checked object has a key called {@code list_bullet}, which should be formatted in
     * this way:
     * <p>
     * {@code {list_bullet: (List<String>)"values"}}
     * <p>
     * create the design and its inner functions
     *
     * @return the created view.
     * @since 1.0
     */
    @NonNull
    private View listBullet () throws OperationCanceledException {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        List<String> list = paragraph.getValue();
        MyBulletListItem item;
        for (String text : list) {
            item = new MyBulletListItem(context);
            item.setText(text);
            layout.addView(item);
        }
        return layout;

    }

    /**
     * The pre-checked object has a key called {@code listChecked}, which should be formatted in
     * this way:
     * {@code {listChecked: (List<String>)"values"}}
     * <p>
     * create the design and its inner functions
     *
     * @return the created view.
     * @since 1.0
     */
    @NonNull
    private View listChecked () throws OperationCanceledException {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        List<String> list = paragraph.getValue();
        MyBulletItem item;
        for (String text : list) {
            item = new MyBulletItem(context);
            item.setText(text);
            layout.addView(item);
        }
        return layout;
    }

    @NonNull
    private View link () {
        List<String> values = paragraph.getValue();
        String link = "";
        String name = "";
        for (String s : values) {
            if (s.contains("://")) {
                link = s;
            } else {
                name = s;
            }
        }
        MyButton button = new MyButton(context);
        button.setText(name);
        String finalLink = link;
        button.setOnClickListener(v -> {
            if (finalLink.isEmpty()) {
                MainActivity.externalListener.browser();
            } else {
                MainActivity.externalListener.browser(finalLink);
            }
        });
        int padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                16f,
                context.getResources().getDisplayMetrics()
        );
        button.setPadding(padding, padding, padding, padding);
        // button.setTextColor(ContextCompat.getColor(context, R.color.white));

        return button;
    }

    @NonNull
    private View block () {
        List<String> values = paragraph.getValue();
        MyTable table = new MyTable(context);
        MySubtitle title = new MySubtitle(context);
        title.setText(values.get(0));
        table.addView(title);
        try {
            MyTextView area = new MyTextView(context);
            area.setText(values.get(1));
            area.setPadding(area.getPaddingLeft(), 0, 0, 0);
            table.addView(area);
        } catch (Exception ignored) {
        }
        try {
            MyTextView distance = new MyTextView(context);
            distance.setText(values.get(2));
            distance.setPadding(distance.getPaddingLeft(), 0, 0, 0);
            table.addView(distance);
        } catch (Exception ignored) {
        }

        return table;
    }

}
