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

public interface IFragment {

    /**
     * called inside the constructor
     */
    default void constructor() {
    }

    /**
     * Set the properties for firebase analytics. It will be run after {@link #start()}
     */
    String analyticsProperties();

    /**
     * called before {@link #createView(LinearLayout)} and {@link #viewCreated()}. In it
     * {@link #registration} should be set before the site start.
     */
    default void start() {
    }

    /**
     * Called before {@link #viewCreated() viewCreated} returns a result. This is to format the
     * toolbar in every Subclass the same way.
     *
     * @see #toolbar
     */
    default void toolbarContent() {
    }

    /**
     * Create the view and initialize the necessary variables for the site.
     *
     * @param view inflated View created in {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * @implNote <b>DON'T CALL FUNCTIONS THAT WORK WITH DATA OF {@link #start()} IN HERE</b>
     * @see #onViewCreated(View, Bundle)
     */
    void createView(@NonNull LinearLayout view);


    /**
     * @see #onViewCreated(View, Bundle)
     */
    default void viewCreated() {
    }

    /**
     * Called when the Fragment is no longer started.  This is generally tied to {@link #onStop()
     * onStop} of the containing Fragment's lifecycle.
     *
     * @implNote Called after every entry in {@link #registration} got stopped and the list
     * cleared.
     * @see #onStop()
     */
    default void stop() {
    }

}
