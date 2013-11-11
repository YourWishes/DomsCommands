package com.domsplace.DomsCommands.Hooks;

import com.domsplace.DomsCommands.Bases.PluginHook;
import com.domsplace.DomsCommands.Listeners.VotifierListener;

public class VotifierHook extends PluginHook {
    private VotifierListener listener;
    
    public VotifierHook() {
        super("Votifier");
        this.shouldHook(true);
    }
    
    @Override
    public void onHook() {
        super.onHook();
        this.listener = new VotifierListener();
    }
    
    @Override
    public void onUnhook() {
        super.onUnhook();
        if(this.listener != null) {
            this.listener.deRegisterListener();
            this.listener = null;
        }
    }
}