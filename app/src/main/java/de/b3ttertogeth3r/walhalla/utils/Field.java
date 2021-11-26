package de.b3ttertogeth3r.walhalla.utils;

import static de.b3ttertogeth3r.walhalla.enums.Design.SUBTITLE1;
import static de.b3ttertogeth3r.walhalla.enums.Design.SUBTITLE2;
import static de.b3ttertogeth3r.walhalla.enums.Design.TEXT;
import static de.b3ttertogeth3r.walhalla.enums.Design.TITLE;

import android.os.OperationCanceledException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import com.google.firebase.database.annotations.NotNull;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstraction.MyTextWatcher;
import de.b3ttertogeth3r.walhalla.annos.Designer;
import de.b3ttertogeth3r.walhalla.design.MySubtitle;
import de.b3ttertogeth3r.walhalla.design.MySubtitle2;
import de.b3ttertogeth3r.walhalla.design.MyTextView;
import de.b3ttertogeth3r.walhalla.design.MyTitle;
import de.b3ttertogeth3r.walhalla.enums.Design;
import de.b3ttertogeth3r.walhalla.firebase.Firebase;
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
    private final boolean isEdit;
    private final LayoutInflater inflater;
    private Paragraph paragraph;

    public Field (LayoutInflater inflater, Paragraph paragraph, boolean isEdit) {
        this.inflater = inflater;
        this.paragraph = paragraph;
        this.isEdit = isEdit;
    }

    public View design (@NonNull @NotNull @Designer Design design) throws OperationCanceledException {
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
            case LIST_CHECKABLE:
                return listCheckable();
            default:
                return null;
        }
    }

    /**
     * The pre-checked object has a key called {@code text}, which should be formatted in this way:
     * <p>
     * {@code {text: (String)value}}
     * <p>
     * Two previous determent cases are possible:
     * <ol>
     *     <li><code>isEdit == false</code><br>
     *         Displays a textview the the formatted style for a text.
     *     </li>
     *     <li><code>isEdit == true</code><br>
     *         Importing relative layout <tt>custom_edit_text</tt>. After setting its title to
     *         {@code text} and adding a {@code onClickListener} on the save button to save the text
     *         written into the EditText field named {@code content}. The button {@code delete}
     *         removes the field from {@link #paragraph}.
     *         TODO find a way to move the field up and down
     *     </li>
     * </ol>
     *
     * @return the created view.
     * @see TextView
     * @see EditText
     * @see #isEdit
     * @since 1.0
     */
    @NonNull
    private View text () {
        if (isEdit) {
            RelativeLayout editText = (RelativeLayout) inflater.inflate(R.layout.custom_edit_text
                    , null);
            ImageButton delete = editText.findViewById(R.id.delete);
            TextView title = editText.findViewById(R.id.title);
            EditText content = editText.findViewById(R.id.text);
            title.setText(TEXT.toString());
            content.setHint(TEXT.toString());
            content.setText(paragraph.getValue().get(0));
            content.addTextChangedListener(new MyTextWatcher() {
                @Override
                public void textChanged (String s) {
                    List<String> change = new ArrayList<>();
                    change.add(s);
                    paragraph.setValue(change);
                }
            });
            delete.setOnClickListener(v -> {
                paragraph = null;
                Site.reload.site();
            });
            return editText;
        } else {
            TextView text = new MyTextView(inflater.getContext());
            text.setText(paragraph.getValue().get(0));
            return text;
        }
    }

    /**
     * !isEdit:<br>
     * in a slide show show all the images. the images uid are saved in the array of the {@link
     * #paragraph}, so the function has to load the image from the "Images" collection, where the
     * "isVisible" boolean is "true".
     * <p>
     * isEdit<br>
     * In the displayed multi check dialog the image description and a small icon shall be
     * displayed.
     *
     * @return the created view
     * @throws OperationCanceledException
     *         If given object list is not formatted properly
     * @see Image
     * @see de.b3ttertogeth3r.walhalla.firebase.Firebase.Storage Firebase Storage
     * @see <a href="https://github.com/denzcoskun/ImageSlideshow">Image Slide Show</a>
     * @since 1.0
     */
    @NonNull
    private View image () throws OperationCanceledException {
        if (isEdit) {
            //TODO start loading circle
            Firebase.FIRESTORE.collection("Images")
                    .whereEqualTo("isVisible", true).get()
                    .addOnSuccessListener(documentSnapshots -> {
                        if(documentSnapshots.isEmpty()){
                            Log.d(TAG, "onSuccess: no images found");
                            return;
                        }

                        Log.d(TAG, "onSuccess: images found");
                        //TODO format all images into an array list
                        //TODO Open multi choice dialog
                        //TODO customize each choice item with image and description

                        //TODO Find a way to make previously selected images selected in dialog
                    });
            throw new OperationCanceledException("Display of editable image view not yet " +
                    "initialized");
        } else {
            //ForEach load Image.class from Images/"paragraph.values().get(i)" into a list for the ImageSlideView
            try {
                View view = inflater.inflate(R.layout.custom_slideview, null);
                SliderView sliderView = view.findViewById(R.id.image_slider);
                SliderAdapter adapter = new SliderAdapter(paragraph.getValue());
                sliderView.setSliderAdapter(adapter);
                sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
                sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
                sliderView.setScrollTimeInSec(10);
                sliderView.startAutoCycle();
                view.setMinimumHeight(300);
                return view;
            } catch (Exception e) {
                Log.e(TAG, "image: error", e);
                throw new NullPointerException("look at the error above /\\");
            }
        }
    }

    /**
     * @return the created view.
     * @since 1.0
     */
    @NonNull
    private View table () throws OperationCanceledException {
        if (isEdit) {
            throw new OperationCanceledException("Function not yet initialized");
        } else {
            throw new OperationCanceledException("Diplay of tables not yet created.");
        }
    }

    /**
     * The pre-checked object has a key called {@code title}, which should be formatted in this way:
     * <p>
     * {@code {title: "value"}}
     * <p>
     * Two previous determent cases are possible:
     * <ol>
     *     <li><code>isEdit == false</code><br>
     *         Displays a textview the the formatted style for a title.
     *     </li>
     *     <li><code>isEdit == true</code><br>
     *         TODO create the design and its functions
     *     </li>
     * </ol>
     *
     * @return the created view.
     * @since 1.0
     */
    @NonNull
    private View title () {
        if (isEdit) {
            RelativeLayout editText = (RelativeLayout) inflater.inflate(R.layout.custom_edit_text
                    , null);
            ImageButton delete = editText.findViewById(R.id.delete);
            TextView title = editText.findViewById(R.id.title);
            EditText content = editText.findViewById(R.id.text);
            title.setText(TITLE.toString());
            content.setHint(TITLE.toString());
            content.setText(paragraph.getValue().get(0));
            content.addTextChangedListener(new MyTextWatcher() {
                @Override
                public void textChanged (String s) {
                    List<String> change = new ArrayList<>();
                    change.add(s);
                    paragraph.setValue(change);
                }
            });
            delete.setOnClickListener(v -> {
                paragraph = null;
                Site.reload.site();
            });
            return editText;
        } else {
            MyTitle title = new MyTitle(inflater.getContext());
            title.setText(paragraph.getValue().get(0));
            return title;
        }
    }

    /**
     * @return The created view.
     * @see Button
     * @see ImageButton
     * @since 1.0
     */
    @NonNull
    private View button () throws OperationCanceledException {
        if (isEdit) {
            throw new OperationCanceledException("Display of editable buttons not yet created.");
        } else {
            throw new OperationCanceledException("Display of buttons not yet created.");
        }
    }

    /**
     * The pre-checked object has a key called {@code subtitle1}, which should be formatted in this
     * way:
     * <p>
     * {@code {subtitle1: "value"}}
     * <p>
     * Two previous determent cases are possible:
     * <ol>
     *     <li><code>isEdit == false</code><br>
     *         Displays a textview the the formatted style for a first subtitle.
     *     </li>
     *     <li><code>isEdit == true</code><br>
     *         TODO create the design and its functions
     *     </li>
     * </ol>
     *
     * @return the created view.
     * @since 1.0
     */
    @NonNull
    @NotNull
    private View subtitle1 () {
        if (isEdit) {
            RelativeLayout editText = (RelativeLayout) inflater.inflate(R.layout.custom_edit_text
                    , null);
            ImageButton delete = editText.findViewById(R.id.delete);
            TextView title = editText.findViewById(R.id.title);
            EditText content = editText.findViewById(R.id.text);
            title.setText(SUBTITLE1.toString());
            content.setHint(SUBTITLE1.toString());
            content.setText(paragraph.getValue().get(0));
            delete.setOnClickListener(v -> {
                paragraph = null;
                Site.reload.site();
            });
            content.addTextChangedListener(new MyTextWatcher() {
                @Override
                public void textChanged (String s) {
                    List<String> change = new ArrayList<>();
                    change.add(s);
                    paragraph.setValue(change);
                }
            });
            return editText;
        } else {
            TextView sub1 = new MySubtitle(inflater.getContext());
            sub1.setText(paragraph.getValue().get(0));
            return sub1;
        }
    }

    /**
     * The pre-checked object has a key called {@code subtitle2}, which should be formatted in this
     * way:
     * <p>
     * {@code {subtitle2: "value"}}
     * <p>
     * Two previous determent cases are possible:
     * <ol>
     *     <li><code>isEdit == false</code><br>
     *         Displays a textview the the formatted style for a second subtitle.
     *     </li>
     *     <li><code>isEdit == true</code><br>
     *         TODO create the design and its functions
     *     </li>
     * </ol>
     *
     * @return the created view.
     * @since 1.0
     */
    @NonNull
    private View subtitle2 () {
        if (isEdit) {
            RelativeLayout editText = (RelativeLayout) inflater.inflate(R.layout.custom_edit_text
                    , null);
            ImageButton delete = editText.findViewById(R.id.delete);
            TextView title = editText.findViewById(R.id.title);
            EditText content = editText.findViewById(R.id.text);
            title.setText(SUBTITLE2.toString());
            content.setHint(SUBTITLE1.toString());
            content.setText(paragraph.getValue().get(0));
            delete.setOnClickListener(v -> {
                paragraph = null;
                Site.reload.site();
            });
            content.addTextChangedListener(new MyTextWatcher() {
                @Override
                public void textChanged (String s) {
                    List<String> change = new ArrayList<>();
                    change.add(s);
                    paragraph.setValue(change);
                }
            });
            return editText;
        } else {
            TextView sub2 = new MySubtitle2(inflater.getContext());
            sub2.setText(paragraph.getValue().get(0));
            return sub2;
        }
    }

    /**
     * The pre-checked object has a key called {@code list_bullet}, which should be formatted in
     * this way:
     * <p>
     * {@code {list_bullet: (Array<String>)"values"}}
     * <p>
     * Two previous determent cases are possible:
     * <ol>
     *     <li><code>isEdit == false</code><br>
     *         TODO create the design and its inner functions
     *     </li>
     *     <li><code>isEdit == true</code><br>
     *         TODO create the design and its functions
     *     </li>
     * </ol>
     *
     * @return the created view.
     * @since 1.0
     */
    private View listBullet () throws OperationCanceledException {
        if (isEdit) {
            throw new OperationCanceledException("Function not yet initialized");
        } else {
            throw new OperationCanceledException("Function not yet initialized");
        }
    }

    /**
     * @return the created view.
     * @since 1.0
     */
    private View listChecked () throws OperationCanceledException {
        if (isEdit) {
            throw new OperationCanceledException("Function not yet initialized");
        } else {
            throw new OperationCanceledException("Function not yet initialized");
        }
    }

    /**
     * @return the created view.
     * @since 1.0
     */
    private View listCheckable () {
        if (isEdit) {
            throw new NullPointerException("Function not yet initialized");
        } else {
            throw new NullPointerException("Function not yet initialized");
        }
    }


}
