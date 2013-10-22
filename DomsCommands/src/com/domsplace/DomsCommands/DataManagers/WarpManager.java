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

import static com.domsplace.DomsCommands.Bases.Base.getDataFolder;
import com.domsplace.DomsCommands.Bases.DataManager;
import com.domsplace.DomsCommands.Enums.ManagerType;
import com.domsplace.DomsCommands.Objects.DomsLocation;
import com.domsplace.DomsCommands.Objects.Warp;
import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @author      Dominic
 * @since       11/10/2013
 */
public class WarpManager extends DataManager {
    private File warpsFile;
    private YamlConfiguration yml;
    
    public WarpManager() {
        super(ManagerType.WARP);
    }
    
    @Override
    public void tryLoad() throws IOException {
        warpsFile = new File(getDataFolder(), "warps.yml");
        if(!warpsFile.exists()) warpsFile.createNewFile();
        
        yml = YamlConfiguration.loadConfiguration(warpsFile);
        
        //Clear old Warps
        for(Warp w : Warp.getWarpsAlphabetically()) {
            if(w == null) continue;
            w.deRegister();
            w = null;
        }
        
        for(String s : yml.getKeys(false)) {
            DomsLocation loc = DomsLocation.guessLocation(yml.getString(s + ".pos", "ERROR"));
            if(loc == null) {
                log("Location for warp \"" + s + "\" contains an error.");
                continue;
            }
            
            String name = yml.getString(s + ".name", "1");
            if(name.equals("1")) {
                log("Name for warp \"" + s + "\" contains an error.");
                continue;
            }
            
            Warp w = new Warp(name, loc);
        }
        
        
    }
    
    @Override
    public void trySave() throws IOException {
        warpsFile = new File(getDataFolder(), "warps.yml");
        if(warpsFile.exists()) warpsFile.delete();
        warpsFile.createNewFile();
        
        yml = YamlConfiguration.loadConfiguration(warpsFile);
        
        for(Warp w : Warp.getWarpsAlphabetically()) {
            yml.set(w.getName() + ".pos", w.getLocation().toString());
            yml.set(w.getName() + ".name", w.getName());
        }
        
        yml.save(warpsFile);
    }
}
