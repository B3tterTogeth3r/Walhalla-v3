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

package de.b3ttertogeth3r.walhalla.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.DocumentReference;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstract_generic.Fragment;
import de.b3ttertogeth3r.walhalla.design.Image;
import de.b3ttertogeth3r.walhalla.design.SideNav;
import de.b3ttertogeth3r.walhalla.enums.Rank;
import de.b3ttertogeth3r.walhalla.exception.NoDataException;
import de.b3ttertogeth3r.walhalla.firebase.Firebase;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IAuth;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IFirestoreDownload;
import de.b3ttertogeth3r.walhalla.object.Log;
import de.b3ttertogeth3r.walhalla.util.Cache;

public class Home extends Fragment implements View.OnClickListener {
    private static final String TAG = "Home";
    private final IAuth auth;
    private final IFirestoreDownload firestoreDownload;
    private int boxWidth;
    private int boxHeight;
    private RelativeLayout program;
    private RelativeLayout greeting;
    private RelativeLayout chargen;
    private RelativeLayout news;
    /**
     * either rooms or users balance
     */
    private RelativeLayout row2first;
    private RelativeLayout login;

    public Home() {
        auth = Firebase.authentication();
        firestoreDownload = Firebase.Firestore.download();
    }

    @Override
    public String analyticsProperties() {
        return TAG;
    }

    @Override
    public void createView(@NonNull LinearLayout view) {
        RelativeLayout frame = new RelativeLayout(requireContext());
        frame.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT));

        Display display =
                ((WindowManager) requireContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        boxWidth = (int) (width / 2.5);
        boxHeight = (height / 4);

        // region semester program
        RelativeLayout.LayoutParams programParams = boxParams();
        programParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        program = new RelativeLayout(requireContext());
        program.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.border_round));
        program.setLayoutParams(programParams);
        program.setId(R.id.program);

        // Add icon
        program.addView(image(AppCompatResources.getDrawable(requireContext(),
                R.drawable.ic_calendar), false));
        // Add description
        firestoreDownload.getNextEvent()
                .setOnSuccessListener(result -> {
                    if (result != null /*&& result.validate()*/ && !result.getTitle().isEmpty()) {
                        String string;
                        try {
                            string = result.getTitle().substring(0, 20);
                        } catch (Exception e) {
                            string = result.getTitle();
                        }
                        program.addView(text(string));
                        return;
                    }
                    program.addView(text(R.string.menu_program));
                })
                .setOnFailListener(e -> {
                    Log.e(TAG, "createView: loading next event did not work", e);
                    program.addView(text(R.string.menu_program));
                });
        frame.addView(program);
        // endregion

        // region Add greeting
        RelativeLayout.LayoutParams greetingParams = boxParams();
        greetingParams.addRule(RelativeLayout.END_OF, R.id.program);
        greeting = new RelativeLayout(requireContext());
        greeting.setBackground(ContextCompat.getDrawable(requireContext(),
                R.drawable.border_round));
        greeting.setLayoutParams(greetingParams);
        greeting.setId(R.id.greeting);

        // Add icon
        firestoreDownload.getSemesterBoard(Cache.CACHE_DATA.getChosenSemester(), Rank.ACTIVE)
                .setOnSuccessListener(result -> {
                    if (result == null || result.size() == 0) {
                        throw new NoDataException("Download of chargen did not work");
                    }
                    DocumentReference ref = result.get(0).getImage();
                    if (ref == null || ref.getPath().isEmpty()) {
                        Log.d(TAG, "createView: charge of current semester has no image");
                        greeting.addView(image(AppCompatResources.getDrawable(requireContext(),
                                R.drawable.ic_greeting), true));
                        return;
                    }
                    greeting.addView(new Image(requireContext()).setImage(result.get(0).getImage()));
                })
                .setOnFailListener(e -> {
                    Log.w(TAG, "onFailureListener: download x", e);
                    greeting.addView(image(AppCompatResources.getDrawable(requireContext(),
                            R.drawable.ic_greeting), true));
                });
        // Add description
        greeting.addView(text(R.string.menu_greeting_long));
        frame.addView(greeting);
        // endregion

        // region second Row first item
        RelativeLayout.LayoutParams row2firstParams = boxParams();
        row2firstParams.addRule(RelativeLayout.BELOW, R.id.program);
        row2first = new RelativeLayout(requireContext());
        row2first.setBackground(ContextCompat.getDrawable(requireContext(),
                R.drawable.border_round));
        row2first.setLayoutParams(row2firstParams);
        row2first.setId(R.id.row2first);
        if (auth.isSignIn()) { // user is signed in:
            // region balance
            row2first.addView(image(AppCompatResources.getDrawable(requireContext(),
                    R.drawable.ic_euro), false));
            row2first.addView(text(R.string.menu_balance));
            // endregion
        } else { // no user is signed in:
            // region rooms
            row2first.addView(image(AppCompatResources.getDrawable(requireContext(),
                    R.drawable.image_garden), true));
            row2first.addView(text(R.string.menu_rooms));
            // endregion
        }
        frame.addView(row2first);
        // endregion

        // region Add chargen
        RelativeLayout.LayoutParams chargenParams = boxParams();
        chargenParams.addRule(RelativeLayout.BELOW, R.id.row2first);
        chargen = new RelativeLayout(requireContext());
        chargen.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.border_round));
        chargen.setLayoutParams(chargenParams);
        chargen.setId(R.id.chargen);

        // Add icon
        chargen.addView(image(AppCompatResources.getDrawable(requireContext(),
                R.drawable.ic_group_line), false));
        // Add description
        chargen.addView(text(R.string.menu_chargen));
        frame.addView(chargen);
        // endregion

        // region Add news
        RelativeLayout.LayoutParams newsParams = boxParams();
        newsParams.addRule(RelativeLayout.END_OF, R.id.chargen);
        newsParams.addRule(RelativeLayout.BELOW, R.id.program);
        news = new RelativeLayout(requireContext());
        news.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.border_round));
        news.setLayoutParams(newsParams);
        news.setId(R.id.news);

        // Add icon
        news.addView(image(AppCompatResources.getDrawable(requireContext(),
                R.drawable.ic_message), false));
        // Add description
        news.addView(text(R.string.menu_messages));
        frame.addView(news);
        // endregion

        // region sign in / sign out / register
        RelativeLayout.LayoutParams loginParams = boxParams();
        loginParams.addRule(RelativeLayout.END_OF, R.id.chargen);
        loginParams.addRule(RelativeLayout.BELOW, R.id.news);
        login = new RelativeLayout(requireContext());
        login.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.border_round));
        login.setLayoutParams(loginParams);
        if (!auth.isSignIn()) { // No user is signed in
            // Add icon
            login.setId(R.id.login);
            login.addView(image(AppCompatResources.getDrawable(requireContext(),
                            R.drawable.ic_login),
                    false));
            // Add description
            login.addView(text(R.string.menu_login));
        } else { // A user is signed in
            // A icon
            login.setId(R.id.logout);
            login.addView(image(AppCompatResources.getDrawable(requireContext(),
                            R.drawable.ic_exit),
                    false));
            // Ad description
            login.addView(text(R.string.menu_logout));
        }
        frame.addView(login);
        // endregion

        view.addView(frame);
    }

    @Override
    public void viewCreated() {
        try {
            program.setOnClickListener(this);
            greeting.setOnClickListener(this);
            chargen.setOnClickListener(this);
            news.setOnClickListener(this);
            row2first.setOnClickListener(this);
            login.setOnClickListener(this);
        } catch (Exception ignored) {
        }
    }

    @Override
    public FragmentActivity authStatusChanged(FirebaseAuth firebaseAuth) {
        return requireActivity();
    }

    // region design params
    @NonNull
    @NotNull
    private RelativeLayout.LayoutParams boxParams() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(boxWidth, boxHeight);
        int boxMargin = 50;
        params.setMargins(boxMargin, boxMargin, boxMargin, boxMargin);
        return params;
    }

    /**
     * @param hasColor true, if icon is not just black
     * @param drawable recourse to display black
     * @return the ImageView to insert into the layout.
     */
    @NonNull
    @NotNull
    private ImageView image(Drawable drawable, boolean hasColor) {
        ImageView icon = new ImageView(requireContext());
        icon.setImageDrawable(drawable);
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        (int) (boxHeight / 1.5));
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.setMargins(25, 0, 25, 75);
        icon.setLayoutParams(params);
        if (!hasColor) {
            icon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.black),
                    android.graphics.PorterDuff.Mode.MULTIPLY);
        }
        icon.setId(R.id.icon);

        return icon;
    }

    @NonNull
    private TextView text(String value) {
        TextView text = new TextView(requireContext());
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(Gravity.CENTER_HORIZONTAL);
        params.setMargins(5, 0, 5, 75);
        text.setLayoutParams(params);
        text.setGravity(Gravity.CENTER_HORIZONTAL);
        text.setTextAppearance(android.R.style.TextAppearance_Material_Subhead);

        text.setText(value);
        return text;
    }

    @NonNull
    private TextView text(@StringRes int resid) {
        return text(requireContext().getString(resid));
    }
    //endregion

    @Override
    public void onClick(@NonNull View view) {
        SideNav.changePage(view.getId(), requireActivity().getSupportFragmentManager().beginTransaction());
    }
}
