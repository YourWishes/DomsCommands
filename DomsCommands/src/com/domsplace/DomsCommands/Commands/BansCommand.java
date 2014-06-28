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

package com.domsplace.DomsCommands.Commands;

import com.domsplace.DomsCommands.Bases.Base;
import com.domsplace.DomsCommands.Bases.BukkitCommand;
import com.domsplace.DomsCommands.Enums.PunishmentType;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import com.domsplace.DomsCommands.Objects.Punishment;
import com.domsplace.DomsCommands.Objects.SubCommandOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Dominic Masters
 */
public class BansCommand extends BukkitCommand {    
    public BansCommand() {
        super("bans");
        this.addSubCommandOption(SubCommandOption.PLAYERS_OPTION);
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length < 1) {
            sendMessage(sender, ChatError + "Please enter a player name or ban ID.");
            return false;
        }
        
        Punishment tryPun = null;
        if(isLong(args[0]) && (tryPun = Punishment.getByID(getLong(args[0]))) != null) {
            //Get By ID
            Punishment p = tryPun;
            if(p == null) {
                sendMessage(sender, ChatError + "No Information Available.");
                return true;
            }
            
            sendMessage(sender, new String[] {
                ChatImportant + p.getType().getType() + " #" + p.getId(),
                p.getPlayer().getUsername() + ChatDefault + " " + p.getType().getPastText() + " by " + p.getBanner(),
                "For " + colorise(p.getReason()),
                "When: " + Base.getHumanDate(new Date(p.getDate())),
                (p.getEndDate() > 1 ? "Until: " + Base.getHumanDate(new Date(p.getEndDate())) : ""),
                "Where: " + p.getLocation().toHumanString(),
            });
            return true;
        }
        
        DomsPlayer player = DomsPlayer.guessPlayerExactly(sender, args[0], false);
        if(player == null || player.isConsole()) {
            sendMessage(sender, ChatError + "Couldn't find player.");
            return true;
        }
        
        int page = 1;
        if(args.length > 1) {
            if(!isInt(args[1])) {
                sendMessage(sender, ChatError + "Page must be a number!");
                return true;
            }
            
            page = getInt(args[1]);
        }
        
        List<Punishment> bans = player.getPunishmentsOfType(PunishmentType.BAN);
        int pages = (int) Math.ceil((double)bans.size() / (double)Punishment.PUNS_PER_PAGE);
        
        if(pages < 1) {
            sendMessage(sender, ChatError + "No Results to display.");
            return true;
        }
        
        if(page < 1 || page > pages) {
            sendMessage(sender, ChatError + "Please enter a page between 1 and " + pages + "!");
            return false;
        }
        
        int start = (int) Math.floor((double) (page-1) * (double) Punishment.PUNS_PER_PAGE);
        int end = start + Punishment.PUNS_PER_PAGE;
        
        List<String> msg = new ArrayList<String>();
        msg.add(ChatImportant + "Results for " + player.getDisplayName() + ChatImportant + " - Page " + page + "/" + pages);
        
        for(int i = start; i < end; i++) {
            try {
                Punishment p = bans.get(i);
                msg.add("#" + p.getId() + " - " + p.getType().getPastText() + " for \"" + Base.colorise(p.getReason()) + "\" by " + p.getBanner());
            } catch(Exception e) {}
        }
        
        sendMessage(sender, msg);
        return true;
    }
}
