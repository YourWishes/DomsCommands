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

import static com.domsplace.DomsCommands.Bases.Base.getConfigManager;
import com.domsplace.DomsCommands.Bases.DomsListener;
import com.domsplace.DomsCommands.Events.PreCommandEvent;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import java.util.List;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * @author      Dominic
 * @since       13/10/2013
 */
public class PlayerAwayListener extends DomsListener {
    @EventHandler
    public void handleAFKCancelOnMove(PlayerMoveEvent e) {
        DomsPlayer plyr = DomsPlayer.getPlayer(e.getPlayer());
        if(plyr.isAFK() && plyr.isVisible()) {
            broadcast(getConfigManager().format(plyr, getConfig().getString("away.messageback", "")));
        }
        plyr.setLastMoveTime(getNow());
        plyr.setAFK(false);
    }
    
    @EventHandler(ignoreCancelled=false)
    public void handleAFKCommand(PreCommandEvent e) {
        DomsPlayer plyr = DomsPlayer.getPlayer(e.getPlayer());
        List<String> cmds = getConfig().getStringList("away.commands.blocked");
        for(String s : cmds) {
            if(e.willResult(s)) {
                return;
            }
        }
        
        plyr.setLastMoveTime(getNow());
        if(!plyr.isAFK()) return;
        if(plyr.isVisible()) {
            broadcast(getConfigManager().format(plyr, getConfig().getString("away.messageback", "")));
        }
        plyr.setLastMoveTime(getNow());
        plyr.setAFK(false);
    }
    
    @EventHandler(ignoreCancelled=true)
    public void handleAFKChat(AsyncPlayerChatEvent e) {
        DomsPlayer plyr = DomsPlayer.getPlayer(e.getPlayer());
        plyr.setLastMoveTime(getNow());
        
        if(!plyr.isAFK()) return;
        if(plyr.isVisible()) {
            broadcast(getConfigManager().format(plyr, getConfig().getString("away.messageback", "")));
        }
        plyr.setLastMoveTime(getNow());
        plyr.setAFK(false);
    }
}
