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

import androidx.annotation.NonNull;

import de.b3ttertogeth3r.walhalla.util.Cache;

public enum Rank {
    ACTIVE,
    ACTIVE_B,
    ACTIVE_F,
    PHILISTINES,
    PHILISTINES_B,
    PHILISTINES_F,
    PHILISTINES_H,
    HONOR,
    GUEST,
    GONE,
    LEFT,
    NONE;

    private final int ADMIN = -1;

    public boolean canSee(Visibility visibility) {
        if (Cache.CACHE_DATA.getCharge() == Charge.ADMIN) {
            return true;
        }
        switch (this) {
            case ACTIVE:
            case ACTIVE_B:
            case ACTIVE_F:
                return (visibility == Visibility.ACTIVE || visibility == Visibility.SIGNED_IN);
            case PHILISTINES:
            case PHILISTINES_B:
            case PHILISTINES_F:
            case PHILISTINES_H:
            case HONOR:
                return (visibility == Visibility.PHILISTINES || visibility == Visibility.SIGNED_IN);
            case GUEST:
            case GONE:
            case LEFT:
            case NONE:
            default:
                return visibility == Visibility.PUBLIC;
        }
    }


    /**
     * format the string value so the first letter s capitalized.
     *
     * @return string value of the enum
     */
    @NonNull
    public String getString() {
        try {
            String name = super.toString().toLowerCase();
            String s1 = name.substring(0, 1).toUpperCase();
            return s1 + name.substring(1);
        } catch (Exception e) {
            return super.toString();
        }
    }
}
