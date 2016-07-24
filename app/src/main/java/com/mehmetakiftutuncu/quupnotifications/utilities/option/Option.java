package com.mehmetakiftutuncu.quupnotifications.utilities.option;

public abstract class Option<T> {
    protected final T value;
    public final boolean isDefined;
    public final boolean isEmpty;

    protected Option() {
        this(null);
    }

    protected Option(T value) {
        this.value = value;
        this.isDefined = value != null;
        this.isEmpty = !isDefined;
    }

    public abstract T get();

    public T getOrElse(T defaultValue) {
        return isDefined ? value : defaultValue;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Option<?> option = (Option<?>) o;

        return value != null ? value.equals(option.value) : option.value == null;

    }

    @Override public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
