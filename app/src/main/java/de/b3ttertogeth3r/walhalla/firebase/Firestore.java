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
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import de.b3ttertogeth3r.walhalla.abstract_generic.Loader;
import de.b3ttertogeth3r.walhalla.annotation.SemesterRange;
import de.b3ttertogeth3r.walhalla.enums.Charge;
import de.b3ttertogeth3r.walhalla.enums.Rank;
import de.b3ttertogeth3r.walhalla.enums.Visibility;
import de.b3ttertogeth3r.walhalla.exception.NoDataException;
import de.b3ttertogeth3r.walhalla.exception.NotValidObjectException;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IFirestoreDownload;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IFirestoreUpload;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IInit;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.OnCompleteListener;
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
                ///*
                createSemesters();
                createChargen();
                createUsers();//*/
            }
            download = new Download();
            upload = new Upload();
            return true;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return false;
        }
    }

    //region emulator creation
    private void createSemesters() {
        for (Semester s : Values.semesterList) {
            FBFS.collection("Semester")
                    .document(String.valueOf(s.getId()))
                    .set(s);
        }
    }

    private void createChargen() {
        for (Semester s : Values.semesterList) {
            BoardMember bm = new BoardMember(
                    "Tobias Tumbrink",
                    "+49 176 64456884",
                    "aer-inf.",
                    "tobi.tumbrink@gmail.com",
                    "Oberursel (Taunus)",
                    Charge.ADMIN,
                    312,
                    null,
                    "cArR58nHmYs9wrDuMRet"
            );
            FBFS.collection("Semester")
                    .document(String.valueOf(s.getId()))
                    .collection("Student_Board")
                    .add(bm);
            FBFS.collection("Semester")
                    .document(String.valueOf(s.getId()))
                    .collection("Philistines_Board")
                    .add(bm);
        }
    }

    private void createUsers() {
        Person p1 = new Person("Tobias",
                "Tumbrink",
                "Oberursel (Taunus)",
                "aer-inf.",
                "Tobi",
                "+49 176 64456884",
                "tobi.tumbrink@gmail.com",
                304,
                new Timestamp(new Date()),
                Rank.ACTIVE);
        Person p2 = new Person("Johanna",
                "Dix",
                "Butzbach",
                "nn",
                "Krapotke",
                "+49 123 45678912",
                "tobias.tumbrink@kartellverband.de",
                316,
                new Timestamp(new Date()),
                Rank.NONE);
        Person p3 = new Person("Maximilian",
                "Jäckel",
                "Schondorf (am Ammersee)",
                "aer-inf.",
                "Minimax",
                "+49 219 87654321",
                "tobi.tumbrink@googlemail.com",
                306,
                new Timestamp(new Date()),
                Rank.PHILISTINES);

        FBFS.collection("Person").add(p1);
        FBFS.collection("Person").add(p2);
        FBFS.collection("Person").add(p3);
    }
    //endregion

    // TODO: 14.08.22 check, that the exceptions are thrown with the correct function description.
    public class Download implements IFirestoreDownload {

        public Download() {
            download = this;
        }

        @Override
        public Loader<ArrayList<File>> getSemesterProtocols(int semesterID) {
            Loader<ArrayList<File>> loader = new Loader<>();
            ArrayList<File> fileList = new ArrayList<>();
            FBFS.collection("Semester")
                    .document(String.valueOf(semesterID))
                    .collection("Greeting")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot, ArrayList<File>>(loader) {
                        @Override
                        public void done(QuerySnapshot qds) {
                            for (DocumentSnapshot ds : qds) {
                                try {
                                    fileList.add(ds.toObject(File.class));
                                } catch (Exception e) {
                                    Log.e(TAG, "getSemesterProtocols: onComplete: parsing object into Chore did not work.", e);
                                }
                            }
                            loader.done(fileList);
                        }
                    });
            return loader;
        }

        @Override
        public Loader<ArrayList<Text>> getSemesterGreeting(int semesterID) {
            Loader<ArrayList<Text>> loader = new Loader<>();
            ArrayList<Text> textList = new ArrayList<>();
            FBFS.collection("Semester")
                    .document(String.valueOf(semesterID))
                    .collection("Greeting")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot, ArrayList<Text>>(loader) {
                        @Override
                        public void done(QuerySnapshot qds) {
                            for (DocumentSnapshot ds : qds) {
                                try {
                                    textList.add(ds.toObject(Text.class));
                                } catch (Exception e) {
                                    Log.e(TAG, "getEventDescription: onComplete: parsing object into Chore did not work.", e);
                                }
                            }
                            loader.done(textList);
                        }
                    });
            return loader;
        }

        @Override
        public Loader<ArrayList<Text>> getSemesterNotes(int semesterID) {
            Loader<ArrayList<Text>> loader = new Loader<>();
            ArrayList<Text> textList = new ArrayList<>();
            FBFS.collection("Semester")
                    .document(String.valueOf(semesterID))
                    .collection("Notes")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot, ArrayList<Text>>(loader) {
                        @Override
                        public void done(QuerySnapshot qds) {
                            for (DocumentSnapshot ds : qds) {
                                try {
                                    textList.add(ds.toObject(Text.class));
                                } catch (Exception e) {
                                    Log.e(TAG, "getEventDescription: onComplete: parsing object into Chore did not work.", e);
                                }
                            }
                            loader.done(textList);
                        }
                    });
            return loader;
        }

        @Override
        public Loader<Account> getSemesterAccount(int semesterID) {
            Loader<Account> loader = new Loader<>();
            FBFS.collection("Semester")
                    .document(String.valueOf(semesterID))
                    .collection("Account")
                    .document("Account")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot, Account>(loader) {
                        @Override
                        public void done(DocumentSnapshot ds) {
                            loader.done(ds.toObject(Account.class));
                        }
                    });
            return loader;
        }

        @Override
        public Loader<ArrayList<Movement>> getSemesterMovements(int semesterID) {
            Loader<ArrayList<Movement>> loader = new Loader<>();
            ArrayList<Movement> movementList = new ArrayList<>();
            FBFS.collection("Semester")
                    .document(String.valueOf(semesterID))
                    .collection("Account")
                    .document("Account")
                    .collection("Movement")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot, ArrayList<Movement>>(loader) {
                        @Override
                        public void done(QuerySnapshot qds) {
                            for (DocumentSnapshot ds : qds) {
                                try {
                                    movementList.add(ds.toObject(Movement.class));
                                } catch (Exception e) {
                                    Log.e(TAG, "getEventDescription: onComplete: parsing object into Chore did not work.", e);
                                }
                            }
                            loader.done(movementList);
                        }
                    });
            return loader;
        }

        @Override
        public Loader<ArrayList<BoardMember>> getSemesterBoard(int semesterID, @NonNull Rank rank) {
            Loader<ArrayList<BoardMember>> loader = new Loader<>();
            ArrayList<BoardMember> memberList = new ArrayList<>();
            DocumentReference ref = FBFS.collection("Semester")
                    .document(String.valueOf(semesterID));
            CollectionReference colRef;
            switch (rank) {
                case ACTIVE:
                    colRef = ref.collection("Student_Board");
                    break;
                case PHILISTINES:
                    colRef = ref.collection("Philistines_Board");
                    break;
                default:
                    return loader.done();
            }
            colRef.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot, ArrayList<BoardMember>>(loader) {
                        @Override
                        public void done(QuerySnapshot qds) {
                            for (DocumentSnapshot ds : qds) {
                                try {
                                    memberList.add(ds.toObject(BoardMember.class));
                                } catch (Exception e) {
                                    Log.e(TAG, "getEventDescription: onComplete: parsing object into Chore did not work.", e);
                                }
                            }
                            loader.done(memberList);
                        }
                    });
            return loader;
        }

        @Override
        public Loader<BoardMember> getSemesterBoardOne(int semesterID, @NonNull Charge charge) {
            Loader<BoardMember> loader = new Loader<>();
            DocumentReference ref = FBFS.collection("Semester")
                    .document(String.valueOf(semesterID));
            CollectionReference colRef;
            switch (charge) {
                case VVOP:
                case VOP:
                case FM:
                case X:
                case VX:
                case XX:
                case XXX:
                    colRef = ref.collection("Student_Board");
                    break;
                case AH_XXX:
                case AH_XX:
                case AH_HW:
                case AH_X:
                case ADMIN:
                    colRef = ref.collection("Philistines_Board");
                    break;
                case NONE:
                default:
                    return loader.done();
            }
            colRef.whereEqualTo("charge", charge.toString().toUpperCase())
                    .limit(1)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot, BoardMember>(loader) {
                        @Override
                        public void done(QuerySnapshot qds) {
                            loader.done(qds.getDocuments().get(0).toObject(BoardMember.class));
                        }
                    });
            return loader;
        }

        @Override
        public Loader<ArrayList<Chore>> getEventChores(int semesterID, String eventId) {
            Loader<ArrayList<Chore>> loader = new Loader<>();
            ArrayList<Chore> choreList = new ArrayList<>();
            FBFS.collection("Semester")
                    .document(String.valueOf(semesterID))
                    .collection("Event")
                    .document(eventId)
                    .collection("Chore")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot, ArrayList<Chore>>(loader) {
                        @Override
                        public void done(QuerySnapshot qds) {
                            for (DocumentSnapshot ds : qds) {
                                try {
                                    choreList.add(ds.toObject(Chore.class));
                                } catch (Exception e) {
                                    Log.e(TAG, "getEventDescription: onComplete: parsing object into Chore did not work.", e);
                                }
                            }
                            loader.done(choreList);
                        }
                    });
            return loader;
        }

        @Override
        public Loader<ArrayList<Text>> getEventDescription(@SemesterRange int semesterID, String eventId) {
            Loader<ArrayList<Text>> loader = new Loader<>();
            ArrayList<Text> textList = new ArrayList<>();
            FBFS.collection("Semster")
                    .document(String.valueOf(semesterID))
                    .collection("Event")
                    .document(eventId)
                    .collection("Description")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot, ArrayList<Text>>(loader) {
                        @Override
                        public void done(QuerySnapshot queryDocumentSnapshots) {
                            for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                                try {
                                    textList.add(ds.toObject(Text.class));
                                } catch (Exception e) {
                                    Log.e(TAG, "getEventDescription: onComplete: parsing object into Text did not work.", e);
                                }
                            }
                            loader.done(textList);
                        }
                    });
            return loader;
        }

        @Override
        public Loader<Location> getEventLocation(@SemesterRange int semesterID, String eventId) {
            Loader<Location> loader = new Loader<>();
            FBFS.collection("Semester")
                    .document(String.valueOf(semesterID))
                    .collection("Event")
                    .document(eventId)
                    .collection("Location")
                    .limit(1)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.getException() != null) {
                            loader.done(task.getException());
                            return;
                        }
                        if (task.getResult().isEmpty()) {
                            Log.e(TAG, "getEventLocation: onComplete: ",
                                    new NoDataException("No location to Event " + eventId + " in semester " + semesterID + " found."));
                            loader.done(new Location());
                            return;
                        }
                        loader.done(task.getResult().getDocuments().get(0).toObject(Location.class));
                    });
            return loader;
        }

        @Override
        public Loader<Event> getNextEvent() {
            Loader<Event> loader = new Loader<>();
            FBFS.collection("Semester")
                    .document(Firebase.remoteConfig().getString(RemoteConfig.CURRENT_SEMESTER))
                    .collection("Event")
                    .whereGreaterThan("time", new Timestamp(Calendar.getInstance().getTime()))
                    .limit(1)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.getException() != null) {
                            loader.done(task.getException());
                            return;
                        }
                        if (task.getResult().isEmpty()) {
                            loader.done(new NoDataException("No Event found"));
                            return;
                        }
                        loader.done(task.getResult().getDocuments().get(0).toObject(Event.class));
                    });
            return loader;
        }

        @Override
        public Loader<Person> person(String uid) {
            Loader<Person> loader = new Loader<>();
            FBFS.collection("Person")
                    .document(uid)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (!documentSnapshot.exists()) {
                            loader.done(new NoDataException("Person does not exist"));
                            return;
                        }
                        try {
                            Person p = documentSnapshot.toObject(Person.class);
                            loader.done(p);
                        } catch (Exception e) {
                            loader.done(e);
                        }
                    }).addOnFailureListener(loader::done);
            return loader;
        }

        @Override
        public Loader<ArrayList<Chore>> getPersonChores(String uid, boolean showDoneChores) {
            Loader<ArrayList<Chore>> loader = new Loader<>();
            ArrayList<Chore> choreList = new ArrayList<>();
            CollectionReference ref = FBFS.collection("Person")
                    .document(uid)
                    .collection("Chore");
            Task<QuerySnapshot> result;
            if (showDoneChores) {
                result = ref.get();
            } else {
                result = ref.whereEqualTo("done", false).get();
            }
            result.addOnCompleteListener(new OnCompleteListener<QuerySnapshot, ArrayList<Chore>>(loader) {
                @Override
                public void done(QuerySnapshot qds) {
                    for (DocumentSnapshot ds : qds) {
                        try {
                            choreList.add(ds.toObject(Chore.class));
                        } catch (Exception e) {
                            Log.e(TAG, "getEventDescription: onComplete: parsing object into Chore did not work.", e);
                        }
                    }
                    loader.done(choreList);
                }
            });
            return loader.done(choreList);
        }

        @Override
        public Loader<Map<Integer, ArrayList<BoardMember>>> getPersonPastChargen(String uid) {
            Loader<Map<Integer, ArrayList<BoardMember>>> loader = new Loader<>();
            // TODO: 14.08.22 download them all and format them into this list. I do not know yet how...
            return loader.done();
        }

        @Override
        public Loader<ArrayList<DrinkMovement>> getPersonDrinkMovement(String uid, int semester) {
            Loader<ArrayList<DrinkMovement>> loader = new Loader<>();
            ArrayList<DrinkMovement> movementList = new ArrayList<>();
            FBFS.collection("Person")
                    .document(uid)
                    .collection("Drinks")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot, ArrayList<DrinkMovement>>(loader) {
                        @Override
                        public void done(QuerySnapshot qds) {
                            for (DocumentSnapshot ds : qds) {
                                try {
                                    movementList.add(ds.toObject(DrinkMovement.class));
                                } catch (Exception e) {
                                    Log.e(TAG, "getEventDescription: onComplete: parsing object into Chore did not work.", e);
                                }
                            }
                            loader.done(movementList);
                        }
                    });
            return loader;
        }

        @Override
        public Loader<Account> getPersonBalance(FragmentActivity activity, String uid) {
            Loader<Account> loader = new Loader<>();
            FBFS.document("Person/" + uid + "/Account")
                    .addSnapshotListener(activity, (value, error) -> {
                        if (error != null) {
                            loader.done(error);
                            return;
                        }
                        if (value == null || !value.exists()) {
                            loader.done(new NoDataException("Account doesn't exist"));
                            return;
                        }
                        try {
                            Account account = value.toObject(Account.class);
                            loader.done(account);
                        } catch (Exception e) {
                            loader.done(e);
                        }
                    });
            return loader;
        }

        @Override
        public Loader<ArrayList<Movement>> getPersonMovements(String uid) {
            Loader<ArrayList<Movement>> loader = new Loader<>();
            ArrayList<Movement> movementList = new ArrayList<>();
            FBFS.collection("Person")
                    .document(uid)
                    .collection("Account")
                    .document("Account")
                    .collection("Movement")
                    .get()
                    .addOnCompleteListener((task) -> {
                        if (task.getException() != null) {
                            loader.done(task.getException());
                            return;
                        } else if (task.getResult().isEmpty()) {
                            loader.done(new NoDataException("No movements to person-uid " + uid + " found."));
                            return;
                        }
                        for (DocumentSnapshot ds : task.getResult().getDocuments()) {
                            try {
                                movementList.add(ds.toObject(Movement.class));
                            } catch (Exception e) {
                                Log.e(TAG, "getNewsText: onComplete: could not parse object into Movement", e);
                            }
                        }
                        loader.done(movementList);
                    });
            return loader;
        }

        @Override
        public Loader<ArrayList<Address>> personAddress(String uid) {
            Loader<ArrayList<Address>> loader = new Loader<>();
            ArrayList<Address> addressList = new ArrayList<>();
            FBFS.collection("Person")
                    .document(uid)
                    .collection("Address")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.getException() != null) {
                            loader.done(task.getException());
                            return;
                        } else if (task.getResult().isEmpty()) {
                            loader.done(new NoDataException("No address to person-uid " + uid + " found."));
                            return;
                        }
                        for (DocumentSnapshot ds : task.getResult().getDocuments()) {
                            try {
                                addressList.add(ds.toObject(Address.class));
                            } catch (Exception e) {
                                Log.e(TAG, "getNewsText: onComplete: could not parse object into Address", e);
                            }
                        }
                        loader.done(addressList);
                    });
            return loader;
        }

        @Override
        public Loader<ArrayList<File>> getPersonImage(String uid) {
            Loader<ArrayList<File>> loader = new Loader<>();
            ArrayList<File> fileList = new ArrayList<>();
            FBFS.collection("Person")
                    .document(uid)
                    .collection("Image")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.getException() != null) {
                            loader.done(task.getException());
                            return;
                        } else if (task.getResult().isEmpty()) {
                            loader.done(new NoDataException("No image to person-uid " + uid + " found."));
                            return;
                        }
                        for (DocumentSnapshot ds : task.getResult().getDocuments()) {
                            try {
                                fileList.add(ds.toObject(File.class));
                            } catch (Exception e) {
                                Log.e(TAG, "getNewsText: onComplete: could not parse object into Text", e);
                            }
                        }
                        loader.done(fileList);
                    });
            return loader;
        }

        @Override
        public Loader<ArrayList<Text>> getNewsText(String newsID) {
            Loader<ArrayList<Text>> loader = new Loader<>();
            ArrayList<Text> textList = new ArrayList<>();
            FBFS.collection("News")
                    .document(newsID)
                    .collection("Text")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.getException() != null) {
                            loader.done(task.getException());
                            return;
                        } else if (task.getResult().isEmpty()) {
                            loader.done(new NoDataException("News entry " + newsID + " has no entries"));
                            return;
                        }
                        for (DocumentSnapshot ds : task.getResult().getDocuments()) {
                            try {
                                textList.add(ds.toObject(Text.class));
                            } catch (Exception e) {
                                Log.e(TAG, "getNewsText: onComplete: could not parse object into Text", e);
                            }
                        }
                        loader.done(textList);
                    });
            return loader;
        }

        @Override
        public Loader<ArrayList<News>> getNews(FragmentActivity activity, Visibility visibility) {
            Loader<ArrayList<News>> loader = new Loader<>();
            ArrayList<News> newsList = new ArrayList<>();
            FBFS.collection("News")
                    .addSnapshotListener(activity, (value, error) -> {
                        if (error != null) {
                            loader.done(error);
                            return;
                        }
                        if (value == null || value.isEmpty()) {
                            loader.done(new NoDataException("No news found"));
                            return;
                        }
                        for (DocumentSnapshot ds : value.getDocuments()) {
                            newsList.add(ds.toObject(News.class));
                        }
                        loader.done(newsList);
                    });
            return loader;
        }

        @Override
        public Loader<ArrayList<Event>> getSemesterEvents(int semesterID) {
            Loader<ArrayList<Event>> loader = new Loader<>();
            ArrayList<Event> eventList = new ArrayList<>();
            FBFS.collection("Semester")
                    .document(String.valueOf(semesterID))
                    .collection("Event")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.getException() != null) {
                            loader.done(task.getException());
                            return;
                        } else if (task.getResult().isEmpty()) {
                            loader.done(new NoDataException("Semester " + semesterID + " has no events"));
                            return;
                        }
                        for (DocumentSnapshot ds : task.getResult().getDocuments()) {
                            try {
                                eventList.add(ds.toObject(Event.class));
                            } catch (Exception e) {
                                Log.e(TAG, "getSemesterEvents: onComplete: could not parse object into event", e);
                            }
                        }
                        loader.done(eventList);
                    });
            return loader;
        }

        @Override
        public Loader<ArrayList<Person>> getPersonList(FragmentActivity activity) {
            Loader<ArrayList<Person>> loader = new Loader<>();
            try {
                FBFS.collection("Person")
                        .orderBy("joined", Query.Direction.DESCENDING)
                        .addSnapshotListener(activity, (values, error) -> {
                            if (error != null) {
                                loader.done(error);
                                return;
                            }
                            if (values == null || values.isEmpty()) {
                                loader.done(new NoDataException("No persons downloaded"));
                                return;
                            }
                            ArrayList<Person> personList = new ArrayList<>();
                            for (DocumentSnapshot ds : values.getDocuments()) {
                                try {
                                    Person p = ds.toObject(Person.class);
                                    personList.add(p);
                                } catch (Exception e) {
                                    Log.e(TAG, "getPersonList: ", e);
                                }
                            }
                            loader.done(personList);
                        });
                return loader;
            } catch (Exception e) {
                return loader.done(e);
            }
        }

        @Override
        public Loader<ArrayList<Location>> locationList() {
            Loader<ArrayList<Location>> loader = new Loader<>();
            ArrayList<Location> locationList = new ArrayList<>();
            FBFS.collection("Location")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.getException() != null) {
                            loader.done(task.getException());
                            return;
                        } else if (task.getResult() == null) {
                            loader.done(new NoDataException("No entries for location list found"));
                            return;
                        }
                        for (DocumentSnapshot ds : task.getResult().getDocuments()) {
                            try {
                                locationList.add(ds.toObject(Location.class));
                            } catch (Exception e) {
                                Log.e(TAG, "locationList: onComplete: couldn't parse object into location", e);
                            }
                        }
                        loader.done(locationList);
                    });
            return loader;
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
    }

    public class Upload implements IFirestoreUpload {
        public Upload() {
            upload = this;
        }

        @Override
        public Loader<Boolean> setBoard(@SemesterRange int semID, @NonNull BoardMember boardMember) {
            Loader<Boolean> loader = new Loader<>();
            CollectionReference colRef;
            switch (boardMember.getCharge()) {
                case X:
                case VX:
                case FM:
                case XX:
                case XXX:
                case VOP:
                case VVOP:
                    // upload into "Student_Board"
                    colRef = FBFS.collection("Semester")
                            .document(String.valueOf(semID))
                            .collection("Student_Board");
                    uploadToBoard(boardMember, colRef, loader);
                    break;
                case AH_X:
                case AH_HW:
                case AH_XX:
                case AH_XXX:
                    // upload into "Philistines_Board"
                    colRef = FBFS.collection("Semester")
                            .document(String.valueOf(semID))
                            .collection("Philistines_Board");
                    uploadToBoard(boardMember, colRef, loader);
                    break;
                case ADMIN:
                    // upload into both
                    colRef = FBFS.collection("Semester")
                            .document(String.valueOf(semID))
                            .collection("Philistines_Board");
                    uploadToBoard(boardMember, colRef, null);
                    colRef = FBFS.collection("Semester")
                            .document(String.valueOf(semID))
                            .collection("Student_Board");
                    uploadToBoard(boardMember, colRef, loader);
                    break;
                default:
                case NONE:
                    loader.done();
                    return loader;
            }
            return loader;
        }

        private void uploadToBoard(@NonNull BoardMember boardMember, CollectionReference ref, Loader<Boolean> loader) {
            try {
                if (boardMember.getId().isEmpty()) {
                    // create board member
                    ref.add(boardMember)
                            .addOnCompleteListener(task -> {
                                if (task.getException() != null) {
                                    if (loader != null)
                                        loader.done(new NoDataException("Upload of new location unsuccessful", task.getException()));
                                    return;
                                }
                                if (task.getResult() == null) {
                                    if (loader != null)
                                        loader.done(new NoDataException("Upload of new location unsuccessful"));
                                    return;
                                }
                                loader.done(true);
                            });
                } else {
                    // update board member
                    ref.document(boardMember.getId())
                            .set(boardMember)
                            .addOnCompleteListener(task -> {
                                if (task.getException() != null) {
                                    if (loader != null)
                                        loader.done(new NoDataException("Upload of new location unsuccessful", task.getException()));
                                    return;
                                }
                                if (loader != null)
                                    loader.done(true);
                            });
                }
            } catch (Exception e) {
                loader.done(false);
            }
        }

        @Override
        public Loader<Boolean> setEvent(@SemesterRange int semID, @NonNull Event event) {
            Loader<Boolean> loader = new Loader<>();
            if (event.validate()) {
                FBFS.collection("Semester")
                        .document(String.valueOf(semID))
                        .collection("Event")
                        .add(event)
                        .addOnCompleteListener(task -> {
                            if (task.getException() != null) {
                                loader.done(new NoDataException("Upload of new location unsuccessful", task.getException()));
                                return;
                            }
                            if (task.getResult() == null) {
                                loader.done(new NoDataException("Upload of new location unsuccessful"));
                                return;
                            }
                            loader.done(true);
                        });
            } else {
                return loader.done(new InvalidParameterException("Not all necessary fields are set"));
            }
            return loader;
        }

        @Override
        public Loader<Boolean> setEventChore(@SemesterRange int semID, @NonNull String eventID, @NonNull Chore chore) {
            Loader<Boolean> loader = new Loader<>();
            CollectionReference ref = FBFS.collection("Semester")
                    .document(String.valueOf(semID))
                    .collection("Event")
                    .document(eventID)
                    .collection("Chore");
            if (chore.getId().isEmpty()) {
                // set new chore
                ref.add(chore)
                        .addOnCompleteListener(task -> {
                            if (task.getException() != null) {
                                loader.done(new NoDataException("Upload of new location unsuccessful", task.getException()));
                                return;
                            }
                            if (task.getResult() == null) {
                                loader.done(new NoDataException("Upload of new location unsuccessful"));
                                return;
                            }
                            loader.done(true);
                        });
            } else {
                // update chore
                ref.document(chore.getId())
                        .set(chore)
                        .addOnCompleteListener(task -> {
                            if (task.getException() != null) {
                                loader.done(new NoDataException("Upload of new location unsuccessful", task.getException()));
                                return;
                            }
                            loader.done(true);
                        });
            }
            return loader;
        }

        @Override
        public Loader<Boolean> setEventLocation(@SemesterRange int semID, @NonNull String eventID, @NonNull Location location) {
            Loader<Boolean> loader = new Loader<>();
            CollectionReference ref = FBFS.collection("Semester")
                    .document(String.valueOf(semID))
                    .collection("Event")
                    .document(eventID)
                    .collection("Location");
            if (!location.getId().isEmpty()) {
                // update location
                ref.document(location.getId())
                        .set(loader)
                        .addOnCompleteListener(task -> {
                            if (task.getException() != null) {
                                loader.done(new NoDataException("Upload of new location unsuccessful", task.getException()));
                                return;
                            }
                            loader.done(true);
                        });
            } else {
                // set new location
                ref.add(loader)
                        .addOnCompleteListener(task -> {
                            if (task.getException() != null) {
                                loader.done(new NoDataException("Upload of new location unsuccessful", task.getException()));
                                return;
                            }
                            if (task.getResult() == null) {
                                loader.done(new NoDataException("Upload of new location unsuccessful"));
                                return;
                            }
                            loader.done(true);
                        });
            }
            return loader;
        }

        @Override
        public Loader<Boolean> setEventDescription(@SemesterRange int semID, @NonNull String eventID, @NonNull ArrayList<Text> description) {
            Loader<Boolean> loader = new Loader<>();
            return loader.done();
        }

        @Override
        public Loader<Boolean> addSemesterMovement(@SemesterRange int semID, @NonNull Movement movement) {
            Loader<Boolean> loader = new Loader<>();
            CollectionReference ref = FBFS.collection("Semester")
                    .document(String.valueOf(semID))
                    .collection("Account")
                    .document("Account")
                    .collection("Movement");
            if (movement.getId().isEmpty()) {
                // set a new movement
                ref.add(movement)
                        .addOnSuccessListener(reference -> {
                            if (reference == null) {
                                loader.done(new NoDataException("New movement has no reference path"));
                                return;
                            }
                            loader.done(true);
                        }).addOnFailureListener(loader::done);
            } else {
                ref.add(movement)
                        .addOnSuccessListener(reference -> {
                            if (reference == null) {
                                loader.done(new NoDataException("New movement has no reference path"));
                                return;
                            }
                            loader.done(true);
                        }).addOnFailureListener(loader::done);
            }
            return loader;
        }

        @Override
        public Loader<Boolean> addSemesterMeetingTranscript(@SemesterRange int semID, @NonNull File file) {
            Loader<Boolean> loader = new Loader<>();
            CollectionReference reference = FBFS.collection("Semester")
                    .document(String.valueOf(semID))
                    .collection("Transcript");
            if (!file.getId().isEmpty()) {
                reference.document(file.getId())
                        .set(file)
                        .addOnSuccessListener(unused -> loader.done(true))
                        .addOnFailureListener(loader::done);
            } else {
                reference.add(file)
                        .addOnSuccessListener(reference1 -> {
                            if (reference1 == null) {
                                loader.done(false);
                                return;
                            }
                            loader.done(true);
                        }).addOnFailureListener(loader::done);
            }
            return loader;
        }

        @Override
        public Loader<Boolean> addNewsEntry(@Nullable News news, @NonNull ArrayList<Text> text) {
            Loader<Boolean> loader = new Loader<>();
            CollectionReference ref = FBFS.collection("News");
            if (news == null) {
                // upload new entry
                ref.add(new News()).addOnSuccessListener(reference -> {
                    CollectionReference textPath = reference.collection("Text");
                    for (Text t : text) {
                        textPath.add(t);
                    }
                    loader.done(true);
                });
            } else {
                // update news
                for (Text t : text) {
                    if (!t.getId().isEmpty()) {
                        ref.document(news.getId())
                                .collection("Text")
                                .document(t.getId())
                                .set(t);
                    }
                }
                loader.done(true);
            }
            return loader;
        }

        @Override
        public Loader<Boolean> setPerson(@NonNull Person person) {
            Loader<Boolean> loader = new Loader<>();
            CollectionReference ref = FBFS.collection("Person");
            if (!person.validate()) {
                return loader.done(new NotValidObjectException("Person not valid"));
            }
            if (person.getId().isEmpty()) {
                // Set a new person
                ref.add(person)
                        .addOnFailureListener(loader::done)
                        .addOnSuccessListener(reference -> {
                            if (reference != null) {
                                loader.done(true);
                                // set default account counter
                                reference.collection("Account")
                                        .add(new Account());
                                // TODO: 13.08.22 check, if this is the only object needing to create
                            } else {
                                loader.done(false);
                            }
                        });
            } else {
                // Update a person
                ref.document(person.getId())
                        .set(person)
                        .addOnSuccessListener(unused -> loader.done(true))
                        .addOnFailureListener(loader::done);
            }
            return loader;
        }

        @Override
        public Loader<Boolean> setPersonAddress(@NonNull String personID, @NonNull ArrayList<Address> addressList) {
            Loader<Boolean> loader = new Loader<>();
            return loader.done();
        }

        @Override
        public Loader<Boolean> setPersonChore(@NonNull String personID, @NonNull Chore chore) {
            Loader<Boolean> loader = new Loader<>();
            return loader.done();
        }

        @Override
        public Loader<Boolean> setPersonCharge(@NonNull String personID, @SemesterRange int semester, @NonNull BoardMember boardMember) {
            Loader<Boolean> loader = new Loader<>();
            return loader.done();
        }

        @Override
        public Loader<Boolean> addPersonMovement(@NonNull String personID, @NonNull Movement movement) {
            Loader<Boolean> loader = new Loader<>();
            return loader.done();
        }

        @Override
        public Loader<Boolean> addPersonPicture(@NonNull String personID, @NonNull File file) {
            Loader<Boolean> loader = new Loader<>();
            return loader.done();
        }
    }
}
