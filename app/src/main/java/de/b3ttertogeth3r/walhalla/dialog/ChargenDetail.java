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

package de.b3ttertogeth3r.walhalla.dialog;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstract_classes.Dialog;
import de.b3ttertogeth3r.walhalla.design.Text;
import de.b3ttertogeth3r.walhalla.enums.DialogSize;
import de.b3ttertogeth3r.walhalla.object.BoardMember;

public class ChargenDetail extends Dialog<BoardMember> {
    private final BoardMember boardMember;

    public ChargenDetail (DialogSize size, BoardMember boardMember) {
        super(size);
        this.boardMember = boardMember;
    }

    @Override
    public BoardMember done () {
        return null;
    }

    @Override
    public void createDialog (@NonNull RelativeLayout container, @NonNull LayoutInflater inflater) {
        de.b3ttertogeth3r.walhalla.object.Text d = boardMember.getCharge().getDescription().get(1);
        for (String s : d.getValue()) {
            container.addView(new Text(requireContext(), s));
        }
    }

    @Override
    public void configToolbar(Toolbar toolbar) {
        toolbar.setTitle(boardMember.getCharge().getDescription().get(0).getValue().get(0));
    }

    @Override
    public void configDialog (@NonNull AlertDialog.Builder builder) {
        builder.setPositiveButton(R.string.close, null);
    }
}
