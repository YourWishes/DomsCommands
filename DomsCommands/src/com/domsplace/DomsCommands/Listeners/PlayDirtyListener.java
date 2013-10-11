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

import com.domsplace.DomsCommands.Bases.BukkitCommand;
import com.domsplace.DomsCommands.Bases.DomsListener;
import com.domsplace.DomsCommands.Events.PreCommandEvent;
import java.util.List;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

/**
 * @author      Dominic
 * @since       08/10/2013
 */
public class PlayDirtyListener extends DomsListener {
    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    public void handlePlayDirtyCommands(PreCommandEvent e) {
        BukkitCommand cmd = BukkitCommand.getCommandSearchAliases(e.getCommand());
        if(cmd == null) return;
        
        //Make sure this command isn't on the "blocked" list
        List<String> overriddenCommands = getConfig().getStringList("commands.overriden");
        boolean blocked = false;
        for(String oc : overriddenCommands) {
            if(cmd.isCommand(oc)) blocked=true;
        }
        
        if(blocked) return;
        
        //Not Blocked, let's play dirty
        e.setCancelled(true);
        cmd.fakeExecute(e.getPlayer(), e.toFullCommand());
    }
}
