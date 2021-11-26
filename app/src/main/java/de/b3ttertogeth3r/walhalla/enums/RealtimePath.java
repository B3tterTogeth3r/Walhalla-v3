package de.b3ttertogeth3r.walhalla.enums;

import androidx.annotation.NonNull;

public enum RealtimePath {
    MEMBER_COUNTER_TOTAL("counter/members"),
    MEMBER_COUNTER_ONLINE("counter/members/online"),
    MEMBER_COUNTER_OFFLINE("counter/members/offline"),
    SEND_PAYMENT_REMINDER("reminder/send");
    private final String name;

    RealtimePath (String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString () {
        return name;
    }
}
