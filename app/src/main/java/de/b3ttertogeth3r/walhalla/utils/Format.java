package de.b3ttertogeth3r.walhalla.utils;

import android.text.TextUtils;

import androidx.annotation.NonNull;

public class Format {

    @NonNull
    public static String imageName (String name) throws NullPointerException {
        if (name == null || name.isEmpty()) {
            throw new NullPointerException("Name has to have a value");
        }
        name = name.replace(" ", "_");
        name = name.replace("ä", "ae");
        name = name.replace("ö", "oe");
        name = name.replace("ü", "ue");
        name = name.replace("ß", "ss");
        name = name.replace(".", "_");
        name = name.replace(":", "_");
        name = name.replace(",", "_");
        name = name.replace(";", "_");
        name = name.replace("/", "");
        name = name.replace("\\", "/");
        return name;
    }

    public static boolean isValidEmail (CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}
