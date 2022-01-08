package de.b3ttertogeth3r.walhalla;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.FragmentManager;
import android.content.Context;
import android.util.Log;

import androidx.appcompat.widget.Toolbar;

import java.util.Stack;

import de.b3ttertogeth3r.walhalla.utils.CacheData;

/**
 * Created by B3tterTogeth3r on 27.07.2021.
 *
 * @author B3tterTogeth3r
 * @version 1.0
 * @see Stack
 * @see android.app.Application
 * @see android.content.Context
 * @since 1.0
 */
@SuppressLint("StaticFieldLeak")
public class App extends Application {
    private static final String TAG = "App";
    private static Context ctx;
    private static boolean internetConnection = true;
    private static FragmentManager fm;

    public App () {
        App.ctx = getContext();
    }

    public static void init () {
        try {
            StartActivity.newDone.appDone();
        } catch (Exception e) {
            Log.e(TAG, "init: error", e);
        }
    }

    public static Context getContext () {
        return ctx;
    }

    public static void setContext (Context ctx) {
        App.ctx = ctx;
    }


    /**
     * @return true, if the app has an active internet connection
     */
    public static boolean isInternetConnection () {
        return internetConnection;
    }

    /**
     * @param internetConnection
     *         true, if the app has an active internet connection
     */
    public static void setInternetConnection (boolean internetConnection) {
        App.internetConnection = internetConnection;
    }

    public static FragmentManager getFragmentManager () {
        return fm;
    }

    public static void setFragmentManager (FragmentManager manager) {
        fm = manager;
    }

    @Override
    public void onCreate () {
        super.onCreate();
        App.ctx = this;
    }
}
