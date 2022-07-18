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

import java.util.ArrayList;

import de.b3ttertogeth3r.walhalla.abstract_classes.Loader;
import de.b3ttertogeth3r.walhalla.object.Address;
import de.b3ttertogeth3r.walhalla.object.Event;
import de.b3ttertogeth3r.walhalla.object.Person;

public interface IFirestoreUpload {

    Loader<Boolean> event(int semID, Event event);

    /**
     * upload the person data of a user to Firestore
     *
     * @param person      person data the user put in
     * @param addressList list of addresses the user has
     * @return true, id upload successfully, false if not
     */
    Loader<Boolean> person(@NonNull Person person, ArrayList<Address> addressList);
}
