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

/**
 * @author B3tterTogeth3r
 * @version 1.0
 * @see de.b3ttertogeth3r.walhalla.interfaces.object.Validate
 * @since 3.1
 */
public interface IAddress extends Validate {
    /**
     * ID of the {@link de.b3ttertogeth3r.walhalla.firebase.Firestore Firestore} path
     */
    int ID = -1;
    /**
     * Name of the street.
     */
    int STREET = 0;
    /**
     * number of the house in the street.
     */
    int NUMBER = 1;
    /**
     * zip code of the city.
     */
    int ZIP = 2;
    /**
     * Name of the city.
     */
    int CITY = 3;
    /**
     * Name of the state in a country.
     */
    int STATE = 4;
    /**
     * Name of the country.
     */
    int COUNTRY = 5;

    String getValue(int valueId);

    void setValue(int valueId, String value);
}
