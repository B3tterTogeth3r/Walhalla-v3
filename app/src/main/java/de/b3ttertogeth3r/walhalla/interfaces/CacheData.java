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

package de.b3ttertogeth3r.walhalla.interfaces;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import de.b3ttertogeth3r.walhalla.enums.Charge;
import de.b3ttertogeth3r.walhalla.enums.Rank;

/**
 * The functions in {@link de.b3ttertogeth3r.walhalla.util.Cache Cache} other classes should have
 * access to. There may be more private functions not declared in here.
 *
 * @author B3tterTogeth3r
 * @version 1.0
 * @see SharedPreferences
 * @since 3.1
 */
public interface CacheData {
    String START_PAGE = "start_page";
    String TAG = "Cache";
    String CHOSEN_SEMESTER = "chosen_semester";
    String FIRST_START = "first_start";
    String START_PAGE_STR = START_PAGE + "_string";
    String BOARD_MEMBER = "board_member";
    String CHARGE = "charge";
    String RANK = "rank";

    /**
     * the user can change, if he/she doesn't want the app to collect analytics data. The default
     * value is <code>true</code>.
     *
     * @param value true, if data is allowed to be collected
     * @since {@link CacheData} version 1.0
     */
    void changeAnalyticsCollections(boolean value);

    /**
     * the user can change, if he/she doesn't want the app to collect analytics data. The default
     * value is <code>true</code>.
     *
     * @return the current state of the data collection
     * @since {@link CacheData} version 1.0
     */
    boolean getAnalyticsCollection();

    /**
     * If the user has set a different start page in {@link #setStartPage(int)}, it is returned.
     * Otherwise the default value of "R.string.menu_home" will be returned.
     *
     * @return string or id value of the set start page.
     * @since {@link CacheData} version 1.0
     */
    int getStartPage();

    /**
     * @param pageId resId of the page
     * @since {@link CacheData} version 1.0
     */
    void setStartPage(int pageId);

    /**
     * If the user has set a different start page in {@link #setStartPage(int)}, it is returned.
     * Otherwise the default value of "R.string.menu_home" will be returned.
     *
     * @return string or id value of the set start page.
     * @since {@link CacheData} version 1.0
     */
    String getStartPageStr();

    /**
     * @return true, if the app has never been launched before or the cache has been reset.
     * @since {@link CacheData} version 1.0
     */
    boolean isFirstStart();

    /**
     * Should only be called on the first start of the app or after the cache has been reset by
     * an error or the user.
     *
     * @param value true
     * @since {@link CacheData} version 1.0
     */
    void setFirstStart(boolean value);

    int getChosenSemester();

    void setChosenSemester(int semId);

    boolean isBoardMember();

    void setBoardMember(boolean value);

    @Nullable
    Charge getCharge();

    void setCharge(@NonNull Charge charge);

    Rank getRank();

    void setRank(@NonNull Rank rank);
}
