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

import static de.b3ttertogeth3r.walhalla.old.enums.Charge.AH_X;
import static de.b3ttertogeth3r.walhalla.old.enums.Charge.AH_XX;
import static de.b3ttertogeth3r.walhalla.old.enums.Charge.AH_XXX;
import static de.b3ttertogeth3r.walhalla.old.enums.Charge.FM;
import static de.b3ttertogeth3r.walhalla.old.enums.Charge.VX;
import static de.b3ttertogeth3r.walhalla.old.enums.Charge.X;
import static de.b3ttertogeth3r.walhalla.old.enums.Charge.XX;
import static de.b3ttertogeth3r.walhalla.old.enums.Charge.XXX;

public enum Page {
    BALANCE(new Charge[]{X, VX, FM, XX, XXX}),
    BEER(new Charge[]{X, VX, FM, XX, XXX, AH_XXX, AH_X}),
    CHARGEN(new Charge[]{X, VX, FM, XX, XXX, AH_XXX, AH_X}),
    CHARGEN_DESCRIPTION("chargen_description"),
    GREETING(new Charge[]{X, VX, FM, XX, XXX, AH_XXX, AH_X, AH_XX}),
    HOME(new Charge[0]),
    NEWS(new Charge[]{X, VX, FM, XX, XXX, AH_XXX, AH_X, AH_XX}),
    PROGRAM(new Charge[]{X, VX, FM, XX, XXX, AH_XXX, AH_X, AH_XX}),
    OWN_HISTORY("history_short"),
    FRAT_WUE("fraternity_wuerzburg"),
    FRAT_GER("fraternity_germany"),
    ROOM("rooms"),
    ABOUT_US("about_us"),
    SEMESTER_NOTES("semester_notes"),
    SIGN_IN(new Charge[0]),
    PROFILE(new Charge[]{X, VX, FM, XX, AH_X, AH_XX, AH_XXX}),
    TRANSCRIPT(new Charge[]{X, VX, FM, XX, XXX, AH_X, AH_XX, AH_XXX});

    private Charge[] editableByWhom = new Charge[0];
    private String name = "";

    Page (String name) {
        this.name = name;
    }

    Page (Charge[] editableByWhom) {
        this.editableByWhom = editableByWhom;
    }

    public String getName () {
        return name;
    }

    public boolean canEditPage (Charge charge) {
        if (charge == Charge.ADMIN) {
            return true;
        }
        if (FRAT_GER.inEditable(charge)) {
            return true;
        }
        if (FRAT_WUE.inEditable(charge)) {
            return true;
        }
        if (ABOUT_US.inEditable(charge)) {
            return true;
        }
        if (BALANCE.inEditable(charge)) {
            return true;
        }
        if (BEER.inEditable(charge)) {
            return true;
        }
        if (CHARGEN.inEditable(charge)) {
            return true;
        }
        if (GREETING.inEditable(charge)) {
            return true;
        }
        if (HOME.inEditable(charge)) {
            return true;
        }
        if (NEWS.inEditable(charge)) {
            return true;
        }
        if (PROGRAM.inEditable(charge)) {
            return true;
        }
        if (OWN_HISTORY.inEditable(charge)) {
            return true;
        }
        if (ROOM.inEditable(charge)) {
            return true;
        }
        if (SIGN_IN.inEditable(charge)) {
            return true;
        }
        return TRANSCRIPT.inEditable(charge);
    }

    private boolean inEditable (Charge charge) {
        for (Charge c : editableByWhom) {
            if (c == charge) {
                return true;
            }
        }
        return false;
    }
}
