package de.b3ttertogeth3r.walhalla.fragments.profile;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.google.firebase.database.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstraction.CustomFragment;
import de.b3ttertogeth3r.walhalla.design.MyTable;
import de.b3ttertogeth3r.walhalla.design.MyTableRow;
import de.b3ttertogeth3r.walhalla.design.MyToast;
import de.b3ttertogeth3r.walhalla.design.ProfileRow;
import de.b3ttertogeth3r.walhalla.dialog.EditDialog;
import de.b3ttertogeth3r.walhalla.enums.Editable;
import de.b3ttertogeth3r.walhalla.exceptions.PersonException;
import de.b3ttertogeth3r.walhalla.firebase.Firebase;
import de.b3ttertogeth3r.walhalla.interfaces.EditListener;
import de.b3ttertogeth3r.walhalla.models.Person;
import de.b3ttertogeth3r.walhalla.utils.CacheData;

/**
 * @author B3tterToegth3r
 * @version 1.0
 * @since 1.0
 */
public class Fragment extends CustomFragment implements View.OnClickListener {
    private static final String TAG = "profile.Fragment";
    private MyTableRow mailRow, nameRow, addressRow, dobRow, mobileRow, pobRow, majorRow, rankRow
            , joinedRow, pictureRow, connectedRow;
    private ProfileRow name, mail, pob, mobile, rank, major, address, joined, dob;
    private Person user;

    @Override
    public void start () {

    }

    @Override
    public void analyticsProperties () {

    }

    @Override
    public void stop () {

    }

    @Override
    public void viewCreated () {
    }

    @Override
    public void toolbarContent () {
        toolbar.setTitle(R.string.menu_profile);
    }

    @Override
    public void createView (@NonNull @NotNull View view,
                            @NonNull @NotNull LayoutInflater inflater) {
        try {
            user = CacheData.getUser();
        } catch (PersonException e) {
            user = new Person();
        }
        LinearLayout layout = view.findViewById(R.id.fragment_container);
        layout.removeAllViews();
        layout.removeAllViewsInLayout();

        //region table and rows with onClickListener
        MyTable profileTable = new MyTable(getContext());
        mailRow = new MyTableRow(getContext());
        nameRow = new MyTableRow(getContext());
        addressRow = new MyTableRow(getContext());
        dobRow = new MyTableRow(getContext());
        mobileRow = new MyTableRow(getContext());
        pobRow = new MyTableRow(getContext());
        majorRow = new MyTableRow(getContext());
        rankRow = new MyTableRow(getContext());
        joinedRow = new MyTableRow(getContext());
        pictureRow = new MyTableRow(getContext());
        connectedRow = new MyTableRow(getContext());
        mailRow.setOnClickListener(this);
        nameRow.setOnClickListener(this);
        addressRow.setOnClickListener(this);
        dobRow.setOnClickListener(this);
        mobileRow.setOnClickListener(this);
        pobRow.setOnClickListener(this);
        majorRow.setOnClickListener(this);
        rankRow.setOnClickListener(this);
        joinedRow.setOnClickListener(this);
        pictureRow.setOnClickListener(this);
        connectedRow.setOnClickListener(this);
        //endregion

        mail = new ProfileRow(getContext());
        mail.setTitleText(getString(R.string.fui_email_hint));
        mail.setValueText(user.getMail());
        mailRow.addView(mail);

        name = new ProfileRow(getContext());
        name.setTitleText(getString(R.string.full_name));
        name.setValueText(user.getFullName());
        nameRow.addView(name);

        pob = new ProfileRow(getContext());
        pob.setTitleText(R.string.pob);
        pob.setValueText(user.getPoB());
        pobRow.addView(pob);

        mobile = new ProfileRow(getContext());
        mobile.setTitleText(R.string.mobile);
        mobile.setValueText(user.getMobile());
        mobileRow.addView(mobile);

        rank = new ProfileRow(getContext());
        rank.setTitleText(R.string.rank);
        rank.setValueText(user.getRank());
        rankRow.addView(rank);

        major = new ProfileRow(getContext());
        major.setTitleText(R.string.major);
        major.setValueText(user.getMajor());
        majorRow.addView(major);

        address = new ProfileRow(getContext());
        address.setTitleText(R.string.address);
        address.setValueText(user.getAddressString());
        addressRow.addView(address);

        joined = new ProfileRow(getContext());
        joined.setTitleText(R.string.joined);
        //TODO Format into semester name, not just the id
        joined.setValueText(user.getJoined() + "");
        joinedRow.addView(joined);

        dob = new ProfileRow(getContext());
        dob.setTitleText(R.string.dob);
        dob.setValueText(user.getDoBString());
        dobRow.addView(dob);

        //region add rows to table and table to layout
        profileTable.addView(mailRow);
        profileTable.addView(nameRow);
        profileTable.addView(addressRow);
        profileTable.addView(dobRow);
        profileTable.addView(mobileRow);
        profileTable.addView(pobRow);
        profileTable.addView(majorRow);
        profileTable.addView(rankRow);
        profileTable.addView(joinedRow);
        profileTable.addView(pictureRow);
        profileTable.addView(connectedRow);

        layout.addView(profileTable);
        //endregion
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onClick (View v) {
        Context ctx = requireContext();
        MyToast toast = new MyToast(ctx);
        EditDialog dialog = null;
        if (v == mailRow) {
            toast.setText("Mail not editable");
            toast.show();
        } else if (v == nameRow) {
            Map<String, String> nameBackup = new HashMap<>();
            nameBackup.put(Person.FIRST_NAME, user.getFirst_Name());
            nameBackup.put(Person.LAST_NAME, user.getLast_Name());
            dialog = new EditDialog(Editable.NAME, new EditListener() {
                @Override
                public void saveEdit (Object value, Editable editable) {
                    try {
                        updateName((Map<String, String>) value);
                    } catch (Exception e) {
                        updateName(nameBackup);
                        Firebase.Crashlytics.log(TAG, R.string.fui_missing_first_and_last_name);
                        toast.setText(R.string.fui_missing_first_and_last_name);
                        toast.show();
                    }
                }

                @Override
                public void abort () {
                    updateName(nameBackup);
                    Log.e(TAG, "abort: dialog dismissed.");
                }

                @Override
                public void sendError (Editable editable, String s) {
                    updateName(nameBackup);
                    toast.setText(s);
                    toast.show();
                }

                @Override
                public void sendLiveChange (String string, Editable editable) {
                    if (editable.equals(Editable.FIRST_NAME)) {
                        user.setFirst_Name(string);
                    } else if (editable.equals(Editable.LAST_NAME)) {
                        user.setLast_Name(string);
                    }
                    name.setValueText(user.getFullName());
                }
            });
        } else if (v == addressRow) {
            toast.setText(getString(R.string.error_dev) + "\naddress");
            toast.show();
        } else if (v == dobRow) {
            toast.setText(getString(R.string.error_dev) + "\ndob");
            toast.show();
        } else if (v == mobileRow) {
            toast.setText(getString(R.string.error_dev) + "\nmobile");
            toast.show();
        } else if (v == pobRow) {
            toast.setText(getString(R.string.error_dev) + "\npob");
            toast.show();
        } else if (v == majorRow) {
            toast.setText(getString(R.string.error_dev) + "\nmajor");
            toast.show();
        } else if (v == rankRow) {
            toast.setText(getString(R.string.error_dev) + "\nrank");
            toast.show();
        } else if (v == joinedRow) {
            toast.setText(getString(R.string.error_dev) + "\njoined");
            toast.show();
        } else if (v == pictureRow) {
            toast.setText(getString(R.string.error_dev) + "\npicture");
            toast.show();
        } else if (v == connectedRow) {
            toast.setText(getString(R.string.error_dev) + "\nconnected");
            toast.show();
        } else {
            toast.setText(R.string.fui_error_unknown);
            toast.show();
        }
        try {
            dialog.show(getParentFragmentManager(), TAG);
        } catch (Exception e) {
            Firebase.Crashlytics.log(TAG, "onClick: dialog could not be displayed", e);
        }
    }

    private void updateName(Map<String, String> nameMap){
        user.setFirst_Name(nameMap.get(Person.FIRST_NAME));
        user.setLast_Name(nameMap.get(Person.LAST_NAME));
        this.name.setValueText(user.getFullName());
    }
}
