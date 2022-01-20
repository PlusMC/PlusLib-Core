package org.plusmc.pluslib.managers;

import org.plusmc.pluslib.plus.PlusCommand;
import org.plusmc.pluslib.plus.PlusItem;
import org.plusmc.pluslib.plus.Tickable;

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
