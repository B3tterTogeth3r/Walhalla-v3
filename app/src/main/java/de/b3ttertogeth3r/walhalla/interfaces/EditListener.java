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

import de.b3ttertogeth3r.walhalla.enums.Editable;

/**
 * The interface is to notify the class which created the acompaning dialog, textview, button,
 * edittext or fields like that.
 *
 * @author B3tterTogeth3r
 * @version 1.0
 * @since 1.11
 */
public interface EditListener {
    /**
     * notifies the holder with the data to save and which kind it is of.
     *
     * @param value    Object of the result
     * @param editable Editable for more than one field, to format data corecctly
     */
    void saveEdit(Object value, Editable editable);

    /**
     * notifies the holder the input is aborted and all changed data should be reset.
     */
    void abort();

    /**
     * notifies the holder the input is aborted and all changed data should be reset.
     *
     * @param editable Editable where the error occurred
     * @param s        error message
     */
    void sendError(Editable editable, String s);

    /**
     * notify holder about live changes
     *
     * @param string   value
     * @param editable Editable for more than one field to change the correct value.
     */
    void sendLiveChange(String string, Editable editable);
}
