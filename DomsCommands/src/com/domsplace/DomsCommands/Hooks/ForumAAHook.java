package com.domsplace.DomsCommands.Hooks;

import com.domsplace.DomsCommands.Bases.PluginHook;
import java.lang.reflect.Field;
import me.furt.forumaa.ForumAA;
import me.furt.forumaa.SQLQuery;

public class ForumAAHook extends PluginHook {
    public ForumAAHook() {
        super("ForumAA");
        this.shouldHook(true);
    }
    
    public SQLQuery getSQLQuery() {
        try {
            Field f = ((ForumAA) this.getHookedPlugin()).getClass().getDeclaredField("sqlDB");
            f.setAccessible(true);
            return (SQLQuery) f.get((ForumAA) this.getHookedPlugin());
        } catch(Exception e) {return null;} catch(Error e) {return null;}
    }
    
    @Override
    public void onHook() {
        super.onHook();
    }
    
    @Override
    public void onUnhook() {
        super.onUnhook();
    }
}