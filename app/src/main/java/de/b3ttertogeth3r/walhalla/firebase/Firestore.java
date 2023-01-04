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

package de.b3ttertogeth3r.walhalla.firebase;

import static de.b3ttertogeth3r.walhalla.firebase.Firestore.CollectionName.ACCOUNT;
import static de.b3ttertogeth3r.walhalla.firebase.Firestore.CollectionName.ADDRESS;
import static de.b3ttertogeth3r.walhalla.firebase.Firestore.CollectionName.BOARD;
import static de.b3ttertogeth3r.walhalla.firebase.Firestore.CollectionName.CHORE;
import static de.b3ttertogeth3r.walhalla.firebase.Firestore.CollectionName.DESCRIPTION;
import static de.b3ttertogeth3r.walhalla.firebase.Firestore.CollectionName.DRINK;
import static de.b3ttertogeth3r.walhalla.firebase.Firestore.CollectionName.DRINK_MOVEMENT;
import static de.b3ttertogeth3r.walhalla.firebase.Firestore.CollectionName.EVENT;
import static de.b3ttertogeth3r.walhalla.firebase.Firestore.CollectionName.GREETING;
import static de.b3ttertogeth3r.walhalla.firebase.Firestore.CollectionName.IMAGE;
import static de.b3ttertogeth3r.walhalla.firebase.Firestore.CollectionName.LOCATION;
import static de.b3ttertogeth3r.walhalla.firebase.Firestore.CollectionName.MOVEMENT;
import static de.b3ttertogeth3r.walhalla.firebase.Firestore.CollectionName.NEWS;
import static de.b3ttertogeth3r.walhalla.firebase.Firestore.CollectionName.NOTES;
import static de.b3ttertogeth3r.walhalla.firebase.Firestore.CollectionName.PERSON;
import static de.b3ttertogeth3r.walhalla.firebase.Firestore.CollectionName.PERSON_LIST_TOTAL;
import static de.b3ttertogeth3r.walhalla.firebase.Firestore.CollectionName.PHILISTINES_BOARD;
import static de.b3ttertogeth3r.walhalla.firebase.Firestore.CollectionName.SEMESTER;
import static de.b3ttertogeth3r.walhalla.firebase.Firestore.CollectionName.STUDENT_BOARD;
import static de.b3ttertogeth3r.walhalla.firebase.Firestore.CollectionName.TEXT;
import static de.b3ttertogeth3r.walhalla.firebase.Firestore.CollectionName.TRANSCRIPT;
import static de.b3ttertogeth3r.walhalla.interfaces.RealtimeListeners.listener;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

import de.b3ttertogeth3r.walhalla.abstract_generic.Loader;
import de.b3ttertogeth3r.walhalla.annotation.SemesterRange;
import de.b3ttertogeth3r.walhalla.enums.Charge;
import de.b3ttertogeth3r.walhalla.enums.Rank;
import de.b3ttertogeth3r.walhalla.enums.Visibility;
import de.b3ttertogeth3r.walhalla.exception.NoDataException;
import de.b3ttertogeth3r.walhalla.exception.NotValidObjectException;
import de.b3ttertogeth3r.walhalla.exception.UserDataError;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IFirestoreDownload;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IFirestoreUpload;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.IInit;
import de.b3ttertogeth3r.walhalla.interfaces.firebase.OnCompleteListener;
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
import de.b3ttertogeth3r.walhalla.object.Text;
import de.b3ttertogeth3r.walhalla.util.Log;
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
            download = new Download();
            upload = new Upload();

            if (isEmulator) {
                FBFS.useEmulator("10.0.2.2", 8080);

                FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                        .setPersistenceEnabled(false)
                        .build();
                FBFS.setFirestoreSettings(settings);
            }
            return true;
        } catch (Exception e) {
            Log.e(TAG, "init: ", e);
            return false;
        }
    }

    /**
     * An enum to extinct typos in collection reference paths.
     */
    protected enum CollectionName {

        SEMESTER("Semester"),
        STUDENT_BOARD("Student_Board"),
        PHILISTINES_BOARD("Philistines_Board"),
        EVENT("Event"),
        CHORE("Chore"),
        DESCRIPTION("Description"),
        ACCOUNT("Account"),
        MOVEMENT("Movement"),
        TRANSCRIPT("Transcript"),
        NOTES("Notes"),
        GREETING("Greeting"),
        PUBLISHED("Published"),
        DRINK("Drink"),
        PERSON("Person"),
        IMAGE("Image"),
        BOARD("Board"),
        LOCATION("Location"),
        NEWS("News"),
        TEXT("Text"),
        FCM_DATA("FCM_Data"),
        PERSON_LIST_TOTAL("PersonListTotal"),
        ADDRESS("address"),
        DRINK_MOVEMENT("DrinkMovement");

        private final String description;

        CollectionName(String description) {
            this.description = description;
        }

        public String get() {
            return description;
        }
    }

    private abstract class References {
        @NonNull
        protected CollectionReference studentPath(int semesterID) {
            return getSemesterReference(semesterID).collection(STUDENT_BOARD.get());
        }

        @NonNull
        protected DocumentReference getSemesterReference(int semesterID) {
            return FBFS.collection(SEMESTER.get())
                    .document(String.valueOf(semesterID));
        }

        @NonNull
        protected CollectionReference philPath(int semesterID) {
            return getSemesterReference(semesterID).collection(PHILISTINES_BOARD.get());
        }

        @NonNull
        protected DocumentReference getEventReference(int semesterID, String eventID) {
            return eventPath(semesterID).document(eventID);
        }

        @NonNull
        protected CollectionReference eventPath(int semID) {
            return getSemesterReference(semID)
                    .collection(EVENT.get());
        }

        @NonNull
        protected DocumentReference getPersonReference(String uid) {
            return FBFS.collection(PERSON.get())
                    .document(uid);

        }

        @NonNull
        CollectionReference getMovementSubRef(@NonNull DocumentReference ref) {
            return ref.collection(ACCOUNT.get())
                    .document(ACCOUNT.get())
                    .collection(MOVEMENT.get());
        }
    }

    /**
     * Actual download the data from the Firebase firestore database
     *
     * @author B3tterTogeth3r
     * @version 2.1
     * @see de.b3ttertogeth3r.walhalla.interfaces.firebase.IFirestoreDownload
     * @since 2.9
     */
    @SuppressWarnings("ConstantConditions")
    public class Download extends References implements IFirestoreDownload {
        private static final String TAG = "Firestore.Download";

        public Download() {
            download = this;
        }

        @Override
        public Loader<ArrayList<File>> getSemesterProtocols(int semesterID) {
            Loader<ArrayList<File>> loader = new Loader<>();
            getSemesterReference(semesterID)
                    .collection(TRANSCRIPT.get())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot, ArrayList<File>>(loader) {
                        @Override
                        public void done(QuerySnapshot qds) {
                            ArrayList<File> fileList = new ArrayList<>();
                            for (DocumentSnapshot ds : qds) {
                                try {
                                    File f = ds.toObject(File.class);
                                    f.setId(ds.getId());
                                    fileList.add(f);
                                } catch (Exception e) {
                                    Log.e(TAG, "getSemesterProtocols: done: parsing object into Chore did not work.", e);
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
            getSemesterReference(semesterID)
                    .collection(GREETING.get())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot, ArrayList<Text>>(loader) {
                        @Override
                        public void done(QuerySnapshot qds) {
                            ArrayList<Text> textList = new ArrayList<>();
                            for (DocumentSnapshot ds : qds) {
                                try {
                                    Text t = ds.toObject(Text.class);
                                    t.setId(ds.getId());
                                    textList.add(t);
                                } catch (Exception e) {
                                    Log.e(TAG, "getSemesterGreeting: done: parsing object into Chore did not work.", e);
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
            getSemesterReference(semesterID)
                    .collection(NOTES.get())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot, ArrayList<Text>>(loader) {
                        @Override
                        public void done(QuerySnapshot qds) {
                            ArrayList<Text> textList = new ArrayList<>();
                            for (DocumentSnapshot ds : qds) {
                                try {
                                    Text t = ds.toObject(Text.class);
                                    t.setId(ds.getId());
                                    textList.add(t);
                                } catch (Exception e) {
                                    Log.e(TAG, "getSemesterNotes: done: parsing object into Chore did not work.", e);
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
            getSemesterReference(semesterID)
                    .collection(ACCOUNT.get())
                    .document(ACCOUNT.get())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot, Account>(loader) {
                        @Override
                        public void done(DocumentSnapshot ds) {
                            try {
                                loader.done(ds.toObject(Account.class));
                            } catch (Exception e) {
                                Log.e(TAG, "getSemesterAccount: done: retrieving account data didn't work.", e);
                                loader.done(e);
                            }
                        }
                    });
            return loader;
        }

        @Override
        public Loader<ArrayList<Movement>> getSemesterMovements(int semesterID) {
            Loader<ArrayList<Movement>> loader = new Loader<>();
            getMovementSubRef(getSemesterReference(semesterID))
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot, ArrayList<Movement>>(loader) {
                        @Override
                        public void done(QuerySnapshot qds) {
                            ArrayList<Movement> movementList = new ArrayList<>();
                            for (DocumentSnapshot ds : qds) {
                                try {
                                    Movement m = ds.toObject(Movement.class);
                                    m.setId(ds.getId());
                                    movementList.add(m);
                                } catch (Exception e) {
                                    Log.e(TAG, "getSemesterMovements: done: parsing object into Chore did not work.", e);
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
            CollectionReference colRef;
            switch (rank) {
                case ACTIVE:
                    colRef = studentPath(semesterID);
                    break;
                case PHILISTINES:
                    colRef = philPath(semesterID);
                    break;
                default:
                    return loader.done();
            }
            colRef.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot, ArrayList<BoardMember>>(loader) {
                        @Override
                        public void done(QuerySnapshot qds) {
                            ArrayList<BoardMember> memberList = new ArrayList<>();
                            for (DocumentSnapshot ds : qds) {
                                try {
                                    BoardMember bm = ds.toObject(BoardMember.class);
                                    if (bm != null && !(bm.getCharge() == Charge.ADMIN |
                                            bm.getCharge() == Charge.NONE |
                                            bm.getCharge() == Charge.VOP |
                                            bm.getCharge() == Charge.VVOP)) {
                                        bm.setId(ds.getId());
                                        memberList.add(bm);
                                    }
                                } catch (Exception e) {
                                    Log.e(TAG, "getSemesterBoard: done: parsing object into Chore did not work.", e);
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
            CollectionReference colRef;
            switch (charge) {
                case VVOP:
                case VOP:
                case FM:
                case X:
                case VX:
                case XX:
                case XXX:
                    colRef = studentPath(semesterID);
                    break;
                case AH_XXX:
                case AH_XX:
                case AH_HW:
                case AH_X:
                case ADMIN:
                    colRef = philPath(semesterID);
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
                            if (qds.isEmpty()) {
                                loader.done(new NoDataException("No " + charge + " board member in semester" + semesterID + " found."));
                                return;
                            }
                            try {
                                BoardMember bm = qds.getDocuments().get(0).toObject(BoardMember.class);
                                bm.setId(qds.getDocuments().get(0).getId());
                                loader.done(bm);
                            } catch (Exception e) {
                                Log.e(TAG, "getBoardMemberOne: done: Loading one Board member didn't work", e);
                                loader.done(e);
                            }
                        }
                    });
            return loader;
        }

        @Override
        public Loader<ArrayList<Event>> getSemesterEvents(int semesterID) {
            Loader<ArrayList<Event>> loader = new Loader<>();
            eventPath(semesterID)
                    .orderBy("time", Query.Direction.ASCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot, ArrayList<Event>>(loader) {
                        @Override
                        public void done(QuerySnapshot qds) {
                            ArrayList<Event> eventList = new ArrayList<>();
                            for (DocumentSnapshot ds : qds.getDocuments()) {
                                try {
                                    Event e = ds.toObject(Event.class);
                                    e.setId(ds.getId());
                                    eventList.add(e);
                                } catch (Exception e) {
                                    Log.e(TAG, "getSemesterEvents: onComplete: could not parse object into event", e);
                                }
                            }
                            loader.done(eventList);
                        }
                    });
            return loader;
        }

        @Override
        public Loader<ArrayList<Chore>> getEventChores(int semesterID, String eventId) {
            Loader<ArrayList<Chore>> loader = new Loader<>();
            getEventReference(semesterID, eventId)
                    .collection(CHORE.get())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot, ArrayList<Chore>>(loader) {
                        @Override
                        public void done(QuerySnapshot qds) {
                            ArrayList<Chore> choreList = new ArrayList<>();
                            for (DocumentSnapshot ds : qds) {
                                try {
                                    Chore c = ds.toObject(Chore.class);
                                    c.setId(ds.getId());
                                    choreList.add(c);
                                } catch (Exception e) {
                                    Log.e(TAG, "getEventChores: done: parsing object into Chore did not work.", e);
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
            getEventReference(semesterID, eventId)
                    .collection(DESCRIPTION.get())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot, ArrayList<Text>>(loader) {
                        @Override
                        public void done(QuerySnapshot qds) {
                            ArrayList<Text> textList = new ArrayList<>();
                            for (DocumentSnapshot ds : qds.getDocuments()) {
                                try {
                                    Text t = ds.toObject(Text.class);
                                    t.setId(ds.getId());
                                    textList.add(t);
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
            getEventReference(semesterID, eventId)
                    .collection(LOCATION.get())
                    .limit(1)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot, Location>(loader) {
                        @Override
                        public void done(QuerySnapshot queryDocumentSnapshots) {
                            try {
                                Location l = queryDocumentSnapshots.getDocuments().get(0).toObject(Location.class);
                                l.setId(queryDocumentSnapshots.getDocuments().get(0).getId());
                                loader.done(l);
                            } catch (Exception e) {
                                Log.e(TAG, "getEventLocation: done: Loading location of event " +
                                        eventId + " in semester " + semesterID + " didn't work", e);
                                loader.done(e);
                            }

                        }
                    });
            return loader;
        }

        @Override
        public Loader<Event> getNextEvent() {
            Loader<Event> loader = new Loader<>();
            eventPath(Values.currentSemester.getId())
                    .whereEqualTo("visibility", "PUBLIC")
                    .whereGreaterThan("time", new Timestamp(Calendar.getInstance().getTime()))
                    .limit(1)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot, Event>(loader) {
                        @Override
                        public void done(QuerySnapshot qds) {
                            if (!qds.getDocuments().isEmpty()) {
                                try {
                                    Event e = qds.getDocuments().get(0).toObject(Event.class);
                                    e.setId(qds.getDocuments().get(0).getId());
                                    loader.done(e);
                                } catch (Exception e) {
                                    Log.e(TAG, "getNextEvent: done: Loading the next event of the " +
                                            "current semester " + Values.currentSemester.getId() +
                                            " didn't work.", e);
                                    loader.done(e);
                                }
                            } else {
                                loader.done();
                            }
                        }
                    });
            return loader;
        }

        @Override
        public Loader<Account> getEventAccount(int semesterID, String eventId) {
            Loader<Account> loader = new Loader<>();
            listener.add(getEventReference(semesterID, eventId)
                    .collection(ACCOUNT.get())
                    .document(ACCOUNT.get())
                    .addSnapshotListener((value, error) -> {
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
                    }));
            return loader;
        }

        @Override
        public Loader<ArrayList<Movement>> getEventMovements(int semesterID, String eventId) {
            Loader<ArrayList<Movement>> loader = new Loader<>();
            getMovementSubRef(getEventReference(semesterID, eventId))
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot, ArrayList<Movement>>(loader) {
                        @Override
                        public void done(QuerySnapshot qds) {
                            ArrayList<Movement> movementList = new ArrayList<>();
                            for (DocumentSnapshot ds : qds) {
                                try {
                                    Movement m = ds.toObject(Movement.class);
                                    m.setId(ds.getId());
                                    movementList.add(m);
                                } catch (Exception e) {
                                    Log.e(TAG, "getSemesterMovements: done: parsing object into Chore did not work.", e);
                                }
                            }
                            loader.done(movementList);
                        }
                    });
            return loader;
        }

        @Override
        public Loader<Person> person(String uid) {
            Loader<Person> loader = new Loader<>();
            getPersonReference(uid)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot, Person>(loader) {
                        @Override
                        public void done(DocumentSnapshot ds) {
                            try {
                                Person p = ds.toObject(Person.class);
                                p.setId(ds.getId());
                                loader.done(p);
                            } catch (Exception e) {
                                Log.e(TAG, "person: done: Loading person with uid " + uid + " didn't work", e);
                            }
                        }
                    });
            return loader;
        }

        @Override
        public Loader<ArrayList<Chore>> getPersonChores(String uid, boolean showDoneChores) {
            Loader<ArrayList<Chore>> loader = new Loader<>();
            CollectionReference ref = getPersonReference(uid)
                    .collection(CHORE.get());
            Task<QuerySnapshot> result;
            if (showDoneChores) {
                result = ref.get();
            } else {
                result = ref.whereEqualTo("done", false).get();
            }
            result.addOnCompleteListener(new OnCompleteListener<QuerySnapshot, ArrayList<Chore>>(loader) {
                @Override
                public void done(QuerySnapshot qds) {
                    ArrayList<Chore> choreList = new ArrayList<>();
                    for (DocumentSnapshot ds : qds) {
                        try {
                            Chore c = ds.toObject(Chore.class);
                            c.setId(ds.getId());
                            choreList.add(c);
                        } catch (Exception e) {
                            Log.e(TAG, "getPersonChores: done: parsing object into Chore did not work.", e);
                        }
                    }
                    loader.done(choreList);
                }
            });
            return loader;
        }

        @Override
        public Loader<ArrayList<BoardMember>> getPersonPastChargen(String uid) {
            Loader<ArrayList<BoardMember>> loader = new Loader<>();
            getPersonReference(uid)
                    .collection(BOARD.get())
                    .orderBy("semester", Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot, ArrayList<BoardMember>>(loader) {
                        @Override
                        public void done(QuerySnapshot qds) {
                            ArrayList<BoardMember> resultList = new ArrayList<>();
                            for (DocumentSnapshot ds : qds.getDocuments()) {
                                try {
                                    BoardMember bm = ds.toObject(BoardMember.class);
                                    bm.setId(ds.getId());
                                    resultList.add(bm);
                                } catch (Exception e) {
                                    Log.e(TAG, "getPersonPastChargen: done: parsing object into BoardMember did not work.", e);
                                }
                            }
                            loader.done(resultList);
                        }
                    });
            return loader;
        }

        @Override
        public Loader<ArrayList<DrinkMovement>> getPersonDrinkMovement(String uid, int semester) {
            Loader<ArrayList<DrinkMovement>> loader = new Loader<>();
            if (uid == null) {
                return loader.done(new UserDataError("no user is singed in"));
            }
            getPersonReference(uid)
                    .collection(DRINK.get())
                    .document(ACCOUNT.get())
                    .collection(DRINK_MOVEMENT.get())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot, ArrayList<DrinkMovement>>(loader) {
                        @Override
                        public void done(QuerySnapshot qds) {
                            ArrayList<DrinkMovement> movementList = new ArrayList<>();
                            for (DocumentSnapshot ds : qds) {
                                try {
                                    DrinkMovement dm = ds.toObject(DrinkMovement.class);
                                    dm.setId(ds.getId());
                                    movementList.add(dm);
                                } catch (Exception e) {
                                    Log.e(TAG, "getPersonDrinkMovement: done:" +
                                            " parsing object into DrinkMovement did not work.", e);
                                }
                            }
                            loader.done(movementList);
                        }
                    });
            return loader;
        }

        @Override
        public Loader<Account> getPersonBalance(String uid) {
            Loader<Account> loader = new Loader<>();
            listener.add(getPersonReference(uid)
                    .collection(ACCOUNT.get())
                    .document(ACCOUNT.get())
                    .addSnapshotListener((value, error) -> {
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
                    }));
            return loader;
        }

        @Override
        public Loader<ArrayList<Movement>> getPersonMovements(String uid) {
            Loader<ArrayList<Movement>> loader = new Loader<>();
            getMovementSubRef(getPersonReference(uid))
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot, ArrayList<Movement>>(loader) {
                        @Override
                        public void done(QuerySnapshot qds) {
                            if (qds.isEmpty()) {
                                loader.done();
                                return;
                            }
                            ArrayList<Movement> movementList = new ArrayList<>();
                            for (DocumentSnapshot ds : qds.getDocuments()) {
                                try {
                                    Movement m = ds.toObject(Movement.class);
                                    m.setId(ds.getId());
                                    movementList.add(m);
                                } catch (Exception e) {
                                    Log.e(TAG, "getPersonMovements: done: could not parse object into Movement", e);
                                }
                            }
                            loader.done(movementList);
                        }
                    });
            return loader;
        }

        @Override
        public Loader<ArrayList<Address>> personAddress(String uid) {
            Loader<ArrayList<Address>> loader = new Loader<>();
            getPersonReference(uid)
                    .collection(ADDRESS.get())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot, ArrayList<Address>>(loader) {
                        @Override
                        public void done(QuerySnapshot qds) {
                            ArrayList<Address> addressList = new ArrayList<>();
                            for (DocumentSnapshot ds : qds.getDocuments()) {
                                try {
                                    Address a = ds.toObject(Address.class);
                                    a.setId(ds.getId());
                                    addressList.add(a);
                                } catch (Exception e) {
                                    Log.e(TAG, "personAddress: done: could not parse object into Address", e);
                                }
                            }
                            loader.done(addressList);
                        }
                    });
            return loader;
        }

        @Override
        public Loader<ArrayList<File>> getPersonImage(String uid) {
            Loader<ArrayList<File>> loader = new Loader<>();
            getPersonReference(uid)
                    .collection(IMAGE.get())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot, ArrayList<File>>(loader) {
                        @Override
                        public void done(QuerySnapshot qds) {
                            ArrayList<File> fileList = new ArrayList<>();
                            for (DocumentSnapshot ds : qds.getDocuments()) {
                                try {
                                    File f = ds.toObject(File.class);
                                    f.setId(ds.getId());
                                    fileList.add(f);
                                } catch (Exception e) {
                                    Log.e(TAG, "getPersonImage: done: could not parse object into File", e);
                                }
                            }
                            loader.done(fileList);
                        }
                    });
            return loader;
        }

        @Override
        public Loader<ArrayList<Text>> getNewsText(String newsID) {
            Loader<ArrayList<Text>> loader = new Loader<>();
            FBFS.collection(NEWS.get())
                    .document(newsID)
                    .collection(TEXT.get())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot, ArrayList<Text>>(loader) {
                        @Override
                        public void done(QuerySnapshot qds) {
                            ArrayList<Text> textList = new ArrayList<>();
                            for (DocumentSnapshot ds : qds.getDocuments()) {
                                try {
                                    Text t = ds.toObject(Text.class);
                                    t.setId(ds.getId());
                                    textList.add(t);
                                } catch (Exception e) {
                                    Log.e(TAG, "getNewsText: done: could not parse object into Text", e);
                                }
                            }
                            loader.done(textList);
                        }
                    });
            return loader;
        }

        @Override
        public Loader<ArrayList<News>> getNews(Visibility visibility) {
            Loader<ArrayList<News>> loader = new Loader<>();
            listener.add(FBFS.collection(NEWS.get())
                    .addSnapshotListener((value, error) -> {
                        if (error != null) {
                            loader.done(error);
                            return;
                        }
                        if (value == null || value.isEmpty()) {
                            loader.done(new NoDataException("No news found"));
                            return;
                        }
                        ArrayList<News> newsList = new ArrayList<>();
                        for (DocumentSnapshot ds : value.getDocuments()) {
                            try {
                                News n = ds.toObject(News.class);
                                n.setId(ds.getId());
                                newsList.add(n);
                            } catch (Exception e) {
                                Log.e(TAG, "getNews: done: could not parse object into News", e);
                            }
                        }
                        loader.done(newsList);
                    }));
            return loader;
        }

        @Override
        public Loader<ArrayList<PersonLight>> getPersonList() {
            Loader<ArrayList<PersonLight>> loader = new Loader<>();
            try {
                listener.add(FBFS.collection(PERSON_LIST_TOTAL.get())
                        .orderBy("full_Name", Query.Direction.ASCENDING)
                        .addSnapshotListener((values, error) -> {
                            if (error != null) {
                                loader.done(error);
                                return;
                            }
                            if (values == null || values.isEmpty()) {
                                loader.done(new NoDataException("No persons downloaded"));
                                return;
                            }
                            ArrayList<PersonLight> personList = new ArrayList<>();
                            for (DocumentSnapshot ds : values.getDocuments()) {
                                try {
                                    PersonLight p = ds.toObject(PersonLight.class);
                                    p.setId(ds.getId());
                                    personList.add(p);
                                } catch (Exception e) {
                                    Log.e(TAG, "getPersonList: ", e);
                                }
                            }
                            loader.done(personList);
                        }));
                return loader;
            } catch (Exception e) {
                return loader.done(e);
            }
        }

        @Override
        public Loader<ArrayList<Location>> locationList() {
            Loader<ArrayList<Location>> loader = new Loader<>();
            FBFS.collection(LOCATION.get())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot, ArrayList<Location>>(loader) {
                        @Override
                        public void done(QuerySnapshot qds) {
                            ArrayList<Location> locationList = new ArrayList<>();
                            for (DocumentSnapshot ds : qds.getDocuments()) {
                                try {
                                    Location l = ds.toObject(Location.class);
                                    l.setId(ds.getId());
                                    locationList.add(l);
                                } catch (Exception e) {
                                    Log.e(TAG, "locationList: onComplete: couldn't parse object into location", e);
                                }
                            }
                            loader.done(locationList);
                        }
                    });
            return loader;
        }

        @Override
        public Loader<ArrayList<Text>> getNotes() {
            Loader<ArrayList<Text>> loader = new Loader<>();
            FBFS.collection(NOTES.get())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot, ArrayList<Text>>(loader) {
                        @Override
                        public void done(QuerySnapshot querySnapshot) {
                            ArrayList<Text> textList = new ArrayList<>();
                            for (DocumentSnapshot ds : querySnapshot) {
                                try {
                                    Text t = ds.toObject(Text.class);
                                    t.setId(ds.getId());
                                    textList.add(t);
                                } catch (Exception e) {
                                    Log.e(TAG, "getNotes: done: formatting into Text object didn't work.", e);
                                }
                            }
                            loader.done(textList);
                        }
                    });
            return loader;
        }

        @Override
        public Loader<ArrayList<Drink>> getDrinkKinds() {
            Loader<ArrayList<Drink>> loader = new Loader<>();
            FBFS.collection(DRINK.get())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot, ArrayList<Drink>>(loader) {
                        @Override
                        public void done(QuerySnapshot querySnapshot) {
                            ArrayList<Drink> drinkList = new ArrayList<>();
                            for (DocumentSnapshot ds : querySnapshot) {
                                try {
                                    Drink d = ds.toObject(Drink.class);
                                    d.setId(ds.getId());
                                    drinkList.add(d);
                                } catch (Exception e) {
                                    Log.e(TAG, "getDrinkKinds: done: Formatting drink didn't work.", e);
                                }
                            }
                            loader.done(drinkList);
                        }
                    });
            return loader;
        }

        @Override
        public Loader<File> file(@NonNull DocumentReference reference) {
            Loader<File> loader = new Loader<>();
            reference.get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot, File>(loader) {
                        @Override
                        public void done(DocumentSnapshot ds) {
                            File f = ds.toObject(File.class);
                            f.setId(ds.getId());
                            loader.done(f);
                        }
                    });
            return loader;
        }
    }

    /**
     * The actual upload of the data to the Firebase firestore database.
     *
     * @author B3tterTogeth3r
     * @version 2.1
     * @see de.b3ttertogeth3r.walhalla.interfaces.firebase.IFirestoreUpload
     * @since 2.9
     */
    public class Upload extends References implements IFirestoreUpload {
        private static final String TAG = "Upload.Upload";

        public Upload() {
            upload = this;
        }

        @Override
        public Loader<Boolean> setBoard(@SemesterRange int semID, @NonNull BoardMember boardMember) {
            Loader<Boolean> loader = new Loader<>();
            final CollectionReference[] colRef = new CollectionReference[1];
            switch (boardMember.getCharge()) {
                case X:
                case VX:
                case FM:
                case XX:
                case XXX:
                case VOP:
                case VVOP:
                    // upload into "Student_Board"
                    colRef[0] = studentPath(semID);
                    uploadToBoard(boardMember, colRef[0], loader);
                    break;
                case AH_X:
                case AH_HW:
                case AH_XX:
                case AH_XXX:
                    // upload into "Philistines_Board"
                    colRef[0] = philPath(semID);
                    uploadToBoard(boardMember, colRef[0], loader);
                    break;
                case ADMIN:
                    // upload into both
                    colRef[0] = philPath(semID);
                    uploadToBoard(boardMember, colRef[0], new Loader<Boolean>()
                            .setOnSuccessListener(result -> {
                                colRef[0] = studentPath(semID);
                                uploadToBoard(boardMember, colRef[0], loader);
                            }).setOnFailListener(e -> {
                                colRef[0] = studentPath(semID);
                                uploadToBoard(boardMember, colRef[0], loader);
                            }));
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
                                        loader.done(new NoDataException("Upload of new board member " +
                                                boardMember.getFull_name() + " unsuccessful", task.getException()));
                                    return;
                                }
                                if (task.getResult() == null) {
                                    if (loader != null)
                                        loader.done(new NoDataException("Upload of new board member " +
                                                boardMember.getFull_name() + " unsuccessful"));
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
                                        loader.done(new NoDataException("Update of board member "
                                                + boardMember.getId() + " unsuccessful", task.getException()));
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
                if (event.getId() != null) {
                    // edit event
                    eventPath(semID).document(event.getId())
                            .set(event)
                            .addOnCompleteListener(task -> {
                                if (task.getException() != null) {
                                    loader.done(new NoDataException("Upload of new event unsuccessful", task.getException()));
                                    return;
                                }
                                loader.done(true);
                            });
                } else {
                    // add new event
                    eventPath(semID).add(event)
                            .addOnCompleteListener(task -> {
                                if (task.getException() != null) {
                                    loader.done(new NoDataException("Upload of new event unsuccessful", task.getException()));
                                    return;
                                }
                                if (task.getResult() == null) {
                                    loader.done(new NoDataException("Upload of new event unsuccessful"));
                                    return;
                                }
                                loader.done(true);
                            });
                }
            } else {
                return loader.done(new InvalidParameterException("setEvent: Not all necessary fields are set"));
            }
            return loader;
        }

        @Override
        public Loader<Boolean> setEventChore(@SemesterRange int semID, @NonNull String eventID, @NonNull Chore chore) {
            Loader<Boolean> loader = new Loader<>();
            CollectionReference ref = eventPath(semID)
                    .document(eventID)
                    .collection(CHORE.get());
            if (!chore.validate()) {
                return loader.done(new NotValidObjectException("Chore object not valid"));
            }
            if (chore.getId().isEmpty()) {
                // set new chore
                ref.add(chore)
                        .addOnCompleteListener(task -> {
                            if (task.getException() != null) {
                                loader.done(new NoDataException("Upload of new event chore unsuccessful", task.getException()));
                                return;
                            }
                            if (task.getResult() == null) {
                                loader.done(new NoDataException("Upload of new event chore unsuccessful"));
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
                                loader.done(new NoDataException("Update of event chore unsuccessful", task.getException()));
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
            if (eventID.isEmpty()) {
                return loader.done(new NoDataException("No event given"));
            }
            CollectionReference ref = eventPath(semID)
                    .document(eventID)
                    .collection(LOCATION.get());
            if (!location.validate()) {
                return loader.done(new NotValidObjectException("Location object not valid."));
            }
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
            if (eventID.isEmpty()) {
                return loader.done(new NoDataException("No event given"));
            }
            if (description.isEmpty()) {
                return loader.done();
            }
            for (Text t : description) {
                if (!t.validate()) {
                    return loader.done(new NotValidObjectException("Text object invalid at position "
                            + t.getPosition() + " - stopping download"));
                }
            }
            CollectionReference ref = eventPath(semID)
                    .document(eventID)
                    .collection(DESCRIPTION.get());
            ref.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot, DocumentSnapshot>(null) {
                        @Override
                        public void done(QuerySnapshot queryDocumentSnapshots) {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                for (DocumentSnapshot ds : queryDocumentSnapshots) {
                                    ds.getReference().delete();
                                }
                            }
                            for (Text t : description) {
                                ref.add(t);
                            }
                        }
                    });
            return loader;
        }

        @Override
        public Loader<Boolean> addSemesterMovement(@SemesterRange int semID, @NonNull Movement movement) {
            Loader<Boolean> loader = new Loader<>();
            CollectionReference ref = getSemesterReference(semID)
                    .collection(ACCOUNT.get())
                    .document(ACCOUNT.get())
                    .collection(MOVEMENT.get());
            if (!movement.validate()) {
                return loader.done(new NotValidObjectException("Movement object is not valid."));
            }
            if (movement.getId().isEmpty()) {
                // set a new movement
                ref.add(movement)
                        .addOnSuccessListener(reference -> loader.done(true))
                        .addOnFailureListener(loader::done);
            } else {
                // update existing movement
                ref.add(movement)
                        .addOnSuccessListener(reference -> loader.done(true))
                        .addOnFailureListener(loader::done);
            }
            return loader;
        }

        @Override
        public Loader<Boolean> addSemesterMeetingTranscript(@SemesterRange int semID, @NonNull File file) {
            Loader<Boolean> loader = new Loader<>();
            CollectionReference reference = getSemesterReference(semID)
                    .collection(TRANSCRIPT.get());
            if (!file.validate()) {
                return loader.done(new NotValidObjectException("Meeting transcript file object is not valid."));
            }
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
            CollectionReference ref = FBFS.collection(NEWS.get());
            if (news == null) {
                // upload new entry
                ref.add(new News()).addOnSuccessListener(reference -> {
                    CollectionReference textPath = reference.collection(TEXT.get());
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
                                .collection(TEXT.get())
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
            CollectionReference ref = FBFS.collection(PERSON.get());
            if (!person.validate()) {
                return loader.done(new NotValidObjectException("Person not valid"));
            }
            if (person.getId() == null || person.getId().isEmpty()) {
                // Set a new person
                ref.add(person)
                        .addOnFailureListener(loader::done)
                        .addOnSuccessListener(reference -> {
                            if (reference != null) {
                                loader.done(true);
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
            // Only one address is allowed at the moment.
            if (addressList.size() != 1) {
                loader.done(new IndexOutOfBoundsException("Only one address is allowed."));
            }
            getPersonReference(personID)
                    .collection(ADDRESS.get())
                    .document("Home")
                    .set(addressList.get(0))
                    .addOnFailureListener(loader::done)
                    .addOnSuccessListener(unused -> loader.done());
            return loader;
        }

        @Override
        public Loader<Boolean> setPersonChore(@NonNull String personID, @NonNull Chore chore) {
            Loader<Boolean> loader = new Loader<>();
            CollectionReference ref = getPersonReference(personID)
                    .collection(CHORE.get());
            if (chore.getId().isEmpty()) {
                // upload new object
                ref.add(chore)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference, Boolean>(loader) {
                            @Override
                            public void done(DocumentReference reference) {
                                loader.done(true);
                            }
                        });
            } else {
                // update existing object
                ref.document(chore.getId())
                        .set(chore)
                        .addOnCompleteListener(new OnCompleteListener<Void, Boolean>(loader) {
                            @Override
                            public void done(Void unused) {
                                loader.done(true);
                            }
                        });
            }
            return loader;
        }

        @Override
        public Loader<Boolean> setPersonCharge(@NonNull String personID, @SemesterRange int semester, @NonNull BoardMember boardMember) {
            Loader<Boolean> loader = new Loader<>();
            getPersonReference(personID)
                    .collection(BOARD.get())
                    .document(String.valueOf(semester))
                    .set(boardMember)
                    .addOnCompleteListener(new OnCompleteListener<Void, Boolean>(loader) {
                        @Override
                        public void done(Void unused) {
                            loader.done(true);
                        }
                    });
            return loader;
        }

        @Override
        public Loader<Boolean> addPersonMovement(@NonNull String personID, @NonNull Movement movement) {
            Loader<Boolean> loader = new Loader<>();
            if (!movement.validate()) {
                return loader.done(new NotValidObjectException("Movement object not valid."));
            }
            CollectionReference ref = getPersonReference(personID)
                    .collection(ACCOUNT.get())
                    .document("Account")
                    .collection(MOVEMENT.get());
            if (movement.getId() == null || movement.getId().isEmpty()) {
                ref.add(movement)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference, Boolean>(loader) {
                            @Override
                            public void done(DocumentReference reference) {
                                loader.done(true);
                            }
                        });
            } else {
                ref.document(movement.getAdd())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot, Boolean>(loader) {
                            @Override
                            public void done(DocumentSnapshot documentSnapshot) {
                                loader.done(true);
                            }
                        });
            }
            return loader;
        }

        @Override
        public Loader<Boolean> addPersonPicture(@NonNull String personID, @NonNull File file) {
            Loader<Boolean> loader = new Loader<>();
            if (!file.validate()) {
                return loader.done(new NotValidObjectException("File object invalid"));
            }
            CollectionReference ref = getPersonReference(personID)
                    .collection(IMAGE.get());
            if (file.getId().isEmpty()) {
                // upload new file object
                ref.add(file)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference, Boolean>(loader) {
                            @Override
                            public void done(DocumentReference reference) {
                                loader.done(true);
                            }
                        });
            } else {
                // update existing file object
                ref.document(file.getId())
                        .set(file)
                        .addOnCompleteListener(new OnCompleteListener<Void, Boolean>(loader) {
                            @Override
                            public void done(Void unused) {
                                loader.done(true);
                            }
                        });
            }
            return loader;
        }
    }
}
