package com.domsplace.DomsCommands.Bases;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DomsEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    
    public void fireEvent() {Bukkit.getPluginManager().callEvent(this);}
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
 
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
