package de.b3ttertogeth3r.walhalla.firebase;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import de.b3ttertogeth3r.walhalla.enums.Charge;
import de.b3ttertogeth3r.walhalla.interfaces.MyCompleteListener;
import de.b3ttertogeth3r.walhalla.models.Account;
import de.b3ttertogeth3r.walhalla.models.Event;
import de.b3ttertogeth3r.walhalla.models.Image;
import de.b3ttertogeth3r.walhalla.models.Person;
import de.b3ttertogeth3r.walhalla.models.Semester;
import de.b3ttertogeth3r.walhalla.utils.CacheData;

import static de.b3ttertogeth3r.walhalla.firebase.Authentication.AUTH;

/**
 * @see <a href="https://firebase.google.com/docs/firestore/quickstart">Cloud Firestore</a>
 */
@SuppressLint("StaticFieldLeak")
public class Firestore {
    private static final String TAG = "Firestore";
    public static FirebaseFirestore FIRESTORE;

    public static void getImage (@NonNull String uid, MyCompleteListener<Image> listener) {
        try {
            FIRESTORE.collection("Images")
                    .document(uid)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        try {
                            Image image = documentSnapshot.toObject(Image.class);
                            if (image != null) {
                                listener.onSuccess(image);
                            } else {
                                listener.onFailure(new NullPointerException("Image not found"));
                            }
                        } catch (Exception e) {
                            listener.onFailure(e);
                        }
                    });
        } catch (Exception e) {
            Crashlytics.error(TAG, "loading image did not work.", e);
        }
    }

    public static void uploadImage (@NonNull Bitmap image, String name,
                                    MyCompleteListener<Void> listener) {
        Storage.uploadImage(image, name, new MyCompleteListener<Uri>() {
            @Override
            public void onSuccess (Uri uri) {
                Image image = new Image();
                image.setLarge_path(uri.toString());
                //TODO add _100x100 before the ".jpeg" and save it as image.setIcon_path(small);
                image.setDescription(name);
                FIRESTORE.collection("Images")
                        .add(image)
                        .addOnCompleteListener(task -> {
                            if (!task.isSuccessful() && task.getException() != null) {
                                listener.onFailure(task.getException());
                            }
                            listener.onSuccess(null);
                        });
            }

            @Override
            public void onFailure (Exception exception) {

            }
        });
    }

    public static void getSemester (int semesterID, MyCompleteListener<DocumentSnapshot> listener) {
        getSemester(semesterID).get().addOnCompleteListener(task -> {
            DocumentSnapshot ds = task.getResult();
            if (!ds.exists()) {
                if (listener != null) {
                    listener.onFailure(task.getException());
                }
            }
            if (listener != null) {
                listener.onSuccess(ds);
            }
        });
    }

    @NonNull
    public static DocumentReference getSemester (int semesterID) {
        if (semesterID < 0) {
            semesterID = semesterID * (-1);
        }
        // Log.d(TAG, "getSemester: " + semesterID);
        return FIRESTORE.collection("Semester").document(String.valueOf(semesterID));
    }

    public static void findUserById (String uid, MyCompleteListener<DocumentSnapshot> listener) {
        FIRESTORE.collection("Person")
                .document(uid)
                .get()
                .addOnCompleteListener(task -> {
                    if(! task.isSuccessful()) {
                        listener.onFailure();
                        return;
                    }
                    listener.onSuccess(task.getResult());
                });
    }

    public static void uploadAccountEntry (String semester, Account account) {
        FIRESTORE.collection("Semester/" + semester + "/Account")
                .add(account)
                .addOnSuccessListener(documentReference ->
                    Log.d(TAG, "uploadAccountEntry: success: " + semester)
                )
                .addOnFailureListener(e -> Log.e(TAG,
                        "uploadAccountEntry: failure: semester" + semester, e));
    }

    public static void editAccountEntry (String semester_id, Account account) {

    }

    @NonNull
    public static CollectionReference loadSemesterEvents (@NonNull @NotNull String semester_id) {
        return FIRESTORE.collection("Semester")
                .document(semester_id)
                .collection("Events");
    }

    public static void uploadPerson (@NonNull Person person) {
        uploadPerson(person, null);
    }

    public static void uploadPerson (@NonNull Person person, MyCompleteListener<String> listener) {
        try {
            if (person.getId() == null) {
                FIRESTORE.collection("Person")
                        .add(person)
                        .addOnCompleteListener(task -> {
                            Log.d(TAG, "userCreate: success");
                            if (listener != null) {
                                listener.onSuccess(task.getResult().getPath());
                            }
                        })
                        .addOnFailureListener(e -> {
                            Crashlytics.error(TAG, "userCreate: onFailure: " +
                                    "update user did not work", e);
                            if (listener != null) {
                                listener.onFailure(e);
                            }
                        });
            } else {
                FIRESTORE.collection("Person").document(person.getId()).set(person.toMap())
                        .addOnCompleteListener(task -> {
                            Log.d(TAG, "userUpdate: success");
                            if (listener != null) {
                                listener.onSuccess(null);
                            }
                        })
                        .addOnFailureListener(e -> {
                            Crashlytics.error(TAG, "onFailure: " +
                                    "update user did not work", e);
                            if (listener != null) {
                                listener.onFailure(e);
                            }
                        });
            }
        } catch (Exception e) {
            e.printStackTrace();
            if(listener != null) {
                listener.onFailure(e);
            }
        }
    }

    public static void getChargen (@NonNull Semester semester,
                                   @NonNull MyCompleteListener<QuerySnapshot> listener) {
        FIRESTORE.collection("Semester")
                .document(semester.getId() + "")
                .collection("Board_Students")
                .get()
                .addOnSuccessListener(listener::onSuccess)
                .addOnFailureListener(listener::onFailure);
    }

    public static void getPhilChargen (@NonNull Semester semester,
                                       @NonNull MyCompleteListener<QuerySnapshot> listener) {
        FIRESTORE.collection("Semester")
                .document(semester.getId() + "")
                .collection("Board_union")
                .get()
                .addOnSuccessListener(listener::onSuccess)
                .addOnFailureListener(listener::onFailure);
    }

    public static void getUserCharge (MyCompleteListener<Void> listener) {
        if (Authentication.isSignIn()) {
            Firestore.findUserCharge(new MyCompleteListener<String>() {
                @Override
                public void onSuccess (String string) {
                    Charge charge = Charge.find(string);
                    CacheData.setCharge(charge);
                    if (listener != null) {
                        listener.onSuccess(null);
                    }
                }

                @Override
                public void onFailure (Exception e) {
                    if (listener != null) {
                        listener.onSuccess(null);
                    }
                }
            });
        } else {
            if (listener != null) {
                listener.onSuccess(null);
            }
        }
    }

    public static void findUserCharge (MyCompleteListener<String> listener) {
        if (AUTH.getUid() == null || AUTH.getUid().isEmpty()) {
            listener.onFailure();
        }
        FIRESTORE.collection("Current")
                .whereArrayContains("UID", AUTH.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if(task.getException() != null) {
                        listener.onFailure();
                        return;
                    }
                    if (task.getResult().isEmpty()) {
                        listener.onFailure();
                        return;
                    }
                    for(QueryDocumentSnapshot qds : task.getResult()) {
                        String id = qds.getId();
                        if (id.equals(Charge.ADMIN.getName())) {
                            listener.onSuccess(id);
                            break;
                        } else {
                            listener.onSuccess(id);
                        }
                    }
                });
    }

    /**
     * Listening to the last 10 drinks the signed in user drank
     *
     * @param listener
     *         OnGetDataListener to send the data back to the fragment
     */
    public static void getUserDrinks (MyCompleteListener<QuerySnapshot> listener) {
        if (Authentication.isSignIn()) {
            String uid = Authentication.getUser().getUid();
            FIRESTORE.collection("Drinks")
                    .whereEqualTo("uid", uid)
                    .orderBy("Date")
                    .limit(10)
                    .addSnapshotListener((value, error) -> {
                        if (error != null) {
                            Crashlytics.error(TAG, "listening to realtime changes " +
                                    "of drinks did not work, mostly because user has no " +
                                    "drinks");
                            listener.onFailure();
                            return;
                        }
                        if (value != null && !value.isEmpty()) {
                            listener.onSuccess(value);
                        }
                    });
        }
    }

    public static void getDrinkValues (MyCompleteListener<QuerySnapshot> listener) {
        FIRESTORE.collection("Enums/rKnY7Tv2NpmTuMGvwPPW/Drink")
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                listener.onSuccess(task.getResult());
                return;
            }
            listener.onFailure();
        });
    }

    public static void findAllPerson (MyCompleteListener<QuerySnapshot> listener) {
        Query query;
        switch (CacheData.getCharge()){
            case X:
            case XXX:
            case ADMIN:
            case AH_X:
                query = FIRESTORE.collection("Person");
                break;
            default:
                query = FIRESTORE.collection("Person")
                        .whereEqualTo("isDisabled", false);
                break;
        }
        query.get()
                .addOnSuccessListener(task -> {
                    if (task.isEmpty()) {
                        listener.onFailure();
                    }
                    listener.onSuccess(task);
                });
    }

    public static void findUserByName (String first_name, String last_name,
                                       MyCompleteListener<QuerySnapshot> listener) {
        FIRESTORE.collection("Person")
                .whereEqualTo("first_name", first_name)
                .whereEqualTo("last_name", last_name)
                .limit(1)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getException() != null) {
                        listener.onFailure(task.getException());
                    } else if(task.getResult().isEmpty()){
                        listener.onFailure();
                    }
                    listener.onSuccess(task.getResult());
                });
    }

    public static void uploadCharge (@NonNull Semester chosenSemester,
                                     @NonNull de.b3ttertogeth3r.walhalla.models.Charge charge
            , @NonNull Charge kind) {
        uploadCharge(chosenSemester, charge, kind, null);
    }

    public static void uploadCharge (@NonNull Semester chosenSemester,
                                     @NonNull de.b3ttertogeth3r.walhalla.models.Charge charge
            , @NonNull Charge kind, @Nullable MyCompleteListener<Void> listener) {
        DocumentReference ref = FIRESTORE.collection("Semester")
                .document(String.valueOf(chosenSemester.getId()));
        switch (kind) {
            case X:
            case VX:
            case FM:
            case XX:
            case XXX:
                ref = ref.collection("Board_Students")
                        .document(kind.getName());
                break;
            case AH_X:
            case AH_XX:
            case AH_XXX:
            case AH_HW:
                ref = ref.collection("Board_union")
                        .document(kind.getName());
                break;
        }
        ref.set(charge)
                .addOnCompleteListener(task -> {
                    if (task.getException() != null && listener != null) {
                        listener.onFailure(task.getException());
                    }
                    if (listener != null) {
                        listener.onSuccess(null);
                    }
                });

    }

    public static void findAllImages (@NonNull MyCompleteListener<QuerySnapshot> listener) {
        FIRESTORE.collection("Images")
                .get()
                .addOnFailureListener(listener::onFailure)
                .addOnSuccessListener(listener::onSuccess);
    }

    public static void getUserByEmail (String email, MyCompleteListener<QuerySnapshot> listener) {
        FIRESTORE.collection("Person")
                .whereEqualTo("email", email)
                .limit(1)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getException() == null) {
                        listener.onSuccess(task.getResult());
                        return;
                    }
                    listener.onFailure();
                });
    }

    public static void uploadEvent (Event event) {
        uploadEvent(event, null);
    }

    public static void uploadEvent (Event event, MyCompleteListener<DocumentReference> listener) {
        String semId = String.valueOf(CacheData.getChosenSemester().getId());
        FIRESTORE.collection("Semester")
                .document(semId)
                .collection("Events")
                .add(event)
                .addOnCompleteListener(task -> {
                    if(task.getException()!= null){
                        if(listener != null) {
                            listener.onFailure(task.getException());
                        }
                        if(listener != null) {
                            listener.onSuccess(task.getResult());
                        }
                    }
                });
    }
}
