package org.plusmc.pluslib;

import org.plusmc.pluslib.commands.PlusCommand;
import org.plusmc.pluslib.item.PlusItem;
import org.plusmc.pluslib.tickable.Tickable;

/**
 * Registers all the commands, items, and tickables to their respectable managers.
 */
@SuppressWarnings("unused")
public class GeneralManager {
    /**
     * Registers all the objects to their respectable manager.
     *
     * @param object The object to register.
     */
    public static void register(Object object) {
        if (object instanceof PlusItem item)
            PlusItemManager.register(item);

        if (object instanceof PlusCommand command)
            PlusCommandManager.register(command);

        if (object instanceof Tickable tickable)
            TickingManager.register(tickable);
    }
}
