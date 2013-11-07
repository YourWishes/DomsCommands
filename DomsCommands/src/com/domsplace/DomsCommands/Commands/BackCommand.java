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

package com.domsplace.DomsCommands.Commands;

import com.domsplace.DomsCommands.Bases.BukkitCommand;
import com.domsplace.DomsCommands.Objects.DomsLocation;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author      Dominic
 * @since       08/10/2013
 */
public class BackCommand extends BukkitCommand {
    public BackCommand() {
        super("back");
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(!isPlayer(sender)) {
            sendMessage(sender, ChatError + "Only players can do this.");
            return true;
        }
        
        DomsPlayer plyr = DomsPlayer.getPlayer(sender);
        DomsLocation back = plyr.getBackLocation();
        if(back == null) {
            sendMessage(sender, ChatError + "No where to go back to.");
            return true;
        }
        
        if(!back.isWorldLoaded()) {
            sendMessage(sender, ChatError + "This world is no longer available.");
            return true;
        }
        
        sendMessage(sender, ChatDefault + "Going back.");
        plyr.setBackLocation(plyr.getLocation());
        plyr.teleport(back);
        return true;
    }
}
