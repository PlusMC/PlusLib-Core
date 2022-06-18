package org.plusmc.pluslib.bukkit.managed;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.plusmc.pluslib.bukkit.gui.GUIElement;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class PlusGUI implements Loadable, InventoryHolder {
    private final Map<Integer, GUIElement> elements;
    @NotNull
    private Inventory inventory;

    protected PlusGUI(boolean generate) {
        this.elements = new HashMap<>();
        if (generate) {
            inventory = this.createInventory();
            draw();
        } else inventory = Bukkit.createInventory(this, 9, "Placeholder");
    }

    protected abstract Inventory createInventory();

    public void draw() {
        this.getInventory().clear();
        for (final Map.Entry<Integer, GUIElement> entry : this.elements.entrySet()) {
            this.getInventory().setItem(entry.getKey(), entry.getValue().getItem());
        }
    }

    public @NotNull Inventory getInventory() {
        return this.inventory;
    }

    protected PlusGUI() {
        this.elements = new HashMap<>();
        inventory = this.createInventory();
        draw();
    }

    protected void regenerateInventory() {
        this.inventory = this.createInventory();
        draw();
    }

    public Collection<GUIElement> getElements() {
        return this.elements.values();
    }

    protected void setElements(final Map<Integer, GUIElement> elements) {
        this.elements.clear();
        this.elements.putAll(elements);
    }

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
}
