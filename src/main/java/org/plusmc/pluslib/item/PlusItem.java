package org.plusmc.pluslib.item;

import org.bukkit.Material;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.plusmc.pluslib.PlusItemManager;

import java.util.Arrays;

/**
 * Makes it easier to create custom items.
 */
@SuppressWarnings("unused")
public interface PlusItem {
    /**
     * @return The ID of the item
     */
    String getID();

    /**
     * @return The name of the item
     */
    String getName();

    /**
     * @return The lore of the item
     */
    String[] getLore();

    /**
     * @return The material of the item
     */
    Material getMaterial();

    /**
     * Called when the plugin is unloaded
     */
    default void unload() {
        //ignore
    }

    /**
     * Called when the plugin is loaded
     */
    default void load() {
        //ignore
    }

    /**
     * Called when the item is used to damage an entity
     *
     * @param event The event
     */
    default void onDamageEntity(EntityDamageByEntityEvent event) {
        //ignore
    }

    /**
     * Called when the item is used on an entity
     *
     * @param event The event
     */
    default void onInteractEntity(PlayerInteractEntityEvent event) {
        //ignore
    }

    /**
     * Called when the item is used on a block
     *
     * @param event The event
     */
    default void onInteractBlock(PlayerInteractEvent event) {
        //ignore
    }

    /**
     * Called when the block is right-clicked as a block
     *
     * @param event The event
     */
    default void onInteractAsBlock(PlayerInteractEvent event) {
        //ignore
    }

    /**
     * Called when the item is placed
     *
     * @param event The event
     */
    default void onBlockPlace(BlockPlaceEvent event) {
        event.setCancelled(true);
    }

    /**
     * Gets the item stack of the item
     *
     * @return The item stack
     */
    default ItemStack getItem() {
        ItemStack stack = new ItemStack(getMaterial());
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(this.getName());
        meta.setLore(Arrays.asList(this.getLore()));
        meta.getPersistentDataContainer().set(PlusItemManager.PLUS_ITEM_KEY, PersistentDataType.STRING, this.getID());
        stack.setItemMeta(meta);
        return stack;
    }
}
