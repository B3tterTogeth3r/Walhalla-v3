package de.b3ttertogeth3r.walhalla.firebase;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

import de.b3ttertogeth3r.walhalla.interfaces.MyCompleteListener;

public class DynamicLinks {
    private static final String TAG = "DynamicLinks";
    public static FirebaseDynamicLinks DYNAMIC_LINKS;

    public static void getLink (Intent intent, MyCompleteListener<Void> listener) {
        DYNAMIC_LINKS.getDynamicLink(intent).addOnSuccessListener(pendingDynamicLinkData -> {
            try {
                if (pendingDynamicLinkData.getLink() != null) {
                    Uri deepLink = pendingDynamicLinkData.getLink();
                    if (deepLink != null) {
                        Log.e(TAG, deepLink.getPath());
                    }
                }
            } catch (Exception ignored) {
            } finally {
                listener.onSuccess(null);
            }
        }).addOnFailureListener(exception -> {
            Crashlytics.error(TAG, ":getDynamicLink" +
                    ":onFailure", exception);
            listener.onSuccess(null);
        });
    }
}
