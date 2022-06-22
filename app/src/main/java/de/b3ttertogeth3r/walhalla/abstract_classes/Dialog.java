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

package de.b3ttertogeth3r.walhalla.abstract_classes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.enums.DialogSize;
import de.b3ttertogeth3r.walhalla.interfaces.IDialog;
import de.b3ttertogeth3r.walhalla.object.Log;

public abstract class Dialog<T> extends DialogFragment implements DialogInterface.OnClickListener, IDialog<T> {
    private static final String TAG = "Dialog";
    private final DialogSize size;
    private final Loader<T> loader;
    private int buttonClickListener = 0;

    public Dialog(DialogSize size, Loader<T> loader) {
        this.size = size;
        this.loader = loader;
    }

    public Dialog(DialogSize size) {
        this.size = size;
        this.loader = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (size == DialogSize.FULL_SCREEN) {
            setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
        }
    }

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Activity activity = requireActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.dialog_layout, null);
        RelativeLayout view = layout.findViewById(R.id.dialog_container);
        view.removeAllViewsInLayout();
        view.removeAllViews();
        Toolbar toolbar = layout.findViewById(R.id.dialog_toolbar);
        toolbar.setNavigationOnClickListener(v -> dismiss());
        try {
            configToolbar(toolbar);
            createDialog(view, inflater);
            configDialog(builder);
        } catch (Exception e) {
            Log.e(TAG, "onCreateDialog: creating dialog didn't work", e);
            dismiss();
        }


        builder.setView(layout);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }



    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        try {
            switch (buttonClickListener) {
                case DialogInterface.BUTTON_POSITIVE:
                    if (loader != null) {
                        loader.done(done());
                    }
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                case DialogInterface.BUTTON_NEUTRAL:
                default:
            }
        } catch (Exception ignored) {
        }
        super.onDismiss(dialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        android.app.Dialog d = getDialog();
        if (d != null) {
            int width, height;
            switch (size) {
                case FULL_SCREEN:
                    width = ViewGroup.LayoutParams.MATCH_PARENT;
                    height = ViewGroup.LayoutParams.MATCH_PARENT;
                    break;
                case WRAP_CONTENT:
                default:
                    width = ViewGroup.LayoutParams.WRAP_CONTENT;
                    height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    break;
            }
            Objects.requireNonNull(d.getWindow()).setLayout(width, height);
            d.getWindow().setWindowAnimations(R.style.AppTheme_Slide);
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                Log.i(TAG, "positive button clicked");
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                Log.i(TAG, "negative button clicked");
                break;
            case DialogInterface.BUTTON_NEUTRAL:
                Log.i(TAG, "neutral button clicked");
                break;
        }
        buttonClickListener = which;
        // onDismiss(dialog);
        dismiss();
    }
}
