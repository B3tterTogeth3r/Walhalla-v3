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

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import java.text.DecimalFormat;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstract_generic.Dialog;
import de.b3ttertogeth3r.walhalla.enums.DialogSize;
import de.b3ttertogeth3r.walhalla.exception.CreateDialogException;
import de.b3ttertogeth3r.walhalla.util.Log;

public class NumericDialog extends Dialog<Float> implements View.OnClickListener {
    private static final String TAG = "NumericDialog";
    private LinearLayout layout;
    private String amount = "";
    private EditText value;

    public NumericDialog() {
        super(DialogSize.WRAP_CONTENT);
    }

    @NonNull
    public static NumericDialog display(FragmentManager fragmentManager) throws CreateDialogException {
        try {
            NumericDialog dialog = new NumericDialog();
            dialog.show(fragmentManager, TAG);
            return dialog;
        } catch (Exception e) {
            throw new CreateDialogException("unable to create dialog", e);
        }
    }

    @Override
    public Float done() throws Exception {
        if (e != null) {
            throw e;
        } else {
            float value = Float.parseFloat(amount.replace(",", "."));
            Log.i(TAG, "Amount value: " + value);
            if (0 < value) {
                DecimalFormat df = new DecimalFormat();
                df.setMaximumFractionDigits(2);
                return Float.parseFloat(df.format(value));
            } else {
                return 0f;
            }
        }
    }

    @Override
    public void createDialog(@NonNull RelativeLayout container, @NonNull LayoutInflater inflater) {
        layout = (LinearLayout) inflater.inflate(R.layout.dialog_dialer_view, null);
        container.addView(layout);
        value = layout.findViewById(R.id.editText_dialpad);
        layout.findViewById(R.id.oneButton).setOnClickListener(this);
        layout.findViewById(R.id.twoButton).setOnClickListener(this);
        layout.findViewById(R.id.threeButton).setOnClickListener(this);
        layout.findViewById(R.id.fourButton).setOnClickListener(this);
        layout.findViewById(R.id.fiveButton).setOnClickListener(this);
        layout.findViewById(R.id.sixButton).setOnClickListener(this);
        layout.findViewById(R.id.sevenButton).setOnClickListener(this);
        layout.findViewById(R.id.eightButton).setOnClickListener(this);
        layout.findViewById(R.id.nineButton).setOnClickListener(this);
        layout.findViewById(R.id.dotButton).setOnClickListener(this);
        layout.findViewById(R.id.zeroButton).setOnClickListener(this);
        layout.findViewById(R.id.doubleZeroButton).setOnClickListener(this);
        layout.findViewById(R.id.imageButton_dialpad_arrow).setOnClickListener(this);
    }

    @Override
    public void configDialog(@NonNull AlertDialog.Builder builder) {
        builder.setPositiveButton(R.string.send, this);
        builder.setNegativeButton(R.string.abort, null);
    }

    @Override
    public void configToolbar(@NonNull Toolbar toolbar) {
        toolbar.setTitle(R.string.numbers_set_value);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(@NonNull View view) {

        if (view.getId() == R.id.imageButton_dialpad_arrow) {
            Log.i(TAG, "delete button pressed");
            amount = amount.substring(0, amount.length() - 1);
        }
        if (amount.contains(",") && amount.substring(amount.indexOf(",")).length() > 2) {
            return;
        }
        switch (view.getId()) {
            case R.id.oneButton:
                Log.i(TAG, "Number one pressed");
                amount += "1";
                break;
            case R.id.twoButton:
                Log.i(TAG, "Number two pressed");
                amount += "2";
                break;
            case R.id.threeButton:
                Log.i(TAG, "Number three pressed");
                amount += "3";
                break;
            case R.id.fourButton:
                Log.i(TAG, "Number four pressed");
                amount += "4";
                break;
            case R.id.fiveButton:
                Log.i(TAG, "Number five pressed");
                amount += "5";
                break;
            case R.id.sixButton:
                Log.i(TAG, "Number six pressed");
                amount += "6";
                break;
            case R.id.sevenButton:
                Log.i(TAG, "Number seven pressed");
                amount += "7";
                break;
            case R.id.eightButton:
                Log.i(TAG, "Number eight pressed");
                amount += "8";
                break;
            case R.id.nineButton:
                Log.i(TAG, "Number nine pressed");
                amount += "9";
                break;
            case R.id.dotButton:
                Log.i(TAG, "Dot pressed");
                amount += ",";
                break;
            case R.id.zeroButton:
                Log.i(TAG, "Number zero pressed");
                amount += "0";
                break;
            case R.id.doubleZeroButton:
                Log.i(TAG, "Number double zero pressed");
                amount += "00";
                break;
            default:
                break;
        }
        String v = amount + " €";
        value.setText(v);

        if (value.getText().toString().contains(",")) {
            layout.findViewById(R.id.dotButton).setOnClickListener(null);
        } else {
            layout.findViewById(R.id.dotButton).setOnClickListener(this);
        }
    }
}
