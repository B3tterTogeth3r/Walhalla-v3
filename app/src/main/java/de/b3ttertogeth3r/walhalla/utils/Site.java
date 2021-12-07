package de.b3ttertogeth3r.walhalla.utils;

import static de.b3ttertogeth3r.walhalla.enums.Design.BUTTON;
import static de.b3ttertogeth3r.walhalla.enums.Design.IMAGE;
import static de.b3ttertogeth3r.walhalla.enums.Design.LIST_BULLET;
import static de.b3ttertogeth3r.walhalla.enums.Design.LIST_CHECKED;
import static de.b3ttertogeth3r.walhalla.enums.Design.SUBTITLE1;
import static de.b3ttertogeth3r.walhalla.enums.Design.SUBTITLE2;
import static de.b3ttertogeth3r.walhalla.enums.Design.TABLE_TITLE;
import static de.b3ttertogeth3r.walhalla.enums.Design.TEXT;
import static de.b3ttertogeth3r.walhalla.enums.Design.TITLE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.OperationCanceledException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import de.b3ttertogeth3r.walhalla.App;
import de.b3ttertogeth3r.walhalla.enums.Display;
import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.annos.EditSave;
import de.b3ttertogeth3r.walhalla.exceptions.SiteNotActiveException;
import de.b3ttertogeth3r.walhalla.firebase.Firebase;
import de.b3ttertogeth3r.walhalla.interfaces.Reload;
import de.b3ttertogeth3r.walhalla.models.Paragraph;

/**
 * This class formats a list of data into a nicely designed layout.
 *
 * @author B3tterTogeth3r
 * @version 1.0
 * @see <a href="https://github.com/denzcoskun/ImageSlideshow">Image Slide Show</a>
 * @since 1.0
 */
@SuppressLint("InflateParams")
public class Site implements Reload {
    private static final String TAG = "Site";
    /**
     * <ol>
     *     <li value=0>Design.TEXT</li>
     *     <li>DESIGN.TITLE</li>
     *     <li>DESIGN.SUBTITLE1</li>
     *     <li>DESIGN.SUBTITLE2</li>
     *     <li>DESIGN.BUTTON</li>
     *     <li>DESIGN.IMAGE</li>
     *     <li>DESIGN.TABLE</li>
     * </ol>
     */
    private final static Button[] button = new Button[7];
    public static Reload reload;
    private final LayoutInflater inflater;
    private final Context context;
    private final LinearLayout layout;
    private final ArrayList<ArrayList<Paragraph>> contentList;

    /**
     * @param context
     *         Context of the view it is displayed on
     * @param layout
     *         LinearLayout to put the content in
     * @param contentList
     *         Map<String, Object> List of data to display
     * @param display
     *         select one from {@link EditSave}
     */
    public Site (@NotNull Context context, @NonNull @NotNull LinearLayout layout,
                 @NotNull ArrayList<ArrayList<Paragraph>> contentList)
                    throws SiteNotActiveException {
        try {
            layout.removeAllViews();
            this.context = context;
            this.layout = layout;
            this.contentList = contentList;
            reload = this;
            this.inflater = LayoutInflater.from(context);
            for (List<Paragraph> one : contentList) {
                layout.addView(display(one));
            }
        } catch (Exception exception) {
            throw new SiteNotActiveException();
        }
    }

    @NonNull
    private View display (@NotNull List<Paragraph> paragraph) {
        Log.d(TAG, "display: creating view");
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.custom_linear, null);
        layout.removeAllViewsInLayout();
        try {
            int size = paragraph.size();
            for (int i = 0; i < size; i++) {
                Paragraph line = paragraph.get(i);
                if (line == null) {
                    Firebase.Crashlytics.log("Object line == null");
                } else {
                    de.b3ttertogeth3r.walhalla.utils.Field field = new de.b3ttertogeth3r.walhalla.utils.Field(inflater, line);
                    findValues(layout, line, field);
                }
            }
        } catch (Exception e) {
            Firebase.Crashlytics.log(TAG, "Site isn't active anymore.", e);
        }
        return layout;
    }

    private void findValues (LinearLayout layout, @NonNull Paragraph paragraph,
                             @NonNull de.b3ttertogeth3r.walhalla.utils.Field field) throws OperationCanceledException {
        if (paragraph.getKind().equals(TEXT.toString())) {
            layout.addView(field.design(TEXT));
        } else if (paragraph.getKind().equals(TITLE.toString())) {
            layout.addView(field.design(TITLE));
        } else if (paragraph.getKind().equals(SUBTITLE1.toString())) {
            layout.addView(field.design(SUBTITLE1));
        } else if (paragraph.getKind().equals(SUBTITLE2.toString())) {
            layout.addView(field.design(SUBTITLE2));
        } else if (paragraph.getKind().equals(BUTTON.toString())) {
            layout.addView(field.design(BUTTON));
        } else if (paragraph.getKind().equals(TABLE_TITLE.toString())) {
            layout.addView(field.design(TABLE_TITLE));
        } else if (paragraph.getKind().equals(IMAGE.toString())) {
            layout.addView(field.design(IMAGE));
        } else if (paragraph.getKind().equals(LIST_CHECKED.toString())) {
            layout.addView(field.design(LIST_CHECKED));
        } else if (paragraph.getKind().equals(LIST_BULLET.toString())) {
            layout.addView(field.design(LIST_BULLET));
        }
    }

    @Override
    public void site () {
        try {
            new Site(context, layout, contentList);
        } catch (SiteNotActiveException exception) {
            exception.printStackTrace();
        }
    }
}
