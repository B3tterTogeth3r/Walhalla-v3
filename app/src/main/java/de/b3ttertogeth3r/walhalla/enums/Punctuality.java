package de.b3ttertogeth3r.walhalla.enums;

import androidx.annotation.NonNull;

import de.b3ttertogeth3r.walhalla.App;
import de.b3ttertogeth3r.walhalla.R;

/**
 * @since 1.0
 * @version 1.0
 * @author B3tterTogeth3r
 * @see Enum
 * @see <a href="http://docs.oracle.com/javase/tutorial/java/javaOO/enum.html">Enum java docs</a>
 */
public enum Punctuality {
    /** event starts 15 minutes later */
    CT(15, "cum tempore", "ct"),
    /** event starts on time */
    ST(0, "sit tempore", "st"),
    /** event goes the whole day */
    ALL_DAY(60 * 12 * 2, App.getContext().getString(R.string.whole_day), "wd"),
    /** duration -1, if event is an info */
    INFO(-1, "Info", "info");

    private final int duration;
    private final String description;
    private final String short_description;

    Punctuality (int duration, String description, String short_description) {
        this.duration = duration;
        this.description = description;
        this.short_description = short_description;
    }

    /**
     * @return Minutes, the event starts after the given time
     */
    public int getDelayMin () {
        return duration;
    }

    /**
     * @return Milliseconds the event starts after the given time
     */
    public int getDelayMill () {
        return (duration * 60000);
    }

    public String getShort_description () {
        return short_description;
    }

    /**
     * @return The description of the value
     */
    @NonNull
    @Override
    public String toString () {
        return description;
    }
}
