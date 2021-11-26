package de.b3ttertogeth3r.walhalla.utils;

import static de.b3ttertogeth3r.walhalla.enums.Design.BUTTON;
import static de.b3ttertogeth3r.walhalla.enums.Design.IMAGE;
import static de.b3ttertogeth3r.walhalla.enums.Design.LIST_BULLET;
import static de.b3ttertogeth3r.walhalla.enums.Design.LIST_CHECKED;
import static de.b3ttertogeth3r.walhalla.enums.Design.SUBTITLE1;
import static de.b3ttertogeth3r.walhalla.enums.Design.SUBTITLE2;
import static de.b3ttertogeth3r.walhalla.enums.Design.TABLE;
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
    private LayoutInflater inflater;
    private Context context;
    private LinearLayout layout;
    private ArrayList<ArrayList<Paragraph>> contentList;
    private Display display;

    /**
     * @param context
     *         Context of the view it is displayed on
     * @param layout
     *         LinearLayout to put the content in
     * @param contentList
     *         Map<String, Object> List of data to display
     */
    public Site (@NotNull Context context, @NotNull LinearLayout layout,
                 @NotNull ArrayList<ArrayList<Paragraph>> contentList) {
        this.context = context;
        this.layout = layout;
        this.contentList = contentList;
        this.display = Display.SHOW;
        reload = this;
        reload.site();
    }

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
                 @NotNull ArrayList<ArrayList<Paragraph>> contentList, @NonNull @EditSave Display display)
                    throws SiteNotActiveException {
        try {
            layout.removeAllViews();
            this.context = context;
            this.layout = layout;
            this.contentList = contentList;
            this.display = display;
            reload = this;
            this.inflater = LayoutInflater.from(context);
            switch (display) {
                // region edit / add
                case EDIT:
                case ADD:
                    // add a button to add a new paragraph.
                    // {(int)ParagraphNr:{             <- contentList
                    //   (Map<String, Object>)Content:{   <- content
                    //    (int)Position:(Object)value  <- key:value - pair
                    //    }
                    //  }
                    // }
                    RelativeLayout addParagraph =
                            (RelativeLayout) inflater.inflate(R.layout.custom_add_paragraph, null);
                    addParagraph.setOnClickListener(v -> {
                        ArrayList<Paragraph> paragraph = new ArrayList<>();
                        contentList.add(paragraph);
                        // reload site
                        try{
                            new Site(context, layout, contentList, Display.ADD);
                        } catch (Exception e) {
                            Log.e(TAG, "Site: site exception", e);
                        }
                    });
                    layout.addView(addParagraph);
                    for (int i = 0; i < contentList.size(); i++) {
                        ArrayList<Paragraph> content = contentList.get(i);
                        int contentSize = content.size();

                        // region add fields into paragraph
                        HorizontalScrollView selector =
                                (HorizontalScrollView) inflater.inflate(R.layout.custom_table, null);
                        TableLayout table = selector.findViewById(R.id.table);
                        TableRow rowOne = (TableRow) inflater.inflate(R.layout.custom_table_row, null);
                        TextView tvTitle = (TextView) inflater.inflate(R.layout.custom_subtitle_2,
                                null);
                        tvTitle.setText(R.string.add_field_inside_paragraph);
                        TableRow rowTwo = (TableRow) inflater.inflate(R.layout.custom_table_row, null);
                        button[0] = (Button) inflater.inflate(R.layout.custom_button, null);
                        button[1] = (Button) inflater.inflate(R.layout.custom_button, null);
                        button[2] = (Button) inflater.inflate(R.layout.custom_button, null);
                        button[3] = (Button) inflater.inflate(R.layout.custom_button, null);
                        button[4] = (Button) inflater.inflate(R.layout.custom_button, null);
                        button[5] = (Button) inflater.inflate(R.layout.custom_button, null);
                        button[6] = (Button) inflater.inflate(R.layout.custom_button, null);
                        button[0].setText(R.string.text);
                        button[1].setText(R.string.title);
                        button[2].setText(R.string.subtitle_1);
                        button[3].setText(R.string.subtitle_2);
                        button[4].setText(R.string.button);
                        button[5].setText(R.string.images);
                        button[6].setText(R.string.table);
                        for (Button b : button) {
                            rowTwo.addView(b);
                        }
                        rowOne.addView(tvTitle);
                        table.addView(rowOne);
                        table.addView(rowTwo);
                        layout.addView(selector);

                        button[0].setOnClickListener(v -> {
                            Paragraph p = new Paragraph(TEXT.toString(), contentSize, new ArrayList<>());
                            content.add(contentSize, p);
                            // reload site
                            reload.site();
                        });
                        button[1].setOnClickListener(v -> {
                            Paragraph p = new Paragraph(TITLE.toString(), contentSize, new ArrayList<>());
                            content.add(contentSize, p);
                            // reload site
                            reload.site();
                        });
                        button[2].setOnClickListener(v -> {
                            Paragraph p = new Paragraph(SUBTITLE1.toString(), contentSize, new ArrayList<>());
                            content.add(contentSize, p);
                            // reload site
                            reload.site();
                        });
                        button[3].setOnClickListener(v -> {
                            Paragraph p = new Paragraph(SUBTITLE2.toString(), contentSize, new ArrayList<>());
                            content.add(contentSize, p);
                            // reload site
                            reload.site();
                        });
                        button[4].setOnClickListener(v -> {
                            Paragraph p = new Paragraph(BUTTON.toString(), contentSize, new ArrayList<>());
                            content.add(contentSize, p);
                            // reload site
                            reload.site();
                        });
                        button[5].setOnClickListener(v -> {
                            Paragraph p = new Paragraph(IMAGE.toString(), contentSize, new ArrayList<>());
                            content.add(contentSize, p);
                            // reload site
                            reload.site();
                        });
                        button[6].setOnClickListener(v -> {
                            Toast.makeText(App.getContext(), R.string.error_dev, Toast.LENGTH_SHORT).show();
                            // reload site
                            reload.site();
                        });
                        // endregion

                        addParagraph =
                                (RelativeLayout) inflater.inflate(R.layout.custom_add_paragraph, null);
                        addParagraph.setOnClickListener(v -> {
                            ArrayList<Paragraph> paragraph = new ArrayList<>();
                            contentList.add(paragraph);
                            // reload site
                            reload.site();
                        });

                        RelativeLayout removeParagraph =
                                (RelativeLayout) inflater.inflate(R.layout.custom_remove_paragraph,
                                        null);
                        removeParagraph.setOnClickListener(v -> {
                            try {
                                contentList.remove(content);
                            } catch (Exception e) {
                                Firebase.Crashlytics.log(TAG, "", e);
                            } finally {
                                // reload site
                                reload.site();
                            }
                        });

                        layout.addView(edit(content));

                        layout.addView(removeParagraph);

                        layout.addView(addParagraph);
                    }
                    break;
                // endregion
                default:
                    for (List<Paragraph> one : contentList) {
                        layout.addView(display(one));
                    }
                    break;
            }
        } catch (Exception exception) {
            throw new SiteNotActiveException();
        }
    }

    @NonNull
    private View edit (@NotNull List<Paragraph> paragraph) {
        // TODO: 10.08.21 display the content editable, movable and addable.
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.custom_linear, null);
        layout.removeAllViewsInLayout();
        try {
            for (int i = 0; i< paragraph.size(); i++) {
                Paragraph line = paragraph.get(i);
                if (line == null) {
                    Firebase.Crashlytics.log("Object line == null");
                } else {
                    de.b3ttertogeth3r.walhalla.utils.Field field = new de.b3ttertogeth3r.walhalla.utils.Field(inflater, line, true);
                    findValues(layout, line, field);
                }
            }
        } catch (Exception e) {
            Firebase.Crashlytics.log(TAG, "Site isn't active anymore.", e);
        }
        return layout;
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
                    de.b3ttertogeth3r.walhalla.utils.Field field = new de.b3ttertogeth3r.walhalla.utils.Field(inflater, line, false);
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
        } else if (paragraph.getKind().equals(TABLE.toString())) {
            layout.addView(field.design(TABLE));
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
            new Site(context, layout, contentList, display);
        } catch (SiteNotActiveException exception) {
            exception.printStackTrace();
        }
    }
}
