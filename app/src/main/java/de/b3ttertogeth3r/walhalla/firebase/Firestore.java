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

package de.b3ttertogeth3r.walhalla.firebase;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import de.b3ttertogeth3r.walhalla.abstract_classes.Loader;
import de.b3ttertogeth3r.walhalla.enums.Rank;
import de.b3ttertogeth3r.walhalla.enums.Visibility;
import de.b3ttertogeth3r.walhalla.exception.NoDataException;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IFirestoreDownload;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IFirestoreUpload;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IInit;
import de.b3ttertogeth3r.walhalla.object.Account;
import de.b3ttertogeth3r.walhalla.object.Address;
import de.b3ttertogeth3r.walhalla.object.BoardMember;
import de.b3ttertogeth3r.walhalla.object.Chore;
import de.b3ttertogeth3r.walhalla.object.DrinkMovement;
import de.b3ttertogeth3r.walhalla.object.Event;
import de.b3ttertogeth3r.walhalla.object.File;
import de.b3ttertogeth3r.walhalla.object.Location;
import de.b3ttertogeth3r.walhalla.object.Log;
import de.b3ttertogeth3r.walhalla.object.Movement;
import de.b3ttertogeth3r.walhalla.object.News;
import de.b3ttertogeth3r.walhalla.object.Person;
import de.b3ttertogeth3r.walhalla.object.Semester;
import de.b3ttertogeth3r.walhalla.object.Text;

public class Firestore implements IInit {
    private static final String TAG = "Firestore";
    protected static IFirestoreDownload download;
    protected static IFirestoreUpload upload;
    private FirebaseFirestore FBFS;

    public Firestore() {
    }

    public boolean init(Context context) {
        try {
            FBFS = FirebaseFirestore.getInstance();
            download = new Download();
            upload = new Upload();
            //createSemesters();
            return true;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return false;
        }
    }

    private void createSemesters() {
        int l = 1864;
        int s = 64;
        for (int i = 0; i < 400; i++) {
            Calendar start = Calendar.getInstance();
            start.set(l, 9, 1, 0, 0, 0);
            Calendar end = Calendar.getInstance();
            end.set((l + 1), 2, 30, 23, 59, 59);
            String longStr = "Wintersemester " + l + " / " + (l + 1);
            String shortStr = "WS" + s + "/" + (s + 1);
            Semester sem = new Semester(String.valueOf(i), longStr, shortStr,
                    new Timestamp(start.getTime()), new Timestamp(end.getTime()));
            FBFS.collection("Semester")
                    .document(String.valueOf(i))
                    .set(sem);

            i++;
            s++;
            l++;
            if (s == 100) {
                s = 0;
            }
            start.set(l, 3, 1, 0, 0, 0);
            end.set(l, 8, 31, 23, 59, 59);
            longStr = "Sommersemester " + l;
            if (s > 10) {
                shortStr = "SS0" + s;
            } else {
                shortStr = "SS" + s;
            }
            sem = new Semester(String.valueOf(i), longStr, shortStr,
                    new Timestamp(start.getTime()), new Timestamp(end.getTime()));
            FBFS.collection("Semester")
                    .document(String.valueOf(i))
                    .set(sem);
        }
    }

    public class Download implements IFirestoreDownload {

        public Download() {
            download = this;
        }

        @Override
        public Loader<ArrayList<Event>> semesterEvents(String semesterID) {
            Loader<ArrayList<Event>> loader = new Loader<>();
            ArrayList<Event> eventList = new ArrayList<>();
            return loader.done(eventList);
        }

        @Override
        public Loader<ArrayList<Person>> personList() {
            Loader<ArrayList<Person>> loader = new Loader<>();
            FBFS.collection("Person")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        ArrayList<Person> personList = new ArrayList<>();
                        if (queryDocumentSnapshots.isEmpty()) {
                            loader.done(new NoDataException("No persons downloaded"));
                            return;
                        }
                        for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                            try {
                                Person p = ds.toObject(Person.class);
                                personList.add(p);
                            } catch (Exception e) {
                                loader.done(e);
                                return;
                            }
                        }
                        loader.done(personList);
                    }).addOnFailureListener(loader::done);
            return loader;
        }

        @Override
        public Loader<ArrayList<BoardMember>> board(Rank rank, String semesterID) {
            Loader<ArrayList<BoardMember>> loader = new Loader<>();
            ArrayList<BoardMember> memberList = new ArrayList<>();
            return loader.done(memberList);
        }

        @Override
        public Loader<ArrayList<Location>> locationList() {
            Loader<ArrayList<Location>> loader = new Loader<>();
            ArrayList<Location> locationList = new ArrayList<>();
            return loader.done(locationList);
        }

        @Override
        public Loader<Map<Integer, ArrayList<BoardMember>>> pastChargen(String personID) {
            Loader<Map<Integer, ArrayList<BoardMember>>> loader = new Loader<>();
            return loader.done();
        }

        @Override
        public Loader<File> file(@NonNull DocumentReference reference) {
            Loader<File> loader = new Loader<>();
            reference.get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (!documentSnapshot.exists()) {
                            loader.done(new NoDataException("File download failed"));
                            return;
                        }
                        File f = documentSnapshot.toObject(File.class);
                        loader.done(f);
                    })
                    .addOnFailureListener(loader::done);
            return loader;
        }

        @Override
        public Loader<ArrayList<Chore>> personUndoneChores(String personID) {
            Loader<ArrayList<Chore>> loader = new Loader<>();
            ArrayList<Chore> choreList = new ArrayList<>();
            return loader.done(choreList);
        }

        @Override
        public Loader<ArrayList<Chore>> personAllChores(String personID) {
            Loader<ArrayList<Chore>> loader = new Loader<>();
            ArrayList<Chore> choreList = new ArrayList<>();
            return loader.done(choreList);
        }

        @Override
        public Loader<ArrayList<Chore>> eventChores(String eventId) {
            Loader<ArrayList<Chore>> loader = new Loader<>();
            ArrayList<Chore> choreList = new ArrayList<>();
            return loader.done(choreList);
        }

        @Override
        public Loader<ArrayList<Text>> eventDescription(String eventId) {
            Loader<ArrayList<Text>> loader = new Loader<>();
            ArrayList<Text> textList = new ArrayList<>();
            return loader.done(textList);
        }

        @Override
        public Loader<Location> eventLocation(String eventId) {
            Loader<Location> loader = new Loader<>();
            return loader.done();
        }

        @Override
        public Loader<ArrayList<File>> protocols(String semesterID) {
            Loader<ArrayList<File>> loader = new Loader<>();
            ArrayList<File> fileList = new ArrayList<>();
            return loader.done(fileList);
        }

        @Override
        public Loader<ArrayList<Text>> semesterGreeting(String semesterID) {
            Loader<ArrayList<Text>> loader = new Loader<>();
            ArrayList<Text> textList = new ArrayList<>();
            return loader.done(textList);
        }

        @Override
        public Loader<ArrayList<News>> news(Visibility visibility) {
            Loader<ArrayList<News>> loader = new Loader<>();
            ArrayList<News> newsList = new ArrayList<>();
            return loader.done(newsList);
        }

        @Override
        public Loader<ArrayList<Text>> newsText(String newsId) {
            Loader<ArrayList<Text>> loader = new Loader<>();
            ArrayList<Text> textList = new ArrayList<>();
            return loader.done(textList);
        }

        @Override
        public Loader<ArrayList<Text>> semesterNotes(String semesterID) {
            Loader<ArrayList<Text>> loader = new Loader<>();
            ArrayList<Text> textList = new ArrayList<>();
            return loader.done(textList);
        }

        @Override
        public ListenerRegistration listenPersonBalance(String uid, Loader<Account> loader) {
            return null;
        }

        @Override
        public Loader<ArrayList<Movement>> getPersonMovements(String uid) {
            Loader<ArrayList<Movement>> loader = new Loader<>();
            ArrayList<Movement> movementList = new ArrayList<>();
            return loader.done(movementList);
        }

        @Override
        public ListenerRegistration semesterAccount(String semesterID, @NonNull Loader<Account> loader) {
            return null;
        }

        @Override
        public Loader<ArrayList<Movement>> getSemesterMovements(String semesterId) {
            Loader<ArrayList<Movement>> loader = new Loader<>();
            ArrayList<Movement> movementList = new ArrayList<>();
            return loader.done(movementList);
        }

        @Override
        public Loader<ArrayList<DrinkMovement>> getPersonDrinkMovement(String uid, String semester) {
            return null;
        }

        @Override
        public Loader<Event> nextEvent() {
            Loader<Event> loader = new Loader<>();
            return loader.done();
        }

        @Override
        public Loader<Person> person(String uid) {
            return null;
        }

        @Override
        public Loader<ArrayList<Address>> personAddress(String uid) {
            Loader<ArrayList<Address>> loader = new Loader<>();
            ArrayList<Address> addressList = new ArrayList<>();
            return loader.done(addressList);
        }

        @Override
        public Loader<File> personImage(String uid) {
            Loader<File> loader = new Loader<>();
            return loader.done();
        }
    }

    public class Upload implements IFirestoreUpload {
        public Upload() {
            upload = this;
        }

        @Override
        public Loader<Boolean> event(int semID, Event event) {
            Loader<Boolean> loader = new Loader<>();
            FBFS.collection("Semester")
                    .document(String.valueOf(semID))
                    .collection("Event")
                    .add(event)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference reference) {
                            loader.done(true);
                        }
                    })
                    .addOnFailureListener(loader::done);
            return loader;
        }

        @Override
        public Loader<Boolean> person(@NonNull Person person, ArrayList<Address> addressList) {
            Loader<Boolean> loader = new Loader<>();

            return loader.done();
        }
    }
}
