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

public enum Pictures {
    CAMERA(0),
    GALLERY(1),
    WEB(2);
    final int number;
    Pictures(int number){
        this.number = number;
    }

    public int value () {
        return number;
    }

    @NonNull
    @Override
    public String toString () {
        return "Pictures";
    }
}
