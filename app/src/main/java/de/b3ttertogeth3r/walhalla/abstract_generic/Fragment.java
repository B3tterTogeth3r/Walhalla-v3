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

package de.b3ttertogeth3r.walhalla.abstract_generic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.NotNull;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.firebase.Firebase;
import de.b3ttertogeth3r.walhalla.interfaces.IFragment;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IAnalytics;
import de.b3ttertogeth3r.walhalla.object.Log;
import de.b3ttertogeth3r.walhalla.util.ToastList;

public abstract class Fragment extends androidx.fragment.app.Fragment implements
        FirebaseAuth.AuthStateListener, IFragment {
    private static final String TAG = "Fragment";
    private final IAnalytics analytics;
    private final FirebaseAuth.AuthStateListener authStateListener;
    /**
     * The top Toolbar of the whole application
     *
     * @see Toolbar
     */
    public Toolbar toolbar;
    /**
     * A customized version of the toolbar
     *
     * @see #toolbar
     */
    public LinearLayout customToolbar;
    public TextView customToolbarTitle;

    public Fragment() {
        analytics = Firebase.analytics();
        authStateListener = this;
        Firebase.authentication().addAuthStateListener(authStateListener);
        constructor();
    }

    /**
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     *                           The fragment should not add the view itself, but this can be used to generate the
     *                           LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as
     *                           given here.
     * @return Return the View for the fragment's UI, or null.
     * @deprecated use abstract {@link #createView(LinearLayout)} instead.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, container, false);
        LinearLayout layout = view.findViewById(R.id.fragment_container);
        createView(layout);
        return view;
    }

    /**
     * @param view               The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as
     *                           given here.
     * @deprecated use abstract {@link #viewCreated()} instead
     */
    @Override
    public void onViewCreated(@NonNull @NotNull View view,
                              @Nullable Bundle savedInstanceState) {
        try {
            toolbar = requireActivity().findViewById(R.id.toolbar);
            toolbar.setTitle("Walhalla");
            toolbar.getMenu().clear();
            customToolbar = toolbar.findViewById(R.id.custom_title);
            customToolbar.setVisibility(View.GONE);
            customToolbarTitle = customToolbar.findViewById(R.id.action_bar_title);
            ToastList.show();
        } catch (Exception ignored) {
        } finally {
            super.onViewCreated(view, savedInstanceState);
            viewCreated();
            toolbarContent();
        }
    }

    /**
     * @deprecated use abstract {@link #start()} instead
     */
    @Override
    public void onStart() {
        try {
            super.onStart();
            start();
        } finally {
            analytics.screenChange(analyticsProperties());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            toolbarContent();
        } catch (Exception e) {
            Log.e(TAG, "Refilling toolbar at onResume did not work", e);
        }
    }

    /**
     * @deprecated use abstract {@link #stop()} instead
     */
    @Override
    public void onStop() {
        super.onStop();
        toolbar.findViewById(R.id.custom_title).setVisibility(View.GONE);
        toolbar.getMenu().clear();
        toolbar.setTitle(R.string.app_name);
        toolbar.setSubtitle("");
        stop();
        Firebase.authentication().removeAuthListener(authStateListener);
    }

    /**
     * Reload the current fragment, if the auth status changes. Every subclass should check,
     * if the then signed in user is allowed to see the selected fragment. If not, navigate one
     * up in the backstack.
     *
     * @param firebaseAuth the auth status from firebase
     */
    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        Log.i(TAG, "onAuthStateChanged: " + Firebase.authentication().isSignIn());
        // reload fragment
        try {
            authStatusChanged(firebaseAuth)
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, this)
                    .commit();
        } catch (Exception ignored) {
        }
    }
}
