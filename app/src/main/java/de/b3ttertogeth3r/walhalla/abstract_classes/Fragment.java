/*
 * Copyright (c) 2022.
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

package de.b3ttertogeth3r.walhalla.abstract_classes;

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
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.firebase.Firestore;
import de.b3ttertogeth3r.walhalla.interfaces.IAnalytics;
import de.b3ttertogeth3r.walhalla.mock.AnalyticsMock;
import de.b3ttertogeth3r.walhalla.object.Log;
import de.b3ttertogeth3r.walhalla.util.ToastList;

public abstract class Fragment extends androidx.fragment.app.Fragment implements FirebaseAuth.AuthStateListener {
    private static final String TAG = "Fragment";
    /**
     * A list to collect all realtime listeners into the Firestore database.
     *
     * @see ListenerRegistration
     * @see Firestore
     * @since 1.0
     */
    public ArrayList<ListenerRegistration> registration;
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
    private final IAnalytics analytics;

    public Fragment () {
        analytics = new AnalyticsMock();
    }

    /**
     * @param inflater
     *         The LayoutInflater object that can be used to inflate any views in the fragment,
     * @param container
     *         If non-null, this is the parent view that the fragment's UI should be attached to.
     *         The fragment should not add the view itself, but this can be used to generate the
     *         LayoutParams of the view.
     * @param savedInstanceState
     *         If non-null, this fragment is being re-constructed from a previous saved state as
     *         given here.
     * @return Return the View for the fragment's UI, or null.
     * @deprecated use abstract {@link #createView(LinearLayout)} instead.
     */
    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater,
                              @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, container, false);
        LinearLayout layout = view.findViewById(R.id.fragment_container);
        createView(layout);
        return view;
    }

    /**
     * @param view
     *         The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState
     *         If non-null, this fragment is being re-constructed from a previous saved state as
     *         given here.
     * @deprecated use abstract {@link #viewCreated()} instead
     */
    @Override
    public void onViewCreated (@NonNull @NotNull View view,
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
    public void onStart () {
        try {
            super.onStart();
            registration = new ArrayList<>();
        } finally {
            start();
            analytics.screenChange(analyticsProperties());
        }
    }

    /**
     * called before {@link #createView(LinearLayout)} and {@link #viewCreated()}. In it
     * {@link #registration} should be set before the site start.
     */
    public abstract void start ();

    /**
     * Set the properties for firebase analytics. It will be run after {@link #start()}
     */
    public abstract String analyticsProperties ();

    @Override
    public void onResume () {
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
    public void onStop () {
        super.onStop();
        stop();
        try {
            for (ListenerRegistration reg : registration) {
                reg.remove();
            }
            registration.clear();
        } catch (Exception e) {
            Log.e(TAG, "Something went wrong while removing the snapshot listener",
                    e);
        } finally {
            toolbar.findViewById(R.id.custom_title).setVisibility(View.GONE);
            toolbar.getMenu().clear();
            toolbar.setTitle(R.string.app_name);
            toolbar.setSubtitle("");
        }
    }

    /**
     * Called when the Fragment is no longer started.  This is generally tied to {@link #onStop()
     * onStop} of the containing Activity's lifecycle.
     *
     * @implNote Called after every entry in {@link #registration} got stopped and the list
     * cleared.
     * @see #onStop()
     */
    public abstract void stop ();

    /**
     * @see #onViewCreated(View, Bundle)
     */
    public abstract void viewCreated ();

    /**
     * Called before {@link #viewCreated() viewCreated} returns a result. This is to format the
     * toolbar in every Subclass the same way.
     *
     * @see #toolbar
     */
    public abstract void toolbarContent ();

    /**
     * Create the view and initialize the necessary variables for the site.
     *
     * @param view
     *         inflated View created in {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * @implNote <b>DON'T CALL FUNCTIONS THAT WORK WITH DATA OF {@link #start()} IN HERE</b>
     * @see #onViewCreated(View, Bundle)
     */
    public abstract void createView (@NonNull @NotNull LinearLayout view);

    /**
     * this should reload the current fragment, if the users auth status changes. Every subclass
     * should check, if the then signed in user is allowed to see the selected fragment. If not,
     * navigate one up in the backstack.
     *
     * @param firebaseAuth
     *         the auth status from firebase
     */
    @Override
    public void onAuthStateChanged (@NonNull FirebaseAuth firebaseAuth) {
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, this)
                .commit();
    }
}