package me.codeflusher.gcalc.util;

import java.util.Objects;

public class Identifier {
    private final String id;

    public Identifier(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Identifier that = (Identifier) object;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
