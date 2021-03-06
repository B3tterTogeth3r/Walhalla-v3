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

package de.b3ttertogeth3r.walhalla.interfaces.firebase;

import androidx.annotation.NonNull;

import de.b3ttertogeth3r.walhalla.enums.Rank;

public interface IAnalytics {
    /**
     * Log the used sites of the user to see which sites are used more often and which are not
     * to enable better usage of ads on the pages.
     *
     * @param name
     *         name of the fragment the user opened
     */
    void screenChange (@NonNull String name);

    /**
     * Set the users custom start page as user property
     */
    void changeStartPage (int string_value);

    void setRank (@NonNull Rank rank);

    /**
     * @see
     * <a href="https://firebase.google.com/docs/analytics/configure-data-collection?platform=android">Data
     * Collection</a>
     * @since 1.0
     */
    void changeDataCollection (boolean value);
}
