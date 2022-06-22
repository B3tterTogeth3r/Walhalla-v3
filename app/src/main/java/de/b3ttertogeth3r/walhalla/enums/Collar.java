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

public enum Collar {
    HO,
    O,
    IO,
    INFO;

    @NonNull
    @Contract(pure = true)
    public String getDescription () {
        switch (this) {
            case HO:
                return "Hochoffiziell - Männer haben einen Anzug mit Krawatte oder Fliege zu " +
                        "tragen.";
            case O:
                return "Offiziell - Männer haben ein Hemd und eine lange Hose zu tragen. Eine " +
                        "Abmeldung von allen Burschen ist notwendig.";
            case IO:
                return "Inoffiziell - Männer haben ein Oberteil mit einem Kragen und eine Hose zu" +
                        " tragen. Eine Abmeldung von inaktiven Burschen ist nicht notwendig.";
            case INFO:
                return "Information";
            default:
                return "";
        }
    }

    @NonNull
    @Override
    public String toString () {
        return super.toString().toLowerCase();
    }
}
