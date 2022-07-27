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

import de.b3ttertogeth3r.walhalla.abstract_generic.Loader;
import de.b3ttertogeth3r.walhalla.object.Account;
import de.b3ttertogeth3r.walhalla.object.Address;
import de.b3ttertogeth3r.walhalla.object.BoardMember;
import de.b3ttertogeth3r.walhalla.object.Chore;
import de.b3ttertogeth3r.walhalla.object.Event;
import de.b3ttertogeth3r.walhalla.object.File;
import de.b3ttertogeth3r.walhalla.object.Location;
import de.b3ttertogeth3r.walhalla.object.Movement;
import de.b3ttertogeth3r.walhalla.object.News;
import de.b3ttertogeth3r.walhalla.object.Person;
import de.b3ttertogeth3r.walhalla.object.Semester;
import de.b3ttertogeth3r.walhalla.object.Text;
import de.b3ttertogeth3r.walhalla.util.Dev.SemesterRange;
import de.b3ttertogeth3r.walhalla.util.Paragraph;

/**
 * Persistence interface to call all the necessary functions in
 * {@link de.b3ttertogeth3r.walhalla.firebase.Firestore.Upload Firestore.Upload} to upload data into
 * the Firebase Firestore Database.
 * The functions are:
 * <ul>
 *     <li>{@link #setBoard(int, ArrayList)}</li>
 *     <li>{@link #setEvent(int, Event)}</li>
 *     <li>{@link #setEventChore(String, Chore)}</li>
 *     <li>{@link #setEventLocation(String, Location)}</li>
 *     <li>{@link #setEventDescription(String, ArrayList)}</li>
 *     <li>{@link #addSemesterMovement(int, Movement)}</li>
 *     <li>{@link #addSemesterProtocol(int, File)}</li>
 *     <li>{@link #addNewsEntry(News, ArrayList)}</li>
 *     <li>{@link #setPerson(Person)}</li>
 *     <li>{@link #setPersonAddress(String, ArrayList)}</li>
 *     <li>{@link #setPersonChore(String, Chore)}</li>
 *     <li>{@link #setPersonCharge(String, int, BoardMember)}</li>
 *     <li>{@link #addPersonMovement(String, Movement)}</li>
 *     <li>{@link #addPersonPicture(String, File)}</li>
 * </ul>
 *
 * @author B3tterTogeth3r
 * @version 2.0
 * @since 3.1
 */
public interface IFirestoreUpload {

    //region SEMESTER

    /**
     * @param semID        ID of the semester the {@link BoardMember}s belong to.
     * @param boardMembers List of the {@link BoardMember}s.
     * @return true, if upload was successful <br>false, if not.
     */
    Loader<Boolean> setBoard(@SemesterRange int semID, @NonNull ArrayList<BoardMember> boardMembers);

    //region EVENT

    /**
     * Upload am {@link Event} to the Firebase Firestore Database.
     *
     * @param semID ID of the {@link Semester} the {@link Event} belongs to
     * @param event {@link Event} to upload
     * @return true, if upload successful <br>false, if not.
     */
    Loader<Boolean> setEvent(@SemesterRange int semID, @NonNull Event event);

    /**
     * Upload a {@link Chore} to an {@link Event}. Firebase CF will sync them with the
     * {@link Person} who has to do the {@link Chore}.
     *
     * @param eventID The id of the {@link Event} the {@link Chore} belongs
     * @param chore   The {@link Chore} that has to be fulfilled.
     * @return true, if upload successful <br>false, if not.
     */
    Loader<Boolean> setEventChore(@NonNull String eventID, @NonNull Chore chore);

    /**
     * Set the location for an {@link Event}.
     *
     * @param eventID  The ID of the {@link Event} the {@link Location} belongs to
     * @param location The {@link Location} object
     * @return true, if upload successful <br>false, if not.
     */
    Loader<Boolean> setEventLocation(@NonNull String eventID, @NonNull Location location);

    /**
     * Set the description of an {@link Event}. Every event can have a description like a {@link News} entry.
     *
     * @param eventID     {@link Event#ID}
     * @param description the description.
     * @return true, if upload successful <br>false, if not.
     */
    Loader<Boolean> setEventDescription(@NonNull String eventID, @NonNull ArrayList<Paragraph<Text>> description);
    //endregion

    /**
     * Add a {@link Movement} to the Firebase Firestore Database. The
     * {@link de.b3ttertogeth3r.walhalla.object.Account} will sync automatically via Firebase CF.
     *
     * @param semID    {@link Semester#ID}
     * @param movement The {@link Movement} to upload.
     * @return true, if upload successful <br>false, if not.
     */
    Loader<Boolean> addSemesterMovement(@SemesterRange int semID, @NonNull Movement movement);

    /**
     * Upload a protocol to a semester. The description should hold to which meeting it belongs.
     *
     * @param semID {@link Semester#ID}
     * @param file  The {@link File}
     * @return true, if upload successful <br>false, if not.
     */
    Loader<Boolean> addSemesterProtocol(@SemesterRange int semID, @NonNull File file);
    //endregion

    /**
     * Upload a new {@link News} entry filled with an array of {@link Text} objects.
     *
     * @param news {@link News} id
     * @param text {@link Text}
     * @return true, if upload successful <br>false, if not.
     */
    Loader<Boolean> addNewsEntry(@NonNull News news, @NonNull ArrayList<Text> text);

    //region Person

    /**
     * upload the {@link Person} data of a user to Firestore
     *
     * @param person {@link Person} data the user put in
     * @return true, if upload successful <br>false, if not.
     */
    Loader<Boolean> setPerson(@NonNull Person person);

    /**
     * Set the given list as the AddressList as the current list in the also given {@link Person}
     *
     * @param personID    {@link Person#ID}
     * @param addressList The list of {@link Address}es to upload.
     * @return true, if upload successful <br>false, if not.
     */
    Loader<Boolean> setPersonAddress(@NonNull String personID, @NonNull ArrayList<Address> addressList);

    /**
     * Upload a new or changed {@link Chore} A {@link Person} has to do. It will be synced with
     * {@link Event} Chores via Firebase CF.
     *
     * @param personID {@link Person#ID}
     * @param chore    The {@link Chore} the person has to do or changed
     * @return true, if upload successful <br>false, if not.
     */
    Loader<Boolean> setPersonChore(@NonNull String personID, @NonNull Chore chore);

    /**
     * Set a {@link BoardMember} aka Charge for a person in a set {@link Semester}.
     *
     * @param personID    {@link Person#ID}
     * @param semester    {@link Semester#ID}.
     * @param boardMember {@link BoardMember} values to upload.
     * @return true, if upload successful <br>false, if not.
     */
    Loader<Boolean> setPersonCharge(@NonNull String personID, @SemesterRange int semester, @NonNull BoardMember boardMember);

    /**
     * Add a {@link Movement} to a person. The {@link Account} will be updated automatically via
     * Firebase CF.
     *
     * @param personID {@link Person#ID}
     * @param movement The {@link Movement} to be added.
     * @return true, if upload successful <br>false, if not.
     */
    Loader<Boolean> addPersonMovement(@NonNull String personID, @NonNull Movement movement);

    /**
     * Add a picture {@link File} to a {@link Person}
     *
     * @param personID {@link Person#ID}
     * @param file     The {@link File} to upload.
     * @return true, if upload successful <br>false, if not.
     */
    Loader<Boolean> addPersonPicture(@NonNull String personID, @NonNull File file);
    //endregion
}
