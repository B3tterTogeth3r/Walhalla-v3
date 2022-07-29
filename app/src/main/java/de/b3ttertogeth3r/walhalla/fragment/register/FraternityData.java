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

package de.b3ttertogeth3r.walhalla.fragment.register;

import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstract_generic.Fragment;
import de.b3ttertogeth3r.walhalla.abstract_generic.Touch;
import de.b3ttertogeth3r.walhalla.design.Button;
import de.b3ttertogeth3r.walhalla.design.ProfileRow;
import de.b3ttertogeth3r.walhalla.design.Title;
import de.b3ttertogeth3r.walhalla.design.Toast;
import de.b3ttertogeth3r.walhalla.dialog.ChangeSemester;
import de.b3ttertogeth3r.walhalla.dialog.InfoDialog;
import de.b3ttertogeth3r.walhalla.dialog.ProfileEditDialog;
import de.b3ttertogeth3r.walhalla.dialog.RankSelectDialog;
import de.b3ttertogeth3r.walhalla.exception.CreateDialogException;
import de.b3ttertogeth3r.walhalla.interfaces.activityMain.IOnBackPressed;
import de.b3ttertogeth3r.walhalla.object.Address;
import de.b3ttertogeth3r.walhalla.object.Log;
import de.b3ttertogeth3r.walhalla.object.Person;
import de.b3ttertogeth3r.walhalla.object.Semester;
import de.b3ttertogeth3r.walhalla.util.Values;


/**
 * Fragment to get the data the fraternity needs.
 *
 * @author B3tterTogeth3r
 * @version 1.0
 * @since 3.0
 */
public class FraternityData extends Fragment implements IOnBackPressed {
    private static final String TAG = "FraternityData";
    private final Person person;
    private final ArrayList<Address> addressList;
    private FragmentManager fm;
    private ProfileRow joined, rank, nickname;
    private Semester currentSemester;


    /**
     * Fragment to get the data the fraternity needs.
     *
     * @param person      Person with the saved data
     * @param addressList List of entered addresses
     * @since 1.0
     */
    public FraternityData(Person person, ArrayList<Address> addressList) {
        this.person = person;
        this.addressList = addressList;
        this.currentSemester = Values.currentSemester;
    }

    @Override
    public String analyticsProperties() {
        return TAG;
    }

    @Override
    public void toolbarContent() {
        toolbar.setTitle("Verbindungsdaten");
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
    public void createView(@NonNull LinearLayout view) {
        // create view for the user to fill out all the necessary data the fraternity needs
        // TODO: 26.07.22 at some point create a way to add other fraternities as well as the possibility to have a different "A-Bund"
        fm = getParentFragmentManager();
        view.setOrientation(LinearLayout.VERTICAL);

        Title title = new Title(requireContext());
        title.setTitle("Verbindungsdaten");
        view.addView(title);
        //region data collection rows
        joined = new ProfileRow(requireContext());
        String joinedStr = requireContext().getString(R.string.joined) + "*";
        joined.setTitle(joinedStr)
                .addTouchListener(new Touch() {
                    @Override
                    public void onClick(View view) {
                        try {
                            ChangeSemester dialog = ChangeSemester.display(fm, currentSemester);
                            dialog.setOnSuccessListener(result -> {
                                if (result == null || result == -1) {
                                    throw new NullPointerException("Semester id cannot be null, 0 or -1");
                                }
                                person.setJoined(result);
                                joined.setContent(Values.semesterList.get(result).getName_long());
                                currentSemester = Values.semesterList.get(result);
                            });
                        } catch (CreateDialogException e) {
                            e.printStackTrace();
                        }
                    }
                });
        view.addView(joined);

        rank = new ProfileRow(requireContext());
        String rankStr = requireContext().getString(R.string.rank) + "*";
        rank.setTitle(rankStr)
                .addTouchListener(new Touch() {
                    @Override
                    public void onClick(View view) {
                        try {
                            RankSelectDialog.display(fm)
                                    .setOnSuccessListener(result -> {
                                        if (result != null) {
                                            rank.setContent(result.toString());
                                            person.setRank(result);
                                        }
                                    })
                                    .onFailureListener(e -> Log.e(TAG, "onFailureListener: RankSelectDialog Error", e));
                        } catch (Exception e) {
                            Log.e(TAG, "onClick: rank", e);
                        }
                    }
                });
        view.addView(rank);

        nickname = new ProfileRow(requireContext());
        nickname.setTitle(R.string.nickname)
                .addTouchListener(new Touch() {
                    @Override
                    public void onClick(View view) {
                        try {
                            ProfileEditDialog dialog = ProfileEditDialog.display(fm, nickname.getContent());
                            dialog.setOnSuccessListener(result -> {
                                        if (result != null) {
                                            nickname.setContent(result);
                                            person.setNickname(result.toString());
                                        }
                                    })
                                    .onFailureListener(e -> Log.e(TAG, "onFailureListener: something went wrong with the ProfileEditDialog", e));
                        } catch (CreateDialogException e) {
                            Log.e(TAG, "onClick: nickname", e);
                        }
                    }
                });
        view.addView(nickname);
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
                if (joined.getContent().length() != 0 && rank.getContent().length() != 0) {
                    // change to fragment to review data
                    fm.beginTransaction()
                            .replace(R.id.fragment_container, new ReviewData(person, addressList))
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
    public void viewCreated() {
        try {
            joined.setContent(String.valueOf(person.getJoined()));
            rank.setContent(person.getRank().toString());
            nickname.setContent(person.getNickname());
        } catch (Exception ignored) {
        }
    }

    @Override
    public FragmentActivity authStatusChanged(FirebaseAuth firebaseAuth) {
        return requireActivity();
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
