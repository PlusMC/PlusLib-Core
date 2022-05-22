package org.plusmc.pluslib.bukkit.handlers;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.plusmc.pluslib.bukkit.gui.GUIElement;
import org.plusmc.pluslib.bukkit.managed.PlusGUI;

public class GUIHandler {
    private static boolean registered = false;
    private final JavaPlugin plugin;

    public GUIHandler(JavaPlugin plugin) {
        this.plugin = plugin;
        registerListener(plugin);
    }



    private static void registerListener(JavaPlugin plugin) {
        if (!registered)
            Bukkit.getPluginManager().registerEvents(new InventoryListener(), plugin);
        registered = true;
    }

    private static class InventoryListener implements Listener {

        @EventHandler
        public void onInventoryClick(InventoryClickEvent event) {
            if (event.getClickedInventory() == null) return;
            if (!(event.getClickedInventory().getHolder() instanceof PlusGUI gui)) return;
            event.setCancelled(true);
            GUIElement element = gui.getElement(event.getSlot());
            if (element == null) return;
            if (element.getAction() == null) return;
            element.getAction().accept(event);
        }
    }
}
