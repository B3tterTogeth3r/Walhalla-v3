package de.b3ttertogeth3r.walhalla.enums;

import androidx.annotation.NonNull;

import com.google.firebase.database.annotations.NotNull;

public enum Charge {
    X("Senior", "all"),
    VX("Consenior", "all"),
    FM("Fuxmajor", "all"),
    XX("Schriftführer", "some"),
    XXX("Kassier", "some"),
    AH_X("Philistersenior", "all"),
    AH_XX("Philisterschriftführer", "none"),
    AH_XXX("Philisterkassier", "none"),
    AH_HW1("Hausverwalter", "none"),
    AH_HW2("Hausverwalter", "none");
    private final String name, canEditPages;

    Charge (String name, String canEditPages) {
        this.name = name;
        this.canEditPages = canEditPages;
    }

    public boolean getCanEditPages (@NotNull Rank rank) {
        if (rank.toString().equals(Rank.ACTIVE.toString()) && canEditPages.equals("some")) {
            return true;
        } else if (canEditPages.equals("all")) {
            return true;
        } else if (canEditPages.equals("some")) {
            return false;
        } else {
            return false;
        }
    }

    public boolean canEdit (Page page) {
        return true;
    }

    public String getName () {
        return name;
    }

    @NonNull
    @Override
    public String toString () {
        return "Charge";
    }
}
