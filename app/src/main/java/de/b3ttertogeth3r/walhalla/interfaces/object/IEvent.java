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

package de.b3ttertogeth3r.walhalla.interfaces.object;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import de.b3ttertogeth3r.walhalla.annotation.EventValue;
import de.b3ttertogeth3r.walhalla.object.Event;

public interface IEvent extends View<de.b3ttertogeth3r.walhalla.design.Event>, Validate {
    /**
     * ID of the {@link Event}.
     */
    int ID = -1;
    /**
     * Title of the {@link Event}.
     */
    int TITLE = 0;
    /**
     * Description of the {@link Event}.
     */
    int DESCRIPTION = 1;
    /**
     * Collar to wear to the {@link Event}.
     */
    int COLLAR = 2;
    /**
     * punctuality of the {@link Event}.
     */
    int PUNCTUALITY = 3;
    /**
     * Start time of the {@link Event}.
     */
    int TIME = 4;
    /**
     * End time of the {@link Event}.
     */
    int END = 5;
    /**
     * Visibility of the {@link Event}.
     */
    int VISIBILITY = 6;

    de.b3ttertogeth3r.walhalla.design.Event getView(FragmentActivity activity);

    Object getValue(@EventValue int valueID);

    void setValue(@EventValue int valueID, @NonNull Object object);
}
