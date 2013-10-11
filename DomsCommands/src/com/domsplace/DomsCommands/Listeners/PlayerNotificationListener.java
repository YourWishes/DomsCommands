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

package com.domsplace.DomsCommands.Listeners;

import com.domsplace.DomsCommands.Bases.Base;
import com.domsplace.DomsCommands.Bases.DomsListener;
import com.domsplace.DomsCommands.Events.PlayerFirstJoinedEvent;
import com.domsplace.DomsCommands.Events.PlayerPostFirstJoinEvent;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;

/**
 * @author      Dominic
 * @since       08/10/2013
 */
public class PlayerNotificationListener extends DomsListener {
    public String[] format(DomsPlayer player, String msg) {
        String[] parts = msg.split("\\n");
        
        for(int i = 0; i < parts.length; i++) {
            String p = parts[i];
            
            p = format(p);
            p = p.replaceAll("\\{NAME\\}", player.getPlayer());
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
        msg = msg.replaceAll("\\{SERVER\\}", Bukkit.getServerName());
        msg = msg.replaceAll("\\{NUMPLAYERS\\}", "" + Base.getPlayersList().size());
        msg = msg.replaceAll("\\{TOTALPLAYERS\\}", "" + Bukkit.getServer().getMaxPlayers());
        
        String m = "";
        List<OfflinePlayer> list = Base.getPlayersList();
        for(int i = 0; i < list.size(); i++) {
            OfflinePlayer op = list.get(i);
            String n = op.getName();
            if(op.isOnline()) n = op.getPlayer().getDisplayName();
            m += ChatDefault + n + ChatDefault;
            if(i < (list.size() - 1)) {
                m += ", ";
            }
        }
        msg = msg.replaceAll("\\{PLAYERS\\}", m);
        
        return msg;
    }
    
    @EventHandler
    public void sendFirstJoinBroadcast(PlayerFirstJoinedEvent e) {
        broadcast((Object) format(e.getPlayer(), getConfig().getString("messages.firstjoin.broadcast", "")));
    }
    
    @EventHandler
    public void sendFirstJoinMessage(PlayerFirstJoinedEvent e) {
        sendMessage(e.getPlayer(), (Object) format(e.getPlayer(), getConfig().getString("messages.firstjoin.message", "")));
    }
    
    @EventHandler(priority=EventPriority.HIGH)
    public void changeLoginMessage(PlayerJoinEvent e) {
        e.setJoinMessage(format(e.getPlayer(), getConfig().getString("messages.login.broadcast", e.getJoinMessage())));
    }
    
    @EventHandler(priority=EventPriority.HIGH)
    public void sendLoginMessage(PlayerPostFirstJoinEvent e) {
        sendMessage(e.getPlayer(), (Object) this.format(e.getPlayer(), getConfig().getString("messages.login.message", "")));
    }
    
    @EventHandler(priority=EventPriority.HIGH)
    public void changeLogoutMessage(PlayerQuitEvent e) {
        e.setQuitMessage(format(e.getPlayer(), getConfig().getString("messages.logout.broadcast", e.getQuitMessage())));
    }
    
    @EventHandler(priority=EventPriority.HIGH)
    public void changeKickedMessage(PlayerKickEvent e) {
        e.setLeaveMessage(format(e.getPlayer(), getConfig().getString("messages.kicked.broadcast", e.getLeaveMessage())));
    }
    
    @EventHandler(priority=EventPriority.HIGH)
    public void changeMessageOfTheDay(ServerListPingEvent e) {
        e.setMotd(Base.colorise(getConfig().getString("messages.motd", e.getMotd())));
    }
}
