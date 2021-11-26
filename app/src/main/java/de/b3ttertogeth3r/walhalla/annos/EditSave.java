package de.b3ttertogeth3r.walhalla.annos;

import de.b3ttertogeth3r.walhalla.enums.Display;

public @interface EditSave {
    Display kind () default Display.SHOW;
}
