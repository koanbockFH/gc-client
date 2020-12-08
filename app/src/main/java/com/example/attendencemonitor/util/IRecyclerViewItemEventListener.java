package com.example.attendencemonitor.util;

/**
 * Recylcler Row event listener with custom events
 * @param <T> Type of model in recycler item
 */
public interface IRecyclerViewItemEventListener<T> {
    void onClick(T item);
    void onLongPress(T item);
    void onPrimaryClick(T item);
    void onSecondaryActionClick(T item);
}
