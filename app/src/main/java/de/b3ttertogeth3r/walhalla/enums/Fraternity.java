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

public enum Fraternity {
    NAME ("K.St.V. Walhalla im KV zu Würzburg"),
    ADH ("Mergenheimer Straße 32\n97082 Würzburg"),
    WEBSITE ("http://walhalla-wuerzburg.de"),
    EMAIL_INFO ("info@walhalla-wuerzburg.de"),
    EMAIL_SENIOR ("senior@walhalla-wuerzburg.de"),
    EMAIL_CONSENIOR ("consenior@walhalla-wuerzburg.de"),
    EMAIL_FM ("fuchsmajor@walhalla-wuerzburg.de"),
    EMAIL_XX ("schriftfuerer@walhalla-wuerzburg.de"),
    EMAIL_XXX ("kassier@walhalla-wuerzburg.de"),
    NAME_1("K.St.V. Walhalla"),
    NAME_2("im KV zu Würzburg");

    private final String value;
    Fraternity(String value) {
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
