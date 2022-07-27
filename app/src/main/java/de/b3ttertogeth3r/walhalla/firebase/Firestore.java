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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.ArrayList;
import java.util.Map;

import de.b3ttertogeth3r.walhalla.abstract_generic.Loader;
import de.b3ttertogeth3r.walhalla.enums.Charge;
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
import de.b3ttertogeth3r.walhalla.util.Dev.SemesterRange;
import de.b3ttertogeth3r.walhalla.util.Paragraph;
import de.b3ttertogeth3r.walhalla.util.Values;

public class Firestore implements IInit {
    private static final String TAG = "Firestore";
    protected static IFirestoreDownload download;
    protected static IFirestoreUpload upload;
    private FirebaseFirestore FBFS;

    public Firestore() {
    }

    public boolean init(Context context, boolean isEmulator) {
        try {
            FBFS = FirebaseFirestore.getInstance();
            if (isEmulator) {
                FBFS.useEmulator("10.0.2.2", 8080);

                FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                        .setPersistenceEnabled(false)
                        .build();
                FBFS.setFirestoreSettings(settings);
            }
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
        for (Semester s : Values.semesterList) {
            FBFS.collection("Semester")
                    .document(s.getId())
                    .set(s);

        }
    }

    public class Download implements IFirestoreDownload {

        public Download() {
            download = this;
        }

        @Override
        public Loader<ArrayList<File>> getSemesterProtocols(String semesterID) {
            Loader<ArrayList<File>> loader = new Loader<>();
            ArrayList<File> fileList = new ArrayList<>();
            return loader.done(fileList);
        }

        @Override
        public Loader<Paragraph<Text>> getSemesterGreeting(String semesterID) {
            Loader<Paragraph<Text>> loader = new Loader<>();
            Paragraph<Text> textList = new Paragraph<>();
            return loader.done(textList);
        }

        @Override
        public Loader<BoardMember> getSemesterBoardOne(int semesterId, @NonNull Charge charge) {
            Loader<BoardMember> loader = new Loader<>();
            return loader.done();
        }

        @Override
        public Loader<ArrayList<Location>> locationList() {
            Loader<ArrayList<Location>> loader = new Loader<>();
            ArrayList<Location> locationList = new ArrayList<>();
            return loader.done(locationList);
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
        public Loader<ArrayList<Text>> getSemesterNotes(int semesterID) {
            Loader<ArrayList<Text>> loader = new Loader<>();
            ArrayList<Text> textList = new ArrayList<>();
            return loader.done(textList);
        }

        @Override
        public Loader<Account> getSemesterAccount(int semesterID) {
            Loader<Account> loader = new Loader<>();
            return loader.done();
        }

        @Override
        public Loader<ArrayList<Movement>> getSemesterMovements(int semesterId) {
            Loader<ArrayList<Movement>> loader = new Loader<>();
            ArrayList<Movement> movementList = new ArrayList<>();
            return loader.done(movementList);
        }

        @Override
        public Loader<ArrayList<BoardMember>> getSemesterBoard(int semesterID, Rank rank) {
            Loader<ArrayList<BoardMember>> loader = new Loader<>();
            ArrayList<BoardMember> memberList = new ArrayList<>();
            return loader.done(memberList);
        }

        @Override
        public Loader<ArrayList<Chore>> getEventChores(String eventId) {
            Loader<ArrayList<Chore>> loader = new Loader<>();
            ArrayList<Chore> choreList = new ArrayList<>();
            return loader.done(choreList);
        }

        @Override
        public Loader<ArrayList<Text>> getEventDescription(String eventId) {
            Loader<ArrayList<Text>> loader = new Loader<>();
            ArrayList<Text> textList = new ArrayList<>();
            return loader.done(textList);
        }

        @Override
        public Loader<Location> getEventLocation(String eventId) {
            Loader<Location> loader = new Loader<>();
            return loader.done();
        }

        @Override
        public Loader<Event> getNextEvent() {
            Loader<Event> loader = new Loader<>();
            return loader.done();
        }

        @Override
        public Loader<ArrayList<Chore>> getPersonChores(String uid, boolean showDoneChores) {
            Loader<ArrayList<Chore>> loader = new Loader<>();
            ArrayList<Chore> choreList = new ArrayList<>();
            return loader.done(choreList);
        }

        @Override
        public Loader<Map<Integer, ArrayList<BoardMember>>> getPersonPastChargen(String personID) {
            Loader<Map<Integer, ArrayList<BoardMember>>> loader = new Loader<>();
            return loader.done();
        }

        @Override
        public Loader<Account> getPersonBalance(String uid) {
            Loader<Account> loader = new Loader<>();
            return loader.done();
        }

        @Override
        public Loader<File> getPersonImage(String uid) {
            Loader<File> loader = new Loader<>();
            return loader.done();
        }

        @Override
        public Loader<Person> person(String uid) {
            return null;
        }

        @Override
        public Loader<ArrayList<Text>> getNewsText(String newsID) {
            Loader<ArrayList<Text>> loader = new Loader<>();
            ArrayList<Text> textList = new ArrayList<>();
            return loader.done(textList);
        }

        @Override
        public Loader<ArrayList<News>> getNews(Visibility visibility) {
            Loader<ArrayList<News>> loader = new Loader<>();
            ArrayList<News> newsList = new ArrayList<>();
            return loader.done(newsList);
        }

        @Override
        public Loader<ArrayList<DrinkMovement>> getPersonDrinkMovement(String uid, int semester) {
            Loader<ArrayList<DrinkMovement>> loader = new Loader<>();
            ArrayList<DrinkMovement> movementList = new ArrayList<>();
            return loader.done(movementList);
        }

        @Override
        public Loader<ArrayList<Event>> getSemesterEvents(int semesterID) {
            Loader<ArrayList<Event>> loader = new Loader<>();
            ArrayList<Event> eventList = new ArrayList<>();
            return loader.done(eventList);
        }

        @Override
        public Loader<ArrayList<Movement>> getPersonMovements(String uid) {
            Loader<ArrayList<Movement>> loader = new Loader<>();
            ArrayList<Movement> movementList = new ArrayList<>();
            return loader.done(movementList);
        }

        @Override
        public Loader<ArrayList<Address>> personAddress(String uid) {
            Loader<ArrayList<Address>> loader = new Loader<>();
            ArrayList<Address> addressList = new ArrayList<>();
            return loader.done(addressList);
        }

        @Override
        public Loader<ArrayList<Person>> getPersonList() {
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
    }

    public class Upload implements IFirestoreUpload {
        public Upload() {
            upload = this;
        }

        @Override
        public Loader<Boolean> setBoard(@SemesterRange int semID, @NonNull ArrayList<BoardMember> boardMembers) {
            return null;
        }

        @Override
        public Loader<Boolean> setEvent(@SemesterRange int semID, @NonNull Event event) {
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
        public Loader<Boolean> setEventChore(@NonNull String eventID, @NonNull Chore chore) {
            return null;
        }

        @Override
        public Loader<Boolean> setEventLocation(@NonNull String eventID, @NonNull Location location) {
            return null;
        }

        @Override
        public Loader<Boolean> setEventDescription(@NonNull String eventID, @NonNull ArrayList<Paragraph<Text>> description) {
            return null;
        }

        @Override
        public Loader<Boolean> addSemesterMovement(@SemesterRange int semID, @NonNull Movement movement) {
            return null;
        }

        @Override
        public Loader<Boolean> addSemesterProtocol(@SemesterRange int semID, @NonNull File file) {
            return null;
        }

        @Override
        public Loader<Boolean> addNewsEntry(@NonNull News news, @NonNull ArrayList<Text> text) {
            return null;
        }

        @Override
        public Loader<Boolean> setPerson(@NonNull Person person) {
            return null;
        }

        @Override
        public Loader<Boolean> setPersonAddress(@NonNull String personID, @NonNull ArrayList<Address> addressList) {
            return null;
        }

        @Override
        public Loader<Boolean> setPersonChore(@NonNull String personID, @NonNull Chore chore) {
            return null;
        }

        @Override
        public Loader<Boolean> setPersonCharge(@NonNull String personID, @SemesterRange int semester, @NonNull BoardMember boardMember) {
            return null;
        }

        @Override
        public Loader<Boolean> addPersonMovement(@NonNull String personID, @NonNull Movement movement) {
            return null;
        }

        @Override
        public Loader<Boolean> addPersonPicture(@NonNull String personID, @NonNull File file) {
            return null;
        }
    }
}
