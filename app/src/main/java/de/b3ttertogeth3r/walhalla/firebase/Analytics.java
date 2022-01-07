package de.b3ttertogeth3r.walhalla.firebase;

import static com.google.firebase.analytics.FirebaseAnalytics.UserProperty.ALLOW_AD_PERSONALIZATION_SIGNALS;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

import de.b3ttertogeth3r.walhalla.App;
import de.b3ttertogeth3r.walhalla.utils.CacheData;

/**
 * @see <a href="https://firebase.google.com/docs/analytics">Analytics</a>
 */
public class Analytics {
    public static final String TAG = "Analytics";

    /**
     * Log the used sites of the user to see which sites are used more often and which are not
     * to enable better usage of ads on the pages.
     *
     * @param menu_id
     *         id of the clicked menu item
     * @param fragment_name
     *         name of the fragment the user opened
     */
    public static void screenChange (int menu_id, String fragment_name) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, String.valueOf(menu_id));
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "fragment." + fragment_name);
        Firebase.ANALYTICS.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
    }

    /**
     * Set the users custom start page as user property
     */
    public static void changeStartPage () {
        Firebase.ANALYTICS.setUserProperty("start_page", String.valueOf(CacheData.getStartPage()));
    }

    /**
     * @see
     * <a href="https://firebase.google.com/docs/analytics/configure-data-collection?platform=android">Data
     * Collection</a>
     */
    public static void changeDataCollection () {
        boolean value = CacheData.getAnalyticsCollection();
        CacheData.changeAnalyticsCollection(value);
        Firebase.MESSAGING.setAutoInitEnabled(value);
        Firebase.ANALYTICS.setUserProperty(ALLOW_AD_PERSONALIZATION_SIGNALS, "" + value);
        Firebase.ANALYTICS.setAnalyticsCollectionEnabled(value);
    }

    public static void setRank (String rank) {
        Firebase.ANALYTICS.setUserProperty(FirebaseAnalytics.Param.GROUP_ID, rank);
    }
}
