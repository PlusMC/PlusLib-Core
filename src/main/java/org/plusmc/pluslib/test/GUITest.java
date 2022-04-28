package org.plusmc.pluslib.test;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.plusmc.pluslib.managed.PlusCommand;

import java.util.List;

public class GUITest implements PlusCommand {

    @Override
    public String getName() {
        return "GUITest";
    }

    @Override
    public String getPermission() {
        return "pluslib.test.gui";
    }

    @Override
    public String getUsage() {
        return "";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public List<String> getCompletions(int index) {
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player p) p.openInventory(new PageTestGUI().getInventory());
        return true;
    }
}
