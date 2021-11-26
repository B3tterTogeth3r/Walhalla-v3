package de.b3ttertogeth3r.walhalla.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;

import androidx.annotation.NonNull;

import com.xw.repo.BubbleSeekBar;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.enums.Punctuality;
import de.b3ttertogeth3r.walhalla.models.Event;

/**
 * @see <a href="https://github.com/woxingxiao/BubbleSeekBar">BubbleSeekBar</a>
 */
public class DetailsDialog extends AlertDialog.Builder implements DialogInterface.OnClickListener,
BubbleSeekBar.OnProgressChangedListener{
    private static final String TAG = "DetailsDialog";
    private Event event;
    private View view;

    public DetailsDialog (Context context, Event event) {
        super(context);
        this.event = event;
        LayoutInflater inflater = LayoutInflater.from(context);
        this.view = inflater.inflate(R.layout.custom_dialog_event_edit, null);
        setTitle(R.string.details_dialog_title);
        setNegativeButton(R.string.abort, this);
        setPositiveButton(R.string.save, this);
        setCancelable(false);
        setIcon(R.drawable.ic_edit);
        setView(view);
        BubbleSeekBar punctualityBar = view.findViewById(R.id.bubble_seek_bar);
        punctualityBar.setOnProgressChangedListener(this);
        punctualityBar.setCustomSectionTextArray(new BubbleSeekBar.CustomSectionTextArray() {
            @NonNull
            @Override
            public SparseArray<String> onCustomize (int sectionCount,
                                                    @NonNull SparseArray<String> array) {
                array.clear();
                array.put(0, Punctuality.CT.getShort_description());
                array.put(1, Punctuality.ST.getShort_description());
                array.put(2, Punctuality.ALL_DAY.toString());
                array.put(3, Punctuality.INFO.toString());
                return array;
            }
        });
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
    public void getProgressOnActionUp (BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

    }

    @Override
    public void getProgressOnFinally (BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
        Log.d(TAG, "getProgressOnFinally: progress: " + progress);
    }
}
