package com.mehmetakiftutuncu.quupnotifications.utilities.option;

public class Some<T> extends Option<T> {
    public Some(T value) {
        super(value);
    }

    @Override public T get() {
        return value;
    }

    @Override public String toString() {
        return "Some(" + value.toString() + ")";
    }
}
