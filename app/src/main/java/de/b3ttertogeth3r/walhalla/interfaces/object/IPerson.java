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

import androidx.annotation.NonNull;

import de.b3ttertogeth3r.walhalla.design.TableRow;
import de.b3ttertogeth3r.walhalla.enums.TrafficLightColor;
import de.b3ttertogeth3r.walhalla.object.Person;
import de.b3ttertogeth3r.walhalla.util.Dev.PersonValue;

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

    /**
     * @return the value of {@link Person#getFirst_Name()} and {@link Person#getLast_Name()}.
     */
    String getFull_Name();

    /**
     * If the account is not enabled, it returns {@link TrafficLightColor#RED}<br>
     * If the account has no password, it returns {@link TrafficLightColor#YELLOW}<br>
     * Otherwise it returns {@link TrafficLightColor#GREEN}
     *
     * @return {@link TrafficLightColor}
     */
    TrafficLightColor getSecurity();

    String getValue(@PersonValue int value);

    void setValue(@PersonValue int value, String content);

    /**
     * Creates a {@link TableRow} designed like:
     * -------------------------------------------------------------------------------------------<br>
     * | {@link #getFull_Name()} | {@link Person#getNickname()} | {@link Person#getRank()} |
     * {@link de.b3ttertogeth3r.walhalla.util.TrafficLight TrafficLight} | > |<br>
     * -------------------------------------------------------------------------------------------<br>
     *
     * @param context {@link Context} to create the row and its content
     * @return {@link TableRow}
     * @since 1.0
     */
    TableRow getViewAll(@NonNull Context context);

    LinearLayout getViewEdit(Context context, View.OnClickListener listener);

    LinearLayout getViewDisplay(Context context);

    /**
     * Called while registering a new person.
     * returns true, if the following fields are <code>NOT null</code>:<br>
     * {@link #FIRST_NAME}, {@link #LAST_NAME}
     *
     * @return {@link Boolean}
     */
    boolean validatePersonal();

    /**
     * Called while registering a new person.
     * returns true, if the following fields are <code>NOT null</code>:<br>
     * {@link #RANK}, {@link #JOINED}
     *
     * @return {@link Boolean}
     */
    boolean validateFratData();


}
