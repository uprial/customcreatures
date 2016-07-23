package com.gmail.uprial.customcreatures.schema.numerics;

public class ValueConst<T> implements IValue<T> {
    private final T value;

    public ValueConst(T value) {
        this.value = value;
    }

    @Override
    public T getValue() {
        return value;
    }

    public String toString() {
        return getValue().toString();
    }
}

