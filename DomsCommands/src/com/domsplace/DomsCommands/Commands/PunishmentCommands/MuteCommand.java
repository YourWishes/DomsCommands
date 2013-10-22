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
import static com.domsplace.DomsCommands.Bases.Base.ChatError;
import static com.domsplace.DomsCommands.Bases.Base.ChatImportant;
import static com.domsplace.DomsCommands.Bases.Base.sendMessage;
import com.domsplace.DomsCommands.Bases.BukkitCommand;
import com.domsplace.DomsCommands.Enums.PunishmentType;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import com.domsplace.DomsCommands.Objects.Punishment;
import com.domsplace.DomsCommands.Objects.SubCommandOption;
import java.util.Date;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author      Dominic
 * @since       21/10/2013
 */
public class MuteCommand extends BukkitCommand {
    public MuteCommand() {
        super("mute");
        this.addSubCommandOption(new SubCommandOption(SubCommandOption.PLAYERS_OPTION, "reason"));
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length < 1) {
            sendMessage(sender, ChatError + "Please enter a player name to mute.");
            return false;
        }
            
        DomsPlayer rel = DomsPlayer.guessPlayer(sender, args[0]);
        if(rel == null) {
            sendMessage(sender, ChatError + args[0] + " isn't online.");
            return true;
        }
        
        if(rel.isMuted()) {
            for(Punishment p : rel.getPunishmentsOfType(PunishmentType.MUTE)) {
                p.isPardoned(true);
            }
            
            sendMessage(sender, "Unmuted " + ChatImportant + rel.getDisplayName() + ChatDefault + ".");
            sendMessage(rel, "You have been unmuted.");
            return true;
        }
        
        if(rel.isConsole() || rel.hasPermisson("DomsCommands.mute.exempt")) {
            sendMessage(sender, ChatError + "You cannot mute this player.");
            return true;
        }
        
        String reason = "Muted by an operator.";
        String tb = "";
        long unbandate = -1;
        if(args.length > 1) {
            //Check if args[1] is a time spec or not
            if(Base.isValidTime(args[1])) {
                //args[1] is a time spec.
                unbandate = Base.addStringToNow(args[1]).getTime();
                if(args.length > 2) reason = "";
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
        
        Punishment p = new Punishment(rel, PunishmentType.MUTE);
        p.setReason(reason);
        p.setEndDate(unbandate);
        p.setBanner(sender.getName());
        rel.addPunishment(p);
        rel.sendMessage(ChatDefault + "You have been muted for " + ChatImportant + colorise(reason) + ChatDefault + tb + ChatDefault + ".");
        
        String name = sender.getName();
        if(isPlayer(sender)) {
            name = DomsPlayer.getPlayer(sender).getDisplayName();
        }
        broadcast(
            "DomsCommands.mute.notify",
            ChatImportant + name + ChatDefault + " muted " + 
            ChatImportant + rel.getDisplayName() + ChatDefault + 
            " for " + ChatImportant + colorise(reason) + ChatDefault + "."
        );
        if(!hasPermission(sender, "DomsCommands.mute.notify")) {
            sendMessage(
                sender,
                ChatImportant + name + ChatDefault + " muted " + 
                ChatImportant + rel.getDisplayName() + ChatDefault + 
                " for " + ChatImportant + colorise(reason) + ChatDefault + "."
            );
        }
        return true;
    }
}
