package com.mrmelon54.DraggableLists;

/** Data supplied only by reorderable entries, excluding headers and LAN servers. */
public interface DragItem<T> {
    T draggable_lists$getUnderlyingData();
    default boolean draggable_lists$canDrag() { return true; }
}
