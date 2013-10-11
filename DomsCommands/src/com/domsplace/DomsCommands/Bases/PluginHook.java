package com.domsplace.DomsCommands.Bases;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class PluginHook extends Base{
    //Constants
    private static final List<PluginHook> PLUGIN_HOOKS = new ArrayList<PluginHook>();
    
    
    
    //Static
    private static void hookPlugin(PluginHook hook) {
        PLUGIN_HOOKS.add(hook);
    }
    
    public static PluginHook getHookFromPlugin(Plugin plugin) {
        for(PluginHook hook : PLUGIN_HOOKS) {
            if(hook == null) continue;
            if(hook.getPluginName().equals(plugin.getName())) return hook;
        }
        
        return null;
    }
    
    public static void hookAll() {
        for(PluginHook plugin : PLUGIN_HOOKS) {
            debug("Try Hoooking " + plugin.getPluginName());
            if(!plugin.shouldHook()) continue;
            if(!plugin.hook()) log("Failed to hook into " + plugin.getPluginName());
        }
    }
    
    public static void unhookAll() {
        for(PluginHook plugin : PLUGIN_HOOKS) {
            debug("Unhoooking " + plugin.getPluginName());
            plugin.unHook();
        }
    }
    
    //Instance
    private String pluginName;
    private Plugin plugin;
    private boolean shouldHook = false;
    
    public PluginHook(String plugin) {
        this.pluginName = plugin;
        hookPlugin(this);
    }
    
    public String getPluginName() {return this.pluginName;}
    public Plugin getHookedPlugin() {return this.plugin;}
    
    public void onUnhook() {log("Unhooked from " + this.pluginName);}
    public void onHook() {log("Hooked into " + this.pluginName);}
    
    public boolean isHooked() {return this.plugin != null;}
    
    public boolean shouldHook() {return this.shouldHook;}
    public void shouldHook(boolean t) {this.shouldHook = t;}
    
    public boolean hook() {
        if(!shouldHook) return false;
        try {
            this.plugin = tryHook();
            if(this.plugin != null) {
                this.onHook();
                return true;
            } else return false;
        } catch(Exception e) {
            this.plugin = null;
            return false;
        } catch(NoClassDefFoundError e) {
            this.plugin = null;
            return false;
        }
    }
    
    public Plugin tryHook() throws NoClassDefFoundError {
        Plugin p = Bukkit.getPluginManager().getPlugin(this.pluginName);
        
        if(p == null || !p.isEnabled()) return null;
        
        return p;
    }
    
    
    public void unHook() {
        if(!this.isHooked()) return;
        this.onUnhook();
        this.plugin = null;
    }
}
