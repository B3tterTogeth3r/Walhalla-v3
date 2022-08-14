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

import de.b3ttertogeth3r.walhalla.abstract_generic.Loader;
import de.b3ttertogeth3r.walhalla.enums.Charge;

/**
 * Interface with all the functions to call CloudFunctions via the app.
 *
 * @author B3tterTogeth3r
 * @version 1.0
 * @since 3.2
 */
public interface ICloudFunctions {
    /**
     * Check, if the signed in user is a member of the student or philstines board.
     *
     * @param uid UID to check against
     * @return true, if uid is in the
     * @since 1.0
     */
    Loader<Boolean> checkBoardMember(String uid);

    Loader<Charge> getCharge(String uid);
}
