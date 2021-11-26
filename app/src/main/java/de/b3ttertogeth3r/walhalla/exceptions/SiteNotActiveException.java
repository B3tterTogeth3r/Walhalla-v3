package de.b3ttertogeth3r.walhalla.exceptions;

import android.util.Log;

public class SiteNotActiveException extends Exception {

    public SiteNotActiveException(){
        super();
        Log.e("SiteNotActiveException", "Site not active anymore");
    }
}
