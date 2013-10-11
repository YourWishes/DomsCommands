package com.domsplace.DomsCommands.DataManagers;

import com.domsplace.DomsCommands.Bases.DataManager;
import com.domsplace.DomsCommands.Enums.ManagerType;
import java.io.IOException;
import java.io.InputStream;
import org.bukkit.configuration.file.YamlConfiguration;

public class PluginManager extends DataManager {
    private YamlConfiguration plugin;
    
    public PluginManager() {
        super(ManagerType.PLUGIN);
    }
    
    @Override
    public void tryLoad() throws IOException {
        if(!getDataFolder().exists()) getDataFolder().mkdir();
        InputStream is = getPlugin().getResource("plugin.yml");
        plugin = YamlConfiguration.loadConfiguration(is);
        is.close();
    }
    
    public YamlConfiguration getYML() {
        return this.plugin;
    }

    public String getVersion() {
        return plugin.getString("version");
    }
}
