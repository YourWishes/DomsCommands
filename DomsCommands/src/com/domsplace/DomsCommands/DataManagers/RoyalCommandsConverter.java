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
import com.domsplace.DomsCommands.Objects.DomsItem;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;

/**
 * @author      Dominic
 * @since       11/10/2013
 */
public class RoyalCommandsConverter extends DataManager {
    private YamlConfiguration config;
    private File configFile;
    
    public RoyalCommandsConverter() {
        super(ManagerType.CONFIG);
    }
    
    public YamlConfiguration getCFG() {
        return config;
    }
    
    @Override
    public void tryLoad() throws IOException {
        debug("Loading RoyalCommands...");
        this.configFile = new File(getDataFolder(), "royalconfig.yml");
        if(!this.configFile.exists()) configFile.createNewFile();
        this.config = YamlConfiguration.loadConfiguration(configFile);
        if(this.config == null) return;
        debug("Parsing..");
        for(String s : ((MemorySection) config.get("kits")).getKeys(false)) {
            debug("Parsing kit " + s);
            List<String> items = config.getStringList("kits." + s + ".items");
            List<String> enchants = config.getStringList("kits." + s + ".enchantments");
            List<String> names = config.getStringList("kits." + s + ".names");
            List<String> lore = config.getStringList("kits." + s + ".lore");
            for(int i = 0; i < items.size(); i++) {
                String item = items.get(i);
                String[] parts = item.split(":");
                int id = getInt(parts[0]);
                int amount = getInt(parts[1]);
                short data = 0;
                if(parts.length > 2) {
                    data = getShort(parts[2]);
                }
                
                DomsItem di = new DomsItem(id);
                di.setData(data);
                
                try {
                    String[] enc = enchants.get(i).split(",");
                    for(String e : enc) {
                        String[] ep = e.split(":");
                        Enchantment ench = Enchantment.getByName(ep[0].toUpperCase());
                        int lvl = getInt(ep[1]);
                        di.addEnchantment(ench, lvl);
                    }
                } catch(Exception e) {}
                
                try {
                    String[] enc = names.get(i).split(",");
                    for(String e : enc) {
                        di.setName(e);
                    }
                } catch(Exception e) {}
                
                try {
                    String[] enc = lore.get(i).split(",");
                    for(String e : enc) {
                        di.addLore(e);
                    }
                } catch(Exception e) {}
                
                if(config.contains("kits." + s + ".cooldown")) getConfig().set("kits." + s + ".cooldown", config.getLong("kits." + s + ".cooldown"));
                getConfig().set("kits." + s + ".item.item" + i, "{size:\"" + amount + "\"}," + di.toString());
            }
        }
        
        getConfigManager().save();
        //Save Data
        this.trySave();
    }
    
    @Override
    public void trySave() throws IOException {
        this.config.save(configFile);
    }
}
