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

import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.io.InvalidObjectException;
import java.util.ArrayList;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstract_classes.Fragment;
import de.b3ttertogeth3r.walhalla.enums.Charge;
import de.b3ttertogeth3r.walhalla.exception.NoDataException;
import de.b3ttertogeth3r.walhalla.firebase.Firebase;
import de.b3ttertogeth3r.walhalla.firebase.RemoteConfig;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IFirestoreDownload;
import de.b3ttertogeth3r.walhalla.object.BoardMember;
import de.b3ttertogeth3r.walhalla.object.Log;
import de.b3ttertogeth3r.walhalla.object.Text;

public class Greeting extends Fragment {
    private static final String TAG = "Greeting";
    private IFirestoreDownload download;
    private ArrayList<Text> greeting;
    private LinearLayout view;
    private int semesterId;

    @Override
    public void constructor() {
        download = Firebase.firestoreDownload();
        greeting = new ArrayList<>();
        semesterId = Firebase.remoteConfig().getInt(RemoteConfig.CURRENT_SEMESTER);
    }

    @Override
    public String analyticsProperties() {
        return TAG;
    }

    @Override
    public void start() {
        download.semesterGreeting(String.valueOf(semesterId))
                .setOnSuccessListener(result -> {
                    if (result != null && !result.isEmpty()) {
                        greeting = result;
                        loadGreeting();
                        return;
                    }
                    throw new NoDataException("Greeting is empty or null");
                })
                .setOnFailListener(e -> Log.e(TAG, "Loading greeting did not work", e));
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
                    // TODO: 18.07.22 format so that it looks the same and is not depending on the length of the names.
                    LinearLayout signing = new LinearLayout(requireContext());
                    signing.setPadding(40, 0, 40, 0);
                    signing.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout x = new LinearLayout(requireContext());
                    x.setOrientation(LinearLayout.VERTICAL);
                    TextView nameX = new TextView(requireContext());
                    nameX.setTextAppearance(R.style.TextAppearance_AppCompat_Subhead);
                    nameX.setText(senior.getFull_name());
                    x.addView(nameX);
                    TextView descX = new TextView(requireContext());
                    descX.setText(Charge.X.toLongString());
                    descX.setGravity(Gravity.CENTER_HORIZONTAL);
                    x.addView(descX);
                    signing.addView(x);

                    ImageView image = new ImageView(requireContext());
                    image.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.wappen_zirkel));
                    image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                            120, 120
                    );
                    image.setLayoutParams(imageParams);
                    signing.addView(image);

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
                    signing.addView(ahx);
                    layout.addView(signing);
                });
    }

    @Override
    public void createView(@NonNull LinearLayout view) {
        this.view = view;
    }
}
