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

package com.domsplace.DomsCommands.Commands.PunishmentCommands;

import static com.domsplace.DomsCommands.Bases.Base.ChatError;
import static com.domsplace.DomsCommands.Bases.Base.getOfflinePlayer;
import static com.domsplace.DomsCommands.Bases.Base.sendMessage;
import com.domsplace.DomsCommands.Bases.BukkitCommand;
import com.domsplace.DomsCommands.Enums.PunishmentType;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import com.domsplace.DomsCommands.Objects.Punishment;
import com.domsplace.DomsCommands.Objects.SubCommandOption;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author      Dominic
 * @since       13/10/2013
 */
public class KickCommand extends BukkitCommand {
    public KickCommand() {
        super("kick");
        this.addSubCommandOption(new SubCommandOption(SubCommandOption.PLAYERS_OPTION, "reason"));
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length < 1) {
            sendMessage(sender, ChatError + "Please enter a player name to ban.");
            return false;
        }
        
        OfflinePlayer op = getOfflinePlayer(sender, args[0]);        
        DomsPlayer rel = DomsPlayer.getPlayer(op);
        
        if(rel.hasPermisson("DomsCommands.kick.exempt")) {
            sendMessage(sender, ChatError + "You cannot kick this player.");
            return true;
        }
        
        if(!rel.isOnline(sender)) {
            sendMessage(sender, ChatError + rel.getDisplayName() + " isn't online.");
            return true;
        }
        
        String reason = "Kicked by an operator";
        if(args.length > 1) {
            reason = "";
            for(int i = 1; i < args.length; i++) {
                reason += args[i];
                if(i < (args.length - 1)) {
                    reason += " ";
                }
            }
        }
        
        Punishment p = new Punishment(rel, PunishmentType.KICK);
        p.setReason(reason);
        p.setBanner(sender.getName());
        rel.addPunishment(p);
        rel.kickPlayer(ChatDefault + "You have been kicked for " + ChatImportant + colorise(reason) + ChatDefault + ".");
        
        
        String name = sender.getName();
        if(isPlayer(sender)) {
            name = DomsPlayer.getPlayer(sender).getDisplayName();
        }
        broadcast(
            "DomsCommands.kick.notify",
            ChatImportant + name + ChatDefault + " kicked " + 
            ChatImportant + rel.getDisplayName() + ChatDefault + 
            " for " + ChatImportant + colorise(reason) + ChatDefault + "."
        );
        if(!hasPermission(sender, "DomsCommands.kick.notify")) {
            sendMessage(
                sender,
                ChatImportant + name + ChatDefault + " kicked " + 
                ChatImportant + rel.getDisplayName() + ChatDefault + 
                " for " + ChatImportant + colorise(reason) + ChatDefault + "."
            );
        }
        return true;
    }
}
