package de.b3ttertogeth3r.walhalla.fragments.profile;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstraction.CustomFragment;
import de.b3ttertogeth3r.walhalla.design.MyTable;
import de.b3ttertogeth3r.walhalla.design.MyTableRow;
import de.b3ttertogeth3r.walhalla.design.MyToast;
import de.b3ttertogeth3r.walhalla.design.ProfileRow;
import de.b3ttertogeth3r.walhalla.dialog.ChangeSemesterDialog;
import de.b3ttertogeth3r.walhalla.dialog.EditDialog;
import de.b3ttertogeth3r.walhalla.enums.Address;
import de.b3ttertogeth3r.walhalla.enums.Editable;
import de.b3ttertogeth3r.walhalla.enums.Kind;
import de.b3ttertogeth3r.walhalla.enums.Rank;
import de.b3ttertogeth3r.walhalla.exceptions.PersonException;
import de.b3ttertogeth3r.walhalla.firebase.Firebase;
import de.b3ttertogeth3r.walhalla.firebase.Firebase.Authentication;
import de.b3ttertogeth3r.walhalla.firebase.Firebase.Firestore;
import de.b3ttertogeth3r.walhalla.firebase.Firebase.Storage;
import de.b3ttertogeth3r.walhalla.interfaces.EditListener;
import de.b3ttertogeth3r.walhalla.interfaces.OnGetDataListener;
import de.b3ttertogeth3r.walhalla.interfaces.SemesterChangeListener;
import de.b3ttertogeth3r.walhalla.models.Person;
import de.b3ttertogeth3r.walhalla.models.Semester;
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
    private Bitmap imageBitmap = null;

    @Override
    public void start () {
    }

    @Override
    public void analyticsProperties () {

    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void stop () {
        //Update changes and set values in users auth too

        /* Data to check before uploading it
         * every field has a value
         */
        if (user.isValid()) {
            try {
                FirebaseUser firebaseUser = Firebase.AUTH.getCurrentUser();
                user.setUid(firebaseUser.getUid());
                //if image selected upload otherwise skip
                if (imageBitmap != null) {
                    Storage.uploadImage(imageBitmap, user.getFullName(), new OnGetDataListener() {
                        @Override
                        public void onSuccess (Uri imageUri) {
                            user.setPicture_path(imageUri.getPath());
                            upload(user, imageUri);
                        }
                    });
                } else {
                    upload(user, null);
                }
            } catch (Exception e) {
                Log.e(TAG, "stop: ", e);
            }
        } else {
            Log.d(TAG, "stop: some data isn't right");
        }
    }

    private void upload (@NonNull Person user, @Nullable Uri imageUri) {
        Firestore.uploadPerson(user);

        Authentication.updateProfileData(imageUri, user.getFullName(), user.getMail());
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
        joinedRow.addView(joined);
        //Format into semester name, not just the id
        Firebase.Firestore.getSemester(user.getJoined(), new OnGetDataListener() {
            @Override
            public void onSuccess (DocumentSnapshot documentSnapshot) {
                Semester semester = documentSnapshot.toObject(Semester.class);
                assert semester != null;
                joined.setValueText(semester.getName_long());
            }
        });

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
        EditDialog dialog;
        if (v == mailRow) {
            dialog = new EditDialog(Editable.MAIL, user.getMail(), new EditListener() {
                @Override
                public void saveEdit (Object value, Editable editable) {
                    String mailStr = value.toString();
                    user.setMail(mailStr);
                    mail.setValueText(mailStr);
                }

                @Override
                public void abort () {
                    mail.setValueText(user.getMail());
                }

                @Override
                public void sendError (Editable editable, String s) {
                    mail.setValueText(user.getMail());
                    toast.setText(s);
                    toast.show();
                }

                @Override
                public void sendLiveChange (String string, Editable editable) {
                    mail.setValueText(string);
                }
            });
            dialog.show(getParentFragmentManager(), TAG);
        } else if (v == nameRow) {
            Map<String, String> nameBackup = new HashMap<>();
            nameBackup.put(Person.FIRST_NAME, user.getFirst_Name());
            nameBackup.put(Person.LAST_NAME, user.getLast_Name());
            dialog = new EditDialog(Editable.NAME, nameBackup, new EditListener() {
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
            dialog.show(getParentFragmentManager(), TAG);
        } else if (v == addressRow) {
            Map<String, Object> addressBackup = new HashMap<>();
            addressBackup.put(Address.NUMBER.toString(),
                    user.getAddress().get(Address.NUMBER.toString()));
            addressBackup.put(Address.STREET.toString(),
                    user.getAddress().get(Address.STREET.toString()));
            addressBackup.put(Address.ZIP.toString(),
                    user.getAddress().get(Address.ZIP.toString()));
            addressBackup.put(Address.CITY.toString(),
                    user.getAddress().get(Address.CITY.toString()));
            Map<String, Object> address = new HashMap<>(addressBackup);
            dialog = new EditDialog(Editable.ADDRESS, addressBackup, new EditListener() {
                @Override
                public void saveEdit (Object value, Editable editable) {
                    try {
                        updateAddress((Map<String, Object>) value);
                    } catch (Exception e) {
                        updateAddress(addressBackup);
                        Firebase.Crashlytics.log(TAG, R.string.fui_missing_first_and_last_name);
                        toast.setText(R.string.fui_missing_first_and_last_name);
                        toast.show();
                    }
                }

                @Override
                public void abort () {
                    updateAddress(addressBackup);
                }

                @Override
                public void sendError (Editable editable, String s) {
                    updateAddress(addressBackup);
                    toast.setText(s);
                    toast.show();
                }

                @Override
                public void sendLiveChange (String string, Editable editable) {
                    switch (editable) {
                        case STREET:
                            address.put(Address.STREET.toString(), string);
                            break;
                        case NUMBER:
                            address.put(Address.NUMBER.toString(), string);
                            break;
                        case ZIP:
                            address.put(Address.ZIP.toString(), string);
                            break;
                        case CITY:
                            address.put(Address.CITY.toString(), string);
                            break;
                    }
                    updateAddress(address);
                }
            });
            dialog.show(getParentFragmentManager(), TAG);
        } else if (v == dobRow) {
            Calendar date = Calendar.getInstance();
            date.setTime(user.getDoB().toDate());
            DatePickerDialog birthday = new DatePickerDialog(requireContext(),
                    (view, year1, month1, dayOfMonth) -> {
                        Calendar c = Calendar.getInstance();
                        c.set(year1, month1, dayOfMonth);
                        user.setDoB(new Timestamp(c.getTime()));
                        dob.setValueText(user.getDoBString());
                    }, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE));
            birthday.show();
        } else if (v == mobileRow) {
            dialog = new EditDialog(Editable.MOBILE, user.getMobile(), new EditListener() {
                @Override
                public void saveEdit (Object value, Editable editable) {
                    user.setMobile(value.toString());
                    mobile.setValueText(value.toString());
                }

                @Override
                public void abort () {
                    mobile.setValueText(user.getMobile());
                }

                @Override
                public void sendError (Editable editable, String s) {
                    mobile.setValueText(user.getMobile());
                }

                @Override
                public void sendLiveChange (String string, Editable editable) {
                    mobile.setValueText(string);
                }
            });
            dialog.show(getParentFragmentManager(), TAG);
        } else if (v == pobRow) {
            dialog = new EditDialog(Editable.POB, user.getPoB(), new EditListener() {
                @Override
                public void saveEdit (Object value, Editable editable) {
                    user.setPoB(value.toString());
                    pob.setValueText(value.toString());
                }

                @Override
                public void abort () {
                    pob.setValueText(user.getPoB());
                }

                @Override
                public void sendError (Editable editable, String s) {
                    pob.setValueText(user.getPoB());
                }

                @Override
                public void sendLiveChange (String string, Editable editable) {
                    pob.setValueText(string);
                }
            });
            dialog.show(getParentFragmentManager(), TAG);
        } else if (v == majorRow) {
            dialog = new EditDialog(Editable.MAJOR, user.getMajor(), new EditListener() {
                @Override
                public void saveEdit (Object value, Editable editable) {
                    String majorStr = value.toString();
                    major.setValueText(majorStr);
                    user.setMajor(majorStr);
                }

                @Override
                public void abort () {
                    major.setValueText(user.getMajor());
                }

                @Override
                public void sendError (Editable editable, String s) {
                    major.setValueText(user.getMajor());
                }

                @Override
                public void sendLiveChange (String string, Editable editable) {
                    major.setValueText(string);
                }
            });
            dialog.show(getParentFragmentManager(), TAG);
        } else if (v == rankRow) {
            try {
                CharSequence[] rankList = getRanks();
                final int[] position = {0};
                for (int i = 0; i < rankList.length; i++) {
                    if (rankList[i].equals(user.getRank())) {
                        position[0] = i;
                        break;
                    }
                }

                AlertDialog rankDialog = new AlertDialog.Builder(requireContext())
                        .setTitle(R.string.rank)
                        .setSingleChoiceItems(rankList, position[0],
                                (dialog1, which) -> position[0] = which)
                        .setPositiveButton(R.string.save, (dialog12, which) -> {
                            user.setRank(rankList[position[0]].toString());
                            rank.setValueText(user.getRank());
                        })
                        .setNegativeButton(R.string.abort,
                                (dialog13, which) -> rank.setValueText(user.getRank()))
                        .create();
                rankDialog.show();
            } catch (Exception e) {
                Log.e(TAG, "onClick: ", e);
            }
        } else if (v == joinedRow) {
            new ChangeSemesterDialog(Kind.JOINED, new SemesterChangeListener() {
                @Override
                public void joinedDone (Semester semester) {
                    user.setJoined(semester.getId());
                    joined.setValueText(semester.getName_long());
                }
            }, CacheData.getCurrentSemester()).show(getParentFragmentManager(), TAG);
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
    }

    private void updateName (@NonNull Map<String, String> nameMap) {
        user.setFirst_Name(nameMap.get(Person.FIRST_NAME));
        user.setLast_Name(nameMap.get(Person.LAST_NAME));
        this.name.setValueText(user.getFullName());
    }

    private void updateAddress (Map<String, Object> addressMap) {
        user.setAddress(addressMap);
        this.address.setValueText(user.getAddressString());
    }

    @NonNull
    private CharSequence[] getRanks () {
        Rank[] list = Rank.values();
        //TODO if user == super-admin set all ranks into the list.
        CharSequence[] ranks = new CharSequence[9];
        for (int i = 0; i < 9; i++) {
            ranks[i] = list[i].toString();
        }
        return ranks;
    }
}
