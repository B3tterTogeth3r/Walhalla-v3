package de.b3ttertogeth3r.walhalla.fragments_signin;

import static android.app.Activity.RESULT_OK;
import static de.b3ttertogeth3r.walhalla.SignInActivity.imageBitmap;
import static de.b3ttertogeth3r.walhalla.SignInActivity.user;
import static de.b3ttertogeth3r.walhalla.enums.Page.PROFILE;
import static de.b3ttertogeth3r.walhalla.utils.Variables.CAMERA;
import static de.b3ttertogeth3r.walhalla.utils.Variables.GALLERY;

import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.firebase.Timestamp;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.SignInActivity;
import de.b3ttertogeth3r.walhalla.abstraction.CustomFragment;
import de.b3ttertogeth3r.walhalla.design.MyButton;
import de.b3ttertogeth3r.walhalla.design.MyProfileImage;
import de.b3ttertogeth3r.walhalla.design.MyToast;
import de.b3ttertogeth3r.walhalla.design.RowProfile;
import de.b3ttertogeth3r.walhalla.dialog.ChangeSemesterDialog;
import de.b3ttertogeth3r.walhalla.dialog.ChoosePicture;
import de.b3ttertogeth3r.walhalla.dialog.EditDialog;
import de.b3ttertogeth3r.walhalla.enums.Address;
import de.b3ttertogeth3r.walhalla.enums.Display;
import de.b3ttertogeth3r.walhalla.enums.Editable;
import de.b3ttertogeth3r.walhalla.enums.Kind;
import de.b3ttertogeth3r.walhalla.enums.Rank;
import de.b3ttertogeth3r.walhalla.firebase.Analytics;
import de.b3ttertogeth3r.walhalla.firebase.Crashlytics;
import de.b3ttertogeth3r.walhalla.firebase.Firestore;
import de.b3ttertogeth3r.walhalla.firebase.Messaging;
import de.b3ttertogeth3r.walhalla.interfaces.ChangeListener;
import de.b3ttertogeth3r.walhalla.interfaces.MyCompleteListener;
import de.b3ttertogeth3r.walhalla.interfaces.EditListener;
import de.b3ttertogeth3r.walhalla.interfaces.OnDoneListener;
import de.b3ttertogeth3r.walhalla.interfaces.SemesterChangeListener;
import de.b3ttertogeth3r.walhalla.models.Person;
import de.b3ttertogeth3r.walhalla.models.Semester;
import de.b3ttertogeth3r.walhalla.utils.CacheData;

/**
 * @author B3tterToegth3r
 * @version 1.0
 * @since 1.0
 */
public class DataFragment extends CustomFragment implements View.OnClickListener {
    private static final String TAG = "transcript.Fragment";
    private RowProfile mail, name, address, dob, mobile, pob, major, rank, joined, picture;
    private MyButton goOnButton;
    private MyProfileImage profileImage;

    @Override
    public void start () {
        Messaging.getFCMToken(new MyCompleteListener<String>() {
            @Override
            public void onSuccess (String string) {
                user.setFcm_token(string);
            }

            @Override
            public void onFailure (Exception exception) {

            }
        });
    }

    @Override
    public void analyticsProperties () {
        Analytics.screenChange(0, TAG);
    }

    @Override
    public void authStatusChanged () {
        getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, this).commit();
    }

    @Override
    public void stop () {
        user.setChangeListener(null);
    }

    @Override
    public void viewCreated () {
        mail.setOnClickListener(this);
        name.setOnClickListener(this);
        address.setOnClickListener(this);
        dob.setOnClickListener(this);
        mobile.setOnClickListener(this);
        pob.setOnClickListener(this);
        major.setOnClickListener(this);
        rank.setOnClickListener(this);
        joined.setOnClickListener(this);
        picture.setOnClickListener(this);
        goOnButton.setEnabled(false);
        user.setChangeListener((ChangeListener<Person>) change -> {
            if(change.isValid()){
                // enable goOnButton
                goOnButton.setEnabled(true);
                goOnButton.setOnClickListener(DataFragment.this);
            }
        });
    }

    @Override
    public void toolbarContent () {
        toolbar.setTitle(R.string.menu_login);
    }

    @Override
    public void createView (@NonNull @NotNull View view,
                            @NonNull @NotNull LayoutInflater inflater) {
        LinearLayout layout = view.findViewById(R.id.fragment_container);

        try {
            mail = new RowProfile(getContext());
            name = new RowProfile(getContext());
            pob = new RowProfile(getContext());
            mobile = new RowProfile(getContext());
            rank = new RowProfile(getContext());
            major = new RowProfile(getContext());
            address = new RowProfile(getContext());
            joined = new RowProfile(getContext());
            dob = new RowProfile(getContext());
            picture = new RowProfile(getContext());
            goOnButton = new MyButton(getContext());

            mail.setTitleText(R.string.fui_email_hint)
                    .addToTitle("*")
                    .setValueText(user.getEmail());
            name.setTitleText(R.string.full_name)
                    .addToTitle("*")
                    .setValueText(user.getFullName());
            pob.setTitleText(R.string.pob)
                    .setValueText(user.getPoB());
            mobile.setTitleText(R.string.mobile)
                    .setValueText(user.getMobile());
            rank.setTitleText(R.string.rank)
                    .addToTitle("*")
                    .setValueText(user.getRank());
            major.setTitleText(R.string.major)
                    .setValueText(user.getMajor());
            address.setValueText(user.getAddressString())
                    .setTitleText(R.string.address);
            joined.setTitleText(R.string.joined);
            // Format number into clear semester
            Firestore.getSemester(user.getJoined(), new MyCompleteListener<DocumentSnapshot>() {
                @Override
                public void onSuccess (DocumentSnapshot documentSnapshot) {
                    Semester semester = documentSnapshot.toObject(Semester.class);
                    assert semester != null;
                    joined.setValueText(semester.getName_long());
                }

                @Override
                public void onFailure (Exception e) {
                    joined.setValueText(String.valueOf(user.getJoined()));
                }
            });

            dob.setTitleText(R.string.dob)
                    .setValueText(user.getDoBString());
            picture.setTitleText(R.string.profile_image);
            profileImage = new MyProfileImage(getContext());
            picture.replaceValue(profileImage);

            goOnButton.setText(R.string.send);

            //TODO add reasons why the data is needed. maybe just in the dialog
            layout.addView(mail);
            layout.addView(name);
            layout.addView(address);
            layout.addView(dob);
            layout.addView(mobile);
            layout.addView(joined);
            layout.addView(major);
            layout.addView(rank);
            layout.addView(pob);
            layout.addView(picture);
            layout.addView(goOnButton);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onClick (View v) {
        MyToast toast = new MyToast(requireContext());
        EditDialog dialog;
        //region if mail
        if (v == mail) {
            dialog = new EditDialog(Editable.MAIL, user.getEmail(), new EditListener() {
                @Override
                public void saveEdit (Object value, Editable editable) {
                    String mailStr = value.toString();
                    user.setEmail(mailStr);
                    mail.setValueText(mailStr);
                }

                @Override
                public void abort () {
                    mail.setValueText(user.getEmail());
                }

                @Override
                public void sendError (Editable editable, String s) {
                    mail.setValueText(user.getEmail());
                    toast.setText(s);
                    toast.show();
                }

                @Override
                public void sendLiveChange (String string, Editable editable) {
                    mail.setValueText(string);
                }
            });
            dialog.show(getParentFragmentManager(), TAG);
        }
        //endregion
        //region else if name
        else if (v == name) {
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
                        Crashlytics.log(TAG, R.string.fui_missing_first_and_last_name);
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
        }
        //endregion
        //region else if address
        else if (v == address) {
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
                        Crashlytics.log(TAG, R.string.fui_missing_first_and_last_name);
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
        }
        //endregion
        //region else if dob
        else if (v == dob) {
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
        }
        //endregion
        //region else if mobile
        else if (v == mobile) {
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
        }
        //endregion
        //region else if pob
        else if (v == pob) {
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
        }
        //endregion
        //region else if major
        else if (v == major) {
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
        }
        //endregion
        //region else if rank
        else if (v == rank) {
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
        }
        //endregion
        //region else if joined
        else if (v == joined) {
            new ChangeSemesterDialog(Kind.JOINED, new SemesterChangeListener() {
                @Override
                public void joinedDone (Semester semester) {
                    user.setJoined(semester.getId());
                    joined.setValueText(semester.getName_long());
                }
            }, CacheData.getCurrentSemester()).show(getParentFragmentManager(), TAG);
        }
        //endregion
        //region else if picture
        else if (v == picture) {
            new ChoosePicture(requireContext(), new OnDoneListener() {
                @Override
                public void positive (Intent resultIntent) {
                    int value = resultIntent.getIntExtra("ImageSelector", -1);
                    switch (value) {
                        case CAMERA:
                            Log.d(TAG, "positive: start activity for result camera");
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            try {
                                startActivityForResult(cameraIntent, CAMERA);
                            } catch (ActivityNotFoundException e) {
                                //TODO Ask user permission to user camera
                                e.printStackTrace();
                            }
                            break;
                        case GALLERY:
                            Log.d(TAG, "positive: get picture from local gallery");
                            Intent galleryIntent = new Intent();
                            galleryIntent.setType("image/*");
                            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                            try {
                                startActivityForResult(Intent.createChooser(galleryIntent,
                                        "Select Picture"), GALLERY);
                            } catch (ActivityNotFoundException e) {
                                //TODO Ask user for permission to use storage
                                e.printStackTrace();
                            }
                            break;
                        default:
                            Log.e(TAG, "positive: an error occurred\nvalue is not 0 or 1");
                            break;
                    }
                    toast.setText(getString(R.string.error_dev) + "\npicture");
                    toast.show();
                }

                @Override
                public void negative (Intent resultIntent) {
                }
            }).create().show();
            toast.setText(getString(R.string.error_dev) + "\npicture");
            toast.show();
        }
        //endregion
        //region else if goOnButton
        else if (v == goOnButton) {
            // if everything is ok, open dialog or fragment to set a password
            //check, if every necessary field is filled
            if (user.isValid()) {
                toast.setMessage(R.string.error_dev).show();
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new PasswordFragment(Display.ADD))
                        .addToBackStack(SignInActivity.TAG)
                        .commit();
            } else {
                toast.setMessage("please fill all fields marked with *").show();
            }
        }
        //endregion
        //region else
        else {
            toast.setText(R.string.fui_error_unknown);
            toast.show();
        }
        //endregion
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
        int size;
        CharSequence[] ranks;
        if (PROFILE.canEditPage(CacheData.getCharge())) {
            size = list.length;
            ranks = new CharSequence[size];
        } else {
            size = 9;
            ranks = new CharSequence[9];
        }
        for (int i = 0; i < size; i++) {
            ranks[i] = list[i].toString();
        }
        return ranks;
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA:
                    Bundle extras = data.getExtras();
                    //TODO Rotate camera image by 90° counter clock wise
                    imageBitmap = extras.getParcelable("data");
                    if (imageBitmap == null) {
                        return;
                    }
                    profileImage.setImageBitmap(imageBitmap);
                    break;
                case GALLERY:
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream =
                                requireContext().getContentResolver().openInputStream(imageUri);
                        imageBitmap = BitmapFactory.decodeStream(imageStream);
                        profileImage.setImageBitmap(imageBitmap);
                    } catch (Exception ignored) {
                        // no image chosen or context == null
                    }
                    break;
                default:
                    break;
            }
            return;
        }
        Log.d(TAG, "onActivityResult: result not ok: " + resultCode);
    }

}
