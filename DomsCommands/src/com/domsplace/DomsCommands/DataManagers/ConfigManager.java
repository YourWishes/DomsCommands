package com.domsplace.DomsCommands.DataManagers;

import com.domsplace.DomsCommands.Bases.Base;
import com.domsplace.DomsCommands.Bases.DataManager;
import com.domsplace.DomsCommands.Enums.ManagerType;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

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
        
        List<String> cmds = new ArrayList<String>();
        cmds.add("kick");
        cmds.add("warn");
        cmds.add("ban");
        cmds.add("mute");
        df("commands.overriden", cmds);
        
        df("colors.default", "&7");
        df("colors.important", "&9");
        df("colors.error", "&c");
        
        df("messages.firstjoin.broadcast", "&d{DISPLAYNAME} has joined {SERVER} for the first time! Please make them feel welcome!");
        df("messages.firstjoin.message", "&7Please be sure to read the rules, &9{NAME}&7!\n&7And most importantly, have fun!");
        
        df("messages.login.broadcast", "&e{DISPLAYNAME} has arrived.");
        df("messages.login.message", "&9Players Online &7{NUMPLAYERS}/{TOTALPLAYERS}:\n&7{PLAYERS}");
        df("messages.logout.broadcast", "&e{DISPLAYNAME} has departed.");
        df("messages.kicked.broadcast", "&e{DISPLAYNAME} was kicked.");
        
        df("messages.shutdown.kickmessage", Bukkit.getServer().getShutdownMessage());
        
        cmds = new ArrayList<String>();
        cmds.add("village admin save");
        df("messages.shutdown.commands", cmds);
        
        df("messages.motd", Bukkit.getMotd());
        
        df("teleport.safe", true);
        
        //Store Values
        Base.DebugMode = this.config.getBoolean("debug", false);
        
        Base.ChatDefault = loadColor("default");
        Base.ChatImportant = loadColor("important");
        Base.ChatError = loadColor("error");
        
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
}
