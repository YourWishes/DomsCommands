/*
 * Copyright 2013 Dominic Masters.
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

package com.domsplace.DomsCommands.Objects;

import com.domsplace.DomsCommands.Bases.Base;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Dominic Masters
 */
public class DomsChannel {
    private static final List<DomsChannel> REGISTERED_CHANNELS = new ArrayList<DomsChannel>();
    
    private static final DomsChannel FALLBACK_CHANNEL = new DomsChannel("ERROR", "", new DomsChatFormat("", "&c[&4ERROR&c] &6{DISPLAYNAME}&7: &f{MESSAGE}"), false);
    
    public static DomsChannel getPlayersChannel(DomsPlayer player) {
        for(DomsChannel c : REGISTERED_CHANNELS) {
            if(c.hasPlayer(player)) return c;
        }
        
        DomsChannel chan = DomsChannel.getChannel("main");
        if(chan != null) return chan;
        return FALLBACK_CHANNEL;
    }
    
    public static void setPlayersChannel(DomsPlayer player, DomsChannel channel) {
        getPlayersChannel(player).removePlayer(player);
        channel.addPlayer(player);
    }

    public static DomsChannel getChannel(String main) {
        for(DomsChannel c : REGISTERED_CHANNELS) {
            if(c.name.equalsIgnoreCase(main)) return c;
        }
        return null;
    }
    
    public static List<DomsChannel> getChannels() {return new ArrayList<DomsChannel>(REGISTERED_CHANNELS);}
    
    //Instance
    private String name;
    private String perm;
    private DomsChatFormat defaultFormat;
    private boolean isprivate;
    private List<DomsPlayer> players;
    private List<DomsChatFormat> chatFormats;
    private Map<String, String> variables;
    
    public DomsChannel (String name, String permission, DomsChatFormat format, boolean isp) {
        this.name = name;
        this.perm = permission;
        this.defaultFormat = format;
        this.isprivate = isp;
        this.players = new ArrayList<DomsPlayer>();
        this.chatFormats = new ArrayList<DomsChatFormat>();
        this.variables = new HashMap<String, String>();
        
        this.variables.put("CHANNEL", this.name);
        
        REGISTERED_CHANNELS.add(this);
    }
    
    public String getName() {return this.name;}
    public String getPermission() {return this.perm;}
    public DomsChatFormat getDefaultFormat() {return this.defaultFormat;}
    public List<DomsPlayer> getPlayers() {return new ArrayList<DomsPlayer>(this.players);}
    public Map<String, String> getVariables() {return new HashMap<String, String>(this.variables);}
    public String getVariable(String key) {return this.variables.get(key);}
    
    public void setDefaultFormat(DomsChatFormat format) {this.defaultFormat = format;}
    public void setPrivate(boolean b) {this.isprivate = b;}
    
    public boolean hasPlayer(DomsPlayer player) {return this.players.contains(player);}
    
    public boolean isPrivate() {return this.isprivate;}
    
    public void removePlayer(DomsPlayer player) {this.players.remove(player);}
    public void removeVariable(String key) {this.variables.remove(key);}
    
    public void addPlayer(DomsPlayer player) {this.players.add(player);}
    public void addVariable(String key, String obj) {this.variables.put(key, obj);}
    public void addGroupFormat(DomsChatFormat format) {this.chatFormats.add(format);}
    
    public DomsChatFormat getFormat(DomsPlayer player) {
        if(player.isConsole()) return this.defaultFormat;
        return this.getGroupFormat(player.getGroup());
    }
    
    public DomsChatFormat getGroupFormat(String group) {
        for(DomsChatFormat format : this.chatFormats) {
            if(format.getGroup().equalsIgnoreCase(group)) return format;
        }
        return this.defaultFormat;
    }
    
    public void deRegister() {if(!this.equals(DomsChannel.FALLBACK_CHANNEL)) REGISTERED_CHANNELS.remove(this);}
    
    public void chat(DomsPlayer player, String[] args) {this.chat(player, Base.arrayToString(args, " "));}
    public void chat(DomsPlayer player, String message) {this.chat(player, this.getFormat(player), message);}
    public void chat(DomsPlayer player, DomsChatFormat format, String message) {
        //TODO: Finish
        String msgFormat = format.getFormat();
        msgFormat = Base.colorise(msgFormat);
        
        for(String key : this.variables.keySet()) {
            msgFormat = msgFormat.replaceAll("\\{" + key + "\\}", Base.colorise(this.variables.get(key)));
        }
        
        Map<String, String> playerVariables = player.getVariables();
        for(String key : playerVariables.keySet()) {
            msgFormat = msgFormat.replaceAll("\\{" + key + "\\}", Base.colorise(playerVariables.get(key)));
        }
        
        msgFormat = msgFormat.replaceAll("\\{MESSAGE\\}", message);
        if(this.isprivate) {
            Base.broadcast(this.players, msgFormat);
        } else {
            Base.broadcast(msgFormat);
        }
    }
}
