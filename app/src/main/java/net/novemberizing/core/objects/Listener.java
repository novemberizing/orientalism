package net.novemberizing.core.objects;

public interface Listener<T> {
    void on(T value);
}
