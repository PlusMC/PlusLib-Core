package org.plusmc.pluslib.bukkit.test;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.plusmc.pluslib.bukkit.gui.GUIElement;
import org.plusmc.pluslib.bukkit.gui.ItemBuilder;
import org.plusmc.pluslib.bukkit.managed.PlusGUI;

import java.util.Random;

public class TestGUI extends PlusGUI {
    private static final Random RANDOM = new Random();

    @Override
    protected Inventory createInventory() {
        ItemStack item = new ItemBuilder(Material.STONE).setName("Test").setLore("This is a test item it will make a noise on click", "it will also move to a random slot").build();
        GUIElement element = new GUIElement(item,event -> {
            if(event.getWhoClicked() instanceof Player player) player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
            moveElement(event.getSlot(), RANDOM.nextInt(getInventory().getSize()));
            draw();
        });
        this.setElement(element, 0);


        return Bukkit.createInventory(this, 9, "TestGUI");
    }
}
