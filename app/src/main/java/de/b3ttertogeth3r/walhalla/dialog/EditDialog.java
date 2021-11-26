package de.b3ttertogeth3r.walhalla.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import java.util.HashMap;
import java.util.Map;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.design.MyEditText;
import de.b3ttertogeth3r.walhalla.design.MyLinearLayout;
import de.b3ttertogeth3r.walhalla.enums.Address;
import de.b3ttertogeth3r.walhalla.enums.Editable;
import de.b3ttertogeth3r.walhalla.interfaces.EditListener;
import de.b3ttertogeth3r.walhalla.models.Person;

public class EditDialog extends DialogFragment implements DialogInterface.OnClickListener {
    private static final String TAG = "EditDialog";
    private final Editable editable;
    private final EditListener listener;
    private MyEditText field_0, field_1, field_2, field_3;

    public EditDialog (@NonNull Editable editable,
                       @NonNull EditListener listener) {
        this.listener = listener;
        this.editable = editable;
    }

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
        field_2 = new MyEditText(getContext());
        field_3 = new MyEditText(getContext());

        switch (editable) {
            case NAME:
                builder.setTitle(R.string.full_name);
                field_0.setHint(R.string.first_name);
                field_1.setHint(R.string.last_name);
                field_0.setEditable(Editable.FIRST_NAME);
                field_1.setEditable(Editable.LAST_NAME);
                layout.addView(field_0);
                layout.addView(field_1);
                view.addView(layout);
                break;
            case ADDRESS:
                builder.setTitle(R.string.address);
                break;
            case DOB:
                builder.setTitle(R.string.dob);
                break;
            case POB:
                builder.setTitle(R.string.pob);
                break;
            case MOBILE:
                builder.setTitle(R.string.mobile);
                break;
            case MAJOR:
                builder.setTitle(R.string.major);
            case JOINED:
                builder.setTitle(R.string.joined);
                break;
            case MAIL:
                builder.setTitle(R.string.mail);
            case PICTURE:
            case CONNECTED_SERVICES:
                break;
            case LAST_NAME:
            case FIRST_NAME:
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
    public void onCancel (@NonNull DialogInterface dialog) {
        listener.abort();
        super.onCancel(dialog);
    }

    @Override
    public void onDismiss (@NonNull DialogInterface dialog) {
        listener.abort();
        super.onDismiss(dialog);
    }

    @Override
    public void onClick (DialogInterface dialog, int which) {
        if (which != AlertDialog.BUTTON_POSITIVE) {
            listener.abort();
            this.dismiss();
        }
        Object result = new Object();
        switch (editable) {
            case ADDRESS:
                /* all 4 edit text fields */
                Map<String, Object> address = new HashMap<>();
                address.put(Address.STREET.toString(), null);
                address.put(Address.NUMBER.toString(), null);
                address.put(Address.ZIP.toString(), null);
                address.put(Address.CITY.toString(), null);
                result = address;
                break;
            case DOB:
                /* timestamp
                 * maybe with 3 number pickers
                 */
            case NAME:
                /* map with first and last name in it */
                String first_name = "";
                String last_name = "";
                String error = getString(R.string.fui_missing_first_and_last_name);
                if (field_0.getText() != null && field_0.getText().length() > 3) {
                    first_name = field_0.getText().toString();
                } else {
                    listener.sendError(editable, error);
                }
                if (field_1.getText() != null && field_1.getText().length() > 3) {
                    last_name = field_1.getText().toString();
                } else {
                    listener.sendError(editable, error);
                }

                Map<String, String> name = new HashMap<>();
                name.put(Person.FIRST_NAME, first_name);
                name.put(Person.LAST_NAME, last_name);
                result = name;
                break;
            case POB:
                /* string */
            case MOBILE:
                /* string */
            case MAIL:
                /* string */
            case MAJOR:
                /* string */
                break;
        }
        listener.saveEdit(result, editable);
        /* only dismiss dialog if result has no exception */
        dialog.dismiss();
    }
}
