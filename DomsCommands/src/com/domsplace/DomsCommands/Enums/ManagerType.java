package com.domsplace.DomsCommands.Enums;

import com.domsplace.DomsCommands.Bases.DomsEnum;

public class ManagerType extends DomsEnum {
    public static final ManagerType CONFIG = new ManagerType("Configuration");
    public static final ManagerType PLUGIN = new ManagerType("Plugin");
    public static final ManagerType WARP = new ManagerType("Warp");
    public static final ManagerType PLAYER = new ManagerType("Player");
    public static final ManagerType CHAT = new ManagerType("Chat");
    public static final ManagerType CRAFT_BUKKIT = new ManagerType("Craftbukkit");
    public static final ManagerType RULES = new ManagerType("Rules");
    public static final ManagerType HELP = new ManagerType("Help");
    public static final ManagerType SPAWN = new ManagerType("Spawn");
    
    //Instance
    private String type;
    
    public ManagerType(String type) {
        this.type = type;
    }
    
    public String getType() {
        return this.type;
    }
}
