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
    public static final int CAMERA = 2;
    public static final int GALLERY = 3;
    public static final int SIGN_IN = 4;
    public static final int REGISTER_COMPLETE = 5;
    public static final int CHANGE_COMPLETE = 6;
    public static final int SIGN_IN_COMPLETE = 7;
}
