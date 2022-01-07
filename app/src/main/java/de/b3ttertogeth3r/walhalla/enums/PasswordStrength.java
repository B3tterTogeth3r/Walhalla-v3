package de.b3ttertogeth3r.walhalla.enums;

import android.content.Context;

import androidx.annotation.NonNull;

import de.b3ttertogeth3r.walhalla.R;

/**
 * @author
 * <a href="https://github.com/yesterselga/password-strength-checker-android/blob/master/app/src/main/java/com/ybs/passwordstrengthchecker/PwdStrength/PasswordStrength.java">yesterselga
 * on GitHub</a>
 */
public enum PasswordStrength {

    WEAK(R.string.password_strength_weak, R.color.red),
    MEDIUM(R.string.password_strength_medium, R.color.medium),
    STRONG(R.string.password_strength_strong, R.color.strong),
    VERY_STRONG(R.string.password_strength_very_strong, R.color.very_strong);

    //--------REQUIREMENTS--------
    static int REQUIRED_LENGTH = 7;
    static int MAXIMUM_LENGTH = 16;
    static boolean REQUIRE_SPECIAL_CHARACTERS = false;
    static boolean REQUIRE_DIGITS = true;
    static boolean REQUIRE_LOWER_CASE = true;
    static boolean REQUIRE_UPPER_CASE = true;

    int resId;
    int color;

    @NonNull
    public String getRequireDescription(@NonNull Context context) {
        return context.getString(R.string.password_description);
    }

    PasswordStrength (int resId, int color) {
        this.resId = resId;
        this.color = color;
    }

    public static PasswordStrength calculateStrength (@NonNull String password) {
        int currentScore = 0;
        boolean sawUpper = false;
        boolean sawLower = false;
        boolean sawDigit = false;
        boolean sawSpecial = false;


        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);

            if (!sawSpecial && !Character.isLetterOrDigit(c)) {
                currentScore += 1;
                sawSpecial = true;
            } else {
                if (!sawDigit && Character.isDigit(c)) {
                    currentScore += 1;
                    sawDigit = true;
                } else {
                    if (!sawUpper || !sawLower) {
                        if (Character.isUpperCase(c)) {
                            sawUpper = true;
                        } else {
                            sawLower = true;
                        }
                        if (sawUpper && sawLower) {
                            currentScore += 1;
                        }
                    }
                }
            }

        }

        if (password.length() > REQUIRED_LENGTH) {
            if ((REQUIRE_SPECIAL_CHARACTERS && !sawSpecial)
                    || (REQUIRE_UPPER_CASE && !sawUpper)
                    || (REQUIRE_LOWER_CASE && !sawLower)
                    || (REQUIRE_DIGITS && !sawDigit)) {
                currentScore = 1;
            } else {
                currentScore = 2;
                if (password.length() > MAXIMUM_LENGTH) {
                    currentScore = 3;
                }
            }
        } else {
            currentScore = 0;
        }

        switch (currentScore) {
            case 1:
                return MEDIUM;
            case 2:
                return STRONG;
            case 3:
                return VERY_STRONG;
            default:
                return WEAK;
        }
    }

    public CharSequence getText (@NonNull Context ctx) {
        return ctx.getText(resId);
    }

    public int getColor () {
        return color;
    }
}
