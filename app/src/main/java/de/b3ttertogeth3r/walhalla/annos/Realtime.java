package de.b3ttertogeth3r.walhalla.annos;

import de.b3ttertogeth3r.walhalla.enums.RealtimePath;

public @interface Realtime {
    RealtimePath kind () default RealtimePath.MEMBER_COUNTER_TOTAL;
}
