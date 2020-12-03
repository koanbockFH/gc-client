package com.example.attendencemonitor.util;

public interface IRecyclerViewItemEventListener<T> {
    void onClick(T item);
    void onLongPress(T item);
    void onPrimaryClick(T item);
    void onSecondaryActionClick(T item);
}
