/*
 * Copyright (c) 2022.
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

import androidx.annotation.Nullable;

/**
 * Text versions saved in firebase
 * <ol start=0>
 *     <li>{@link #BULLET_ITEM}</li>
 *     <li>{@link #BUTTON}</li>
 *     <li>{@link #DATE}</li>
 *     <li>{@link #EMAIL}</li>
 *     <li>{@link #IMAGE}</li>
 *     <li>{@link #LINEAR_LAYOUT}</li>
 *     <li>{@link #LINK}</li>
 *     <li>{@link #SLIDER_VIEW}</li>
 *     <li>{@link #SUBTITLE1}</li>
 *     <li>{@link #SUBTITLE2}</li>
 *     <li>{@link #TABLE}</li>
 *     <li>{@link #TABLE_ROW}</li>
 *     <li>{@link #TEXT}</li>
 *     <li>{@link #TIME_FRAT}</li>
 *     <li>{@link #TITLE}</li>
 *     <li>{@link #TOAST}</li>
 * </ol>
 *
 * @author B3tterTogeth3r
 * @version 1.1
 * @see java.lang.Enum
 * @since 2.0
 */
public enum TextType {
    BULLET_ITEM,
    BUTTON,
    DATE,
    EMAIL,
    IMAGE,
    LINEAR_LAYOUT,
    LINK,
    SLIDER_VIEW,
    SUBTITLE1,
    SUBTITLE2,
    TABLE,
    TABLE_ROW,
    TEXT,
    TIME_FRAT,
    TITLE,
    TOAST;

    /**
     * find the enum depending of the
     *
     * @param number value of the enum
     * @return the enum
     * @since 1.1
     */
    @Nullable
    public static TextType find(int number) {
        if (BULLET_ITEM.ordinal() == number) {
            return BULLET_ITEM;
        } else if (BUTTON.ordinal() == number) {
            return BUTTON;
        } else if (DATE.ordinal() == number) {
            return DATE;
        } else if (EMAIL.ordinal() == number) {
            return EMAIL;
        } else if (IMAGE.ordinal() == number) {
            return IMAGE;
        } else if (LINEAR_LAYOUT.ordinal() == number) {
            return LINEAR_LAYOUT;
        } else if (LINK.ordinal() == number) {
            return LINK;
        } else if (SLIDER_VIEW.ordinal() == number) {
            return SLIDER_VIEW;
        } else if (SUBTITLE1.ordinal() == number) {
            return SUBTITLE1;
        } else if (SUBTITLE2.ordinal() == number) {
            return SUBTITLE2;
        } else if (TABLE.ordinal() == number) {
            return TABLE;
        } else if (TABLE_ROW.ordinal() == number) {
            return TABLE_ROW;
        } else if (TEXT.ordinal() == number) {
            return TEXT;
        } else if (TIME_FRAT.ordinal() == number) {
            return TIME_FRAT;
        } else if (TITLE.ordinal() == number) {
            return TITLE;
        } else if (TOAST.ordinal() == number) {
            return TOAST;
        } else {
            return find(String.valueOf(number));
        }
    }
    /**
     * find the enum depending of the
     *
     * @param value value of the enum
     * @return the enum
     * @since 1.1
     */
    @Nullable
    public static TextType find(String value) {
        if (BULLET_ITEM.toString().compareToIgnoreCase(value) == 0) {
            return BULLET_ITEM;
        } else if (BUTTON.toString().compareToIgnoreCase(value) == 0) {
            return BUTTON;
        } else if (DATE.toString().compareToIgnoreCase(value) == 0) {
            return DATE;
        } else if (EMAIL.toString().compareToIgnoreCase(value) == 0) {
            return EMAIL;
        } else if (IMAGE.toString().compareToIgnoreCase(value) == 0) {
            return IMAGE;
        } else if (LINEAR_LAYOUT.toString().compareToIgnoreCase(value) == 0) {
            return LINEAR_LAYOUT;
        } else if (LINK.toString().compareToIgnoreCase(value) == 0) {
            return LINK;
        } else if (SLIDER_VIEW.toString().compareToIgnoreCase(value) == 0) {
            return SLIDER_VIEW;
        } else if (SUBTITLE1.toString().compareToIgnoreCase(value) == 0) {
            return SUBTITLE1;
        } else if (SUBTITLE2.toString().compareToIgnoreCase(value) == 0) {
            return SUBTITLE2;
        } else if (TABLE.toString().compareToIgnoreCase(value) == 0) {
            return TABLE;
        } else if (TABLE_ROW.toString().compareToIgnoreCase(value) == 0) {
            return TABLE_ROW;
        } else if (TEXT.toString().compareToIgnoreCase(value) == 0) {
            return TEXT;
        } else if (TIME_FRAT.toString().compareToIgnoreCase(value) == 0) {
            return TIME_FRAT;
        } else if (TITLE.toString().compareToIgnoreCase(value) == 0) {
            return TITLE;
        } else if (TOAST.toString().compareToIgnoreCase(value) == 0) {
            return TOAST;
        } else {
            return null;
        }
    }
}
