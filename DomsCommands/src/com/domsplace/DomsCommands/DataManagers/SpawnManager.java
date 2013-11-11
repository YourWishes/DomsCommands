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

package com.domsplace.DomsCommands.DataManagers;

import com.domsplace.DomsCommands.Bases.DataManager;
import com.domsplace.DomsCommands.Enums.ManagerType;
import com.domsplace.DomsCommands.Objects.DomsLocation;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @author      Dominic
 * @since       9/11/2013
 */
public class SpawnManager extends DataManager {
    private File file;
    private YamlConfiguration yml;
    
    private Map<String, DomsLocation> spawns;
    
    public SpawnManager() {
        super(ManagerType.SPAWN);
    }
    
    @Override
    public void tryLoad() throws IOException {
        file = new File(getDataFolder(), "spawn.yml");
        if(!file.exists()) file.createNewFile();
        
        yml = YamlConfiguration.loadConfiguration(file);
        
        spawns = new HashMap<String, DomsLocation>();
        
        for(String key : yml.getKeys(false)) {
            String world = key;
            DomsLocation spawn = DomsLocation.guessLocation(yml.getString(key, ""));
            if(spawn == null) continue;
            spawns.put(world, spawn);
        }
        
        this.trySave();
    }
    
    @Override
    public void trySave() throws IOException {
        file = new File(getDataFolder(), "spawn.yml");
        if(!file.exists()) file.createNewFile();
        
        for(String s : this.spawns.keySet()) {
            yml.set(s, this.spawns.get(s).toString());
        }
        
        yml.save(file);
    }
    
    public DomsLocation getSpawn(String world) {
        DomsLocation x = null;
        for(String s : spawns.keySet()) {
            if(!s.equalsIgnoreCase(world)) continue;
            return spawns.get(world);
        }
        
        World w = Bukkit.getWorld(world);
        if(w == null) return x;
        return new DomsLocation(w.getSpawnLocation());
    }
    
    public void setSpawn(DomsLocation location, String world) {
        World w = Bukkit.getWorld(world);
        if(w != null) w.setSpawnLocation((int) location.getX(), (int) location.getY(), (int) location.getZ());
        spawns.put(world, location);
    }
}
