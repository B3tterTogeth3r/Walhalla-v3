package de.b3ttertogeth3r.walhalla.enums;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import de.b3ttertogeth3r.walhalla.models.Semester;


public enum Rank {
    ACTIVE("Bursch", 20f),
    ACTIVE_FUX("Fux", 10f),
    ACTIVE_FRIEND("Korporationsfreund_Aktiv", 20f),
    PHILISTINES("Philister", 400f),
    PHILISTINES_B("Philister_B", 100f),
    PHILISTINES_FRIEND("Korporationsfreund_AH", 100f),
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
            return null;
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
