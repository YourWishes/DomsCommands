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

package com.domsplace.DomsCommands.Commands.ItemCommands;

import com.domsplace.DomsCommands.Bases.BukkitCommand;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import com.domsplace.DomsCommands.Objects.SubCommandOption;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author      Dominic
 * @since       30/10/2013
 */
public class ExperienceCommand extends BukkitCommand {
    public ExperienceCommand() {
        super("experience");
        this.addSubCommandOption(SubCommandOption.PLAYERS_OPTION);
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 0) {
            sendMessage(sender, ChatError + "Enter an amount to add.");
            return false;
        }
        
        int amt = 0;
        String addSetRemove = "ADD";
        DomsPlayer player = DomsPlayer.getPlayer(sender);
        boolean level = false;
        
        if(args.length == 1) {
            if(!isPlayer(sender)) {
                sendMessage(sender, ChatError + "Please enter a player name.");
                return false;
            }
            
            if(!isInt(args[0].toUpperCase().replaceAll("L", ""))) {
                sendMessage(sender, ChatError + "Amount must be a number.");
                return false;
            }
            
            amt = getInt(args[0].toUpperCase().replaceAll("L", ""));
            if(args[0].toUpperCase().contains("L")) level = true;
        } else if(args.length == 2) {
            DomsPlayer guess = DomsPlayer.guessPlayer(sender, args[0]);
            if(guess != null) {
                player = guess;
                if(!isInt(args[1].toUpperCase().replaceAll("L", ""))) {
                    sendMessage(sender, ChatError + "Amount must be a number.");
                    return false;
                }
                amt = getInt(args[1].toUpperCase().replaceAll("L", ""));
                if(args[1].toUpperCase().contains("L")) level = true;
            } else {
                addSetRemove = args[0].toUpperCase();
                if(!isInt(args[1].toUpperCase().replaceAll("L", ""))) {
                    sendMessage(sender, ChatError + "Amount must be a number.");
                    return false;
                }
                amt = getInt(args[1].toUpperCase().replaceAll("L", ""));
                if(args[1].toUpperCase().contains("L")) level = true;
            }
        } else if(args.length > 2) {
            player = DomsPlayer.guessPlayer(sender, args[0]);
            addSetRemove = args[1].toUpperCase();
            if(!isInt(args[2].toUpperCase().replaceAll("L", ""))) {
                sendMessage(sender, ChatError + "Amount must be a number.");
                return false;
            }
            amt = getInt(args[2].toUpperCase().replaceAll("L", ""));
            if(args[2].toUpperCase().contains("L")) level = true;
        }
        
        if(player == null || !player.isOnline(sender) || player.isConsole()) {
            sendMessage(sender, ChatError + "Couldn't find player.");
            return true;
        }
        
        boolean isValid = 
                (addSetRemove.equalsIgnoreCase("add")) ||
                (addSetRemove.equalsIgnoreCase("set")) ||
                (addSetRemove.equalsIgnoreCase("remove"))
            ;
        
        if(!isValid) {
            sendMessage(sender, ChatError + "Type must be Add, Set or Remove");
            return true;
        }
        
        if(amt < 0) {
            sendMessage(sender, ChatError + "Amount must be 0 or above.");
            return true;
        }
        
        Player p = player.getOnlinePlayer();
        if(level) {
            if(addSetRemove.equalsIgnoreCase("set")) {
                p.setLevel(amt);
            } else if(addSetRemove.equalsIgnoreCase("add")) {
                p.giveExpLevels(amt);
            } else if(addSetRemove.equalsIgnoreCase("remove")) {
                p.giveExpLevels(-amt);
            }
        } else {
            if(addSetRemove.equalsIgnoreCase("set")) {
                int o = p.getTotalExperience();
                p.giveExpLevels(-Integer.MAX_VALUE);
                p.giveExp(amt);
            } else if(addSetRemove.equalsIgnoreCase("add")) {
                p.giveExp(amt);
            } else if(addSetRemove.equalsIgnoreCase("remove")) {
                p.giveExp(-amt);
            }
        }
        
        String message;
        String on = "on to";
        if(addSetRemove.equalsIgnoreCase("set")) {
            message = "Set ";
        } else if(addSetRemove.equalsIgnoreCase("add")) {
            message = "Added ";
            on = "to";
        } else {
            message = "Removed ";
            on = "from";
        }
        
        message += ChatImportant + amt + (level ? " Levels " : " Experience ");
        message += ChatDefault + on + " " + ChatImportant + p.getDisplayName() + ChatDefault + ".";
        
        sendMessage(sender, message);
        return true;
    }
}
