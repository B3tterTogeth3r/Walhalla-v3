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

package de.b3ttertogeth3r.walhalla.old.utils;

import java.util.Locale;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.old.App;

public class Variables {
    public static final Locale LOCALE = new Locale("de", "DE");
    public static final String[] MONTHS = {App.getContext().getString(R.string.month_jan),
            App.getContext().getString(R.string.month_feb), App.getContext().getString(R.string.month_mar),
            App.getContext().getString(R.string.month_apr), App.getContext().getString(R.string.month_may),
            App.getContext().getString(R.string.month_jun), App.getContext().getString(R.string.month_jul),
            App.getContext().getString(R.string.month_aug), App.getContext().getString(R.string.month_sep),
            App.getContext().getString(R.string.month_oct), App.getContext().getString(R.string.month_nov),
            App.getContext().getString(R.string.month_dec)};
    public static final int CAMERA = 2;
    public static final int GALLERY = 3;
    public static final int SIGN_IN = 4;
    public static final int REGISTER_COMPLETE = 5;
    public static final int CHANGE_COMPLETE = 6;
    public static final int SIGN_IN_COMPLETE = 7;
}
