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

import com.domsplace.DomsCommands.Bases.DataManager;
import com.domsplace.DomsCommands.Bases.DomsListener;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

/**
 *
 * @author Dominic Masters
 */
public class VotifierListener extends DomsListener {
    @EventHandler(priority=EventPriority.NORMAL)
    @Deprecated
    public void onVotifierEvent(VotifierEvent event) {
        Vote vote = event.getVote();
        debug(vote.getUsername() + " VOTED ON " + vote.getAddress());
        
        DomsPlayer player = DomsPlayer.getDomsPlayerFromUsername(vote.getUsername());
        
        for(String s : getConfig().getStringList("votifier.commands")) {
            for(String cmd : DataManager.CONFIG_MANAGER.format(player, s)) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
            }
        }
    }
}
