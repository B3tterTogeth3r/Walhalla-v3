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
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstract_generic.Dialog;
import de.b3ttertogeth3r.walhalla.design.Text;
import de.b3ttertogeth3r.walhalla.enums.DialogSize;

public class InfoDialog extends Dialog<Void> {
    private static final String TAG = "InfoDialog";
    private final String text;
    private View view;

    public InfoDialog() {
        super(DialogSize.WRAP_CONTENT);
        String t = "";
        try {
            t = requireContext().getString(R.string.info);
        } catch (Exception ignored) {
        }
        this.text = t;
    }

    public InfoDialog(View view) {
        super(DialogSize.FULL_SCREEN);
        this.text = "";
        this.view = view;
    }

    public InfoDialog(String text) {
        super(DialogSize.WRAP_CONTENT);
        this.text = text;
    }

    @Override
    public void createDialog(@NonNull RelativeLayout container, @NonNull LayoutInflater inflater) {
        if (view == null) {
            Text content = new Text(requireContext());
            content.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY));
            container.addView(content);
        } else {
            container.addView(view);
        }
    }

    @Override
    public void configDialog(@NonNull AlertDialog.Builder builder) {
        builder.setIcon(R.drawable.ic_info);
    }

    @Override
    public void configToolbar(@NonNull Toolbar toolbar) {
        toolbar.setTitle(R.string.info);
    }
}
