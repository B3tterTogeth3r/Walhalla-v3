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
        switch (this) {
            case BAR_B:
                return context.getString(R.string.chore_bar_b);
            case BAR_C:
                return context.getString(R.string.chore_bar_c);
            case BAR_1:
                return context.getString(R.string.chore_bar_1);
            case BAR_2:
                return context.getString(R.string.chore_bar_2);
            case BAR_3:
                return context.getString(R.string.chore_bar_3);
            case BAR_4:
                return context.getString(R.string.chore_bar_4);
            case KITCHEN_C:
                return context.getString(R.string.chore_kitchen_c);
            case KITCHEN_B:
                return context.getString(R.string.chore_kitchen_b);
            case FOOD_B:
                return context.getString(R.string.chore_food_b);
            case FOOD_C:
                return context.getString(R.string.chore_food_c);
            case OMNI_C:
                return context.getString(R.string.chore_omni_c);
            case OMNI_B:
                return context.getString(R.string.chore_omni_b);
            case GARDEN_C:
                return context.getString(R.string.chore_garden_c);
            case GARDEN_B:
                return context.getString(R.string.chore_garden_b);
            case CHAR:
                return context.getString(R.string.chore_char);
            case TENNIS_C:
                return context.getString(R.string.chore_tennis_c);
            case TENNIS_B:
                return context.getString(R.string.chore_tennis_b);
            case TENNIS_H_B:
                return context.getString(R.string.chore_tennis_h_b);
            case TENNIS_H_C:
                return context.getString(R.string.chore_tennis_h_c);
        }
        return "TODO";
    }
}
