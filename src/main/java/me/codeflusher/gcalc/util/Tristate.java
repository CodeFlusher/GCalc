package me.codeflusher.gcalc.util;

public enum Tristate {
    RUN,
    AWAIT,
    STOP;

    public Tristate getInversed() {
        if (this == RUN) {
            return AWAIT;
        } else {
            return RUN;
        }
    }
}
