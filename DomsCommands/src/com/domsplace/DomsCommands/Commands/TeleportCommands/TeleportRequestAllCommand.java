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

import static com.domsplace.DomsCommands.Bases.Base.ChatError;
import static com.domsplace.DomsCommands.Bases.Base.ChatImportant;
import static com.domsplace.DomsCommands.Bases.Base.isPlayer;
import static com.domsplace.DomsCommands.Bases.Base.sendMessage;
import com.domsplace.DomsCommands.Bases.BukkitCommand;
import com.domsplace.DomsCommands.Enums.TeleportRequestType;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import com.domsplace.DomsCommands.Objects.SubCommandOption;
import com.domsplace.DomsCommands.Objects.TeleportRequest;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author      Dominic
 * @since       11/10/2013
 */
public class TeleportRequestAllCommand extends BukkitCommand {
    public TeleportRequestAllCommand() {
        super("teleportrequestall");
        this.addSubCommandOption(SubCommandOption.PLAYERS_OPTION);
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 0 && !isPlayer(sender)) {
            sendMessage(sender, ChatError + "Please enter a player name.");
            return true;
        }
        
        DomsPlayer target;
        
        if(args.length == 0) {
            target = DomsPlayer.getPlayer(sender);
        } else {
            target = DomsPlayer.guessPlayer(sender, args[0]);
            if(target == null || !target.isOnline(sender)) {
                sendMessage(sender, ChatError + args[0] + " isn't online.");
                return true;
            }
        }
        
        if(target == null) return true;
        for(Player p : Bukkit.getOnlinePlayers()) {
            DomsPlayer pl = DomsPlayer.getPlayer(p);
            if(pl == null) continue;
            if(target.equals(pl)) continue;
            TeleportRequest tpr = new TeleportRequest(target, pl, TeleportRequestType.TELEPORT_REQUEST_HERE);
            pl.setLastTeleportRequest(tpr);
            sendMessage(pl, (Object)new String[] {
                ChatImportant + target.getDisplayName() + ChatDefault + " has requested for you to teleport to them.",
                ChatDefault + "\tType " + ChatImportant + "/tpaccept" + ChatDefault + " to accept.",
                ChatDefault + "\tType " + ChatImportant + "/tpdeny" + ChatDefault + " to deny."
            });
        }
        
        sendMessage(sender, "Sent request to everyone");
        return true;
    }

}
