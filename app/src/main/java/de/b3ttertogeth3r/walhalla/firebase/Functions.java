package de.b3ttertogeth3r.walhalla.firebase;


import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.b3ttertogeth3r.walhalla.interfaces.MyCompleteListener;
import de.b3ttertogeth3r.walhalla.models.Charge;
import de.b3ttertogeth3r.walhalla.models.Person;
import de.b3ttertogeth3r.walhalla.models.User;

/**
 * <font color="yellow">
 * TODO Add function to edit email-addresses of users
 * <p><font color="yellow">
 * TODO Add function to find all auth-accounts which aren't verified yet
 * <p><font color="yellow">
 * TODO Add function to send a verification email and maybe make the mail customizable via the app
 *  or change it directly in the console or CF
 *
 * @see
 * <a href="https://firebase.google.com/docs/functions/callable?authuser=0#call_the_function">Firebase
 * Cloud Functions from Android</a>
 */
public class Functions {
    private static final String TAG = "CloudFunctions";
    protected static FirebaseFunctions FUNCTIONS;

    /**
     * create a user after creating a profile of this person without a password
     *
     * @param person
     *         all the values of the person
     * @param listener
     *         listener to get the result of the upload
     */
    public static void createUser (Person person, MyCompleteListener<String> listener){
        if (person == null) {
            Log.e(TAG, "createUser: Person cannot be empty");
            return;
        }
        createUser(person, "Walhalla1864", listener);
    }

    /**
     * create a user after creating a profile of this person.
     *
     * @param person
     *         all the values of the person
     * @param password password the user set
     * @param listener
     *         listener to get the result of the upload
     */
    public static void createUser (Person person, String password, MyCompleteListener<String> listener) {
        if (person == null) {
            Log.e(TAG, "createUser: Person cannot be empty");
            return;
        }
        Map<String, Object> data = new HashMap<>();
        data.put("email", person.getEmail());
        data.put("displayName", person.getFullName());
        data.put("password", password);

        if (!person.getMobile().isEmpty()) {
            data.put("phoneNumber", person.getMobile());
        }

        if (!person.getPicture_path().isEmpty()) {
            Storage.getUri(person.getPicture_path(), new MyCompleteListener<Uri>() {
                @Override
                public void onSuccess (Uri imageUri) {
                    data.put("photoURL", imageUri.toString());
                    createUser(data, listener);
                }

                @Override
                public void onFailure (Exception exception) {
                    listener.onFailure(exception);
                    createUser(data, listener);
                }
            });
            return;
        }
        createUser(data, listener);
    }

    protected static void createUser (Map<String, Object> userData, MyCompleteListener<String> listener) {
        try {
            addUser(userData)
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful() && task.getException() != null) {
                            Log.e(TAG, "onFailure: ", task.getException());
                            return;
                        }
                        Map<String, Object> result = task.getResult();
                        if (!Objects.equals(result.get("error"), "null")) {
                            String uid = (String) result.get("uid");
                            listener.onSuccess(uid);
                        } else {
                            String error = (String) result.get("error");
                            listener.onFailure(new Exception(error));
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "createUser: ", e);
        }
    }

    @NonNull
    private static Task<Map<String, Object>> addUser (Map<String, Object> data) {
        return FUNCTIONS
                .getHttpsCallable("createUser")
                .call(data)
                .continueWith(task -> (Map<String, Object>) task.getResult().getData());
    }

    /**
     * create an auth account for the new charge.
     *
     * @param charge
     *         all the data of the charge
     * @param listener
     *         get the result of the upload
     */
    public static void createUser (Charge charge, MyCompleteListener<String> listener) {
        if (charge == null) {
            Log.e(TAG, "createUser: charge cannot be empty or null");
            return;
        }
        Map<String, Object> data = new HashMap<>();
        data.put("email", charge.getMail());
        data.put("displayName", charge.getFullName());

        // only add phone number if on is available
        if (!charge.getMobile().isEmpty()) {
            data.put("phoneNumber", charge.getMobile());
        }
        // only add image if one was selected and uploaded.
        // if upload previously failed, create user without an image.
        if (!charge.getPicture_path().isEmpty()) {
            Storage.getUri(charge.getPicture_path(), new MyCompleteListener<Uri>() {
                @Override
                public void onSuccess (Uri imageUri) {
                    data.put("photoURL", imageUri.toString());
                    createUser(data, listener);
                }

                @Override
                public void onFailure (Exception exception) {
                    listener.onFailure(exception);
                    createUser(data, listener);
                }
            });
            return;
        }
        createUser(data, listener);
    }

    public static void findAllUsers (MyCompleteListener<List<User>> listener) {
        try{
            findAll()
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful() && task.getException() != null) {
                            listener.onFailure(task.getException());
                            return;
                        }
                        ArrayList<Object> list = task.getResult();
                        List<User> userList = new ArrayList<>();
                        if(list.size() == 0) {
                            listener.onFailure();
                            return;
                        }
                        for(Object u : list) {
                            try {
                                userList.add((User) u);
                            } catch (Exception e){
                                Log.e(TAG, "findAllUsers: ", e);
                            }
                        }
                        if(listener != null){
                            listener.onSuccess(userList);
                        }
                    });
        } catch (Exception e){
            Log.e(TAG, "findAllUsers: something went wrong", e);
        }
    }

    @NonNull
    private static Task<ArrayList<Object>> findAll() {
        return FUNCTIONS
                .getHttpsCallable("getAllUsers")
                .call()
                .continueWith(task ->
                        (ArrayList<Object>) task.getResult().getData()
                );
    }
}
