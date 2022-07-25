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

import android.view.View;

import de.b3ttertogeth3r.walhalla.abstract_generic.Touch;

/**
 * This class is to determine which motion was done on a view.
 * The current functions are:
 * <ul>
 *     <li>{@link #onClick(View)}</li>
 *     <li>{@link #onLongClick(View)}</li>
 *     <li>{@link #onBottomToTopSwipe(View)}</li>
 *     <li>{@link #onTopToBottomSwipe(View)}</li>
 *     <li>{@link #onRightToLeftSwipe(View)}</li>
 *     <li>{@link #onLeftToRightSwipe(View)}</li>
 * </ul>
 *
 * @author B3tterTogeth3r
 * @version 1.1
 * @since 2.0
 */
public interface ClickSwipeListener {

    /**
     * @param view View which was clicked
     * @see View.OnClickListener
     * @since 1.0
     */
    default void onClick(View view) {
    }

    /**
     * @param view View which was clicked
     * @see View.OnLongClickListener
     * @since 1.0
     */
    default void onLongClick(View view) {
    }

    /**
     * @param view View which was clicked
     * @see Touch TouchListener
     * @since 1.0
     */
    default void onLeftToRightSwipe(View view) {
    }

    /**
     * @param view View which was clicked
     * @see Touch TouchListener
     * @since 1.0
     */
    default void onRightToLeftSwipe(View view) {
    }

    /**
     * @param view View which was clicked
     * @see Touch TouchListener
     * @since 1.0
     */
    default void onTopToBottomSwipe(View view) {
    }

    /**
     * @param view View which was clicked
     * @see Touch TouchListener
     * @since 1.0
     */
    default void onBottomToTopSwipe(View view) {
    }
}
