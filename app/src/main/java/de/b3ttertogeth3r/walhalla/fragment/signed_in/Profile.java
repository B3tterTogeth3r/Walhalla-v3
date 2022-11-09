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

package de.b3ttertogeth3r.walhalla.fragment.signed_in;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;

import java.io.InvalidObjectException;
import java.nio.channels.UnsupportedAddressTypeException;
import java.util.ArrayList;
import java.util.Calendar;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstract_generic.Fragment;
import de.b3ttertogeth3r.walhalla.design.ProfileRow;
import de.b3ttertogeth3r.walhalla.design.SideNav;
import de.b3ttertogeth3r.walhalla.design.Text;
import de.b3ttertogeth3r.walhalla.design.Toast;
import de.b3ttertogeth3r.walhalla.dialog.AddressDialog;
import de.b3ttertogeth3r.walhalla.dialog.ChangeSemester;
import de.b3ttertogeth3r.walhalla.dialog.ProfileEditDialog;
import de.b3ttertogeth3r.walhalla.dialog.RankSelectDialog;
import de.b3ttertogeth3r.walhalla.exception.NoDataException;
import de.b3ttertogeth3r.walhalla.exception.UploadError;
import de.b3ttertogeth3r.walhalla.firebase.Firebase;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IAuth;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IFirestoreDownload;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IFirestoreUpload;
import de.b3ttertogeth3r.walhalla.object.Address;
import de.b3ttertogeth3r.walhalla.object.Person;
import de.b3ttertogeth3r.walhalla.object.Semester;
import de.b3ttertogeth3r.walhalla.util.Log;
import de.b3ttertogeth3r.walhalla.util.Values;

public class Profile extends Fragment implements View.OnClickListener {
    private static final String TAG = "Profile";
    Text uidText;
    private IAuth auth;
    private IFirestoreDownload download;
    private Person person;
    private LinearLayout personLayout;
    private LinearLayout addressLayout;
    private String personId;
    private ArrayList<Address> addressList;
    private IFirestoreUpload upload;

    @Override
    public void constructor() {
        auth = Firebase.authentication();
        upload = Firebase.Firestore.upload();
        download = Firebase.Firestore.download();
        if (!auth.isSignIn()) {
            Toast.makeToast(requireContext(), R.string.fui_error_session_expired).show();
            SideNav.changePage(R.string.menu_home, requireActivity().getSupportFragmentManager().beginTransaction());
        }
    }

    @Override
    public String analyticsProperties() {
        return TAG;
    }

    @Override
    public void start() {
        if (auth.isSignIn()) {
            personId = auth.getUser().getUid();
            download.person(personId)
                    .setOnSuccessListener(result -> {
                        if (result == null || !result.validate()) {
                            throw new NoDataException("No person data downloaded");
                        }
                        this.person = result;
                        personLayout.removeAllViewsInLayout();
                        personLayout.addView(person.getViewEdit(requireContext(), this));
                    })
                    .setOnFailListener(e -> Log.e(TAG, "onFailureListener: ", e));
            download.personAddress(personId)
                    .setOnSuccessListener(result -> {
                        if (result == null || result.isEmpty()) {
                            throw new NoDataException("No person address downloaded");
                        }
                        this.addressList = result;
                        addressLayout.removeAllViewsInLayout();
                        for (int i = 0; i < addressList.size(); i++) {
                            Address a = addressList.get(i);
                            if (a.validate()) {
                                ProfileRow address = new ProfileRow(requireContext(), true);
                                address.setId(a.getId().hashCode());
                                address.setTitle(R.string.address).setContent(a.toString()).setOnClickListener(this);
                                addressLayout.addView(address);
                            }
                        }
                    })
                    .setOnFailListener(e -> Log.e(TAG, "onFailureListener: ", e));
            download.getPersonImage(personId)
                    .setOnSuccessListener(result -> {
                        if (result == null || result.isEmpty()) {
                            throw new NoDataException("User has no profile image");
                        }
                        // TODO: 25.07.22 find a way to display the profile image
                    })
                    .setOnFailListener(e -> {
                    });//Log.e(TAG, "onFailureListener: ", e));
            try {
                uidText.setText(personId);
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    public void toolbarContent() {
        toolbar.setTitle(R.string.menu_profile);
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.default_save_abort);
        toolbar.getMenu().getItem(0).setVisible(false);
        toolbar.getMenu().getItem(1)
                .setOnMenuItemClickListener(menuItem -> {
                    Log.i(TAG, "toolbarContent: saving user profile");
                    try {
                        uploadData();
                    } catch (Exception e) {
                        Toast.makeToast(requireContext(), e.getMessage()).show();
                        Log.e(TAG, "stop: ", e);
                    }
                    return false;
                });
    }

    @Override
    public void createView(@NonNull LinearLayout view) {
        view.setOrientation(LinearLayout.VERTICAL);
        personLayout = new LinearLayout(requireContext());
        addressLayout = new LinearLayout(requireContext());
        addressLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout imageLayout = new LinearLayout(requireContext());
        imageLayout.setOrientation(LinearLayout.VERTICAL);
        view.addView(personLayout);
        view.addView(addressLayout);
        //view.addView(imageLayout);
        uidText = new Text(requireContext(), personId);
        uidText.setAlpha(0.3f);
        view.addView(uidText);
    }

    @Override
    public void viewCreated() {
        uidText.setText(personId);
    }

    @Override
    public void stop() {
        try {
            uploadData();
        } catch (Exception e) {
            Toast.makeToast(requireContext(), e.getMessage()).show();
            Log.e(TAG, "stop: ", e);
        }
    }

    @Override
    public FragmentActivity authStatusChanged(FirebaseAuth firebaseAuth) {
        return requireActivity();
    }

    private void uploadData() throws Exception {
        for (Address a : addressList) {
            if (!a.validate()) {
                throw new InvalidObjectException("An address is invalid");
            }
        }
        if (!person.validate()) {
            throw new InvalidObjectException("Person data is invalid");
        }
        // TODO: 26.07.22 upload data
        upload.setPerson(person)
                .setOnSuccessListener(result -> {
                    if (result == null || !result) {
                        throw new UploadError("unable to upload person");
                    }
                }).setOnFailListener(e -> Log.e(TAG, "uploadData: ", e));
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        if (view != null) {
            try {
                switch (view.getId()) {
                    case R.id.first_name:
                        Log.i(TAG, "onClick: name");
                        ProfileEditDialog.display(getParentFragmentManager(), person.getFirst_Name())
                                .setOnSuccessListener(result -> {
                                    ((ProfileRow) view).setContent(result);
                                    person.setFirst_Name(String.valueOf(result));
                                });
                        break;
                    case R.id.last_name:
                        Log.i(TAG, "onClick: name");
                        ProfileEditDialog.display(getParentFragmentManager(), person.getLast_Name())
                                .setOnSuccessListener(((ProfileRow) view)::setContent);
                        break;
                    case R.id.mobile:
                        Log.i(TAG, "onClick: mobile");
                        ProfileEditDialog.display(getParentFragmentManager(), person.getMobile())
                                .changeInputType(InputType.TYPE_CLASS_PHONE)
                                .setOnSuccessListener(((ProfileRow) view)::setContent);
                        break;
                    case R.id.from:
                        Log.i(TAG, "onClick: from");
                        ProfileEditDialog.display(getParentFragmentManager(), person.getOrigin())
                                .setOnSuccessListener(((ProfileRow) view)::setContent);
                        break;
                    case R.id.major:
                        Log.i(TAG, "onClick: major");
                        ProfileEditDialog.display(getParentFragmentManager(), person.getMajor())
                                .setOnSuccessListener(((ProfileRow) view)::setContent);
                        break;
                    case R.id.nickname:
                        Log.i(TAG, "onClick: nickname");
                        ProfileEditDialog.display(getParentFragmentManager(), person.getNickname())
                                .setOnSuccessListener(((ProfileRow) view)::setContent);
                        break;
                    case R.id.dob:
                        Log.i(TAG, "onClick: dob");

                        // Create dialog to change the birthday
                        Calendar date = Calendar.getInstance();
                        if (person.getBirthday() != null) {
                            date.setTime(person.getBirthday().toDate());
                        }
                        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                Calendar date = Calendar.getInstance();
                                date.set(Calendar.YEAR, year);
                                date.set(Calendar.MONTH, month);
                                date.set(Calendar.DAY_OF_MONTH, day);
                                person.setBirthday(new Timestamp(date.getTime()));
                                ((ProfileRow) view).setContent(day + "." + (month + 1) + "." + year);
                            }
                        }, date.get(Calendar.YEAR), date.get(Calendar.MONTH),
                                date.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.show();
                        break;
                    case R.id.rank:
                        Log.i(TAG, "onClick: rank");
                        RankSelectDialog.display(getParentFragmentManager())
                                .setOnSuccessListener(result -> {
                                    if (result != null) {
                                        ((ProfileRow) view).setContent(result.toString());
                                        person.setRank(result);
                                    }
                                });
                        break;
                    case R.id.joined:
                        Log.i(TAG, "onClick: joined");
                        ChangeSemester.display(getParentFragmentManager(), new Semester(person.getJoined()))
                                .setOnSuccessListener(result -> {
                                    if (result == null || result == -1) {
                                        throw new NullPointerException("Semester id cannot be null or -1");
                                    }
                                    person.setJoined(result);
                                    ((ProfileRow) view).setContent(Values.semesterList.get(result).getName_long());
                                });
                        break;
                    default:
                        for (Address address : addressList) {
                            if (address.getId().hashCode() == view.getId()) {
                                Log.i(TAG, "onClick: address clicked: " + address.getId());
                                AddressDialog addressDialog = AddressDialog.display(getParentFragmentManager(), address);
                                addressDialog.setOnSuccessListener(result -> {
                                            if (result != null && result.validate()) {
                                                addressList.remove(address);
                                                addressList.add(result);
                                                ((ProfileRow) view).setContent(result.toString());
                                                return;
                                            }
                                            throw new UnsupportedAddressTypeException();
                                        })
                                        .setOnFailureListener(e -> {
                                            Log.e(TAG, "onFailureListener: something went wrong with the AddressDialog", e);
                                            Toast.makeToast(requireContext(), R.string.error_address).show();
                                        });
                                Toast.makeToast(requireContext(), R.string.error_dev).show();
                                break;
                            }
                        }
                        Log.i(TAG, "onClick: id not found; id: " + view.getId());
                        break;
                }
            } catch (Exception e) {
                Log.e(TAG, "onClick: ", e);
            }
        }
    }

    @NonNull
    @Override
    public String toString() {
        return TAG + "extends Fragment implements View.OnClickListener";
    }
}
