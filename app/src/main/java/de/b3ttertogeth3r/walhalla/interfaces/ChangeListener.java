package de.b3ttertogeth3r.walhalla.interfaces;

/**
 *
 * @author B3tterTogeth3r
 * @since 1.3
 * @param <T> model, that is changed
 */
public interface ChangeListener<T> {
    /**
     * @param change notify the listener, that a value in the model has changed
     */
    void change(T change);
}
