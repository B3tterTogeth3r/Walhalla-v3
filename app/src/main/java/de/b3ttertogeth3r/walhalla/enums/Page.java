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
    OWN_HISTORY("history_short"),
    FRAT_WUE("fraternity_wuerzburg"),
    FRAT_GER("fraternity_germany"),
    ROOM("rooms"),
    ABOUT_US("about_us"),
    SEMESTER_NOTES("semester_notes"),
    SIGN_IN(new Charge[0]),
    TRANSCRIPT(new Charge[]{X, VX, FM, XX, XXX, AH_X, AH_XX, AH_XXX});

    private Charge[] editableByWhom = new Charge[0];
    private String name = "";
    Page (String name){
        this.name = name;
    }

    Page (Charge[] editableByWhom) {
        this.editableByWhom = editableByWhom;
    }

    public String getName(){
        return name;
    }

    public boolean canEditPage (Charge charge) {
        if(charge == Charge.ADMIN)
            return true;
        if(FRAT_GER.inEditable(charge))
            return true;
        if(FRAT_WUE.inEditable(charge))
            return true;
        if(ABOUT_US.inEditable(charge))
            return true;
        if(BALANCE.inEditable(charge))
            return true;
        if(BEER.inEditable(charge))
            return true;
        if(CHARGEN.inEditable(charge))
            return true;
        if(GREETING.inEditable(charge))
            return true;
        if(HOME.inEditable(charge))
            return true;
        if(NEWS.inEditable(charge))
            return true;
        if(PROGRAM.inEditable(charge))
            return true;
        if(OWN_HISTORY.inEditable(charge))
            return true;
        if(ROOM.inEditable(charge))
            return true;
        if(SIGN_IN.inEditable(charge))
            return true;
        if(TRANSCRIPT.inEditable(charge))
            return true;
        return false;
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
