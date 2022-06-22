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

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.HashMap;
import java.util.Map;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.old.design.MyEditText;
import de.b3ttertogeth3r.walhalla.old.design.MyLinearLayout;
import de.b3ttertogeth3r.walhalla.old.enums.Address;
import de.b3ttertogeth3r.walhalla.old.enums.Editable;
import de.b3ttertogeth3r.walhalla.old.interfaces.EditListener;
import de.b3ttertogeth3r.walhalla.old.models.Person;

/**
 * @author B3tterTogether
 * @see DialogFragment
 * @since 1.11
 */
public class EditDialog extends DialogFragment implements DialogInterface.OnClickListener {
    private static final String TAG = "EditDialog";
    private final Editable editable;
    private final EditListener listener;
    private MyEditText field_0, field_1, field_2, field_3;
    private final Object value;

    /**
     * default constructor
     *
     * @param editable
     *         Editable for more than one field
     * @param listener
     *         EditListener to check for changes
     */
    public EditDialog (@NonNull Editable editable, Object value,
                       @NonNull EditListener listener) {
        this.listener = listener;
        this.editable = editable;
        this.value = value;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public Dialog onCreateDialog (@Nullable Bundle savedInstanceState) {
        Activity activity = requireActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.dialog_change_semester,
                null);
        view.removeAllViewsInLayout();
        MyLinearLayout layout = new MyLinearLayout(getContext());
        field_0 = new MyEditText(getContext(), listener);
        field_1 = new MyEditText(getContext(), listener);
        field_2 = new MyEditText(getContext(), listener);
        field_3 = new MyEditText(getContext(), listener);

        switch (editable) {
            case NAME:
                builder.setTitle(R.string.full_name);
                field_0.setHint(R.string.first_name);
                field_1.setHint(R.string.last_name);
                field_0.setEditable(Editable.FIRST_NAME);
                field_1.setEditable(Editable.LAST_NAME);
                Map<String, String> names = (Map<String, String>) value;
                field_0.setContent(names.get(Person.FIRST_NAME));
                field_1.setContent(names.get(Person.LAST_NAME));
                layout.addView(field_0);
                layout.addView(field_1);
                view.addView(layout);
                break;
            case ADDRESS:
                builder.setTitle(R.string.address);
                field_0.setHint(R.string.street);
                field_1.setHint(R.string.number);
                field_1.setInputType(InputType.TYPE_CLASS_TEXT);
                field_2.setHint(R.string.zip);
                field_2.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_CLASS_TEXT);
                field_3.setHint(R.string.city);
                Map<String, String> address = (Map<String, String>) value;
                field_0.setContent(address.get(Address.STREET.toString()));
                field_1.setContent(address.get(Address.NUMBER.toString()));
                field_2.setContent(address.get(Address.ZIP.toString()));
                field_3.setContent(address.get(Address.CITY.toString()));
                layout.addView(field_0);
                layout.addView(field_1);
                layout.addView(field_2);
                layout.addView(field_3);
                view.addView(layout);
                break;
            case POB:
                field_0.setHint(R.string.pob);
                field_0.setContent(value.toString());
                layout.addView(field_0);
                view.addView(layout);
                builder.setTitle(R.string.pob);
                break;
            case MOBILE:
                field_0.setHint(R.string.mobile);
                field_0.setContent(value.toString());
                field_0.setInputType(InputType.TYPE_CLASS_PHONE);
                layout.addView(field_0);
                view.addView(layout);
                builder.setTitle(R.string.mobile);
                break;
            case MAJOR:
                field_0.setHint(R.string.major);
                field_0.setContent(value.toString());
                layout.addView(field_0);
                view.addView(layout);
                builder.setTitle(R.string.major);
                break;
            case MAIL:
                builder.setTitle(R.string.mail);
                field_0.setContent(value.toString());
                field_0.setHint(R.string.fui_email_hint);
                field_0.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                layout.addView(field_0);
                view.addView(layout);
                break;
            case PICTURE:
            case CONNECTED_SERVICES:
            case LAST_NAME:
            case FIRST_NAME:
            case DOB:
            case JOINED:
            default:
                this.dismiss();
        }

        builder.setIcon(R.drawable.ic_edit);
        builder.setView(view);
        builder.setPositiveButton(R.string.save, this);
        builder.setNegativeButton(R.string.abort, this);

        return builder.create();
    }

    @Override
    public void onClick (DialogInterface dialog, int which) {
        if (which != AlertDialog.BUTTON_POSITIVE) {
            listener.abort();
            this.dismiss();
            return;
        }
        Object result = new Object();
        String error;

        switch (editable) {
            case ADDRESS:
                //region ADDRESS
                /* all 4 edit text fields */
                error = getString(R.string.error_address);
                Map<String, Object> address = new HashMap<>();
                if (field_0.getText() != null && field_0.getText().length() > 1) {
                    address.put(Address.STREET.toString(), field_0.getText());
                } else {
                    listener.sendError(editable, error);
                    return;
                }
                if (field_1.getText() != null) {
                    address.put(Address.NUMBER.toString(), field_1.getText());
                } else {
                    listener.sendError(editable, error);
                    return;
                }
                if (field_2.getText() != null && field_2.getText().length() >= 3) {
                    address.put(Address.ZIP.toString(), field_2.getText());
                } else {
                    listener.sendError(editable, error);
                    return;
                }
                if (field_3.getText() != null) {
                    address.put(Address.CITY.toString(), field_3.getText());
                } else {
                    listener.sendError(editable, error);
                    return;
                }
                result = address;
                break;
            //endregion
            case NAME:
                /* map with first and last name in it */
                String first_name;
                String last_name;
                error = getString(R.string.fui_missing_first_and_last_name);
                if (field_0.getText() != null && field_0.getText().length() >= 2) {
                    first_name = field_0.getText();
                } else {
                    listener.sendError(editable, error);
                    return;
                }
                if (field_1.getText() != null && field_1.getText().length() >= 2) {
                    last_name = field_1.getText();
                } else {
                    listener.sendError(editable, error);
                    return;
                }

                Map<String, String> name = new HashMap<>();
                name.put(Person.FIRST_NAME, first_name);
                name.put(Person.LAST_NAME, last_name);
                result = name;
                break;
            case POB:
                /* string */
                error = getString(R.string.error_pob);
                if (field_0.getText() != null && field_0.getText().length() != 0) {
                    result = field_0.getText();
                } else {
                    listener.sendError(editable, error);
                    return;
                }
                break;
            case MOBILE:
                /* string */
                // if input longer than 7 -> go on, else show error
                error = getString(R.string.error_mobile);
                if (field_0.getText() != null && field_0.getText().length() >= 7) {
                    String field = field_0.getText();
                    if (field.startsWith("+")) {
                        result = field;
                    } else if (field.startsWith("0")) {
                        result = field.replaceFirst("0", "+49");
                    }
                    //TODO remove " " (spaces)
                } else {
                    listener.sendError(editable, error);
                    return;
                }
                break;
            case MAIL:
                /* string */
                error = getString(R.string.fui_invalid_email_address);
                if (field_0.getText() != null && field_0.getText().length() > 3) {
                    result = field_0.getText();
                } else {
                    listener.sendError(editable, error);
                    return;
                }
                break;
            case MAJOR:
                /* string */
                error = getString(R.string.error_major);
                if (field_0.getText() != null && field_0.getText().length() != 0) {
                    result = field_0.getText();
                } else {
                    listener.sendError(editable, error);
                    return;
                }
                break;
        }

        listener.saveEdit(result, editable);
        /* only dismiss dialog if result has no exception */
        dialog.dismiss();
    }
}
