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

public enum Editable {
    NAME("Full name"),
    FIRST_NAME("First name"),
    LAST_NAME("Last name"),
    ADDRESS("Post address"),
    DOB("Date of birth"),
    POB("Place of birth"),
    MOBILE("Mobile number"),
    MAIL("Email address"),
    MAJOR("Major or occupation"),
    JOINED("Joined semester"),
    PICTURE("Picture or just its path"),
    CONNECTED_SERVICES("Don't know yet"),
    EMPTY("No field to edit"),
    STREET("Street"),
    NUMBER("Number"),
    ZIP("Zip"),
    CITY("City");
    private final String description;

    Editable (String description) {
        this.description = description;
    }

    @NonNull
    @Override
    public String toString () {
        return description;
    }
}
