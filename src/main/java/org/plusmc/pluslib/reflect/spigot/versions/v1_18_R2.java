package org.plusmc.pluslib.reflect.spigot.versions;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.game.PacketPlayOutCamera;
import net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.nio.ByteBuffer;


public class v1_18_R2 extends VersionReflect {

    @Override
    public void setCamera(Player player, Entity entity) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        if (!player.equals(entity)) {
            PacketPlayOutNamedEntitySpawn packet = new PacketPlayOutNamedEntitySpawn(craftPlayer.getHandle());
            PacketDataSerializer packetData = new PacketDataSerializer(ByteBufAllocator.DEFAULT.buffer());
            packet.a(packetData);
            packetData.d(entity.getEntityId());
            packet = new PacketPlayOutNamedEntitySpawn(packetData);

            craftPlayer.getHandle().b.a(packet);
        }
        craftPlayer.getHandle().b.a(new PacketPlayOutCamera(((CraftEntity) entity).getHandle()));

    }
}
