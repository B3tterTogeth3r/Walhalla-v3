/*
 * Copyright (c) 2022-2022.
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

package de.b3ttertogeth3r.walhalla.dialog;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstract_generic.Dialog;
import de.b3ttertogeth3r.walhalla.enums.DialogSize;
import de.b3ttertogeth3r.walhalla.enums.Rank;
import de.b3ttertogeth3r.walhalla.exception.CreateDialogException;

public class RankSelectDialog extends Dialog<Rank> {
    private static final String TAG = "RankSelectDialog";
    private NumberPicker rank;

    public RankSelectDialog() {
        super(DialogSize.WRAP_CONTENT);
    }

    @NonNull
    public static RankSelectDialog display(FragmentManager manager) throws CreateDialogException {
        try {
            RankSelectDialog dialog = new RankSelectDialog();
            dialog.show(manager, TAG);
            return dialog;
        } catch (Exception e) {
            throw new CreateDialogException("Unable to create " + TAG + ".", e);
        }
    }

    @Override
    public Rank done() throws Exception {
        return Rank.values()[rank.getValue()];
    }

    @Override
    public void createDialog(@NonNull RelativeLayout container, @NonNull LayoutInflater inflater) {
        RelativeLayout pickers = (RelativeLayout) inflater.inflate(R.layout.dialog_item_sem_change, null);
        pickers.findViewById(R.id.np_center).setVisibility(View.GONE);
        pickers.findViewById(R.id.np_left).setVisibility(View.GONE);
        rank = pickers.findViewById(R.id.np_right);
        rank.setDisplayedValues(getRankNames());
        rank.setMinValue(0);
        rank.setMaxValue(getRankNames().length - 1);
        rank.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        container.addView(pickers);
    }

    @NonNull
    private String[] getRankNames() {
        ArrayList<String> names = new ArrayList<>();
        for (Rank r : Rank.values()) {
            names.add(r.getString());
        }
        return names.toArray(new String[0]);
    }

    @Override
    public void configDialog(@NonNull AlertDialog.Builder builder) {
        builder.setPositiveButton(R.string.send, this);
        builder.setNegativeButton(R.string.abort, this);
    }

    @Override
    public void configToolbar(@NonNull Toolbar toolbar) {

    }
}
