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

import com.google.firebase.database.annotations.NotNull;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.abstract_classes.Fragment;
import de.b3ttertogeth3r.walhalla.interfaces.IAuth;
import de.b3ttertogeth3r.walhalla.mock.AuthMock;
import de.b3ttertogeth3r.walhalla.object.Log;

public class Home extends Fragment implements View.OnClickListener {
    private static final String TAG = "Home";
    private final IAuth auth;
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
        auth = new AuthMock();
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

        // region Add program
        RelativeLayout.LayoutParams programParams = boxParams();
        programParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        program = new RelativeLayout(requireContext());
        program.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.border_round));
        program.setLayoutParams(programParams);
        program.setId(R.id.program);

        // Add icon
        // TODO: 10.08.21 maybe change that to the next event being displayed?
        program.addView(image(AppCompatResources.getDrawable(requireContext(),
                R.drawable.ic_calendar), false));
        // Add description
        program.addView(text(R.string.menu_program));
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
        // TODO: 07.08.21 set to image of the current X
        greeting.addView(image(AppCompatResources.getDrawable(requireContext(),
                R.drawable.ic_greeting), true));
        // Add description
        greeting.addView(text(R.string.menu_greeting));
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
        login.setId(R.id.login);
        if (!auth.isSignIn()) { // No user is signed in
            // Add icon
            login.addView(image(AppCompatResources.getDrawable(requireContext(),
                            R.drawable.ic_login),
                    false));
            // Add description
            login.addView(text(R.string.menu_login));
        } else { // A user is signed in
            // A icon
            login.addView(image(AppCompatResources.getDrawable(requireContext(),
                            R.drawable.ic_exit),
                    false));
            // Ad description
            login.addView(text(R.string.menu_logout));
            // refresh site
            try {
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, this)
                        .commit();
            } catch (Exception e) {
                Log.e(TAG, "refreshing fragment did not work", e);
            }
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
    public String analyticsProperties() {
        return TAG;
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
    @NotNull
    private TextView text(@StringRes int resid) {
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

        text.setText(resid);
        return text;
    }
    //endregion

    @Override
    public void onClick(View view) {
        // TODO: 12.07.22 switch page and add it to the backstack
    }
}
