package org.plusmc.pluslib.bukkit.test;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.plusmc.pluslib.bukkit.managed.PlusCommand;
import org.plusmc.pluslib.bukkit.util.BungeeUtil;

import java.util.Collections;
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
        return Collections.emptyList();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        BungeeUtil.checkPlusMC();
        return true;
    }
}
