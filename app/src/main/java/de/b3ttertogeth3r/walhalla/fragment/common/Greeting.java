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

package de.b3ttertogeth3r.walhalla.fragment.common;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.io.InvalidObjectException;
import java.util.ArrayList;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstract_generic.Fragment;
import de.b3ttertogeth3r.walhalla.design.AdView;
import de.b3ttertogeth3r.walhalla.design.Title;
import de.b3ttertogeth3r.walhalla.enums.Charge;
import de.b3ttertogeth3r.walhalla.exception.NoDataException;
import de.b3ttertogeth3r.walhalla.firebase.Firebase;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IFirestoreDownload;
import de.b3ttertogeth3r.walhalla.object.BoardMember;
import de.b3ttertogeth3r.walhalla.object.Text;
import de.b3ttertogeth3r.walhalla.util.Cache;
import de.b3ttertogeth3r.walhalla.util.Log;
import de.b3ttertogeth3r.walhalla.util.Values;

public class Greeting extends Fragment {
    private static final String TAG = "Greeting";
    private IFirestoreDownload download;
    private ArrayList<Text> greeting;
    private LinearLayout view;
    private int semesterId;

    @Override
    public void constructor() {
        download = Firebase.Firestore.download();
        greeting = new ArrayList<>();
        semesterId = Values.currentSemester.getId();
    }

    @Override
    public String analyticsProperties() {
        return TAG;
    }

    @Override
    public void start() {
        download.getSemesterGreeting(semesterId)
                .setOnSuccessListener(result -> {
                    if (result != null && !result.isEmpty()) {
                        greeting = result;
                        loadGreeting();
                        return;
                    }
                    throw new NoDataException("Greeting is empty or null");
                })
                .setOnFailListener(e -> {
                    Log.w(TAG, "Loading greeting of semester " + semesterId + " did not work", e);
                    Title noGreeting = new Title(requireContext());
                    noGreeting.setText(R.string.greeting_no_data);
                    view.addView(noGreeting);
                });
    }

    @Override
    public void toolbarContent() {
        toolbar.setTitle(R.string.menu_greeting_long);
        Log.i(TAG, "toolbarContent: Cache.CACHE_DATA.isBoardMember: " + Cache.CACHE_DATA.isBoardMember());
        if (Cache.CACHE_DATA.isBoardMember()) {
            // TODO: 29.07.22 add menu to edit greeting
        }
    }

    @Override
    public void createView(@NonNull LinearLayout view) {
        this.view = view;
    }

    @Override
    public FragmentActivity authStatusChanged(FirebaseAuth firebaseAuth) {
        return requireActivity();
    }

    private void loadGreeting() {
        de.b3ttertogeth3r.walhalla.design.LinearLayout layout = new de.b3ttertogeth3r.walhalla.design.LinearLayout(requireContext());
        for (Text t : greeting) {
            layout.addView(t.getView(requireActivity()));
        }

        download.getSemesterBoardOne(semesterId, Charge.X)
                .setOnSuccessListener(result -> {
                    if (result == null || !result.validate()) {
                        throw new InvalidObjectException("Result not valid");
                    }
                    signingRow(result, layout);
                }).setOnFailListener(e -> Log.e(TAG, "loadGreeting: ", e));
        view.addView(layout);
    }

    private void signingRow(BoardMember senior, LinearLayout layout) {
        download.getSemesterBoardOne(semesterId, Charge.AH_X)
                .setOnSuccessListener(philX -> {
                    if (philX == null || !philX.validate()) {
                        throw new NoDataException("Charge not found");
                    }
                    RelativeLayout row = new RelativeLayout(requireContext());

                    int padding = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            4f,
                            requireContext().getResources().getDisplayMetrics()
                    );
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(/*left*/padding,
                            /*top*/padding,
                            /*right*/(padding),
                            /*bottom*/padding);
                    row.setLayoutParams(params);
                    row.setPadding(/*left*/padding,
                            /*top*/padding,
                            /*right*/padding,
                            /*bottom*/padding);
                    LinearLayout x = new LinearLayout(requireContext());
                    x.setId(R.id.x_left);
                    x.setOrientation(LinearLayout.VERTICAL);
                    TextView nameX = new TextView(requireContext());
                    nameX.setTextAppearance(R.style.TextAppearance_AppCompat_Subhead);
                    nameX.setText(senior.getFull_name());
                    x.addView(nameX);
                    TextView descX = new TextView(requireContext());
                    descX.setText(Charge.X.toLongString());
                    descX.setGravity(Gravity.CENTER_HORIZONTAL);
                    x.addView(descX);
                    RelativeLayout.LayoutParams xParams = new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    xParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    x.setLayoutParams(xParams);
                    row.addView(x);

                    ImageView image = new ImageView(requireContext());
                    image.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.wappen_zirkel));
                    image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
                            (padding * 30),
                            (padding * 15)
                    );
                    imageParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                    image.setLayoutParams(imageParams);
                    row.addView(image);

                    LinearLayout ahx = new LinearLayout(requireContext());
                    ahx.setOrientation(LinearLayout.VERTICAL);
                    TextView nameahX = new TextView(requireContext());
                    nameahX.setTextAppearance(R.style.TextAppearance_AppCompat_Subhead);
                    nameahX.setText(philX.getFull_name());
                    ahx.addView(nameahX);
                    TextView descahX = new TextView(requireContext());
                    descahX.setText(Charge.AH_X.toLongString());
                    descahX.setGravity(Gravity.CENTER_HORIZONTAL);
                    ahx.addView(descahX);
                    RelativeLayout.LayoutParams ahxParams = new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    ahxParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    ahx.setLayoutParams(ahxParams);
                    row.addView(ahx);
                    layout.addView(row);
                    layout.addView(new AdView(requireContext()));
                });
    }
}
