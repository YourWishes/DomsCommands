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

import com.domsplace.DomsCommands.Bases.Base;
import static com.domsplace.DomsCommands.Bases.Base.ChatDefault;
import com.domsplace.DomsCommands.Bases.BukkitCommand;
import com.domsplace.DomsCommands.Enums.PunishmentType;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import com.domsplace.DomsCommands.Objects.Punishment;
import com.domsplace.DomsCommands.Objects.SubCommandOption;
import java.util.Date;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author      Dominic
 * @since       11/10/2013
 */
public class BanCommand extends BukkitCommand {
    public BanCommand() {
        super("ban");
        this.addSubCommandOption(new SubCommandOption(SubCommandOption.PLAYERS_OPTION, "1hour", "reason"));
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length < 1) {
            sendMessage(sender, ChatError + "Please enter a player name to ban.");
            return false;
        }
        
        OfflinePlayer op = getOfflinePlayer(sender, args[0]);
        DomsPlayer rel = DomsPlayer.getPlayer(op);
        
        if(rel.isConsole() || rel.hasPermisson("DomsCommands.ban.exempt")) {
            sendMessage(sender, ChatError + "You cannot ban this player.");
            return true;
        }
        
        String reason = "Banned by an operator.";
        String tb = "";
        long unbandate = -1;
        if(args.length > 1) {
            //Check if args[1] is a time spec or not
            if(Base.isValidTime(args[1])) {
                //args[1] is a time spec.
                unbandate = Base.addStringToNow(args[1]).getTime();
                reason = "";
                tb = " for " + ChatImportant + Base.getHumanTimeAway(new Date(unbandate)) + ChatDefault;
            } else {
                reason = args[1] + " ";
            }
            
            if(args.length > 2) {
                for(int i = 2; i < args.length; i++) {
                    reason += args[i];
                    if(i < args.length-1) {
                        reason += " ";
                    }
                }
            }
        }
        
        Punishment p = new Punishment(rel, PunishmentType.BAN);
        rel.addPunishment(p);
        p.setReason(reason);
        p.setEndDate(unbandate);
        p.setBanner(sender.getName());
        rel.kickPlayer(ChatDefault + "You have been banned for " + ChatImportant + colorise(reason) + ChatDefault + tb + ".");
        op.setBanned(true);
        
        String name = sender.getName();
        if(isPlayer(sender)) {
            name = DomsPlayer.getPlayer(sender).getDisplayName();
        }
        broadcast(
            "DomsCommands.ban.notify",
            ChatImportant + name + ChatDefault + " banned " + 
            ChatImportant + rel.getDisplayName() + ChatDefault + 
            " for " + ChatImportant + colorise(reason) + ChatDefault + tb + "."
        );
        if(!hasPermission(sender, "DomsCommands.ban.notify")) {
            sendMessage(
                sender,
                ChatImportant + name + ChatDefault + " banned " + 
                ChatImportant + rel.getDisplayName() + ChatDefault + 
                " for " + ChatImportant + colorise(reason) + ChatDefault + tb + "."
            );
        }
        return false;
    }
}
