package com.domsplace.DomsCommands.Enums;

import com.domsplace.DomsCommands.Bases.DomsEnum;

public class ManagerType extends DomsEnum {
    public static final ManagerType CONFIG = new ManagerType("Configuration");
    public static final ManagerType PLUGIN = new ManagerType("Plugin");
    public static final ManagerType WARP = new ManagerType("Warp");
    
    //Instance
    private String type;
    
    public ManagerType(String type) {
        this.type = type;
    }
    
    public String getType() {
        return this.type;
    }
}
