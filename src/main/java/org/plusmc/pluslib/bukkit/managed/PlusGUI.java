package org.plusmc.pluslib.bukkit.managed;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.plusmc.pluslib.bukkit.gui.GUIElement;

import java.util.*;

public abstract class PlusGUI implements Loadable, InventoryHolder {
    private final Map<Integer, GUIElement> elements;
    private final Inventory inventory;

    public PlusGUI() {
        this.elements = new HashMap<>();
        this.inventory = this.createInventory();
        draw();
    }

    public Collection<GUIElement> getElements() {
        return this.elements.values();
    }

    public @NotNull Inventory getInventory() {
        return this.inventory;
    }


    protected abstract Inventory createInventory();

    public void setElement(final GUIElement element, final int index) {
        this.elements.put(index, element);
    }

    public void moveElement(final int from, final int to) {
        final GUIElement element = this.elements.remove(from);
        this.elements.put(to, element);
    }

    @Nullable
    public GUIElement getElement(final int index) {
        return this.elements.get(index);
    }

    public void removeElement(final int index) {
        this.elements.remove(index);
    }

    public void clearElements() {
        this.elements.clear();
    }

    protected void setElements(final Map<Integer, GUIElement> elements) {
        this.elements.clear();
        this.elements.putAll(elements);
    }

    public void draw() {
        this.getInventory().clear();
        for (final Map.Entry<Integer, GUIElement> entry : this.elements.entrySet()) {
            this.getInventory().setItem(entry.getKey(), entry.getValue().getItem());
        }
    }
}
