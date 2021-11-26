package de.b3ttertogeth3r.walhalla.interfaces;

import de.b3ttertogeth3r.walhalla.enums.Editable;

public interface EditListener {
    void saveEdit (Object value, Editable editable);

    default void abort () {
    }

    void sendError (Editable editable, String s);

    default void sendLiveChange (String string, Editable editable) {
    }
}
