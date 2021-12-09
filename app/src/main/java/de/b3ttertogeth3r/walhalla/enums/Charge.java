package de.b3ttertogeth3r.walhalla.enums;

import androidx.annotation.NonNull;

public enum Charge {
    ADMIN("Admin"),
    X("Senior"),
    VX("Consenior"),
    FM("Fuxmajor"),
    XX("Schriftführer"),
    XXX("Kassier"),
    AH_X("Philistersenior"),
    AH_XX("Philisterschriftführer"),
    AH_XXX("Philisterkassier"),
    AH_HW("Hausverwalter"),
    NONE("Chargenlos");

    private final String name;

    Charge (String name) {
        this.name = name;
    }

    public static Charge find (String name) {
        if(name == null || name.isEmpty()) {
            return NONE;
        } else if (name.equals(X.getName())) {
            return X;
        } else if (name.equals(VX.getName())) {
            return VX;
        } else if (name.equals(ADMIN.getName())){
            return ADMIN;
        } else if(name.equals(FM.getName())){
            return FM;
        } else if (name.equals(XX.getName())){
            return XX;
        } else if (name.equals(XXX.getName())){
            return XXX;
        } else if (name.equals(AH_X.getName())){
            return AH_X;
        } else if (name.equals(AH_XX.getName())){
            return AH_XX;
        } else if (name.equals(AH_XXX.getName())){
            return AH_XXX;
        } else if (name.equals(AH_HW.getName())){
            return AH_HW;
        } else {
            return NONE;
        }
    }

    public String getName () {
        return name;
    }

    @NonNull
    @Override
    public String toString () {
        return super.toString();
    }
}