package com.mrmelon54.DraggableLists;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import com.mojang.blaze3d.platform.cursor.CursorTypes;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.input.MouseButtonEvent;
import java.util.List;
import java.util.ArrayList;
import java.util.function.BiConsumer;

/** Drag state follows real entry bounds, including variable-height headers. */
public final class DragManager<E extends ObjectSelectionList.Entry<E>> {
    private static DragManager<?> active;
    private final ObjectSelectionList<E> list;
    private final BiConsumer<DragItem<?>, DragItem<?>> move;
    private E selected;
    private double offsetY;
    private double mouseY;
    private long lastFrame;

    public DragManager(ObjectSelectionList<E> list, BiConsumer<DragItem<?>, DragItem<?>> move) {
        this.list = list;
        this.move = move;
    }

    public boolean isDragging() { return selected != null; }

    public boolean mouseDragged(MouseButtonEvent event) {
        if (event.button() != 0) return false;
        mouseY = event.y();
        if (selected != null) return true;
        if (!list.isMouseOver(event.x(), event.y()) || event.x() < list.getRowLeft() + 34
            || event.x() >= list.getRowLeft() + list.getRowWidth()) return false;
        for (E entry : list.children()) {
            if (entry.isMouseOver(event.x(), event.y()) && entry instanceof DragItem<?> item && item.draggable_lists$canDrag()) {
                if (active != null && active != this) active.cancel();
                active = this;
                selected = entry;
                offsetY = event.y() - entry.getY();
                lastFrame = System.nanoTime();
                list.setDragging(true);
                Cursor.setDragging();
                return true;
            }
        }
        return false;
    }

    public boolean mouseReleased(MouseButtonEvent event) {
        if (selected == null || event.button() != 0) return false;
        E source = selected;
        E target = targetAt(event.y());
        cancel();
        if (target != null && source != target) move.accept((DragItem<?>)source, (DragItem<?>)target);
        return true;
    }

    public static void cancelActive() { if (active != null) active.cancel(); }

    public void cancel() {
        if (active == this) active = null;
        selected = null;
        list.setDragging(false);
        Cursor.reset();
    }

    private E targetAt(double y) {
        E nearest = null;
        double distance = Double.MAX_VALUE;
        List<E> entries = list.children();
        int start = entries.indexOf(selected), end = start;
        if (start < 0) return null;
        while (start > 0 && entries.get(start - 1) instanceof DragItem<?> item && item.draggable_lists$canDrag()) start--;
        while (end + 1 < entries.size() && entries.get(end + 1) instanceof DragItem<?> item && item.draggable_lists$canDrag()) end++;
        for (E entry : entries.subList(start, end + 1)) {
            double d = Math.abs(y - (entry.getY() + entry.getHeight() / 2.0));
            if (d < distance) { distance = d; nearest = entry; }
        }
        return nearest;
    }

    public void renderListItems(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta,
                                java.util.function.Consumer<E> render) {
        if (selected != null && !list.children().contains(selected)) cancel();
        this.mouseY = mouseY;
        List<E> entries = list.children();
        List<E> preview = new ArrayList<>(entries);
        if (selected != null) {
            E target = targetAt(mouseY);
            if (target != null) {
                int destination = preview.indexOf(target);
                preview.remove(selected);
                preview.add(destination, selected);
            }
        }
        for (int i = 0; i < preview.size(); i++) {
            E entry = preview.get(i);
            if (entry == selected) continue;
            int oldY = entry.getY();
            int previewY = entries.get(i).getY();
            entry.setY(previewY);
            if (entry.getY() + entry.getHeight() >= list.getY() && entry.getY() <= list.getBottom()) render.accept(entry);
            entry.setY(oldY);
        }
    }

    public void renderOverlay(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        if (selected == null) return;
        graphics.requestCursor(CursorTypes.RESIZE_NS);
        int oldY = selected.getY();
        int y = (int)Math.max(list.getY() + 2, Math.min(mouseY - offsetY, list.getBottom() - selected.getHeight() - 2));
        selected.setY(y);
        graphics.fill(selected.getX(), y, selected.getX() + selected.getWidth(), y + selected.getHeight(), 0x60999999);
        selected.extractContent(graphics, mouseX, mouseY, false, delta);
        selected.setY(oldY);
        long now = System.nanoTime();
        double seconds = Math.min(0.05, (now - lastFrame) / 1_000_000_000.0);
        lastFrame = now;
        if (mouseY < list.getY() + 16) list.setScrollAmount(list.scrollAmount() - seconds * 160);
        else if (mouseY > list.getBottom() - 16) list.setScrollAmount(list.scrollAmount() + seconds * 160);
    }
}
