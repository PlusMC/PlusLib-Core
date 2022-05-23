package org.plusmc.pluslib.bukkit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.plusmc.pluslib.bukkit.managed.PlusCommand;
import org.plusmc.pluslib.bukkit.voicechat.PlusLibVoicechat;

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

        PlusLibVoicechat.getInstance().createAudioPlayer(player, PlusLibBukkit.SOUND_TEST).startPlaying();
        return false;
    }
}
