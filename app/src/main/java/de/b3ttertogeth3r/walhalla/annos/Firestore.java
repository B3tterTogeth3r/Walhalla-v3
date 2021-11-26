package de.b3ttertogeth3r.walhalla.annos;

import de.b3ttertogeth3r.walhalla.enums.FirestorePath;

public @interface Firestore {
    FirestorePath kind () default FirestorePath.IMAGES;
}
