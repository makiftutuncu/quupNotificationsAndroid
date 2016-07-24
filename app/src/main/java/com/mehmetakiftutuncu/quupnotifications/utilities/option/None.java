package com.mehmetakiftutuncu.quupnotifications.utilities.option;

import java.util.NoSuchElementException;

public class None<T> extends Option<T> {
    public None() {
        super();
    }

    @Override public T get() {
        throw new NoSuchElementException("Called get() on a None object! Call isDefined() to check the existence of the value first.");
    }

    @Override public String toString() {
        return "None";
    }
}
