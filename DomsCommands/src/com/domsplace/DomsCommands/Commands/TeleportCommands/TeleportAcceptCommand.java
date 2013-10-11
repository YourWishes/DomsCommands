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

package com.domsplace.DomsCommands.Commands.TeleportCommands;

import com.domsplace.DomsCommands.Bases.BukkitCommand;
import com.domsplace.DomsCommands.Enums.TeleportRequestType;
import com.domsplace.DomsCommands.Objects.DomsLocation;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import com.domsplace.DomsCommands.Objects.TeleportRequest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author      Dominic
 * @since       08/10/2013
 */
public class TeleportAcceptCommand extends BukkitCommand {
    public TeleportAcceptCommand() {
        super("tpaccept");
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(!isPlayer(sender)) {
            sendMessage(sender, ChatError + "Only players can do this.");
            return true;
        }
        
        DomsPlayer plyr = DomsPlayer.getPlayer(sender);
        TeleportRequest tpr = plyr.getLastTeleporRequest();
        if(tpr == null) {
            sendMessage(sender, ChatError + "You have not recieved any requests.");
            return true;
        }
        
        if(!tpr.getFrom().isOnline(sender)) {
            sendMessage(sender, ChatError + "The player is no longer online.");
            return true;
        }
        
        //"Accept"
        sendMessage(tpr.getFrom(), ChatImportant + tpr.getTo().getDisplayName() + ChatDefault + " accepted your teleport request.");
        sendMessage(tpr.getTo(), ChatDefault + "Accepted the request.");
        
        //"Teleport"
        if(tpr.getType().equals(TeleportRequestType.TELEPORT_REQUEST_TO)) {
            DomsLocation to = tpr.getTo().getLocation();
            tpr.getFrom().setBackLocation(tpr.getFrom().getLocation());
            tpr.getFrom().teleport(to);
        } else if(tpr.getType().equals(TeleportRequestType.TELEPORT_REQUEST_HERE)) {
            DomsLocation to = tpr.getFrom().getLocation();
            tpr.getTo().setBackLocation(tpr.getTo().getLocation());
            tpr.getTo().teleport(to);
        }
        plyr.setLastTeleportRequest(null);
        return true;
    }
}
