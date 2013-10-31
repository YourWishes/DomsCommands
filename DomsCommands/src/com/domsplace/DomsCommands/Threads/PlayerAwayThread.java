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

package com.domsplace.DomsCommands.Threads;

import com.domsplace.DomsCommands.Bases.Base;
import com.domsplace.DomsCommands.Bases.DomsThread;
import com.domsplace.DomsCommands.Enums.PunishmentType;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import com.domsplace.DomsCommands.Objects.Punishment;

/**
 * @author      Dominic
 * @since       17/10/2013
 */
public class PlayerAwayThread extends DomsThread {
    public PlayerAwayThread() {
        super(1,1);
    }
    
    @Override
    public void run() {
        long now = getNow();
        long awayTime = getConfig().getInt("away.autoaway", 0)*1000;
        long kickTime = getConfig().getInt("away.autokick", 0)*1000;
        
        for(DomsPlayer player : DomsPlayer.getOnlinePlayers()) {
            if(player.hasPermisson("DomsCommands.away.exempt")) continue;
            
            long AFK = player.getAFKTime();
            long move = player.getLastMoveTime();
            long diff = now;
            
            if(!player.isAFK()) {
                //Get the time they last moved
                diff -= move;
                if(awayTime > diff) continue;
                
                if(player.isVisible()) {
                    broadcast(getConfigManager().format(player, getConfig().getString("away.message", "")));
                }
                
                player.setAFK(true);
                player.setAFKTime(now);
                continue;
            }
            
            //Get When they went AFK
            diff -= AFK;
            if(kickTime > diff) continue;
            
            Punishment p = new Punishment(player, PunishmentType.KICK, getConfig().getString("away.kickmessage", "You have been away too long."));
            player.addPunishment(p);
            broadcast(
                "DomsCommands.kick.notify",
                ChatImportant + player + ChatDefault + " was kicked " + 
                "for " + ChatImportant + colorise(p.getReason()) + ChatDefault + "."
            );
            player.kickPlayer(ChatDefault + "You have been kicked for " + ChatImportant + colorise(p.getReason()) + ChatDefault + ".");
            player.setAFK(false);
        }
    }
}
