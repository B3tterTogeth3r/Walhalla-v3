/*
 * Copyright (c) 2022-2022.
 *
 * Licensed under the Apace License, Version 2.0 (the "Licence"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 *  License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 *  either express or implied. See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package de.b3ttertogeth3r.walhalla.enums;

import de.b3ttertogeth3r.walhalla.R;

/**
 * An enum to check a password string for safety.
 *
 * @author B3tterTogeth3r
 * @version 1.0
 * @since 3.0
 */
public enum PasswordStrength {
    WEAK(R.string.password_strength_weak, R.color.red),
    MEDIUM(R.string.password_strength_medium, R.color.medium),
    STRONG(R.string.password_strength_strong, R.color.strong),
    VERY_STRONG(R.string.password_strength_very_strong, R.color.very_strong);
    static int REQUIRED_LENGTH = 7;
    int strID, colorId;

    PasswordStrength(int strID, int colorId) {
        this.colorId = colorId;
        this.strID = strID;
    }

    /**
     * A password must have more than 7 signs, contain at least one lower letter, one upper case
     * letter and one number.
     *
     * @param password password to check
     * @return the strength of the password
     */
    public static PasswordStrength getStrength(CharSequence password) {
        int currentScore = 0;
        boolean sawUpper = false;
        boolean sawLower = false;
        boolean sawDigit = false;
        boolean sawSpecial = false;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (!sawSpecial && !Character.isLetterOrDigit(c)) {
                sawSpecial = true;
            } else {
                if (!sawDigit && Character.isDigit(c)) {
                    sawDigit = true;
                } else if (!sawUpper || !sawLower) {
                    if (Character.isUpperCase(c)) {
                        sawUpper = true;
                    } else {
                        sawLower = true;
                    }
                }
            }
        }

        if (password.length() > 7) {
            currentScore++;
            if (sawLower && sawUpper)
                currentScore++;
            if (sawDigit)
                currentScore++;
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
}
