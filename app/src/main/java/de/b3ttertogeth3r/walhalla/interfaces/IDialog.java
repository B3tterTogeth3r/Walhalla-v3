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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

public interface IDialog<T> {
    /**
     * Format the data the fragment is supposed to change or edit on the click of
     * the {@link DialogInterface#BUTTON_POSITIVE PositiveButton}
     *
     * @return default: null<br>if overridden: The value the background Fragment needs
     * @implNote only available if {@link de.b3ttertogeth3r.walhalla.abstract_classes.Dialog#loader Dialog#loader} is not {@code null}
     */
    default T done() throws Exception {
        return null;
    }

    /**
     * Create the design inside the dialog
     *
     * @param container Layout to put different design items in
     * @param inflater  Inflater to get designs from xml files
     * @since 1.0
     */
    void createDialog(@NonNull RelativeLayout container,
                      @NonNull LayoutInflater inflater);

    /**
     * Customize and add builder items like i.e.
     * {@link android.app.AlertDialog.Builder#setPositiveButton(int,
     * DialogInterface.OnClickListener) setPositiveButton(int, DialogInterface.OnClickListener)}
     *
     * @param builder Dialog builder
     * @since 1.0
     */
    default void configDialog(@NonNull AlertDialog.Builder builder) {
    }

    /**
     * @param toolbar The toolbar.
     */
    void configToolbar(@NonNull Toolbar toolbar);
}
