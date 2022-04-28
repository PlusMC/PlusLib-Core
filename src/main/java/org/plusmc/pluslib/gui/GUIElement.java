package org.plusmc.pluslib.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class GUIElement {
    private ItemStack item;
    private final Consumer<InventoryClickEvent> action;

    public GUIElement(ItemStack item, Consumer<InventoryClickEvent> action) {
        this.item = item;
        this.action = action;
    }

    public ItemStack getItem() {
        return item.clone();
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    @Nullable
    public Consumer<InventoryClickEvent> getAction() {
        return action;
    }
}
