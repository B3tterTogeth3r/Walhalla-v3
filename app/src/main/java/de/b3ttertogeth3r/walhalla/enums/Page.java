package de.b3ttertogeth3r.walhalla.enums;

import static de.b3ttertogeth3r.walhalla.enums.Charge.AH_X;
import static de.b3ttertogeth3r.walhalla.enums.Charge.AH_XX;
import static de.b3ttertogeth3r.walhalla.enums.Charge.AH_XXX;
import static de.b3ttertogeth3r.walhalla.enums.Charge.FM;
import static de.b3ttertogeth3r.walhalla.enums.Charge.VX;
import static de.b3ttertogeth3r.walhalla.enums.Charge.X;
import static de.b3ttertogeth3r.walhalla.enums.Charge.XX;
import static de.b3ttertogeth3r.walhalla.enums.Charge.XXX;

public enum Page {
    BALANCE(new Charge[]{X, VX, FM, XX, XXX}),
    BEER(new Charge[]{X, VX, FM, XX, XXX, AH_XXX, AH_X}),
    CHARGEN(new Charge[]{X, VX, FM, XX, XXX, AH_XXX, AH_X}),
    GREETING(new Charge[]{X, VX, FM, XX, XXX, AH_XXX, AH_X, AH_XX}),
    HOME(new Charge[0]),
    NEWS(new Charge[]{X, VX, FM, XX, XXX, AH_XXX, AH_X, AH_XX}),
    PROGRAM(new Charge[]{X, VX, FM, XX, XXX, AH_XXX, AH_X, AH_XX}),
    OWN_HISTORY(new Charge[0]),
    ROOM(new Charge[]{X}),
    SIGN_IN(new Charge[0]),
    TRANSCRIPT(new Charge[]{X, VX, FM, XX, XXX, AH_X, AH_XX, AH_XXX});

    private final Charge[] editableByWhom;

    Page (Charge[] editableByWhom) {
        this.editableByWhom = editableByWhom;
    }

    public boolean canEditPage (Charge charge) {
        if(charge == Charge.ADMIN) {
            return true;
        }
        for (Charge c : editableByWhom) {
            if (c == charge) {
                return true;
            }
        }
        return false;
    }
}
