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

package com.domsplace.DomsCommands.Listeners;

import com.domsplace.DomsCommands.Bases.Base;
import static com.domsplace.DomsCommands.Bases.Base.ChatDefault;
import static com.domsplace.DomsCommands.Bases.Base.ChatImportant;
import com.domsplace.DomsCommands.Bases.DomsListener;
import com.domsplace.DomsCommands.Enums.PunishmentType;
import com.domsplace.DomsCommands.Events.PreCommandEvent;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import com.domsplace.DomsCommands.Objects.Punishment;
import java.util.Date;
import java.util.List;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;

/**
 *
 * @author Dominic Masters
 */
public class PunishmentListener extends DomsListener {
    @EventHandler(priority=EventPriority.LOWEST)
    public void handleMutedChat(AsyncPlayerChatEvent e) {
        DomsPlayer player = DomsPlayer.getPlayer(e.getPlayer());
        if(player == null || player.isConsole()) return;
        if(!player.isMuted()) return;
        e.setMessage("");
        e.setFormat("");
        e.setCancelled(true);
        Punishment pun = player.getMostRecentPunishmentOfType(PunishmentType.MUTE);
        sendMessage(player, ChatError + "You can't talk, you're muted for \"" + colorise(pun.getReason()) + ChatError + "\".");
    }
    
    @EventHandler(priority=EventPriority.LOWEST)
    public void handleMutedCommands(PreCommandEvent e) {
        DomsPlayer player = DomsPlayer.getPlayer(e.getPlayer());
        if(player == null || player.isConsole()) return;
        if(!player.isMuted()) return;
        List<String> cmds = getConfig().getStringList("punishment.mute.blockedcommands");
        for(String s : cmds) {
            if(!e.willResult(s)) continue;
            player.sendMessage(ChatError + "You can't run this command, you're muted.");
            e.setCancelled(true);
            return;
        }
    }
    
    @EventHandler(priority=EventPriority.LOWEST)
    public void handleBanReason(PlayerLoginEvent e) {
        DomsPlayer player = DomsPlayer.guessPlayer(e.getPlayer().getName(), false);
        if(player == null) return;
        if(!player.isBanned()) return;
        Punishment p = player.getMostRecentPunishmentOfType(PunishmentType.BAN);
        String tb = "";
        if(!p.isPermanent()) tb = " for " + ChatImportant + Base.getHumanTimeAway(new Date(p.getEndDate())) + ChatDefault;
        String msg = ChatDefault + "You have been banned for " + ChatImportant + colorise(p.getReason()) + ChatDefault + tb + ".";
        e.setKickMessage(msg);
        e.setResult(PlayerLoginEvent.Result.KICK_BANNED);
        e.disallow(PlayerLoginEvent.Result.KICK_BANNED, msg);
    }
}