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

package de.b3ttertogeth3r.walhalla.old.enums;

import androidx.annotation.NonNull;

/**
 * @author B3tterTogeth3r
 * @version 1.0
 * @see Enum
 * @see <a href="http://docs.oracle.com/javase/tutorial/java/javaOO/enum.html">Enum Java docs</a>
 * @since 1.0
 */
public enum Display {
    EDIT("edit"),
    ADD("add"),
    SAVE("save changes"),
    DELETE("delete document"),
    DETAILS("details of event"),
    SHOW("show details whole document");

    private final String description;

    Display (String description) {
        this.description = description;
    }

    @NonNull
    @Override
    public String toString () {
        return description;
    }
}
