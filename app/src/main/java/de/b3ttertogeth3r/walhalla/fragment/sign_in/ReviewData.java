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

import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.itextpdf.text.exceptions.BadPasswordException;

import java.util.ArrayList;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstract_classes.Fragment;
import de.b3ttertogeth3r.walhalla.abstract_classes.Touch;
import de.b3ttertogeth3r.walhalla.design.Button;
import de.b3ttertogeth3r.walhalla.design.ProfileRow;
import de.b3ttertogeth3r.walhalla.design.Toast;
import de.b3ttertogeth3r.walhalla.dialog.SetPasswordDialog;
import de.b3ttertogeth3r.walhalla.exception.CreateDialogException;
import de.b3ttertogeth3r.walhalla.exception.UploadError;
import de.b3ttertogeth3r.walhalla.exception.UserDataError;
import de.b3ttertogeth3r.walhalla.fragment.Home;
import de.b3ttertogeth3r.walhalla.interfaces.IAuth;
import de.b3ttertogeth3r.walhalla.interfaces.IFirestoreUpload;
import de.b3ttertogeth3r.walhalla.interfaces.IOnBackPressed;
import de.b3ttertogeth3r.walhalla.mock.AuthMock;
import de.b3ttertogeth3r.walhalla.mock.FirestoreMock;
import de.b3ttertogeth3r.walhalla.object.Address;
import de.b3ttertogeth3r.walhalla.object.Log;
import de.b3ttertogeth3r.walhalla.object.Person;

/**
 * A class to review the data set by the user previously before registering the user.
 */
public class ReviewData extends Fragment implements IOnBackPressed {
    private static final String TAG = "ReviewData";
    private final Person p;
    private final ArrayList<Address> addressList;
    private final IFirestoreUpload upload;
    private final IAuth auth;
    private FragmentManager fm;

    /**
     * Show the data to review to the user. If the data seems fine, the user is asked to set a
     * password. Afterwards the data is uploaded and the user is registered.
     *
     * @param p           data input from the previous fragments
     * @param addressList address put in the {@link PersonalData} fragment
     */
    public ReviewData(Person p, ArrayList<Address> addressList) {
        this.p = p;
        this.addressList = addressList;
        upload = new FirestoreMock.Upload();
        auth = new AuthMock();
    }

    @Override
    public void createView(@NonNull LinearLayout view) {
        view.setOrientation(LinearLayout.VERTICAL);
        fm = getParentFragmentManager();
        view.addView(p.getViewDisplay(requireContext()));
        ProfileRow address = new ProfileRow(requireContext(), false);
        address.setTitle(R.string.address)
                .setContent(addressList.get(0).toString());
        view.addView(address);

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
                // all data ok -> ask password -> upload all to firebase
                try {
                    SetPasswordDialog.display(fm).setOnSuccessListener(result -> {
                        if (result != null) {
                            p.setPasswordString(result);
                            // TODO: 07.07.22 upload data to firebase and sign user in
                            upload.person(p, addressList).setOnSuccessListener(result1 -> {
                                if (result1 != null && result1) {
                                    auth.signIn(p.getMail(), p.getPasswordString())
                                            .setOnSuccessListener(authResult -> {
                                                if (authResult != null && authResult.getUser() != null) {
                                                    Log.i(TAG, "onClick: upload -> auth -> sign in: complete");
                                                    fm.beginTransaction()
                                                            .replace(R.id.fragment_container, new Home())
                                                            .commit();
                                                    return;
                                                }
                                                throw new UserDataError("Sign in not possible");
                                            })
                                            .setOnFailListener(e -> {
                                                Log.e(TAG, "onClick: upload -> auth -> sign in", e);
                                            });
                                    return;
                                }
                                throw new UploadError("Upload of user data unsuccessful.");
                            }).setOnFailListener(e -> {
                                e.printStackTrace();
                                Log.e(TAG, "onFailureListener: upload data", e);
                            });
                            return;
                        }
                        throw new BadPasswordException("Password is null");
                    }).onFailureListener(e -> {
                        Log.e(TAG, "onFailureListener: SetPasswordDialog Error", e);
                        Toast.makeToast(requireContext(), R.string.fui_error_invalid_password).show();
                    });
                } catch (CreateDialogException e) {
                    e.printStackTrace();
                }
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

    }

    @Override
    public String analyticsProperties() {
        return TAG;
    }

    @Override
    public boolean onBackPressed() {
        if (fm.getBackStackEntryCount() != 0) {
            fm.popBackStack();
            return true;
        }
        return false;
    }
}
