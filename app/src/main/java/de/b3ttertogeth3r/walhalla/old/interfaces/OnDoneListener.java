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

package de.b3ttertogeth3r.walhalla.old.interfaces;

import android.content.DialogInterface;
import android.content.Intent;

public interface OnDoneListener {

    default void sortResult (int which, Intent intent) {
        if (which == DialogInterface.BUTTON_NEUTRAL){
            neutral(intent);
        } else if (which == DialogInterface.BUTTON_POSITIVE) {
            positive(intent);
        } else if (which == DialogInterface.BUTTON_NEGATIVE) {
            negative(intent);
        }
    }

    default void positive (Intent resultIntent) {
    }

    default void negative (Intent resultIntent) {
    }

    default void neutral (Intent resultIntent) {
    }
}
