package de.b3ttertogeth3r.walhalla.interfaces;

import de.b3ttertogeth3r.walhalla.enums.Editable;

/**
 * The interface is to notify the class which created the acompaning dialog, textview, button,
 * edittext or fields like that.
 *
 * @author B3tterTogeth3r
 * @version 1.0
 * @since 1.11
 */
public interface EditListener {
    /**
     * notifies the holder with the data to save and which kind it is of.
     *
     * @param value
     *         Object of the result
     * @param editable
     *         Editable for more than one field, to format data corecctly
     */
    void saveEdit (Object value, Editable editable);

    /**
     * notifies the holder the input is aborted and all changed data should be reset.
     */
    void abort ();

    /**
     * notifies the holder the input is aborted and all changed data should be reset.
     *
     * @param editable
     *         Editable where the error occurred
     * @param s
     *         error message
     */
    void sendError (Editable editable, String s);

    /**
     * notify holder about live changes
     *
     * @param string
     *         value
     * @param editable
     *         Editable for more than one field to change the correct value.
     */
    void sendLiveChange (String string, Editable editable);
}
