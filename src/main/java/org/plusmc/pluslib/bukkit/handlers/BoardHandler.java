package org.plusmc.pluslib.bukkit.handlers;

import org.bukkit.plugin.java.JavaPlugin;
import org.plusmc.pluslib.bukkit.managed.PlusBoard;
import org.plusmc.pluslib.bukkit.managed.Tickable;
import org.plusmc.pluslib.bukkit.managing.BaseManager;
import org.plusmc.pluslib.bukkit.managing.TickingManager;

import java.util.*;

public class BoardHandler implements Tickable {
    private final List<PlusBoard> boards = new ArrayList<>();

    public BoardHandler(JavaPlugin plugin) {
        BaseManager.createManager(TickingManager.class, plugin);
        BaseManager.registerAny(this, plugin);
    }

    public void addBoard(PlusBoard board) {
        board.load();
        boards.add(board);
    }

    public void removeBoard(PlusBoard board) {
        boards.remove(board);
        board.unload();
    }

    @Override
    public void tick(long tick) {
        for (PlusBoard board : boards) {
            if (board.isRunning())
                board.tick(tick);
        }
    }
}
