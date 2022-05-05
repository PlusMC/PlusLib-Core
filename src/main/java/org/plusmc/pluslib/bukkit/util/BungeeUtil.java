package org.plusmc.pluslib.bukkit.util;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;
import org.plusmc.pluslib.bukkit.PlusLib;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Level;

/**
 * A utility class for BungeeCord.
 */
@SuppressWarnings("unused")
public class BungeeUtil implements PluginMessageListener {
    public static final String BUNGEE_CORD = "BungeeCord";
    private static HashMap<String, ServerInfo> serverInfo = new HashMap<>();
    private static HashMap<String, PlayerList> playerList = new HashMap<>();
    private static boolean isPlusMC = false;

    /**
     * Creates a new BungeeUtil.
     * Do not use this constructor as it is only for internal use.
     */

    public static boolean isPlusMC() {
        return isPlusMC;
    }

    /**
     * Sends a player to a server
     *
     * @param player Player to send
     * @param server Server to send to
     */
    public static void connectServer(Player player, String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);

        player.sendPluginMessage(PlusLib.getInstance(), BUNGEE_CORD, out.toByteArray());
    }

    /**
     * Sends player to a server
     *
     * @param name   Name of the player to send
     * @param server Server to send to
     */
    public static void connectServer(String name, String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("ConnectOther");
        out.writeUTF(name);
        out.writeUTF(server);

        PlusLib.getInstance().getServer().sendPluginMessage(PlusLib.getInstance(), BUNGEE_CORD, out.toByteArray());
    }

    /**
     * Updates the server info
     *
     * @param server Server name
     */
    public static void updateServer(String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PlayerList");
        out.writeUTF(server);
        PlusLib.getInstance().getServer().sendPluginMessage(PlusLib.getInstance(), BUNGEE_CORD, out.toByteArray());

        out = ByteStreams.newDataOutput();
        out.writeUTF("ServerIP");
        out.writeUTF(server);
        PlusLib.getInstance().getServer().sendPluginMessage(PlusLib.getInstance(), BUNGEE_CORD, out.toByteArray());
    }

    public static void checkPlusMC() {
        PlusLib.logger().info("Checking if PlusMCBungee is running...");
        PlusLib.getInstance().getServer().sendPluginMessage(PlusLib.getInstance(), "plusmc:bungee", "CheckPlusMC".getBytes());
    }

    /**
     * Gets the server info
     *
     * @param server Server name
     * @return {@link ServerInfo} of the server
     */
    public static ServerInfo getInfo(String server) {
        return serverInfo.getOrDefault(server, new ServerInfo(server, "", 0, false));
    }

    /**
     * Gets the player list
     *
     * @param server Server name
     * @return {@link PlayerList} of the server
     */
    public static PlayerList getPlayerList(String server) {
        return playerList.getOrDefault(server, new PlayerList(server, new String[]{}, 0));
    }

    public void onPluginMessageReceived(String channel, @NotNull Player player, byte[] message) {
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        if (!channel.equals(BUNGEE_CORD) && !channel.equals("plusmc:bungee")) return;
        String subchannel = in.readUTF();
        switch (subchannel) {
            case "PlayerList" -> handlePlayerList(in);
            case "ServerIP" -> handleServerIP(in);
            case "CheckPlusMC" -> handleCheckPlusMC();
            default -> PlusLib.logger().log(Level.WARNING, "Unknown subchannel: {0}", subchannel);
        }
    }

    private void handlePlayerList(ByteArrayDataInput in) {
        try {
            String server = in.readUTF();
            String[] players = in.readUTF().split(", ");
            players = players.length == 1 && players[0].equals("") ? new String[0] : players;
            BungeeUtil.playerList.put(server, new PlayerList(server, players, players.length));
        } catch (Exception e) {
            //ignore
        }
    }

    private void updateOnline(ServerInfo info) {
        Bukkit.getScheduler().runTaskAsynchronously(PlusLib.getInstance(), () -> {
            InetSocketAddress address = new InetSocketAddress(info.ip(), info.port());
            Socket socket = new Socket();
            try {
                socket.connect(address, 500);
                socket.close();
                ServerInfo info1 = new ServerInfo(info.server(), info.ip(), info.port(), true);
                serverInfo.put(info.server(), info1);
            } catch (Exception e) {
                ServerInfo info1 = new ServerInfo(info.server(), info.ip(), info.port(), false);
                serverInfo.put(info.server(), info1);
            }
        });
    }

    private void handleServerIP(ByteArrayDataInput in) {
        try {
            String server = in.readUTF();
            String ip = in.readUTF();
            int port = in.readUnsignedShort();
            serverInfo.put(server, new ServerInfo(server, ip, port, false));
            updateOnline(serverInfo.get(server));
        } catch (Exception e) {
            //ignore
        }
    }


    private static void handleCheckPlusMC() {
        PlusLib.getInstance().getLogger().info("Using PlusMC Bungee!");
        isPlusMC = true;
    }

    /**
     * A class to store server info
     * ServerInfo stores the server name, ip, port, and if the server is online
     */
    public record ServerInfo(String server, String ip, int port, boolean online) {
    }

    /**
     * A class to store player list
     * PlayerList stores the server name, player list, and the amount of players
     */
    public record PlayerList(String server, String[] playerList, int playerCount) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PlayerList that = (PlayerList) o;
            return server.equals(that.server);
        }

        @Override
        public int hashCode() {
            return Objects.hash(server);
        }

        @Override
        public String toString() {
            return server + ": " + Arrays.toString(playerList);
        }
    }
}
