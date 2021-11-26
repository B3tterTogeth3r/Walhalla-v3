package de.b3ttertogeth3r.walhalla.enums;

import androidx.annotation.NonNull;

/**
 * @since 1.0
 * @version 1.0
 * @author B3tterTogeth3r
 * @see Enum
 * @see <a href="http://docs.oracle.com/javase/tutorial/java/javaOO/enum.html">Enum java docs</a>
 */
public enum Walhalla {
    NAME ("K.St.V. Walhalla im KV zu Würzburg"),
    ADH ("Mergenheimer Straße 32\n97082 Würzburg"),
    WEBSITE ("http://walhalla-wuerzburg.de"),
    EMAIL_INFO ("info@walhalla-wuerzburg.de"),
    EMAIL_SENIOR ("senior@walhalla-wuerzburg.de"),
    EMAIL_CONSENIOR ("consenior@walhalla-wuerzburg.de"),
    EMAIL_FM ("fuchsmajor@walhalla-wuerzburg.de"),
    EMAIL_XX ("schriftfuerer@walhalla-wuerzburg.de"),
    EMAIL_XXX ("kassier@walhalla-wuerzburg.de");

    private final String value;
    Walhalla (String value) {
        this.value = value;
    }

    @NonNull
    private String getValue (){
        return "walhalla-wuerzburg.de";
    }

    @NonNull
    @Override
    public String toString () {
        return value;
    }
}
