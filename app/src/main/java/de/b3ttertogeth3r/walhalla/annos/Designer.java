package de.b3ttertogeth3r.walhalla.annos;

import de.b3ttertogeth3r.walhalla.enums.Design;

public @interface Designer {
    Design kind () default Design.TEXT;
}
