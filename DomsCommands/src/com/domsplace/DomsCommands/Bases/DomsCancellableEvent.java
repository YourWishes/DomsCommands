package com.domsplace.DomsCommands.Bases;

import org.bukkit.event.Cancellable;

public class DomsCancellableEvent extends DomsEvent implements Cancellable {
    private boolean isCancelled = false;
    
    public DomsCancellableEvent() {
    }
    
    @Override
    public boolean isCancelled() {return isCancelled;}
    @Override
    public void setCancelled(boolean result) {this.isCancelled = result;}
}
