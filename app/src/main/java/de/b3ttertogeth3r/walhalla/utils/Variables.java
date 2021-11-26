package de.b3ttertogeth3r.walhalla.utils;

import java.util.Locale;

import de.b3ttertogeth3r.walhalla.App;
import de.b3ttertogeth3r.walhalla.R;

public class Variables {
    public static final Locale LOCALE = new Locale("de", "DE");
    public static final String[] MONTHS = {App.getContext().getString(R.string.month_jan),
            App.getContext().getString(R.string.month_feb), App.getContext().getString(R.string.month_mar),
            App.getContext().getString(R.string.month_apr), App.getContext().getString(R.string.month_may),
            App.getContext().getString(R.string.month_jun), App.getContext().getString(R.string.month_jul),
            App.getContext().getString(R.string.month_aug), App.getContext().getString(R.string.month_sep),
            App.getContext().getString(R.string.month_oct), App.getContext().getString(R.string.month_nov),
            App.getContext().getString(R.string.month_dec)};
    public static final String GOOGLE_CLIENT_ID = "456221124895-jjhv97lsl2eckfoggqd7phj95s7u05ti.apps.googleusercontent.com";
}
