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
import com.domsplace.DomsCommands.Objects.DomsChannel;
import com.domsplace.DomsCommands.Objects.DomsChatFormat;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @author      Dominic
 * @since       11/10/2013
 */
public class ChatManager extends DataManager {
    private File chatFile;
    private YamlConfiguration yml;
    
    public ChatManager() {
        super(ManagerType.CHAT);
    }
    
    @Override
    public void tryLoad() throws IOException {
        chatFile = new File(getDataFolder(), "chat.yml");
        boolean n = false;
        if(!chatFile.exists()) {
            chatFile.createNewFile();
            n = true;
        }
        
        yml = YamlConfiguration.loadConfiguration(chatFile);
        
        if(!yml.contains("main.format")) yml.set("main.format", "&8{DISPLAYNAME}&7: &f{MESSAGE}");
        if(!yml.contains("main.groups.Admin") && n) yml.set("main.groups.OP", "&4[&c{PREFIX}&c{GROUP}&c{SUFFIX}&4] &8{NAME}&7: &f{MESSAGE}");
        
        if(!yml.contains("server.format")) yml.set("server.format", "&d[{DISPLAYNAME}&d] &d{MESSAGE}");
        
        if(!yml.contains("broadcast.format")) {
            yml.set("broadcast.format", "&c[&4Broadcast&c] &6{MESSAGE}");
            yml.set("broadcast.permission", "DomsCommands.broadcast");
            List<String> commands = new ArrayList<String>();
            commands.add("broadcast");
            commands.add("bc");
            yml.set("broadcast.commands", commands);
        }
        
        if(n) {
            yml.set("AdminChat.format",
                    "{CHANNEL}[{PREFIX}{GROUP}{SUFFIX}] [{WORLD}|{GAMEMODE}] [{DISPLAYNAME}/{NAME}] SAID:&r {MESSAGE}");
            yml.set("AdminChat.chatpermission", "DomsCommands.adminchat");
            yml.set("AdminChat.receivepermission", "DomsCommands.receiveadminchat");
        }
        
        for(DomsChannel channel : DomsChannel.getChannels()) {
            channel.deRegister();
        }
        
        //Load Data
        for(String s : yml.getKeys(false)) {
            if(!yml.contains(s + ".format")) {
                log("Can't setup chat format \"" + s + "\", missing default format.");
                continue;
            }
            
            String format = yml.getString(s + ".format");
            String permission = "";
            boolean ipriv = false;
            
            if(yml.contains(s + ".chatpermission")) permission = yml.getString(s + ".chatpermission");
            if(yml.contains(s + ".private")) ipriv = yml.getBoolean(s + ".private");
            
            DomsChatFormat dformat = new DomsChatFormat("", format);
            
            List<String> commands = new ArrayList<String>();
            if(yml.contains(s + ".commands")) commands = yml.getStringList(s + ".commands");
            
            DomsChannel channel = new DomsChannel(s, permission, dformat, ipriv, commands);
            
            if(yml.contains(s + ".receivepermission")) channel.setReceivePermission(yml.getString(s + ".receivepermission"));
            
            if(yml.contains(s + ".groups")) {
                MemorySection ms = (MemorySection) yml.get(s + ".groups");
                for(String g : ms.getKeys(false)) {
                    DomsChatFormat nform = new DomsChatFormat(g, yml.getString(s + ".groups." + g));
                    channel.addGroupFormat(nform);
                }
            }
        }
        
        this.trySave();
    }
    
    @Override
    public void trySave() throws IOException {
        chatFile = new File(getDataFolder(), "chat.yml");
        if(!chatFile.exists()) chatFile.createNewFile();
        yml.save(chatFile);
    }
}
