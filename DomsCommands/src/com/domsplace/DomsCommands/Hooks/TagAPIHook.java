package com.domsplace.DomsCommands.Hooks;

import com.domsplace.DomsCommands.Bases.PluginHook;
import com.domsplace.DomsCommands.Listeners.TagAPILegacyListener;
import com.domsplace.DomsCommands.Listeners.TagAPIListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.kitteh.tag.TagAPI;

public class TagAPIHook extends PluginHook {
    private TagAPILegacyListener listenerLegacy;
    private TagAPIListener listener;
    
    public TagAPIHook() {
        super("TagAPI");
        this.shouldHook(true);
    }
    
    public TagAPI getTagAPI() {
        return (TagAPI) this.getHookedPlugin();
    }
    
    public void refreshTags(Player p) {
        if(!this.isHooked()) return;
        try {
            TagAPI.refreshPlayer(p);
        } catch(NoClassDefFoundError e) {
        }
    }

    public void refreshTags() {
        for(Player p : Bukkit.getOnlinePlayers()) {
            refreshTags(p);
        }
    }
    
    @Override
    public void onHook() {
        super.onHook();
        
        if(useLegacy()) {
            this.listenerLegacy = new TagAPILegacyListener();
        } else {
            this.listener = new TagAPIListener();
        }
    }
    
    public boolean useLegacy() {
        try {
            return !Class.forName("AsyncPlayerReceiveNameTagEvent").equals(null);
        } catch(Throwable t) {
        }
        return false;
    }
    
    @Override
    public void onUnhook() {
        super.onUnhook();
        if(this.listenerLegacy != null) {
            this.listenerLegacy.deRegisterListener();
            this.listenerLegacy = null;
        }
        
        if(this.listener != null) {
            this.listener.deRegisterListener();;
            this.listener = null;
        }
    }
}
