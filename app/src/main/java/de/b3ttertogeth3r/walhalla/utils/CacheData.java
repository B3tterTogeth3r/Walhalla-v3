package de.b3ttertogeth3r.walhalla.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.Timestamp;
import com.google.firebase.database.annotations.NotNull;

import org.jetbrains.annotations.Contract;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.b3ttertogeth3r.walhalla.R;
import de.b3ttertogeth3r.walhalla.enums.Address;
import de.b3ttertogeth3r.walhalla.enums.Charge;
import de.b3ttertogeth3r.walhalla.exceptions.PersonException;
import de.b3ttertogeth3r.walhalla.firebase.Firebase;
import de.b3ttertogeth3r.walhalla.models.Person;
import de.b3ttertogeth3r.walhalla.models.ProfileError;
import de.b3ttertogeth3r.walhalla.models.Semester;

/**
 * @author B3tterTogeth3r
 * @version 1.0
 * @see SharedPreferences
 * @since 1.1
 */
public class CacheData {
    public static final String CURRENT_SEMESTER = "current_semester";
    public static final String START_PAGE = "start_page";
    public static final String CHOSEN_SEMESTER = "chosen_semester";
    public static final String PROFILE_ERROR = "profile_error";
    public static final String PROFILE_ERROR_EXISTS = "profile_error_exists";
    public static final String USER_DATA = "user_data";
    private static final String TAG = "CacheData";
    private static final String INTENT_START_PAGE = "intent_start_page";
    private static final String USER_CHARGE = "Charge";
    private static SharedPreferences SP;

    public CacheData () {
    }

    /**
     * Class to organize {@link SharedPreferences} inside the app.
     */
    public static void init (@NonNull @NotNull Context ctx) {
        SP = ctx.getSharedPreferences(TAG, Context.MODE_PRIVATE);
    }

    /**
     * Change the value of the analytics data collection.
     *
     * @param value
     *         <code>true</code>: the user wants to send analytics data.<br>
     *         <code>false</code>: the user <b>does not</b> want to send analytics data.
     */
    public static void ChangeAnalyticsCollection (boolean value) {
        if (value) {
            SP.edit().putBoolean(Firebase.Analytics.TAG, true).apply();
        } else {
            SP.edit().putBoolean(Firebase.Analytics.TAG, false).apply();
        }
    }

    /**
     * the value of the users setting to send analytics data.
     *
     * @return true, if nothing is set, otherwise users choice.
     * @since 1.0
     */
    public static boolean getAnalyticsCollection () {
        return SP.getBoolean(Firebase.Analytics.TAG, true);
    }

    public static int getIntentStartPage () {
        int result = SP.getInt(INTENT_START_PAGE, getStartPage());
        SP.edit().remove(INTENT_START_PAGE).apply();
        return result;
    }

    /**
     * The user can choose a custom page the app can start to. It doesn't have to be the "home"
     * page. If no page is selected, the {@link de.b3ttertogeth3r.walhalla.fragments.home.Fragment
     * Home Fragment} will be returned.
     *
     * @return the id of the page
     */
    public static int getStartPage () {
        return SP.getInt(START_PAGE, R.string.menu_home);
    }

    /**
     * Change the starting fragment after loading all the data from {@link
     * de.b3ttertogeth3r.walhalla.SplashActivity SplashActivity}
     *
     * @param page_id
     *         string value of page name
     */
    public static void setStartPage (int page_id) {
        SP.edit().putInt(START_PAGE, page_id).apply();
        Firebase.Analytics.changeStartPage();
    }

    public static void setIntentStartPage (int page_id) {
        SP.edit().putInt(INTENT_START_PAGE, page_id).apply();
    }

    /**
     * get the user choice or the current semester
     *
     * @return the Semester
     */
    @NonNull
    @Contract(" -> new")
    public static Semester getChosenSemester () {
        return new Semester(SP.getStringSet(CHOSEN_SEMESTER, getCurrentSemester().getSet()));
    }

    @NonNull
    @Contract(" -> new")
    public static Semester getCurrentSemester () {
        return new Semester(SP.getStringSet(CURRENT_SEMESTER, null));
    }

    /**
     * @param sem_id
     *         the id of the semester from the
     *         {@link de.b3ttertogeth3r.walhalla.firebase.Firebase.Firestore
     *         Firestore}-Database
     * @since 1.0
     */
    public static void setCurrentSemester (int sem_id) {
        Firebase.Firestore.getSemester(sem_id).get().addOnSuccessListener(documentSnapshot -> {
            if (!documentSnapshot.exists()) {
                Firebase.Crashlytics.log(TAG, "could not load semester with id " + sem_id);
            } else {
                try {
                    Semester currentSemester = documentSnapshot.toObject(Semester.class);
                    //noinspection ConstantConditions
                    currentSemester.setId(Integer.parseInt(documentSnapshot.getId()));
                    SP.edit().putStringSet(CURRENT_SEMESTER, currentSemester.getSet())
                            .apply();
                    // at each start of the app reset chosen semester to the current one.
                    setChosenSemester(currentSemester);
                } catch (Exception e) {
                    Firebase.Crashlytics.log(TAG, "could not parse semester with id " + sem_id, e);
                    Semester currentSemester = new Semester();
                    SP.edit().putStringSet(CURRENT_SEMESTER, currentSemester.getSet())
                            .apply();
                }
            }
        });
    }

    /**
     * Save the semester onto the device via {@link SharedPreferences}
     *
     * @param semester
     *         the semester, the user chose.
     * @since 1.1
     */
    public static void setChosenSemester (@NonNull Semester semester) {
        SP.edit().putStringSet(CHOSEN_SEMESTER, semester.getSet())
                .apply();
    }

    @Nullable
    public static ProfileError getProfileError () {
        if (SP.getBoolean(PROFILE_ERROR_EXISTS, false)) {
            ProfileError pe = new ProfileError(SP.getStringSet(PROFILE_ERROR, null));
            SP.edit().remove(PROFILE_ERROR_EXISTS).remove(PROFILE_ERROR).apply();
            return pe;
        }
        return null;
    }

    /**
     * Save a message if a loading error occurred. It cann occur while downloading the users profile
     * information
     *
     * @param profileError
     *         {@link ProfileError}
     */
    public static void setProfileError (@NonNull ProfileError profileError) {
        SP.edit().putBoolean(PROFILE_ERROR_EXISTS, true)
                .putStringSet(PROFILE_ERROR, profileError.getSet())
                .apply();
    }

    public static void saveUser (@NonNull Person user) {
        Map<String, Object> address = user.getAddress();
        SharedPreferences.Editor editor = SP.edit();
        try {
            editor.putString(USER_DATA + Person.ADDRESS_CITY,
                    address.get(Address.CITY.toString()).toString());
        } catch (Exception ignored) {
        }
        try {
            editor.putString(USER_DATA + Person.ADDRESS_NUMBER,
                    address.get(Address.NUMBER.toString()).toString());
        } catch (Exception ignored) {
        }
        try {
            editor.putString(USER_DATA + Person.ADDRESS_STREET,
                    address.get(Address.STREET.toString()).toString());
        } catch (Exception ignored) {
        }
        try {
            editor.putString(USER_DATA + Person.ADDRESS_ZIP_CODE,
                    address.get(Address.CITY.toString()).toString());
        } catch (Exception ignored) {
        }

        //TODO RankSettings

        editor.putFloat(USER_DATA + Person.BALANCE, user.getBalance())
                .putLong(USER_DATA + Person.DOB, user.getDoB().getSeconds())
                .putString(USER_DATA + Person.FIRST_NAME, user.getFirst_Name())
                .putString(USER_DATA + Person.FCM_TOKEN, user.getFcm_token())
                .putString(USER_DATA + Person.ID, user.getId())
                .putInt(USER_DATA + Person.JOINED, user.getJoined())
                .putString(USER_DATA + Person.LAST_NAME, user.getLast_Name())
                .putString(USER_DATA + Person.MAIL, user.getMail())
                .putString(USER_DATA + Person.MAJOR, user.getMajor())
                .putString(USER_DATA + Person.MOBILE, user.getMobile())
                .putString(USER_DATA + Person.PICTURE_PATH, user.getPicture_path())
                .putString(USER_DATA + Person.POB, user.getPoB())
                .putString(USER_DATA + Person.RANK, user.getRank())
                .putString(USER_DATA + Person.UID, user.getUid())
                .apply();
    }

    @NonNull
    @Contract(" -> new")
    public static Person getUser () throws PersonException {
        Person person = new Person();
        person.setBalance(SP.getFloat(USER_DATA + Person.BALANCE, 0.0f));
        person.setDoB(new Timestamp(SP.getLong(USER_DATA + Person.DOB, new Date().getTime()), 0));
        person.setFirst_Name(SP.getString(USER_DATA + Person.FIRST_NAME, ""));
        person.setFcm_token(SP.getString(USER_DATA + Person.FCM_TOKEN, ""));
        person.setId(SP.getString(USER_DATA + Person.ID, ""));
        person.setJoined(SP.getInt(USER_DATA + Person.JOINED, 0));
        person.setLast_Name(SP.getString(USER_DATA + Person.LAST_NAME, ""));
        person.setMail(SP.getString(USER_DATA + Person.MAIL, ""));
        person.setMajor(SP.getString(USER_DATA + Person.MAJOR, ""));
        person.setMobile(SP.getString(USER_DATA + Person.MOBILE, ""));
        person.setPicture_path(SP.getString(USER_DATA + Person.PICTURE_PATH, ""));
        person.setPoB(SP.getString(USER_DATA + Person.POB, ""));
        person.setRank(SP.getString(USER_DATA + Person.RANK, ""));
        person.setUid(SP.getString(USER_DATA + Person.UID, ""));

        Map<String, Object> address = new HashMap<>();
        address.put(Address.CITY.toString(), SP.getString(USER_DATA + Person.ADDRESS_CITY, ""));
        address.put(Address.NUMBER.toString(), SP.getString(USER_DATA + Person.ADDRESS_NUMBER, ""));
        address.put(Address.STREET.toString(), SP.getString(USER_DATA + Person.ADDRESS_STREET, ""));
        address.put(Address.ZIP.toString(), SP.getString(Person.ADDRESS_ZIP_CODE, ""));
        person.setAddress(address);

        Map<String, Object> rankSettings = new HashMap<>();

        person.setRankSettings(rankSettings);

        return person;
    }

    public static void putCharge (@NonNull Charge charge) {
        SP.edit().putString(USER_CHARGE, charge.getName()).apply();
    }

    public static Charge getCharge(){
        return Charge.find(SP.getString(USER_CHARGE, Charge.NONE.toString()));
    }
}
