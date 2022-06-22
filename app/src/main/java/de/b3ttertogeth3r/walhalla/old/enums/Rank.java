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

package de.b3ttertogeth3r.walhalla.old.enums;

import androidx.annotation.NonNull;


public enum Rank {
    ACTIVE_FUX("Fux"),
    ACTIVE("Bursch"),
    PHILISTINES("Philister"),
    PHILISTINES_B("B-Philister"),
    ACTIVE_FRIEND("Korporationsfreund Aktiver"),
    PHILISTINES_FRIEND("Korporationsfreund Alter Herr"),
    PHILISTINES_HONOR("Ehrenphilister"),
    HONOR_MEMBER("Ehrenmitglied"),
    GUEST("Gast"),
    GONE("Verstorben"),
    LEFT("Ausgetreten"),
    NONE("not_signed_in"),
    ERROR("Error_while_loading");
    /** german description as a string */
    private final String description;

    Rank (String description) {
        this.description = description;
    }

    /**
     * this function is to find the enum based on the {@link #description} of it
     *
     * @param rankName
     *         String value of a name
     * @return the Rank
     */
    public static Rank find (String rankName) {
        if (rankName == null || rankName.isEmpty()) {
            return ERROR;
        } else if (ACTIVE_FUX.getDescription().equals(rankName)) {
            return ACTIVE_FUX;
        } else if (ACTIVE.getDescription().equals(rankName)) {
            return ACTIVE;
        } else if (PHILISTINES.getDescription().equals(rankName)) {
            return PHILISTINES;
        } else if (PHILISTINES_B.getDescription().equals(rankName)) {
            return PHILISTINES_B;
        } else if (ACTIVE_FRIEND.getDescription().equals(rankName)) {
            return ACTIVE_FRIEND;
        } else if (PHILISTINES_FRIEND.getDescription().equals(rankName)) {
            return PHILISTINES_FRIEND;
        } else if (PHILISTINES_HONOR.getDescription().equals(rankName)) {
            return PHILISTINES_HONOR;
        } else if (HONOR_MEMBER.getDescription().equals(rankName)) {
            return HONOR_MEMBER;
        } else if (GUEST.getDescription().equals(rankName)) {
            return GUEST;
        } else if (GONE.getDescription().equals(rankName)) {
            return GONE;
        } else if (LEFT.getDescription().equals(rankName)) {
            return LEFT;
        } else if (NONE.getDescription().equals(rankName)) {
            return NONE;
        } else if (ERROR.getDescription().equals(rankName)) {
            return ERROR;
        } else {
            return NONE;
        }
    }

    public String getDescription () {
        return description;
    }

    public Group getGroup () {
        switch (this) {
            case PHILISTINES:
            case HONOR_MEMBER:
            case PHILISTINES_B:
            case PHILISTINES_HONOR:
            case PHILISTINES_FRIEND:
                return Group.PHILISTINES;
            case ACTIVE:
            case ACTIVE_FUX:
            case ACTIVE_FRIEND:
                return Group.ACTIVE;
            case GUEST:
            case GONE:
            case LEFT:
            case NONE:
            case ERROR:
            default:
                return Group.PUBLIC;
        }
    }

    @NonNull
    @Override
    public String toString () {
        return description;
    }
}
