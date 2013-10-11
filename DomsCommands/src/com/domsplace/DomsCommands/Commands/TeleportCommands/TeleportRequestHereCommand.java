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
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import com.domsplace.DomsCommands.Objects.SubCommandOption;
import com.domsplace.DomsCommands.Objects.TeleportRequest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author      Dominic
 * @since       08/10/2013
 */
public class TeleportRequestHereCommand extends BukkitCommand {
    public TeleportRequestHereCommand() {
        super("teleportrequesthere");
        this.addSubCommandOption(SubCommandOption.PLAYERS_OPTION);
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(!isPlayer(sender)) {
            sendMessage(sender, ChatError + "Only players can do this.");
            return true;
        }
        
        if(args.length < 1) {
            sendMessage(sender, "Please enter a player name.");
            return false;
        }
        
        Player target = getPlayer(sender, args[0]);
        if(target == null) {
            sendMessage(sender, ChatError + args[0] + " isn't online.");
            return true;
        }
        
        DomsPlayer from = DomsPlayer.getPlayer(sender);
        DomsPlayer to = DomsPlayer.getPlayer(target);
        
        if(from.equals(to)) {
            sendMessage(sender, ChatError + "You cannot send a request to yourself!");
            return true;
        }
        
        TeleportRequest request = new TeleportRequest(from, to, TeleportRequestType.TELEPORT_REQUEST_HERE);
        to.setLastTeleportRequest(request);
        
        sendMessage(from, "Sent Teleport request to " + ChatImportant + to.getDisplayName());
        sendMessage(to, (Object)new String[] {
            ChatImportant + from.getDisplayName() + ChatDefault + " has requested for you to teleport to them.",
            ChatDefault + "\tType " + ChatImportant + "/tpaccept" + ChatDefault + " to accept.",
            ChatDefault + "\tType " + ChatImportant + "/tpdeny" + ChatDefault + " to deny."
        });
        return true;
    }
}
