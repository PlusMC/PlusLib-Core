package org.plusmc.pluslib.bungee;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.Subscribe;
import manifold.ext.rt.api.Jailbreak;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.event.EventBus;
import net.md_5.bungee.event.EventHandlerMethod;
import org.bukkit.event.Event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

public class BungeeUtil {
    public static void registerWithPreChecks(Plugin plugin, Listener listener, BiConsumer<AtomicBoolean, Event> preChecks) {
        @Jailbreak PluginManager manager = ProxyServer.getInstance().getPluginManager();
        for (Method method : listener.getClass().getDeclaredMethods()) {
            Preconditions.checkArgument(!method.isAnnotationPresent(Subscribe.class),
                    "Listener %s has registered using deprecated subscribe annotation! Please update to @EventHandler.", listener);
        }
        registerToEventBus(manager.eventBus, listener, preChecks);
        manager.listenersByPlugin.put(plugin, listener);
    }

    private static void registerToEventBus(@Jailbreak EventBus eventBus, Listener listener, BiConsumer<AtomicBoolean, Event> preChecks) {
        Map<Class<?>, Map<Byte, Set<Method>>> handler = eventBus.findHandlers(listener);
        eventBus.lock.lock();
        try {
            for (Map.Entry<Class<?>, Map<Byte, Set<Method>>> e : handler.entrySet()) {
                Map<Byte, Map<Object, Method[]>> prioritiesMap = eventBus.byListenerAndPriority.computeIfAbsent(e.getKey(), k -> new HashMap<>());
                for (Map.Entry<Byte, Set<Method>> entry : e.getValue().entrySet()) {
                    Map<Object, Method[]> currentPriorityMap = prioritiesMap.computeIfAbsent(entry.getKey(), k -> new HashMap<>());
                    currentPriorityMap.put(listener, entry.getValue().toArray(new Method[0]));
                }
                bakeHandlers(eventBus, e.getKey(), preChecks);
            }
        } finally {
            eventBus.lock.unlock();
        }
    }

    private static void bakeHandlers(@Jailbreak EventBus eventBus, Class<?> eventClass, BiConsumer<AtomicBoolean, Event> preChecks) {
        Map<Byte, Map<Object, Method[]>> handlersByPriority = eventBus.byListenerAndPriority.get(eventClass);
        if (handlersByPriority != null) {
            List<EventHandlerMethod> handlersList = new ArrayList<>(handlersByPriority.size() * 2);

            byte value = Byte.MIN_VALUE;
            do {
                Map<Object, Method[]> handlersByListener = handlersByPriority.get(value);
                if (handlersByListener != null) {
                    for (Map.Entry<Object, Method[]> listenerHandlers : handlersByListener.entrySet()) {
                        for (Method method : listenerHandlers.getValue()) {
                            EventHandlerMethod ehm = new EventHandlerPreCheckMethod(listenerHandlers.getKey(), method, preChecks);
                            handlersList.add(ehm);
                        }
                    }
                }
            } while (value++ < Byte.MAX_VALUE);
            eventBus.byEventBaked.put(eventClass, handlersList.toArray(new EventHandlerMethod[0]));
        } else {
            eventBus.byEventBaked.remove(eventClass);
        }
    }


    private static class EventHandlerPreCheckMethod extends EventHandlerMethod {
        private final BiConsumer<AtomicBoolean, Event> preChecks;

        public EventHandlerPreCheckMethod(Object listener, Method method, BiConsumer<AtomicBoolean, Event> preChecks) {
            super(listener, method);
            this.preChecks = preChecks;
        }

        @Override
        public void invoke(Object event) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            AtomicBoolean preCheck = new AtomicBoolean(true);
            preChecks.accept(preCheck, (Event) event);
            if (preCheck.get()) {
                super.invoke(event);
            }
        }
    }

}
