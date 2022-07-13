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

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.Map;

import de.b3ttertogeth3r.walhalla.abstract_classes.Loader;
import de.b3ttertogeth3r.walhalla.enums.Rank;
import de.b3ttertogeth3r.walhalla.enums.Visibility;
import de.b3ttertogeth3r.walhalla.object.Account;
import de.b3ttertogeth3r.walhalla.object.Address;
import de.b3ttertogeth3r.walhalla.object.BoardMember;
import de.b3ttertogeth3r.walhalla.object.Chore;
import de.b3ttertogeth3r.walhalla.object.DrinkMovement;
import de.b3ttertogeth3r.walhalla.object.Event;
import de.b3ttertogeth3r.walhalla.object.File;
import de.b3ttertogeth3r.walhalla.object.Location;
import de.b3ttertogeth3r.walhalla.object.Movement;
import de.b3ttertogeth3r.walhalla.object.News;
import de.b3ttertogeth3r.walhalla.object.Person;
import de.b3ttertogeth3r.walhalla.object.Text;

/**
 * Persistence methods to download data from the Firebase firestore database
 *
 * @author B3tterTogeth3r
 * @version 1.1
 * @since 2.0
 */
public interface IFirestoreDownload {
    /**
     * download the events of a given semester as a list of events
     *
     * @param semesterID semester to download the events from
     * @return Loader to return the downloaded list
     */
    Loader<ArrayList<Event>> semesterEvents(String semesterID);

    Loader<ArrayList<Person>> personList();

    /**
     * @param rank       has to be {@link Rank#ACTIVE} or {@link Rank#PHILISTINES}
     * @param semesterID id of the semester
     * @return Loader to return the downloaded list
     */
    Loader<ArrayList<BoardMember>> board(Rank rank, String semesterID);

    Loader<ArrayList<Address>> address(String personID);

    Loader<ArrayList<Location>> locationList();

    Loader<Map<Integer, ArrayList<BoardMember>>> pastChargen(String personID);

    Loader<File> file(DocumentReference reference);

    Loader<ArrayList<Chore>> personUndoneChores(String personID);

    Loader<ArrayList<Chore>> personAllChores(String personID);

    /**
     * Download a list of chores, if the event has any. If the list is empty, return an empty list.
     *
     * @param eventId event to load the cores from
     * @return list with the chores
     * @see Chore
     */
    Loader<ArrayList<Chore>> eventChores(String eventId);

    /**
     * Download the description of a specific event.
     *
     * @param eventId Event-ID to load the description from
     * @return listener for the download results
     */
    Loader<ArrayList<Text>> eventDescription(String eventId);

    /**
     * Download the location of a specific event.
     *
     * @param eventId Event-ID to load the location from
     * @return listener for the download results
     */
    Loader<Location> eventLocation(String eventId);

    Loader<ArrayList<File>> protocols(String semesterID);

    /**
     * download the greeting of a given semester
     *
     * @param semesterID semester to download the greeting from
     * @return listener to wait for the result
     */
    Loader<ArrayList<Text>> semesterGreeting(String semesterID);

    /**
     * Download a list of news entries according to their visibility.
     *
     * @param visibility {@link Rank} group to whom the news entry is visible to
     * @return Listener to wait for the result
     */
    Loader<ArrayList<News>> news(Visibility visibility);

    Loader<ArrayList<Text>> newsText(String newsId);

    Loader<ArrayList<Text>> semesterNotes(String semesterID);

    ListenerRegistration listenPersonBalance(String uid, Loader<Account> loader);

    Loader<ArrayList<Movement>> getPersonMovements(String uid);

    ListenerRegistration semesterAccount(String semesterID, @NonNull Loader<Account> loader);

    Loader<ArrayList<Movement>> getSemesterMovements(String semesterId);

    Loader<ArrayList<DrinkMovement>> getPersonDrinkMovement(String uid, String semester);

    Loader<Event> nextEvent();

    Loader<Person> person(String uid);

    Loader<ArrayList<Address>> personAddress(String uid);

    Loader<File> personImage(String uid);
}