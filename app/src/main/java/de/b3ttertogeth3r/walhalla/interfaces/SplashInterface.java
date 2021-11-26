package de.b3ttertogeth3r.walhalla.interfaces;

/**
 * @author B3tterTogeth3r
 * @since 1.0
 * @version 1.0
 * @implNote <b>DO NOT IMPLEMENT ANYWHERE EXCEPT {@link de.b3ttertogeth3r.walhalla.SplashActivity SPLASHACTIVITY}</b>
 * @implSpec <b>ONLY IMPLEMENTED BY {@link de.b3ttertogeth3r.walhalla.SplashActivity SPLASHACTIVITY}</b>
 */
public interface SplashInterface {
    /** only use in the constructor of {@link de.b3ttertogeth3r.walhalla.App App} */
    void appDone();
    /** only use in the constructor of {@link de.b3ttertogeth3r.walhalla.firebase.Firebase Firebase} */
    void firebaseDone();
}
