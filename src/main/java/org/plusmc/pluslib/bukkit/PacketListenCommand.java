package org.plusmc.pluslib.bukkit;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.plusmc.pluslib.bukkit.managed.PlusCommand;

import java.util.Collections;
import java.util.List;

public class PacketListenCommand implements PlusCommand {
    @Override
    public String getName() {
        return "packetlisten";
    }

    @Override
    public String getPermission() {
        return "packetlisten";
    }

    @Override
    public String getUsage() {
        return "/packetlisten";
    }

    @Override
    public String getDescription() {
        return "listen to packets";
    }

    @Override
    public List<String> getCompletions(int index) {
        return Collections.emptyList();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player))
            return false;

        //inject into netty and listen to packets
        CraftPlayer craftPlayer = (CraftPlayer) player;
        Channel channel = craftPlayer.getHandle().b.a.m;
        channel.pipeline().addBefore("packet_handler", "packet_listener", new ChannelDuplexHandler() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                System.out.println("channelRead: " + msg);
                super.channelRead(ctx, msg);
            }
        });
        player.sendMessage("listening to packets");


        return true;
    }
}
