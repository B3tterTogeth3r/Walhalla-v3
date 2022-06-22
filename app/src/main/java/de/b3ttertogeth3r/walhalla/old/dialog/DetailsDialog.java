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

package de.b3ttertogeth3r.walhalla.old.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.xw.repo.BubbleSeekBar;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.old.design.MyImageView;
import de.b3ttertogeth3r.walhalla.old.design.MyLinearLayout;
import de.b3ttertogeth3r.walhalla.old.enums.Punctuality;
import de.b3ttertogeth3r.walhalla.old.models.Event;

/**
 * @see <a href="https://github.com/woxingxiao/BubbleSeekBar">BubbleSeekBar</a>
 */
public class DetailsDialog extends AlertDialog.Builder implements DialogInterface.OnClickListener,
        BubbleSeekBar.OnProgressChangedListener {
    private static final String TAG = "DetailsDialog";
    private final Event event;
    private final SparseArray<String> punctualityArray = new SparseArray<>();
    private TextView dateTV, timeTV, punctualityTV;

    public DetailsDialog (Context context, @NonNull Event event) {
        super(context);
        int oneDP = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                1f,
                context.getResources().getDisplayMetrics()
        );
        punctualityArray.put(0, Punctuality.CT.getShort_description());
        punctualityArray.put(1, Punctuality.ST.getShort_description());
        punctualityArray.put(2, Punctuality.ALL_DAY.toString());
        punctualityArray.put(3, Punctuality.INFO.toString());
        this.event = event;
        MyLinearLayout view = new MyLinearLayout(context);
        setTitle(R.string.details_dialog_title);
        setNegativeButton(R.string.abort, this);
        setPositiveButton(R.string.save, this);
        setCancelable(true);
        setIcon(R.drawable.ic_edit);
        view.setBackground(null);
        int buttonWidth = (view.getWidth()/3);
        //region date
        // 3 buttons-> first: date, second: time, third: duration
        LinearLayout timePickers = new LinearLayout(context);
        MyImageView date = new MyImageView(context, buttonWidth, 0);
        //date.setDescription(R.string.calendar);
        date.setImage(ContextCompat.getDrawable(context, R.drawable.ic_calendar));
        MyImageView time = new MyImageView(context, buttonWidth, 0);
        time.setImage(ContextCompat.getDrawable(context, R.drawable.ic_time));
        MyImageView duration = new MyImageView(context, buttonWidth, 0);
        duration.setImage(ContextCompat.getDrawable(context, R.drawable.ic_hourglass_empty_24));
        timePickers.addView(date);
        timePickers.addView(time);
        timePickers.addView(duration);
        //endregion
        //region seekBar
        BubbleSeekBar seekBar = new BubbleSeekBar(context);
        seekBar.getConfigBuilder()
                .hideBubble()
                .max(3)
                .min(0)
                .progress(0)
                .secondTrackColor(ContextCompat.getColor(context, R.color.colorAccent))
                .sectionCount(3)
                .sectionTextPosition(BubbleSeekBar.TextPosition.BELOW_SECTION_MARK)
                .showSectionMark()
                .showSectionText()
                .sectionTextInterval(1)
                .showThumbText()
                .trackColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
                .build();
        seekBar.setPadding((4 * oneDP), 0, (4 * oneDP), 0);
        seekBar.setOnProgressChangedListener(this);
        seekBar.setCustomSectionTextArray(new BubbleSeekBar.CustomSectionTextArray() {
            @NonNull
            @Override
            public SparseArray<String> onCustomize (int sectionCount,
                                                    @NonNull SparseArray<String> array) {
                array.clear();
                array = punctualityArray.clone();
                return array;
            }
        });
        int progress = punctualityArray.indexOfValue(event.getPunctuality());
        seekBar.setProgress(Math.max(progress, 0));
        //endregion
        view.addView(timePickers);
        view.addView(seekBar);
        setView(view);
    }

    @Override
    public void onClick (DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                Log.d(TAG, "onClick: upload changes, refresh program.fragment and dismiss dialog");
                //todo upload changes and edit event in program.Fragment's arrayList
                break;
            case DialogInterface.BUTTON_NEGATIVE:
            default:
                Log.d(TAG, "onClick: just dismiss dialog, but this should never be the case");
                dialog.dismiss();
                break;
        }
    }

    @Override
    public void onProgressChanged (BubbleSeekBar bubbleSeekBar, int progress, float progressFloat
            , boolean fromUser) {
    }

    @Override
    public void getProgressOnActionUp (BubbleSeekBar bubbleSeekBar, int progress,
                                       float progressFloat) {
        bubbleSeekBar.setProgress(progress);
        event.setPunctuality(punctualityArray.get(progress, "ct"));
    }

    @Override
    public void getProgressOnFinally (BubbleSeekBar bubbleSeekBar, int progress,
                                      float progressFloat, boolean fromUser) {
    }
}
