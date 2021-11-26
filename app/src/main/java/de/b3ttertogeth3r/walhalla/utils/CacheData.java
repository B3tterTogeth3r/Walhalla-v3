package de.b3ttertogeth3r.walhalla.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.annotations.NotNull;

import org.jetbrains.annotations.Contract;

import de.b3ttertogeth3r.walhalla.R;
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
    private static final String TAG = "CacheData";
    public static final String USER_DATA = "user_data";
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
        SP.edit().putStringSet(USER_DATA, user.getSet()).apply();
    }

    @NonNull
    @Contract(" -> new")
    public static Person getUser() throws PersonException {
        return new Person(SP.getStringSet(USER_DATA, null));
    }
}
