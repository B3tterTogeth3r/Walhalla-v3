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

import android.content.Context;

import de.b3ttertogeth3r.walhalla.R;

public enum Chore {
    BAR_B,
    BAR_C,
    BAR_1,
    BAR_2,
    BAR_3,
    BAR_4,
    KITCHEN_C,
    KITCHEN_B,
    FOOD_B,
    FOOD_C,
    OMNI_C,
    OMNI_B,
    GARDEN_C,
    GARDEN_B,
    CHAR,
    TENNIS_C,
    TENNIS_B,
    TENNIS_H_B,
    TENNIS_H_C;

    public String getDescription(Context context) {
        // TODO: 16.05.22 fill rest of the case values
        switch (this) {
            case BAR_B:
                return context.getString(R.string.chore_bar_b);
            case BAR_C:
                break;
            case BAR_1:
                break;
            case BAR_2:
                break;
            case BAR_3:
                break;
            case BAR_4:
                break;
            case KITCHEN_C:
                break;
            case KITCHEN_B:
                break;
            case FOOD_B:
                break;
            case FOOD_C:
                break;
            case OMNI_C:
                break;
            case OMNI_B:
                break;
            case GARDEN_C:
                break;
            case GARDEN_B:
                break;
            case CHAR:
                break;
            case TENNIS_C:
                break;
            case TENNIS_B:
                break;
            case TENNIS_H_B:
                break;
            case TENNIS_H_C:
                break;
        }
        return "TODO";
    }
}
