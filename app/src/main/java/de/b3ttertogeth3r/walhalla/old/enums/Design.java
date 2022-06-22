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

public enum Design {
    TEXT("text"),
    TITLE("title"),
    SUBTITLE1("subtitle1"),
    SUBTITLE2("subtitle2"),
    BUTTON("button"),
    IMAGE("image"),
    LINK("link"),
    TABLE("table"),
    TABLE_TITLE("table_title"),
    TABLE_ROW("table_row"),
    LIST_CHECKED("list_checked"),
    LIST_BULLET("list_bullet"),
    BLOCK("block");
    private final String description;

    Design (String description) {
        this.description = description;
    }

    public String getDescription(){
        return description;
    }

    @NonNull
    @Override
    public String toString () {
        return description;
    }
}
