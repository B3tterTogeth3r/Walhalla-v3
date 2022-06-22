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

package de.b3ttertogeth3r.walhalla.enums;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

public enum Punctuality {
    CT,
    ST,
    CT_SF,
    ST_CF,
    WHOLE,
    INFO;

    @NonNull
    @Contract(pure = true)
    public String getDescription () {
        switch (this) {
            case CT:
                return "Die Veranstaltung beginnt 15 Minuten nach der angegebenen Zeit " +
                        "(akademisches Viertel)";
            case ST:
                return "Die Veranstaltung beginnt zur angegebenen Zeit.\n";
            case CT_SF:
                return "ct - Die Anwesenheit von Damen ist explizit gewünscht.\n";
            case ST_CF:
                return "st - Die Anwesenheit von Damen ist explizit gewünscht.\n";
            case WHOLE:
                return "ganzägige Veranstaltung";
            case INFO:
                return "Infoanzeige";
            default:
                return "";
        }
    }

    @NonNull
    @Override
    public String toString () {
        return super.toString().toLowerCase().replace("_", " ");
    }
}
