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

package de.b3ttertogeth3r.walhalla.fragment.sign_in;

import android.app.DatePickerDialog;
import android.icu.text.SimpleDateFormat;
import android.text.InputType;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.Timestamp;

import java.nio.channels.UnsupportedAddressTypeException;
import java.util.ArrayList;
import java.util.Calendar;

import de.b3ttertogeth3r.walhalla.MainActivity;
import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstract_classes.Fragment;
import de.b3ttertogeth3r.walhalla.abstract_classes.Touch;
import de.b3ttertogeth3r.walhalla.design.Button;
import de.b3ttertogeth3r.walhalla.design.ProfileRow;
import de.b3ttertogeth3r.walhalla.design.Title;
import de.b3ttertogeth3r.walhalla.design.Toast;
import de.b3ttertogeth3r.walhalla.dialog.AddressDialog;
import de.b3ttertogeth3r.walhalla.dialog.InfoDialog;
import de.b3ttertogeth3r.walhalla.dialog.ProfileEditDialog;
import de.b3ttertogeth3r.walhalla.exception.CreateDialogException;
import de.b3ttertogeth3r.walhalla.interfaces.activityMain.IOnBackPressed;
import de.b3ttertogeth3r.walhalla.object.Address;
import de.b3ttertogeth3r.walhalla.object.Log;
import de.b3ttertogeth3r.walhalla.object.Person;
import de.b3ttertogeth3r.walhalla.util.Values;

public class PersonalData extends Fragment implements IOnBackPressed, DatePickerDialog.OnDateSetListener {
    private static final String TAG = "PersonalData";
    private final Person person;
    private final ArrayList<Address> addressList;
    private ProfileRow firstName, lastName, address, mobile, birthday, major;
    private FragmentManager fm;

    public PersonalData(String email) {
        this.person = new Person();
        this.person.setMail(email);
        this.addressList = new ArrayList<>();
    }

    @Override
    public void createView(@NonNull LinearLayout view) {
        fm = getParentFragmentManager();
        view.setOrientation(LinearLayout.VERTICAL);

        Title register = new Title(requireContext());
        register.setTitle("Persönliche Daten");
        view.addView(register);

        //region ProfileRows
        // Creating view for the user to fill out all the necessary personal data
        //  1. name, address, email address are necessary
        //  2. mobile number, dob, pob and major/job are mandatory
        firstName = new ProfileRow(getContext());
        String firstNameStr = requireContext().getString(R.string.first_name) + "*";
        firstName.setTitle(firstNameStr)
                .addTouchListener(new Touch() {
                    @Override
                    public void onClick(View view) {
                        // Create dialog to change the content of the view
                        FragmentManager fm = getChildFragmentManager();
                        try {
                            ProfileEditDialog dialog = ProfileEditDialog.display(fm, firstName.getContent());
                            dialog.setOnSuccessListener(result -> {
                                        if (result != null && result.length() != 0) {
                                            firstName.setContent(result);
                                            person.setFirst_Name(result.toString());
                                        }
                                    })
                                    .onFailureListener(e -> Log.e(TAG, "onFailureListener: ProfileEditDialog Error", e));
                        } catch (CreateDialogException e) {
                            e.printStackTrace();
                        }
                    }
                });
        view.addView(firstName);

        lastName = new ProfileRow(getContext());
        String lastNameStr = requireContext().getString(R.string.last_name) + "*";
        lastName.setTitle(lastNameStr)
                .addTouchListener(new Touch() {
                    @Override
                    public void onClick(View view) {
                        // Create dialog to change the content of the view
                        FragmentManager fm = getChildFragmentManager();
                        try {
                            ProfileEditDialog dialog = ProfileEditDialog.display(fm, lastName.getContent());
                            dialog.setOnSuccessListener(result -> {
                                        if (result != null && result.length() != 0) {
                                            lastName.setContent(result);
                                            person.setLast_Name(result.toString());
                                        }
                                    })
                                    .onFailureListener(e -> Log.e(TAG, "onFailureListener: something went wrong with the ProfileEditDialog", e));
                        } catch (CreateDialogException e) {
                            e.printStackTrace();
                        }
                    }
                });
        view.addView(lastName);

        address = new ProfileRow(getContext());
        String addressStr = requireContext().getString(R.string.address) + "*";
        address.setTitle(addressStr)
                .addTouchListener(new Touch() {
                    @Override
                    public void onClick(View view) {
                        // Create dialog to change the content of the view
                        // TODO: 03.07.22 change field to an address field
                        FragmentManager fm = getChildFragmentManager();
                        try {
                            AddressDialog dialog;
                            if (addressList.size() == 0) {
                                dialog = AddressDialog.display(fm);
                            } else {
                                dialog = AddressDialog.display(fm, addressList.get(0));
                            }
                            dialog.setOnSuccessListener(result -> {
                                        if (result != null && result.validate()) {
                                            addressList.add(result);
                                            address.setContent(result.toString());
                                            return;
                                        }
                                        throw new UnsupportedAddressTypeException();
                                    })
                                    .onFailureListener(e -> {
                                        Log.e(TAG, "onFailureListener: something went wrong with the AddressDialog", e);
                                        Toast.makeToast(requireContext(), R.string.error_address).show();
                                    });
                        } catch (CreateDialogException e) {
                            Log.e(TAG, "onClick: ", e);
                        }
                    }
                });
        view.addView(address);

        mobile = new ProfileRow(getContext());
        mobile.setTitle(R.string.mobile)
                .addTouchListener(new Touch() {
                    @Override
                    public void onClick(View view) {
                        // Create dialog to change the content of the view
                        // TODO: 03.07.22 change input style to phone number
                        FragmentManager fm = getChildFragmentManager();
                        try {
                            ProfileEditDialog dialog = ProfileEditDialog.display(fm, mobile.getContent());
                            dialog.changeInputType(InputType.TYPE_CLASS_PHONE)
                                    .setOnSuccessListener(result -> {
                                        if (result != null && result.length() != 0) {
                                            mobile.setContent(result);
                                            person.setMobile(result.toString());
                                        }
                                    })
                                    .onFailureListener(e -> Log.e(TAG, "onFailureListener: something went wrong with the ProfileEditDialog", e));
                        } catch (CreateDialogException e) {
                            e.printStackTrace();
                        }
                    }
                });
        view.addView(mobile);

        birthday = new ProfileRow(getContext());
        birthday.setTitle(R.string.dob)
                .addTouchListener(new Touch() {
                    @Override
                    public void onClick(View view) {
                        // Create dialog to change the birthday
                        Calendar date = Calendar.getInstance();
                        if (person.getBirthday() != null) {
                            date.setTime(person.getBirthday().toDate());
                        }
                        DatePickerDialog dialog = new DatePickerDialog(requireContext(), PersonalData.this,
                                date.get(Calendar.YEAR), date.get(Calendar.MONTH),
                                date.get(Calendar.DAY_OF_MONTH));
                        dialog.show();
                    }
                });
        view.addView(birthday);

        major = new ProfileRow(getContext());
        major.setTitle(R.string.major)
                .addTouchListener(new Touch() {
                    @Override
                    public void onClick(View view) {
                        // Create dialog to change the content of the view
                        FragmentManager fm = getChildFragmentManager();
                        try {
                            ProfileEditDialog dialog = ProfileEditDialog.display(fm, major.getContent());
                            dialog.setOnSuccessListener(result -> {
                                        if (result != null && result.length() != 0) {
                                            major.setContent(result);
                                            person.setMajor(result.toString());
                                        }
                                    })
                                    .onFailureListener(e -> Log.e(TAG, "onFailureListener: something went wrong with the ProfileEditDialog", e));
                        } catch (CreateDialogException e) {
                            e.printStackTrace();
                        }
                    }
                });
        view.addView(major);
        //endregion

        //region buttons
        int dp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                4f,
                requireContext().getResources().getDisplayMetrics()
        );
        RelativeLayout buttonLayout = new RelativeLayout(requireContext());
        RelativeLayout.LayoutParams reParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        reParams.setMargins(dp, dp, dp, dp);
        buttonLayout.setLayoutParams(reParams);

        RelativeLayout.LayoutParams backParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        backParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        Button back = new Button(requireContext());
        back.setText(R.string.go_back);
        back.addTouchListener(new Touch() {
            @Override
            public void onClick(View view) {
                fm.popBackStack();
            }
        });
        back.setLayoutParams(backParams);
        buttonLayout.addView(back);

        RelativeLayout.LayoutParams nextParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        Button next = new Button(requireContext());
        next.setText(R.string.resume);
        next.addTouchListener(new Touch() {
            @Override
            public void onClick(View view) {
                if (person.validatePersonal() && addressList.size() > 0) {
                    fm.beginTransaction()
                            .replace(R.id.fragment_container, new FraternityData(person, addressList))
                            .addToBackStack("SignIn")
                            .commit();
                    return;
                }
                Toast.makeToast(requireContext(), R.string.error).show();
            }
        });
        nextParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        next.setLayoutParams(nextParams);
        buttonLayout.addView(next);
        view.addView(buttonLayout);
        //endregion
    }

    @Override
    public void toolbarContent() {
        toolbar.setTitle("Persönliche Daten");
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.info);
        toolbar.setOnMenuItemClickListener(item -> {
            // TODO: 05.07.22 open dialog with description which data is needed and why
            try {
                InfoDialog dialog = new InfoDialog("I am an info text");
                dialog.show(fm, TAG);
            } catch (Exception e) {
                Log.e(TAG, "toolbarContent: creating info dialog did not work", e);
            }
            return false;
        });
    }

    @Override
    public void viewCreated() {
        if (addressList.size() != 0) {
            address.setContent(addressList.get(0).toString());
        }
        firstName.setContent(person.getFirst_Name());
        lastName.setContent(person.getLast_Name());
        mobile.setContent(person.getMobile());
        major.setContent(person.getMajor());
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Values.LOCALE);
        try {
            birthday.setContent(sdf.format(person.getBirthday().toDate()));
        } catch (Exception ignored) {
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public String analyticsProperties() {
        return TAG;
    }

    @Override
    public void start() {
        MainActivity.hideKeyBoard.hide();
    }

    @Override
    public boolean onBackPressed() {
        if (fm.getBackStackEntryCount() != 0) {
            fm.popBackStack();
            return true;
        }
        return false;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Log.i(TAG, "onDateSet: i: " + year + "; i1: " + month + "; i2: " + day);
        Calendar date = Calendar.getInstance();
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, month);
        date.set(Calendar.DAY_OF_MONTH, day);
        this.person.setBirthday(new Timestamp(date.getTime()));
        this.birthday.setContent(day + "." + (month + 1) + "." + year);
    }
}
