package de.b3ttertogeth3r.walhalla.annos;

import de.b3ttertogeth3r.walhalla.enums.Rank;

public @interface Ranks {
    Rank kind () default Rank.ACTIVE;
}
