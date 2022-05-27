package org.plusmc.pluslib.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.plusmc.pluslib.bukkit.managed.PlusCommand;
import org.plusmc.pluslib.bukkit.voicechat.PlusLibVoicechat;
import org.plusmc.pluslib.reflect.spigot.PaperSpigotReflection;
import org.plusmc.pluslib.reflect.spigot.versions.VersionReflect;

import java.util.Collections;
import java.util.List;

public class TestCommand implements PlusCommand {
    @Override
    public String getName() {
        return "test";
    }

    @Override
    public String getPermission() {
        return "test";
    }

    @Override
    public String getUsage() {
        return "/test";
    }

    @Override
    public String getDescription() {
        return "Test command";
    }

    @Override
    public List<String> getCompletions(int index) {
        return Collections.emptyList();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player))
            return false;

        ArmorStand armorStand = player.getWorld().spawn(player.getLocation(), ArmorStand.class);
        armorStand.setVisible(false);
        armorStand.setVelocity(player.getVelocity().setY(10));
        VersionReflect versionReflect = PaperSpigotReflection.getVersionReflect();
        versionReflect.setCamera(player, armorStand);
        Bukkit.getScheduler().runTaskLater(PlusLibBukkit.getInstance(), ()-> {
            versionReflect.setCamera(player, player);
            armorStand.remove();
        }, 100);

        return false;
    }
}
