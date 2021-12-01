package de.b3ttertogeth3r.walhalla.enums;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import de.b3ttertogeth3r.walhalla.models.Semester;


public enum Rank {
    ACTIVE_FUX("Fux", 10f),
    ACTIVE("Bursch", 20f),
    PHILISTINES("A-Philister", 400f),
    PHILISTINES_B("B-Philister", 100f),
    ACTIVE_FRIEND("Korporationsfreund Aktiver", 20f),
    PHILISTINES_FRIEND("Korporationsfreund Alter Herr", 100f),
    PHILISTINES_HONOR("Ehrenphilister"),
    HONOR_MEMBER("Ehrenmitglied"),
    GUEST("Gast"),
    GONE("Verstorben"),
    LEFT("Ausgetreten"),
    NONE("not_signed_in"),
    ERROR("Error_while_loading");
    private final String description;
    private final float price;

    Rank(String description){
        this.description = description;
        this.price = 0f;
    }

    Rank (String description, float price) {
        this.description = description;
        this.price = price;
    }

    public Rank getRank(@NonNull String rankName){
        if(rankName.equals(description)){
            return this;
        } else {
            return ERROR;
        }
    }

    public float getPrice () {
        return price;
    }

    @NonNull
    @Override
    public String toString () {
        return description;
    }
}
