/*
 * Copyright (c) 2022-2023.
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

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

import de.b3ttertogeth3r.walhalla.abstract_generic.Loader;
import de.b3ttertogeth3r.walhalla.annotation.SemesterRange;
import de.b3ttertogeth3r.walhalla.enums.Charge;
import de.b3ttertogeth3r.walhalla.enums.Rank;
import de.b3ttertogeth3r.walhalla.enums.Visibility;
import de.b3ttertogeth3r.walhalla.object.Account;
import de.b3ttertogeth3r.walhalla.object.Address;
import de.b3ttertogeth3r.walhalla.object.BoardMember;
import de.b3ttertogeth3r.walhalla.object.Chore;
import de.b3ttertogeth3r.walhalla.object.Drink;
import de.b3ttertogeth3r.walhalla.object.DrinkMovement;
import de.b3ttertogeth3r.walhalla.object.Event;
import de.b3ttertogeth3r.walhalla.object.File;
import de.b3ttertogeth3r.walhalla.object.Location;
import de.b3ttertogeth3r.walhalla.object.Movement;
import de.b3ttertogeth3r.walhalla.object.News;
import de.b3ttertogeth3r.walhalla.object.Person;
import de.b3ttertogeth3r.walhalla.object.PersonLight;
import de.b3ttertogeth3r.walhalla.object.Semester;
import de.b3ttertogeth3r.walhalla.object.Text;

/**
 * Persistence methods to download data from the Firebase firestore database.
 * It contains the functions:
 * <p>
 * Ungrouped:
 * <ul>
 *     <li>{@link #getSemesterEvents(int)}</li>
 *     <li>{@link #getPersonList()}</li>
 *     <li>{@link #getNews(Visibility)}</li>
 *     <li>{@link #locationList()}</li>
 *     <li>{@link #file(DocumentReference)}</li>
 * </ul>
 * For the Semester:
 * <ul>
 *     <li>{@link #getSemesterProtocols(int)}</li>
 *     <li>{@link #getSemesterGreeting(int)}</li>
 *     <li>{@link #getSemesterNotes(int)}</li>
 *     <li>{@link #getSemesterAccount(int)}</li>
 *     <li>{@link #getSemesterMovements(int)}</li>
 *     <li>{@link #getSemesterBoard(int, Rank)}</li>
 *     <li>{@link #getSemesterBoardOne(int, Charge)}</li>
 * </ul>
 * For an Event:
 * <ul>
 *     <li>{@link #getEventChores(int, String)}</li>
 *     <li>{@link #getEventDescription(int, String)}</li>
 *     <li>{@link #getEventLocation(int, String)}</li>
 *     <li>{@link #getNextEvent()}</li>
 * </ul>
 * For a Person:
 * <ul>
 *     <li>{@link #person(String)}</li>
 *     <li>{@link #getPersonChores(String, boolean)}</li>
 *     <li>{@link #getPersonPastChargen(String)}</li>
 *     <li>{@link #getPersonDrinkMovement(String, int)}</li>
 *     <li>{@link #getPersonBalance(String)}</li>
 *     <li>{@link #getPersonMovements(String)}</li>
 *     <li>{@link #personAddress(String)}</li>
 *     <li>{@link #getPersonImage(String)}</li>
 * </ul>
 * For a News:
 * <ul>
 *     <li>{@link #getNewsText(String)}</li>
 * </ul>
 *
 * @author B3tterTogeth3r
 * @version 1.1
 * @since 2.0
 */
public interface IFirestoreDownload {
    //region SEMESTER

    /**
     * Download the {@link File} objects to the semester Transcripts.
     *
     * @param semesterID {@link Semester#ID}
     * @return {@link Loader} with a {@link File} filled {@link ArrayList}
     */
    Loader<ArrayList<File>> getSemesterProtocols(int semesterID);

    /**
     * Download the greeting of a given semester to display in the
     * {@link de.b3ttertogeth3r.walhalla.fragment.common.Greeting Greeting Fragment}.
     *
     * @param semesterID {@link Semester#ID}
     * @return {@link Loader} with a {@link Text} filled {@link ArrayList} list.
     * @firestorePath /Semester/{SemesterID}/Greeting/{TextID}
     * @since 1.0
     */
    Loader<ArrayList<Text>> getSemesterGreeting(int semesterID);

    /**
     * Download the notes for the given {@link Semester}.
     *
     * @param semesterID {@link Semester#ID}
     * @return {@link Loader} with a {@link Text} filled {@link ArrayList}
     * @firestorePath /Semester/{SemesterID}/Notes/{TextID}
     */
    Loader<ArrayList<Text>> getSemesterNotes(@SemesterRange int semesterID);

    /**
     * Download the Account of the given {@link Semester} from the Firebase Firestore Database.
     *
     * @param semesterID {@link Semester#ID}
     * @return {@link Loader} with an {@link Account} Object
     * @firestorePath /Semester/{SemesterID}/Account/Account
     */
    Loader<Account> getSemesterAccount(@SemesterRange int semesterID);

    /**
     * Download the movements inside the given {@link Semester}.
     *
     * @param semesterID {@link Semester#ID}
     * @return {@link Loader} with a {@link Movement} filled {@link ArrayList}
     * @firestorePath /Semester/{SemesterID}/Account/Account/Movement/{MovementID}
     */
    Loader<ArrayList<Movement>> getSemesterMovements(@SemesterRange int semesterID);

    /**
     * Download the complete Board of the selected {@link Semester}.
     *
     * @param semesterID id of the semester
     * @param rank       has to be {@link Rank#ACTIVE} or {@link Rank#PHILISTINES}
     * @return {@link Loader} to return the downloaded list
     * @firestorePath Depending on the rank-value:
     * @firestorePath /Semester/{SemesterID}/Philistines_Board/{BoardMemberID}
     * @firestorePath /Semester/{SemesterID}/Student_Board/{BoardMemberID}
     * @since 1.0
     */
    Loader<ArrayList<BoardMember>> getSemesterBoard(@SemesterRange int semesterID, @NonNull Rank rank);

    /**
     * Download one board member of the selected semester which can be chosen by the {@link Charge} value.
     *
     * @param semesterID {@link Semester#ID}
     * @param charge     {@link Charge}
     * @return {@link Loader} with one {@link BoardMember}
     * @firestorePath Depending on the Charge-value:
     * @firestorePath /Semester/{SemesterID}/Philistines_Board/{BoardMemberID}
     * @firestorePath /Semester/{SemesterID}/Student_Board/{BoardMemberID}
     */
    Loader<BoardMember> getSemesterBoardOne(int semesterID, @NonNull Charge charge);

    /**
     * Download the events of the given semester as a list.
     *
     * @param semesterID {@link de.b3ttertogeth3r.walhalla.object.Semester#ID}
     * @return {@link Loader} with an {@link Event} filled {@link ArrayList}
     * @firestorePath /Semester/{SemesterID}/Event{EventID}
     * @since 1.0
     */
    Loader<ArrayList<Event>> getSemesterEvents(int semesterID);
    //endregion

    //region EVENT

    /**
     * Download a list of chores, if the event has any.
     *
     * @param eventId    {@link Event#ID}
     * @param semesterID {@link Semester#ID}
     * @return A {@link Loader} with a {@link Chore} filled {@link ArrayList}
     * @firestorePath /Semester/{SemesterID}/Event/{EventID}/Chore/{ChoreID}
     * @since 1.0
     */
    Loader<ArrayList<Chore>> getEventChores(@SemesterRange int semesterID, String eventId);

    /**
     * Download the description of a specific event.
     *
     * @param eventId    {@link Event#ID}
     * @param semesterID {@link Semester#ID}
     * @return {@link Loader} with a {@link Text} filled {@link ArrayList}
     * @firestorePath /Semester/{SemesterID}/Event/{EventID}/Description/{TextID}
     * @since 1.0
     */
    Loader<ArrayList<Text>> getEventDescription(@SemesterRange int semesterID, String eventId);

    /**
     * Download the {@link Location} of a specific {@link Event}.
     *
     * @param eventId    {@link Event#ID}
     * @param semesterID {@link Semester#ID}
     * @return {@link Loader} with a {@link Location} object.
     * @firestorePath /Semester/{SemesterID}/Event/{EventID}/Location/{LocationID}
     */
    Loader<Location> getEventLocation(@SemesterRange int semesterID, String eventId);

    /**
     * Download the next {@link Event} of the current semester.
     *
     * @return {@link Loader} with an {@link Event}
     * @firestorePath /Semester/{SemesterID}/Event/{EventID}
     * @since 1.0
     */
    Loader<Event> getNextEvent();

    Loader<Account> getEventAccount(@SemesterRange int semesterID, String eventId);

    Loader<ArrayList<Movement>> getEventMovements(@SemesterRange int semesterID, String eventId);
    //endregion

    //region PERSON

    /**
     * Download data belonging to one Person
     *
     * @param uid {@link Person#ID}
     * @return {@link Loader} with one {@link Person}
     * @firestorePath /Person/{PersonID}
     */
    Loader<Person> person(String uid);

    /**
     * Download the Chores a person has to do.
     *
     * @param uid            {@link Person#ID}
     * @param showDoneChores true, if all chores shall be downloaded.
     * @return {@link Loader} with a {@link Chore}s filled {@link ArrayList}
     * @firestorePath /Person/{PersonID}/Chore/{ChoreID}
     */
    Loader<ArrayList<Chore>> getPersonChores(String uid, boolean showDoneChores);

    /**
     * Download the past {@link BoardMember Chargen} of a person
     *
     * @param uid {@link Person#ID}
     * @return {@link Loader} with an {@link ArrayList} containing {@link BoardMember}s ordered by {@link BoardMember#SEMESTER}
     * @firestorePath /Person/{PersonID}/Board/{BoardMemberID}
     */
    Loader<ArrayList<BoardMember>> getPersonPastChargen(String uid);

    /**
     * Download the movements of a specific user in a given semester.
     *
     * @param uid      {@link Person#ID}
     * @param semester {@link Semester#ID}
     * @return {@link Loader} with a {@link DrinkMovement} filled {@link ArrayList}
     * @firestorePath /Person/{PersonID}/Drink/{DrinkMovementID}
     */
    Loader<ArrayList<DrinkMovement>> getPersonDrinkMovement(String uid, int semester);

    /**
     * Download the current balance of a given person and return it as a realtime listener
     *
     * @param uid {@link Person#ID}
     * @return {@link Loader} with an {@link Account} element
     * @firestorePath /Person/{PersonID}
     */
    Loader<Account> getPersonBalance(String uid);

    /**
     * Download all {@link Movement}s of a person.
     *
     * @param uid {@link Person#ID}
     * @return {@link Loader} with a {@link Movement} filled {@link ArrayList}
     * @firestorePath /Person/{PersonID}/Account/Account/Movement/{MovementID}
     */
    Loader<ArrayList<Movement>> getPersonMovements(String uid);

    /**
     * Download all {@link Address} of a person.
     *
     * @param uid {@link Person#ID}
     * @return {@link Loader} with an {@link Address} filled {@link ArrayList}
     * @firestorePath /Person/{PersonID}/Address/{AddressID}
     */
    Loader<ArrayList<Address>> personAddress(String uid);

    /**
     * Download the class which has the files data saved.
     *
     * @param uid {@link Person#ID}
     * @return {@link Loader} with a list of {@link File}
     * @firestorePath /Person/{PersonID}/Image/{ImageID}
     */
    Loader<ArrayList<File>> getPersonImage(String uid);
    //endregion

    //region NEWS

    /**
     * Download the content belonging to a {@link News} object downloaded via
     * {@link #getNews(Visibility)}.
     *
     * @param newsID {@link News#ID}
     * @return {@link Loader} with a {@link Text} filled {@link ArrayList}
     * @firestorePath News/News/Text{TextID}
     * @since 1.0
     */
    Loader<ArrayList<Text>> getNewsText(String newsID);

    /**
     * Download a list of news entries according to their visibility.
     *
     * @param visibility {@link Rank} group to whom the news entry is visible to.
     * @return Listener to wait for the result
     */
    Loader<ArrayList<News>> getNews(Visibility visibility);
    //endregion

    //region LISTS

    /**
     * Download a list of all {@link Person} persons in Firestore.
     *
     * @return {@link Loader} with a {@link Person} filled {@link ArrayList}
     * @firestorePath Person/{PersonID}
     * @firestorePath /Person/{PersonID}
     * @since 1.0
     */
    Loader<ArrayList<PersonLight>> getPersonList();

    /**
     * Download a list of default locations from the Firebase Firestore Database.
     *
     * @return A {@link Loader} with an {@link ArrayList} filled with {@link Location}s.
     * @firestorePath /Location/{LocationID}
     * @since 1.0
     */
    Loader<ArrayList<Location>> locationList();

    Loader<ArrayList<Text>> getNotes();

    Loader<ArrayList<Drink>> getDrinkKinds();
    //endregion

    /**
     * Download a {@link File} according to the {@link DocumentReference} given.
     *
     * @param reference {@link DocumentReference} of the path.
     * @return {@link Loader} with a {@link File} object.
     * @firestorePath at the given reference
     * @since 1.1
     */
    Loader<File> file(@NonNull DocumentReference reference);
}