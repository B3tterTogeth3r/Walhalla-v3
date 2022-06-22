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

package de.b3ttertogeth3r.walhalla.old.utils;

import static de.b3ttertogeth3r.walhalla.old.enums.Design.BLOCK;
import static de.b3ttertogeth3r.walhalla.old.enums.Design.BUTTON;
import static de.b3ttertogeth3r.walhalla.old.enums.Design.IMAGE;
import static de.b3ttertogeth3r.walhalla.old.enums.Design.LINK;
import static de.b3ttertogeth3r.walhalla.old.enums.Design.LIST_BULLET;
import static de.b3ttertogeth3r.walhalla.old.enums.Design.LIST_CHECKED;
import static de.b3ttertogeth3r.walhalla.old.enums.Design.SUBTITLE1;
import static de.b3ttertogeth3r.walhalla.old.enums.Design.SUBTITLE2;
import static de.b3ttertogeth3r.walhalla.old.enums.Design.TABLE;
import static de.b3ttertogeth3r.walhalla.old.enums.Design.TEXT;
import static de.b3ttertogeth3r.walhalla.old.enums.Design.TITLE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import de.b3ttertogeth3r.walhalla.old.design.MyLinearLayout;
import de.b3ttertogeth3r.walhalla.old.firebase.Crashlytics;
import de.b3ttertogeth3r.walhalla.old.interfaces.Reload;
import de.b3ttertogeth3r.walhalla.old.models.Paragraph;

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
    public static Reload reload;
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
     */
    public Site (@NotNull Context context, @NonNull @NotNull LinearLayout layout,
                 @NonNull @NotNull ArrayList<ArrayList<Paragraph>> contentList) {
        layout.removeAllViews();
        this.context = context;
        this.layout = layout;
        this.contentList = contentList;
        reload = this;
        for (List<Paragraph> one : contentList) {
            layout.addView(display(one));
        }
    }

    @NonNull
    private View display (@NotNull List<Paragraph> paragraph) {
        Log.d(TAG, "display: creating paragraph");
        LinearLayout layout = new MyLinearLayout(context);
        layout.removeAllViewsInLayout();
        try {
            int size = paragraph.size();
            for (int i = 0; i < size; i++) {
                Paragraph line = paragraph.get(i);
                if (line == null) {
                    Crashlytics.error(TAG, "Object line == null");
                } else {
                    if (line.getKind().startsWith("table_")) {
                        List<Paragraph> tableList = new ArrayList<>();
                        try {
                            do {
                                tableList.add(line);
                                i++;
                                line = paragraph.get(i);
                            } while (line.getKind().startsWith("table_"));
                        } catch (Exception ignored) {
                        }
                        Field field = new Field(context, tableList);
                        addTable(layout, field);
                    } else {
                        Field field =
                                new Field(context, line);
                        findValues(layout, line, field);
                    }
                    //TODO add advert after every paragraph except the last
                }
            }
        } catch (Exception e) {
            Crashlytics.error(TAG, "Site isn't active anymore.", e);
        }
        return layout;
    }

    private void addTable (@NonNull LinearLayout layout, @NonNull Field field) {
        layout.addView(field.design(TABLE));
    }

    private void findValues (LinearLayout layout, @NonNull Paragraph paragraph,
                             @NonNull Field field) {
        try {
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
            } else if (paragraph.getKind().equals(TABLE.toString())) {
                layout.addView(field.design(TABLE));
            } else if (paragraph.getKind().equals(IMAGE.toString())) {
                layout.addView(field.design(IMAGE));
            } else if (paragraph.getKind().equals(LIST_CHECKED.toString())) {
                layout.addView(field.design(LIST_CHECKED));
            } else if (paragraph.getKind().equals(LIST_BULLET.toString())) {
                layout.addView(field.design(LIST_BULLET));
            } else if (paragraph.getKind().equals(LINK.toString())) {
                layout.addView(field.design(LINK));
            } else if (paragraph.getKind().equals(BLOCK.toString())) {
                layout.addView(field.design(BLOCK));
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public void site () {
        new Site(context, layout, contentList);
    }
}
