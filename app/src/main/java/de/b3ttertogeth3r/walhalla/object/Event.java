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

package de.b3ttertogeth3r.walhalla.object;

import android.annotation.SuppressLint;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstract_classes.Loader;
import de.b3ttertogeth3r.walhalla.abstract_classes.MyObject;
import de.b3ttertogeth3r.walhalla.abstract_classes.TouchListener;
import de.b3ttertogeth3r.walhalla.design.Date;
import de.b3ttertogeth3r.walhalla.design.Text;
import de.b3ttertogeth3r.walhalla.design.TimeFrat;
import de.b3ttertogeth3r.walhalla.design.Title;
import de.b3ttertogeth3r.walhalla.dialog.EventDetails;
import de.b3ttertogeth3r.walhalla.enums.Collar;
import de.b3ttertogeth3r.walhalla.enums.DialogSize;
import de.b3ttertogeth3r.walhalla.enums.Punctuality;
import de.b3ttertogeth3r.walhalla.enums.Rank;
import de.b3ttertogeth3r.walhalla.enums.Visibility;
import de.b3ttertogeth3r.walhalla.interfaces.Validate;

/**
 * @author B3tterTogeth3r
 * @version 3.0
 * @see de.b3ttertogeth3r.walhalla.abstract_classes.MyObject
 * @see de.b3ttertogeth3r.walhalla.interfaces.Validate
 * @since 2.0
 */
public class Event extends MyObject implements Validate {
    private Timestamp end;
    private String title;
    private String description;
    private Collar collar;
    private Punctuality punctuality;
    private Visibility visibility;
    private String id;

    public Event() {
    }

    public Event(Timestamp start, Timestamp end, String title, String description, Collar collar,
                 Punctuality punctuality, Visibility visibility) {
        this.time = start;
        this.end = end;
        this.title = title;
        this.description = description;
        this.collar = collar;
        this.punctuality = punctuality;
        this.visibility = visibility;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp start) {
        this.time = start;
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Collar getCollar() {
        return collar;
    }

    public void setCollar(Collar collar) {
        this.collar = collar;
    }

    public Punctuality getPunctuality() {
        return punctuality;
    }

    public void setPunctuality(Punctuality punctuality) {
        this.punctuality = punctuality;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    /**
     * Displays the event as a layout to the user. On the left hand side is the {@link
     * de.b3ttertogeth3r.walhalla.design.Date Date}. Next to it is the {@link
     * de.b3ttertogeth3r.walhalla.design.TimeFrat TimeFrat} with the {@link #time},
     * {@link #collar} and {@link #punctuality}.
     * On the right is a linear layout with the title of the event at the {@link #title} and the
     * {@link #description} below.
     * <dl>
     * <dt>onLongClick</dt>
     * <dd>TODO: 17.05.22 Open dialog with the users undone chores of this event</dd>
     * <dt>onLeftToRightSwipe</dt>
     * <dd>
     *     TODO: 17.05.22 open a dialog with the following
     *     <ol type="a">
     *       <li>if user public: question to save the event in the users calendar</li>
     *       <li>if a user is signed in: select a chore to do by the signed in user</li>
     *       <li>if a board member is signed in: select a chore a user has to do</li>
     *       <li>if a board member is signed in: ability to edit: time (begin and end),title,
     *       collar, punctuality, visibility</li>
     *     </ol>
     *   </dd>
     *   <dt>onClick</dt>
     *   <dd>TODO: 16.05.22 open full screen dialog and download the missing parts
     *   </dd>
     * </dl>
     *
     * @param activity context of the site the event is displayed on
     * @param userRank rank of the signed in user
     * @return LinearLayout the layout with the designed Event
     * @since 1.0
     */
    @SuppressLint("ClickableViewAccessibility")
    @Exclude
    public LinearLayout getView(@NonNull FragmentActivity activity, @NonNull Rank userRank) {
        LinearLayout layout = new LinearLayout(activity);
        if (userRank.canSee(visibility)) {

            //region create design
            int margin = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_SP,
                    (float) 4,
                    activity.getResources().getDisplayMetrics()
            );
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.addView(new Date(activity, time));
            TimeFrat timeFrat = new TimeFrat(activity, time, collar, punctuality);
            timeFrat.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams timeFratParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            );
            timeFratParams.setMargins(margin,margin,margin,margin);
            timeFrat.setLayoutParams(timeFratParams);
            timeFrat.setGravity(Gravity.CENTER);
            layout.addView(timeFrat);

            LinearLayout layoutTitle = new LinearLayout(activity);
            LinearLayout.LayoutParams layoutTitleParams =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutTitleParams.setMargins(margin, margin, margin, margin);
            layoutTitle.setLayoutParams(layoutTitleParams);
            layoutTitle.setOrientation(LinearLayout.VERTICAL);
            Title tTitle = new Title(activity);
            Text tDescription = new Text(activity);
            tTitle.setPadding(0, 0, 0, 0);
            layoutTitleParams.setMargins(0, 0, 0, 0);
            tTitle.setLayoutParams(layoutTitleParams);
            tDescription.setPadding(0, 0, 0, 0);

            tTitle.setText(title);
            tDescription.setText(description);

            layoutTitle.addView(tTitle);
            layoutTitle.addView(tDescription);
            layout.addView(layoutTitle);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 0, (margin * 2));
            layout.setBackground(ContextCompat.getDrawable(activity, R.drawable.border_bottom_gray));
            layout.setLayoutParams(layoutParams);
            //endregion

            layout.setOnTouchListener(new TouchListener<Event>(this) {
                @Override
                public void onClick(Event event, View view) {
                    try {
                        FragmentManager fm = ((FragmentActivity) activity).getSupportFragmentManager();
                        EventDetails.display(fm, DialogSize.FULL_SCREEN, Event.this, new Loader<Void>() {
                            @Override
                            public void onSuccessListener(@Nullable Void result) {

                            }
                        });
                    } catch (Exception e) {
                        Log.e("Event", "onClickListener: Fragment manager not found", e);
                    }
                }

                @Override
                public void onLongClick(Event e, View view) {
                    Log.i("Event", "onLongClickListener");
                }
            });
        } else {
            layout.setVisibility(View.GONE);
        }
        return layout;
    }

    @Override
    public boolean validate() {
        return false;
    }
}
