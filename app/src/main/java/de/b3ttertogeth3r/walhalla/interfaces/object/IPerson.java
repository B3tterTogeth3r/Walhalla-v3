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

package de.b3ttertogeth3r.walhalla.interfaces.object;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import de.b3ttertogeth3r.walhalla.design.TableRow;
import de.b3ttertogeth3r.walhalla.enums.TrafficLightColor;

/**
 * @author B3tterTogeth3r
 * @version 1.0
 * @see Validate
 * @since 3.0
 */
public interface IPerson extends Validate {
    int ID = -1;
    int FIRST_NAME = 0;
    int LAST_NAME = 1;
    int ORIGIN = 2;
    int MAJOR = 3;
    int NICKNAME = 4;
    /**
     * @deprecated use {@link #PASSWORD} instead;
     */
    @Deprecated
    int PASSWORD_STRING = 5;
    int MOBILE = 6;
    int MAIL = 7;
    int JOINED = 8;
    int BIRTHDAY = 9;
    int RANK = 10;
    int ENABLED = 11;
    int PASSWORD = 12;

    String getFull_Name();

    TrafficLightColor getSecurity();

    String getValue(int value);

    TableRow getViewAll(Context context);

    LinearLayout getViewEdit(Context context, View.OnClickListener listener);

    LinearLayout getViewDisplay(Context context);

    @Override
    boolean validate();

    boolean validatePersonal();

    boolean validateFratData();
}
