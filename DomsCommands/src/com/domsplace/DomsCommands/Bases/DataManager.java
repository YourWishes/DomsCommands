/*
 * Copyright 2013 Dominic.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.domsplace.DomsCommands.Bases;

import com.domsplace.DomsCommands.DataManagers.*;
import com.domsplace.DomsCommands.Enums.ManagerType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author      Dominic
 * @since       11/10/2013
 */
public class DataManager extends Base {
    private static final List<DataManager> MANAGERS = new ArrayList<DataManager>();
    
    public static final PluginManager PLUGIN_MANAGER = new PluginManager();
    public static final ConfigManager CONFIG_MANAGER = new ConfigManager();
    public static final PlayerManager PLAYER_MANAGER = new PlayerManager();
    public static final WarpManager WARP_MANAGER = new WarpManager();
    public static final ChatManager CHAT_MANAGER = new ChatManager();
    public static final CraftBukkitManager CRAFT_BUKKIT_MANAGER = new CraftBukkitManager();
    public static final RulesManager RULES_MANAGER = new RulesManager();
    public static final HelpManager HELP_MANAGER = new HelpManager();
    
    private static void registerManager(DataManager manager) {
        DataManager.MANAGERS.add(manager);
    }
    
    public static List<DataManager> getManagers() {
        return new ArrayList<DataManager>(MANAGERS);
    }
    
    public static boolean loadAll() {
        for(DataManager dm : MANAGERS) {
            if(dm.load()) continue;
            return false;
        }
        
        return true;
    }
    
    public static boolean saveAll() {
        for(DataManager dm : MANAGERS) {
            if(dm.getType().equals(ManagerType.CONFIG)) continue;
            if(dm.save()) continue;
            return false;
        }
        
        return true;
    }
    
    //Instance
    private ManagerType type;
    
    public DataManager(ManagerType type) {
        this.type = type;
        
        DataManager.registerManager(this);
    }
    
    public ManagerType getType() {
        return this.type;
    }
    
    public boolean load() {
        try {
            tryLoad();
            return true;
        } catch(IOException e) {
            error("Failed to load " + this.getType().getType(), e);
            return false;
        }
    }
    
    public void tryLoad() throws IOException {
    }
    
    public boolean save() {
        try {
            trySave();
            return true;
        } catch(Exception e) {
            error("Failed to save " + this.getType().getType(), e);
            return false;
        }
    }
    
    public void trySave() throws IOException {
    }
}
