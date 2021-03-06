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

import com.domsplace.DomsCommands.Bases.Base;
import static com.domsplace.DomsCommands.Bases.Base.ChatDefault;
import com.domsplace.DomsCommands.Bases.DataManager;
import com.domsplace.DomsCommands.Enums.ManagerType;
import com.domsplace.DomsCommands.Objects.DomsItem;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import com.domsplace.DomsCommands.Objects.Kit;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

/**
 * @author      Dominic
 * @since       11/10/2013
 */
public class ConfigManager extends DataManager {
    private YamlConfiguration config;
    private File configFile;
    
    public ConfigManager() {
        super(ManagerType.CONFIG);
    }
    
    public YamlConfiguration getCFG() {
        return config;
    }
    
    @Override
    public void tryLoad() throws IOException {
        this.configFile = new File(getDataFolder(), "config.yml");
        if(!this.configFile.exists()) configFile.createNewFile();
        this.config = YamlConfiguration.loadConfiguration(configFile);
        
        /*** GENERATE DEFAULT CONFIG ***/
        df("debug", false);
        df("onlineupdates", true);
        
        List<String> cmds = new ArrayList<String>();
        cmds.add("kick");
        cmds.add("warn");
        cmds.add("ban");
        cmds.add("mute");
        df("commands.overriden", cmds);
        
        df("colors.default", "&7");
        df("colors.important", "&9");
        df("colors.error", "&c");
        df("colors.nickname.tablist", true);
        
        df("messages.firstjoin.broadcast", "&d{DISPLAYNAME} has joined {SERVER} for the first time! Please make them feel welcome!");
        df("messages.firstjoin.message", "&7Please be sure to read the rules, &9{NAME}&7!\n&7And most importantly, have fun!");
        
        df("messages.login.broadcast", "&e{DISPLAYNAME}&e has arrived.");
        df("messages.login.message", "&9Players Online &7{NUMPLAYERS}/{TOTALPLAYERS}:\n&7{PLAYERS}");
        df("messages.logout.broadcast", "&e{DISPLAYNAME}&e has departed.");
        df("messages.kicked.broadcast", "&e{DISPLAYNAME}&e was kicked.");
        
        df("messages.shutdown.kickmessage", Bukkit.getServer().getShutdownMessage());
        
        //df("messages.achievements.broadcast", "{text\"{DISPLAYNAME} earned the achievement \",extra:[{text:\"{ACHIEVEMENT}\",hoverEvent:{action:show_achievement,value:\"{ACHIEVEMENTID}\"}}]}");
        
        cmds = new ArrayList<String>();
        cmds.add("village admin save");
        df("messages.shutdown.commands", cmds);
        
        df("messages.motd", Bukkit.getMotd());
        
        df("teleport.safe", true);
        
        df("away.autoaway", 300);
        df("away.autokick", 600);
        df("away.message", "&9{DISPLAYNAME} &7is now Away.");
        df("away.messageback", "&9{DISPLAYNAME} &7has returned.");
        df("away.kickmessage", "Away too long!");
        df("away.commands.cancel", true);
        
        List<String> commands = new ArrayList<String>();
        commands.add("msg");
        commands.add("ping");
        df("away.commands.blocked", commands);
        
        commands = new ArrayList<String>();
        commands.add("say");
        commands.add("broadcast");
        df("punishment.mute.blockedcommands", commands);
        
        commands = new ArrayList<String>();
        for(World w : Bukkit.getWorlds()) {
            commands.add(w.getName());
        }
        df("inventory.groups.default", commands);
        
        df("commands.help.usecustom", true);
        df("chat.usechat", true);
        
        df("joinkit", "default");
        
        df("scoreboards.enabled", true);
        
        if(!config.contains("kits")) {
            df("kits.default.name", "Default");
            df("kits.default.item.item0", "{size:\"1\"},{id:\"273\"}");
            df("kits.default.item.item1", "{size:\"1\"},{id:\"274\"}");
            df("kits.default.item.item2", "{size:\"1\"},{id:\"275\"}");
            df("kits.default.item.item3", "{size:\"1\"},{id:\"291\"}");
            df("kits.default.cooldown", 1200);
        }
        
        if(!config.contains("votifier.commands")) {
            cmds = new ArrayList<String>();
            cmds.add("give {PLAYER} {id:\"IRON_INGOT\"} 50");
            cmds.add("broadcast Thank you to {PLAYER} for Voting!");
            df("votifier.commands", cmds);
        }
        
        //De-register old values
        for(Kit k : Kit.getKits()) {
            k.deRegister();
        }
        
        //Store Values
        Base.DebugMode = this.config.getBoolean("debug", false);
        
        Base.ChatDefault = loadColor("default");
        Base.ChatImportant = loadColor("important");
        Base.ChatError = loadColor("error");
        
        for(String s : ((MemorySection) config.get("kits")).getKeys(false)) {
            String key = "kits." + s + ".";
            try {
                String name = config.getString(key + "name");
                long cooldown = -1;
                if(config.contains(key + "cooldown")) cooldown = config.getLong(key + "cooldown");
                
                Kit kit = new Kit(name);
                kit.setCooldown(cooldown);
                
                for(String i : ((MemorySection) config.get(key + "item")).getKeys(false)) {
                    List<DomsItem> items = DomsItem.createItems(config.getString(key + "item." + i));
                    kit.addItems(items);
                }
            } catch(Exception e) {
                error("Failed to load kit \"" + s + "\".", e);
            }
        }
        
        //Save Data
        this.trySave();
    }
    
    @Override
    public void trySave() throws IOException {
        this.config.save(configFile);
    }
    
    private void df(String key, Object o) {
        if(config.contains(key)) return;
        config.set(key, o);
    }
    
    private String gs(String key) {
        return gs(key, "");
    }
    
    private String gs(String key, String dv) {
        if(!config.contains(key)) return dv;
        return config.getString(key);
    }
    
    private String loadColor(String key) {
        return colorise(gs("colors." + key, "&7"));
    }
    
    public String[] format(DomsPlayer player, String msg) {
        String[] parts = msg.split("\\n");
        
        for(int i = 0; i < parts.length; i++) {
            String p = parts[i];
            
            p = format(p);
            p = p.replaceAll("\\{NAME\\}", player.getUsername());
            p = p.replaceAll("\\{DISPLAYNAME\\}", player.getDisplayName());
            
            parts[i] = Base.colorise(p);
        }
        
        return parts;
    }
    
    public String format(Player player, String msg) {
        msg = format(msg);
        msg = msg.replaceAll("\\{NAME\\}", player.getName());
        msg = msg.replaceAll("\\{DISPLAYNAME\\}", player.getDisplayName());

        return Base.colorise(msg);
    }
    
    public String format(String msg) {
        List<DomsPlayer> list = DomsPlayer.getVisibleOnlinePlayers();
        msg = msg.replaceAll("\\{SERVER\\}", Bukkit.getServerName());
        msg = msg.replaceAll("\\{NUMPLAYERS\\}", "" + list.size());
        msg = msg.replaceAll("\\{TOTALPLAYERS\\}", "" + Bukkit.getServer().getMaxPlayers());
        
        String m = "";
        for(int i = 0; i < list.size(); i++) {
            DomsPlayer player = list.get(i);
            String n = player.getDisplayName();
            if(player.isAFK()) n = ChatDefault + "[Away] " + n;
            m += ChatDefault + n + ChatDefault;
            if(i < (list.size() - 1)) {
                m += ", ";
            }
        }
        msg = msg.replaceAll("\\{PLAYERS\\}", m);
        msg = msg.replaceAll("\\\\n", "\n");
        
        return msg;
    }
}
