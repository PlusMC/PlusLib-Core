package org.plusmc.pluslib.test;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.plusmc.pluslib.gui.GUIElement;
import org.plusmc.pluslib.gui.ItemBuilder;
import org.plusmc.pluslib.managed.PaginatedGUI;

import java.util.HashMap;
import java.util.Map;

public class PageTestGUI extends PaginatedGUI {


    @Override
    protected Inventory createInventory() {
        return Bukkit.createInventory(this, 54, "PageTestGUI");
    }


    @Override
    protected void createPages() {
        ItemStack item = new ItemBuilder(Material.ARROW).setName("Back").build();
        ItemStack item2 = new ItemBuilder(Material.ARROW).setName("Next").build();

        GUIElement back = new GUIElement(item, (event) -> {
            if(getPage() - 1 < 0) return;
            setPage(getPage() - 1, true);
        });

        GUIElement next = new GUIElement(item2, (event) -> {
            if(getPage() + 1 > getPageAmount()) return;
            setPage(getPage() + 1, true);
        });

        for(int i = 0; i < 10; i++) {
            Map<Integer, GUIElement> elements = new HashMap<>();
            for (int k = 0; k < 54; k++)
                elements.put(k,new GUIElement(new ItemBuilder(true).build(), null));

            elements.put(45, back);
            elements.put(53, next);
            addPage(i,elements);
        }
    }
}
