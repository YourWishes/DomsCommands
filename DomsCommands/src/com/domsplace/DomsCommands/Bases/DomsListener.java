package com.domsplace.DomsCommands.Bases;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.event.Listener;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DomsListener extends Base implements Listener {
    private static final List<DomsListener> LISTENERS = new ArrayList<DomsListener>();
    
    protected static void regsiterListener(DomsListener listener) {
        Bukkit.getPluginManager().registerEvents(listener, getPlugin());
        DomsListener.getListeners().add(listener);
        debug("Registered Listener: " + listener.getClass().getSimpleName());
    }
    
    protected static void deRegsiterListener(DomsListener listener) {
        Method[] methods = listener.getClass().getMethods();
        for(Method m : methods) {
            EventHandler ev = m.getAnnotation(EventHandler.class);
            if(ev == null) continue;
            Class<?>[] params = m.getParameterTypes();
            for(Class<?> param : params) {
                if(param == null) continue;
                Class<? extends Event> e = param.asSubclass(Event.class);
                if(e == null) continue;
                try {
                    Method h = e.getMethod("getHandlerList");
                    HandlerList r = (HandlerList) h.invoke(null);
                    r.unregister(listener);
                } catch(Exception ex) {
                    continue;
                }
            }
        }
        DomsListener.getListeners().remove(listener);
        debug("De-Registered Listener: " + listener.getClass().getSimpleName());
    }
    
    public static List<DomsListener> getListeners() {
        return DomsListener.LISTENERS;
    }
    
    //Instance
    public DomsListener() {
        DomsListener.regsiterListener(this);
    }
    
    public void deRegisterListener() {
        DomsListener.deRegsiterListener(this);
    }
}
