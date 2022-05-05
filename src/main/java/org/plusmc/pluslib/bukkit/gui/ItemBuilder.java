package org.plusmc.pluslib.bukkit.gui;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ItemBuilder {

    private final ItemStack item;
    private final ItemMeta meta;

    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
        this.meta = this.item.getItemMeta();
    }

    public ItemBuilder(Random random) {
        Material material = Material.values()[random.nextInt(Material.values().length)];
        this.item = new ItemStack(material);
        this.meta = this.item.getItemMeta();
    }

    public ItemStack build() {
        item.setItemMeta(meta);
        return this.item.clone();
    }

    public ItemBuilder setAmount(final int amount) {
        item.setAmount(amount);
        return this;
    }

    public ItemBuilder setName(final String name) {
        meta.setDisplayName(name);
        return this;
    }

    public ItemBuilder setLore(final String... lore) {
        meta.setLore(Arrays.asList(lore));
        return this;
    }

    public ItemBuilder setLore(final List<String> lore) {
        meta.setLore(lore);
        return this;
    }

    public ItemBuilder addEnchant(final Enchantment enchantment, final int level) {
        meta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemBuilder setSkullOwner(final OfflinePlayer owner) {
        if(!(meta instanceof SkullMeta skullMeta)) {
            throw new IllegalStateException("Cannot set skull owner on non-skull item");
        }
        skullMeta.setOwningPlayer(owner);
        return this;
    }


    public ItemBuilder setUnbreakable(final boolean unbreakable) {
        meta.setUnbreakable(unbreakable);
        return this;
    }
}
