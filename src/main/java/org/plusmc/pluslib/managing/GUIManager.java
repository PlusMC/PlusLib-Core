package org.plusmc.pluslib.managing;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.plusmc.pluslib.gui.GUIElement;
import org.plusmc.pluslib.managed.Loadable;
import org.plusmc.pluslib.managed.PlusGUI;

public class GUIManager extends BaseManager{
    private static boolean isInitialized = false;

    protected GUIManager(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    Class<? extends Loadable> getManaged() {
        return PlusGUI.class;
    }

    //todo: add a listener for guielement clicks and add list of open guis

    //only use register if you want to use the "load" method
    @Override
    void register(Loadable loadable) {
    }

    @Override
    void unregister(Loadable loadable) {
        loadable.unload();
    }

    @Override
    protected void init() {
        if(isInitialized) return;
        Bukkit.getPluginManager().registerEvents(new Listener(), getPlugin());
        isInitialized = true;
    }

    @Override
    protected void shutdown() {
        isInitialized = false;
    }

    private static class Listener implements org.bukkit.event.Listener {

        @EventHandler
        public void onInventoryClick(InventoryClickEvent event) {
            if(event.getClickedInventory() == null) return;
            if(!(event.getClickedInventory().getHolder() instanceof PlusGUI gui)) return;
            event.setCancelled(true);
            GUIElement element = gui.getElement(event.getSlot());
            if(element == null) return;
            if(element.getAction() == null) return;
            element.getAction().accept(event);
        }
    }
}
