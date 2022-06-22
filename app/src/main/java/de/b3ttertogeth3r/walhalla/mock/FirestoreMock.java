/*
 * Copyright (c) 2022.
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

package de.b3ttertogeth3r.walhalla.mock;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.b3ttertogeth3r.walhalla.App;
import de.b3ttertogeth3r.walhalla.abstract_classes.Loader;
import de.b3ttertogeth3r.walhalla.design.Toast;
import de.b3ttertogeth3r.walhalla.enums.Charge;
import de.b3ttertogeth3r.walhalla.enums.Collar;
import de.b3ttertogeth3r.walhalla.enums.Punctuality;
import de.b3ttertogeth3r.walhalla.enums.Rank;
import de.b3ttertogeth3r.walhalla.enums.TextType;
import de.b3ttertogeth3r.walhalla.enums.Visibility;
import de.b3ttertogeth3r.walhalla.exception.NoDataException;
import de.b3ttertogeth3r.walhalla.interfaces.IFirestoreDownload;
import de.b3ttertogeth3r.walhalla.interfaces.IFirestoreUpload;
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
import de.b3ttertogeth3r.walhalla.util.List;
import de.b3ttertogeth3r.walhalla.util.ToastList;

public class FirestoreMock {

    public static class Download implements IFirestoreDownload {
        private static final String TAG = "FirestoreMockDownload";

        public Download() {
            ToastList.addToast(Toast.makeToast(App.getContext(), TAG + "-MOCK-DATA"));
        }

        @Override
        public Loader<ArrayList<Event>> semesterEvents(String semesterID) {
            Loader<ArrayList<Event>> loader = new Loader<>();
            List<Event> eventList = new List<>();
            Timestamp start = new Timestamp(new Date());
            Event e1 = new Event(start, start, "Event 1", "This is the first event", Collar.IO,
                    Punctuality.CT, Visibility.PUBLIC);
            Event e2 = new Event(start, start, "Event 2", "This is the second event", Collar.HO,
                    Punctuality.CT, Visibility.PUBLIC);
            Event e3 = new Event(start, start, "Event 3", "The last event of the semester",
                    Collar.IO, Punctuality.CT, Visibility.PUBLIC);
            eventList.add(e1);
            eventList.add(e2);
            eventList.add(e3);
            return loader.done(eventList);
        }

        @Override
        public Loader<ArrayList<Person>> personList() {
            @NonNull Loader<ArrayList<Person>> loader = new Loader<>();
            ArrayList<Person> result = new ArrayList<>();
            Person p1 = new Person();
            p1.setFirst_Name("Tobias");
            p1.setLast_Name("Tumbrink");
            p1.setNickname("");
            p1.setEnabled(true);
            p1.setPassword(true);
            p1.setRank(Rank.ACTIVE);

            Person p2 = new Person();
            p2.setLast_Name("Jäckel");
            p2.setFirst_Name("Maximilian");
            p2.setNickname("Minimax");
            p2.setEnabled(true);
            p2.setPassword(false);
            p1.setRank(Rank.ACTIVE);

            Person p3 = new Person();
            p3.setLast_Name("Beulich");
            p3.setFirst_Name("Max");
            p3.setNickname("Bimbo");
            p3.setEnabled(false);
            p3.setPassword(false);
            p3.setRank(Rank.PHILISTINES);

            result.add(p1);
            result.add(p2);
            result.add(p3);

            return loader.done(result);
        }

        @Override
        public Loader<ArrayList<BoardMember>> board(@NonNull Rank rank, String semesterID) {
          Loader<ArrayList<BoardMember>> loader = new Loader<>();
            BoardMember bm1, bm2, bm3, bm4, bm5;
            switch (rank) {
                case ACTIVE:
                    bm1 = new BoardMember("Jonas Kohl", Charge.X);
                    bm2 = new BoardMember("Maximilian Jäckel", Charge.VX);
                    bm3 = new BoardMember("Vinzent Gresser", Charge.FM);
                    bm4 = new BoardMember("Justus Höll", Charge.XX);
                    bm5 = new BoardMember("Marko Koblja", Charge.XXX);
                    break;
                case PHILISTINES:
                    bm1 = new BoardMember("Thilo Berdami", Charge.AH_X);
                    bm2 = new BoardMember("Dieter Vogel", Charge.AH_XX);
                    bm3 = new BoardMember("Christian Söllner", Charge.AH_XXX);
                    bm4 = new BoardMember("Ernst Höring", Charge.AH_HW);
                    bm5 = new BoardMember("Armin Hoch", Charge.AH_HW);
                    break;
                default:
                    bm1 = new BoardMember();
                    bm2 = new BoardMember();
                    bm3 = new BoardMember();
                    bm4 = new BoardMember();
                    bm5 = new BoardMember();
                    break;
            }
            return loader.done(new ArrayList<>(Arrays.asList(bm1, bm2, bm3, bm4, bm5)));
        }

        @Override
        public Loader<ArrayList<Address>> address(String personID){
            Loader<ArrayList<Address>> loader = new Loader<>();
            ArrayList<Address> results = new ArrayList<>();
            Address a1 = new Address("Mergentheimer Straße", "32 - 34a", "97082", "Würzburg",
                    "Bayern", "Deutschland");
            Address a2 = new Address("Uhlandstraße", "9", "97072", "Würzburg", "Bayern",
                    "Deutschland");
            Address a3 = new Address("Sanderstraße", "24", "97070", "Würzburg", "Bayern",
                    "Deutschland");

            results.add(a1);
            results.add(a2);
            results.add(a3);

            return loader.done(results);
        }

        @Override
        public Loader<ArrayList<Location>> locationList() {
            @NonNull Loader<ArrayList<Location>> loader = new Loader<>();
            Location l1 = new Location("Walhalla", new GeoPoint(49.784420, 9.924580));
            Location l2 = new Location("Normannia", new GeoPoint(449.781190, 9.925320));
            Location l3 = new Location("Askania-Burgundia", new GeoPoint(52.468660, 13.280510));
            Location l4 = new Location("Rheno-Frankonia", new GeoPoint(49.792250, 9.935500));
            Location l5 = new Location("St. Kilians Dom, Würzburg", new GeoPoint(49.793150,
                    9.931400));
            Location l6 = new Location("St. Peter und Paul, Würzburg", new GeoPoint(49.791590,
                    9.931630));
            return loader.done(new ArrayList<>(Arrays.asList(l1, l2, l3, l4, l5, l6)));
        }

        @Override
        public Loader<Map<Integer, ArrayList<BoardMember>>> pastChargen(String personID) {
            Loader<Map<Integer, ArrayList<BoardMember>>> loader = new Loader<>();
            BoardMember bm1 = new BoardMember();
            bm1.setCharge(Charge.X);
            bm1.setFull_name("Tobias Tumbrink");
            BoardMember bm2 = new BoardMember();
            bm2.setCharge(Charge.XXX);
            bm2.setFull_name("Tobias Tumbrink");

            Map<Integer, ArrayList<BoardMember>> result = new HashMap<>();
            result.put(310, new ArrayList<>(Collections.singletonList(bm1)));
            result.put(311, new ArrayList<>(Collections.singletonList(bm2)));
            result.put(312, new ArrayList<>(Collections.singletonList(bm2)));
            result.put(313, new ArrayList<>(Collections.singletonList(bm2)));
            result.put(314, new ArrayList<>(Collections.singletonList(bm2)));
            result.put(315, new ArrayList<>(Collections.singletonList(bm1)));
            return loader.done(result);
        }

        @Override
        public Loader<File> file(DocumentReference reference) {
            Loader<File> loader = new Loader<>();
            return loader;

        }

        @Override
        public Loader<ArrayList<Chore>> personUndoneChores(String personID) {
            Loader<ArrayList<Chore>> loader = new Loader<>();
            Chore c1, c3, c4, c6;
            c1 = new Chore();
            c1.setChore(de.b3ttertogeth3r.walhalla.enums.Chore.BAR_1);
            c1.setEvent("Event 1");
            c1.setDone(false);
            c3 = new Chore();
            c3.setChore(de.b3ttertogeth3r.walhalla.enums.Chore.CHAR);
            c3.setEvent("Event 1");
            c3.setDone(false);
            c4 = new Chore();
            c4.setChore(de.b3ttertogeth3r.walhalla.enums.Chore.BAR_1);
            c4.setEvent("Event 2");
            c4.setDone(false);
            c6 = new Chore();
            c6.setChore(de.b3ttertogeth3r.walhalla.enums.Chore.CHAR);
            c6.setEvent("Event 3");
            c6.setDone(false);
            return loader.done(new ArrayList<>(Arrays.asList(c1, c3, c4, c6)));
        }

        @Override
        public Loader<ArrayList<Chore>> personAllChores(String personID) {
            Loader<ArrayList<Chore>> loader = new Loader<>();
            Chore c1, c2, c3, c4, c5, c6;
            c1 = new Chore();
            c1.setChore(de.b3ttertogeth3r.walhalla.enums.Chore.BAR_1);
            c1.setEvent("Event 1");
            c1.setDone(false);
            c2 = new Chore();
            c2.setChore(de.b3ttertogeth3r.walhalla.enums.Chore.FOOD_B);
            c2.setEvent("Event 1");
            c2.setDone(true);
            c3 = new Chore();
            c3.setChore(de.b3ttertogeth3r.walhalla.enums.Chore.CHAR);
            c3.setEvent("Event 1");
            c3.setDone(false);
            c4 = new Chore();
            c4.setChore(de.b3ttertogeth3r.walhalla.enums.Chore.BAR_1);
            c4.setEvent("Event 2");
            c4.setDone(false);
            c5 = new Chore();
            c5.setChore(de.b3ttertogeth3r.walhalla.enums.Chore.FOOD_B);
            c5.setEvent("Event 2");
            c5.setDone(true);
            c6 = new Chore();
            c6.setChore(de.b3ttertogeth3r.walhalla.enums.Chore.CHAR);
            c6.setEvent("Event 3");
            c6.setDone(false);
            return loader.done(new ArrayList<>(Arrays.asList(c1, c2, c3, c4, c5, c6)));
        }

        @Override
        public Loader<ArrayList<Chore>> eventChores(String eventId) {
            Loader<ArrayList<Chore>> loader = new Loader<>();
            List<Chore> resultList = new List<>();
            Chore c1 = new Chore();
            c1.setChore(de.b3ttertogeth3r.walhalla.enums.Chore.BAR_1);
            c1.setEvent("Event 1");
            c1.setTime(new Timestamp(new Date()));
            c1.setDone(false);
            Chore c2 = new Chore();
            c2.setChore(de.b3ttertogeth3r.walhalla.enums.Chore.FOOD_B);
            c2.setEvent("Event 1");
            c2.setTime(new Timestamp(new Date()));
            c2.setDone(true);
            Chore c3 = new Chore();
            c3.setChore(de.b3ttertogeth3r.walhalla.enums.Chore.CHAR);
            c3.setEvent("Event 1");
            c3.setTime(new Timestamp(new Date()));
            c3.setDone(false);
            resultList.add(c1);
            resultList.add(c2);
            resultList.add(c3);
            if (resultList.isEmpty()) {
                loader.done(new NoDataException("No Chores found"));
            }
            return loader.done(resultList);
        }

        @SuppressWarnings("unchecked")
        @Override
        public Loader<ArrayList<Text>> eventDescription(String eventId) {
            Loader<ArrayList<Text>> loader = new Loader<>();
            ArrayList<Text> list = new ArrayList<>();
            Text one = new Text();
            one.setKind(TextType.TEXT);
            one.setPosition(0);
            ArrayList<String> values = new ArrayList<>();
            values.add("unter der Leitung unsers lb. AH Monsignore Dr. Matthias Türk.");
            one.setValue((ArrayList<String>) values.clone());
            list.add(one);

            Text two = new Text();
            two.setKind(TextType.TEXT);
            two.setPosition(0);
            values.set(0, "in der Sepultur des Domes zu Würzburg");
            two.setValue((ArrayList<String>) values.clone());
            list.add(two);

            Text three = new Text();
            three.setKind(TextType.TEXT);
            three.setPosition(0);
            values.set(0, "gemeinsames loslaufen zum Dom adH um 17.30 st");
            three.setValue((ArrayList<String>) values.clone());
            list.add(three);
            return loader.done(list);
        }

        @Override
        public Loader<Location> eventLocation(String eventId) {
            Loader<Location> loader = new Loader<>();
            Location l1 = new Location("Walhalla", new GeoPoint(49.784420, 9.924580));
            return loader.done(l1);
        }

        @Override
        public Loader<ArrayList<File>> protocols(String semesterID) {
            Loader<ArrayList<File>> loader = new Loader<>();
            ArrayList<File> list = new ArrayList<>();
            return loader.done(list);
        }

        @SuppressWarnings("unchecked")
        @Override
        public Loader<ArrayList<Text>> semesterGreeting(String semesterID) {
            Loader<ArrayList<Text>> loader = new Loader<>();
            ArrayList<Text> list = new ArrayList<>();
            Text one = new Text();
            one.setKind(TextType.TITLE);
            one.setPosition(0);
            ArrayList<String> values = new ArrayList<>();
            values.add("Grußwort des Seniors");
            one.setValue((ArrayList<String>) values.clone());
            list.add(one);

            Text two = new Text();
            two.setKind(TextType.TEXT);
            two.setPosition(0);
            values.add("Liebe Bundesbrüder, werte Kartellbrüder, verehrte Damen und liebe Leser,");
            two.setValue((ArrayList<String>) values.clone());
            list.add(two);

            Text three = new Text();
            three.setKind(TextType.TEXT);
            three.setPosition(0);
            values.add("Hier kommt demnächst ein Grußwort rein.");
            three.setValue((ArrayList<String>) values.clone());
            list.add(three);

            return loader.done(list);
        }

        @Override
        public Loader<ArrayList<News>> news(Visibility visibility) {
            Loader<ArrayList<News>> loader = new Loader<>();
            ArrayList<News> newsList = new ArrayList<>(Arrays.asList(
                    new News("First news entry", Visibility.PUBLIC, 3, "MOCK",
                            new Timestamp(new Date())),
                    new News("First private news entry", Visibility.ACTIVE, 3, "MOCK",
                            new Timestamp(new Date())),
                    new News("Second entry, plus one private", Visibility.PUBLIC, 3, "MOCK",
                            new Timestamp(new Date())),
                    new News("Third entry, plus one private", Visibility.PUBLIC, 3, "MOCK",
                            new Timestamp(new Date())),
                    new News("Second private news entry", Visibility.ACTIVE, 3, "MOCK",
                            new Timestamp(new Date()))
            ));
            ArrayList<News> resultList = new ArrayList<>();
            for (News n : newsList) {
                if (n.getVisibility() == visibility) {
                    resultList.add(n);
                }
            }
            return loader.done(resultList);
        }

        @Override
        public Loader<ArrayList<Text>> newsText(String newsId) {
            Loader<ArrayList<Text>> loader = new Loader<>();
            Text t1 = new Text();
            t1.setValue(new ArrayList<>(Collections.singletonList("First Text Item")));
            Text t2 = new Text();
            t2.setValue(new ArrayList<>(Collections.singletonList("Second Text Item")));
            Text t3 = new Text();
            t3.setValue(new ArrayList<>(Collections.singletonList("Third Text Item")));

            return loader.done(new ArrayList<>(Arrays.asList(t1, t2, t3)));
        }

        @Override
        public Loader<ArrayList<Text>> semesterNotes(String semesterID) {
            Loader<ArrayList<Text>> loader = new Loader<>();

            return loader;

        }

        @Override
        public ListenerRegistration listenPersonBalance(String uid,
                                                        @NonNull Loader<Account> loader) {
            loader.done(new Account(4.5f, 6, 15.5f, 11f));
            return () -> {
            };
        }

        @Override
        public Loader<ArrayList<Movement>> getPersonMovements(String uid) {
            Loader<ArrayList<Movement>> loader = new Loader<>();
            Movement m1 = new Movement(new Timestamp(new Date()), 5.5, "2 Beer", "Payment", null);
            Movement m2 = new Movement(new Timestamp(new Date()), 6.0, "1 Maß", "Payment", null);
            Movement m3 = new Movement(new Timestamp(new Date()), -11.0, "", "Invoice", null);
            Movement m4 = new Movement(new Timestamp(new Date()), 6.5, "2 Beer", "Payment", null);
            Movement m5 = new Movement(new Timestamp(new Date()), 6.0, "1 Maß", "Payment", null);
            Movement m6 = new Movement(new Timestamp(new Date()), -11.0, "", "Invoice", null);
            List<Movement> mL = new List<>();
            mL.add(m1);
            mL.add(m3);
            mL.add(m2);
            mL.add(m4);
            mL.add(m5);
            mL.add(m6);
            return loader.done(mL);
        }

        @Override
        public ListenerRegistration semesterAccount(String semesterID,
                                                    @NonNull Loader<Account> loader) {
            Account head = new Account(3.0f, 6, 12.5f, 11f);
            loader.done(head);
            return null;
        }

        @Override
        public Loader<ArrayList<Movement>> getSemesterMovements(String semesterId) {
            Loader<ArrayList<Movement>> loader = new Loader<>();
            Movement m1 = new Movement(new Timestamp(new Date()), 6.5, "2 Beer", "iksuhgfdfjgls",
                    null);
            Movement m2 = new Movement(new Timestamp(new Date()), 6.0, "1 Maß", "akdjfln", null);
            Movement m3 = new Movement(new Timestamp(new Date()), -11.0, "", "Event 1", null);
            Movement m4 = new Movement(new Timestamp(new Date()), 6.5, "2 Beer", "iksuhgfdfjgls",
                    null);
            Movement m5 = new Movement(new Timestamp(new Date()), 6.0, "1 Maß", "akdjfln", null);
            Movement m6 = new Movement(new Timestamp(new Date()), -11.0, "", "Event 1", null);
            List<Movement> mL = new List<>();
            mL.add(m1);
            mL.add(m3);
            mL.add(m2);
            mL.add(m4);
            mL.add(m5);
            mL.add(m6);
            return loader.done(mL);
        }

        @Override
        public Loader<ArrayList<DrinkMovement>> getPersonDrinkMovement(String uid, String semester) {
            Loader<ArrayList<DrinkMovement>> loader = new Loader<>();

            DrinkMovement dm1 = new DrinkMovement("Martinsbräu Pils", 102, 1,
                    new Timestamp(new Date()));
            DrinkMovement dm2 = new DrinkMovement("Martinsbräu Helles", 29, 1,
                    new Timestamp(new Date()));
            DrinkMovement dm3 = new DrinkMovement("Vollzahlung", 29, 1,
                    new Timestamp(new Date()));
            DrinkMovement dm4 = new DrinkMovement("Martinsbräu Weizen", 120, 1,
                    new Timestamp(new Date()));
            DrinkMovement dm5 = new DrinkMovement("Martinsbräu Märzen", 50, 1,
                    new Timestamp(new Date()));
            DrinkMovement dm6 = new DrinkMovement("Zahlung via Dauerauftrag", 5, 1,
                    new Timestamp(new Date()));
            List<DrinkMovement> dml = new List<>();
            dml.add(dm1);
            dml.add(dm2);
            dml.add(dm3);
            dml.add(dm4);
            dml.add(dm5);
            dml.add(dm6);
            return loader.done(dml);
        }
    }

    public static class Upload implements IFirestoreUpload {
        private static final String TAG = "FirestoreUpload";

        public Upload() {
            ToastList.addToast(Toast.makeToast(App.getContext(), TAG + "-MOCK-DATA"));
        }

        @Override
        public void event(int semID, @NonNull Event event) {
            if (event.validate()) {
                // TODO: 16.05.22 upload event and send success or fail message to user
            }
        }
    }
}
