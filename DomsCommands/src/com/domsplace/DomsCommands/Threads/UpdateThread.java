package com.domsplace.DomsCommands.Threads;

import com.domsplace.DomsCommands.Bases.Base;
import com.domsplace.DomsCommands.Bases.DomsThread;
import com.domsplace.DomsCommands.DataManagers.PluginManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;


public class UpdateThread extends DomsThread {
    public static String CheckUpdateURL = "https://api.curseforge.com/servermods/files?projectIds=68249";
    public static String LatestVersionURL = "http://dev.bukkit.org/bukkit-plugins/domscommands/";
    //public static String APIKey = "";
    
    public UpdateThread() {
        super(10, 3600, true);
    }
    
    @Override
    public void run() {
        if(!getConfig().getBoolean("onlineupdates", true)) return;
        //Download JSON
        Base.debug("Checking for updates...");
        try {
            URL url = new URL(CheckUpdateURL);
            
            URLConnection urlCon = url.openConnection();
            //TODO: Add User API Key
            //urlCon.addRequestProperty("X-API-Key", APIKey);
            
            urlCon.addRequestProperty("User-Agent", getPlugin().getName() + "/v"
                    + PluginManager.PLUGIN_MANAGER.getVersion() + " (by " + 
                    PluginManager.PLUGIN_MANAGER.getAuthor() + ")");
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
            String response = reader.readLine();
            
            JSONArray array = (JSONArray) JSONValue.parse(response);
            JSONObject latestFile = (JSONObject) array.get(array.size() - 1);
            
            String name = (String) latestFile.get("name");
            name = name.toLowerCase();
            name = name.replaceAll("(?i)" + getPlugin().getName(), "");
            name = name.replaceAll("version", "");
            name = name.replaceAll(" ", "");
            name = name.replaceAll("v", "");
            
            double onlineVersion = getDouble(name);
            double thisVersion = getDouble(PluginManager.PLUGIN_MANAGER.getVersion());
            
            Base.debug("This Version: " + thisVersion);
            Base.debug("Online Version: " + onlineVersion);
            
            if(thisVersion >= onlineVersion) {
                Base.debug("No Updates Required!");
                return;
            }
            
            broadcast("Villages.admin", new String[]{
                ChatImportant + "The new Version of " + getPlugin().getName() + " is available to download!",
                "Download " + getPlugin().getName() + " v" + onlineVersion + " from: " + LatestVersionURL
            });
            this.stopThread();
        } catch(Exception e) {
            Base.error("Failed to check for updates.", e);
            this.stopThread();
        }
    }
}
