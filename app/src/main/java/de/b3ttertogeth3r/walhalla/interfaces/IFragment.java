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

package de.b3ttertogeth3r.walhalla.interfaces;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.auth.FirebaseAuth;

public interface IFragment {

    /**
     * called inside the constructor
     */
    default void constructor() {
    }

    /**
     * Set the properties for firebase analytics. It will be run after {@link #start()}
     *
     * @return String the TAG of the Fragment
     */
    String analyticsProperties();

    /**
     * Called before {@link #createView(LinearLayout)} and {@link #viewCreated()}
     */
    default void start() {
    }

    /**
     * Called before {@link #viewCreated() viewCreated} returns a result. This is to format the
     * toolbar in every Subclass the same way.
     *
     * @see de.b3ttertogeth3r.walhalla.abstract_generic.Fragment#toolbar
     */
    default void toolbarContent() {
    }

    /**
     * Create the view and initialize the necessary variables for the site.
     *
     * @param view inflated View created in {@link de.b3ttertogeth3r.walhalla.abstract_generic.Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * @see de.b3ttertogeth3r.walhalla.abstract_generic.Fragment#onViewCreated(View, Bundle)
     */
    void createView(@NonNull LinearLayout view);


    /**
     * @see de.b3ttertogeth3r.walhalla.abstract_generic.Fragment#onViewCreated(View, Bundle)
     */
    default void viewCreated() {
    }

    /**
     * Called when the Fragment is no longer started.  This is generally tied to {@link de.b3ttertogeth3r.walhalla.abstract_generic.Fragment#onStop()
     * onStop} of the containing Fragment's lifecycle.
     *
     * @see de.b3ttertogeth3r.walhalla.abstract_generic.Fragment#onStop()
     */
    default void stop() {
    }

    /**
     * Needed to make the code shorter.
     *
     * @param firebaseAuth {@link FirebaseAuth} object
     * @return The Activity of the Fragment.
     */
    FragmentActivity authStatusChanged(FirebaseAuth firebaseAuth);
}
