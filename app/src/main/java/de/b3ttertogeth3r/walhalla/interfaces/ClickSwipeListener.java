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

import de.b3ttertogeth3r.walhalla.abstract_classes.TouchListener;

/**
 * This class is to determine which motion was done on a view.
 * The current functions are:
 * <ul>
 *     <li>{@link #onClick(T, View)}</li>
 *     <li>{@link #onLongClick(T, View)}</li>
 *     <li>{@link #onBottomToTopSwipe(T, View)}</li>
 *     <li>{@link #onTopToBottomSwipe(T, View)}</li>
 *     <li>{@link #onRightToLeftSwipe(T, View)}</li>
 *     <li>{@link #onLeftToRightSwipe(T, View)}</li>
 * </ul>
 *
 * @author B3tterTogeth3r
 * @version 1.0
 * @since 2.0
 */
public interface ClickSwipeListener<T> {

    /**
     * @param view View which was clicked
     * @see View.OnClickListener
     * @since 1.0
     */
    default void onClick(T object, View view) {
    }

    /**
     * @param view View which was clicked
     * @see View.OnLongClickListener
     * @since 1.0
     */
    default void onLongClick(T object, View view) {
    }

    /**
     * @param view View which was clicked
     * @see TouchListener TouchListener
     * @since 1.0
     */
    default void onLeftToRightSwipe(T object, View view) {
    }

    /**
     * @param view View which was clicked
     * @see TouchListener TouchListener
     * @since 1.0
     */
    default void onRightToLeftSwipe(T object, View view) {
    }

    /**
     * @param view View which was clicked
     * @see TouchListener TouchListener
     * @since 1.0
     */
    default void onTopToBottomSwipe(T object, View view) {
    }

    /**
     * @param view View which was clicked
     * @see TouchListener TouchListener
     * @since 1.0
     */
    default void onBottomToTopSwipe(T object, View view) {
    }
}
