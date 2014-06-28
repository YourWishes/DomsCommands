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
import com.domsplace.DomsCommands.Objects.SubCommandOption;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author      Dominic
 * @since       10/10/2013
 */
public class TeleportCommand extends BukkitCommand {
    public TeleportCommand() {
        super("teleport");
        this.addSubCommandOption(new SubCommandOption(SubCommandOption.PLAYERS_OPTION, SubCommandOption.PLAYERS_OPTION));
        this.addSubCommandOption(SubCommandOption.WORLD_OPTION);
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length < 1) {
            sendMessage(sender, ChatError + "Please enter a player or coordinates.");
            return true;
        }
        
        DomsPlayer player = null;
        DomsLocation target = null;
        if(args.length == 1) {
            target = DomsLocation.guessLocation(args[0]);
            if(target != null) {
                if(!hasPermission(sender, "DomsCommands.teleport.coordinates")) {
                    sendMessage(sender, ChatError + "You don't have permission to TP to Coordinates.");
                    return this.noPermission(sender, cmd, label, args);
                }
                player = DomsPlayer.getPlayer(sender);
            } else {
                DomsPlayer found = DomsPlayer.guessPlayer(sender, args[0]);
                if(found == null || found.isConsole()) {
                    sendMessage(sender, ChatError + "Couldn't find player.");
                    return true;
                }
                target = found.getLocation();
                player = DomsPlayer.getPlayer(sender);
                if(player == null || player.isConsole()) {
                    sendMessage(sender, ChatError + "You cannot TP.");
                    return true;
                }
                
                if(found.compare(sender)) {
                    sendMessage(sender, ChatError + "Can't tp to yourself.");
                    return true;
                }
                
                player.setBackLocation(player.getLocation());
                player.teleport(target);
                sendMessage(sender, "Teleporting you to " + ChatImportant + found.getDisplayName() + ChatDefault + ".");
                return true;
            }
        } else if(args.length == 2) {
            player = DomsPlayer.guessPlayer(sender, args[0]);
            if(player == null || !player.isOnline(sender) || player.isConsole()) {
                sendMessage(sender, ChatError + "Couldn't find player.");
                return true;
            }
            
            target = DomsLocation.guessLocation(args[1]);
            if(target == null) {
                DomsPlayer guess = DomsPlayer.guessPlayer(sender, args[1]);
                if(guess == null || !guess.isOnline(sender) || guess.isConsole()) {
                    sendMessage(sender, ChatError + "Couldn't find player.");
                    return true;
                }
                if(guess.equals(player)) {
                    sendMessage(sender, ChatError + "Can't teleport to same player.");
                    return true;
                }
                
                if(!player.compare(sender) && !hasPermission(sender, "DomsCommands.teleport.others")) {
                    sendMessage(sender, ChatError + "You can't teleport other players.");
                    return true;
                }
                
                target = guess.getLocation();
                player.setBackLocation(player.getLocation());
                player.teleport(target);
                sendMessage(sender, "Teleporting you to " + ChatImportant + guess.getDisplayName() + ChatDefault + ".");
                if(!player.compare(sender)) {
                    sendMessage(sender, "Teleporting " + ChatImportant + player.getDisplayName() + ChatDefault + " to " + ChatImportant + guess.getDisplayName() + ChatDefault + ".");
                }
                return true;
            }
                
            if(!player.compare(sender) && !hasPermission(sender, "DomsCommands.teleport.coordinates.others")) {
                sendMessage(sender, ChatError + "You can't teleport other players to coordinates.");
                return true;
            }
            
            if(!hasPermission(sender, "DomsCommands.teleport.coordinates")) {
                sendMessage(sender, ChatError + "You don't have permission to TP to Coordinates.");
                return this.noPermission(sender, cmd, label, args);
            }
            player.setBackLocation(player.getLocation());
            player.teleport(target);
            player.sendMessage("Teleporting you to " + ChatImportant + target.toHumanString() + ChatDefault + ".");
            if(!player.compare(sender)) {
                sendMessage(sender, "Teleporting " + ChatImportant + player.getDisplayName() + ChatDefault + " to " + ChatImportant + target.toHumanString() + ChatDefault + ".");
            }
            return true;
        } else {
            DomsPlayer guessA = null;
            DomsPlayer guessB = null;
            String loc = "";
            for(String s : args) {
                if(guessA != null && guessB != null) {
                    loc += s + ",";
                    continue;
                }
                
                if(isDouble(s) || isInt(s) || isLong(s) || isFloat(s)) {
                    loc += s + ",";
                    continue;
                }
                
                if(Bukkit.getWorld(s) != null) {
                    loc += s + ",";
                    continue;
                }
                
                DomsPlayer currentGuess = DomsPlayer.guessPlayer(sender, args[0]);
                if(currentGuess == null) {
                    loc += s + ",";
                    continue;
                }
                
                if(guessA != null) {
                    guessA = currentGuess;
                    continue;
                }
                
                guessB = currentGuess;
            }
            
            DomsLocation guessLoc = DomsLocation.guessLocation(loc);
            if(guessLoc == null) {
                DomsPlayer from = (guessA == null ? DomsPlayer.getPlayer(sender) : guessA);
                if(from == null || from.isConsole() || !from.isOnline(sender)) {
                    sendMessage(sender, ChatError + "Couldn't find player.");
                    return true;
                }
                if(!from.compare(sender) && !hasPermission(sender, "DomsCommands.teleport.others")) {
                    sendMessage(sender, ChatError + "You can't teleport other players.");
                    return true;
                }
                if(guessB != null && !guessB.isConsole()) {
                    if(from.equals(guessB)) {
                        sendMessage(sender, ChatError + "Can't teleport to same player.");
                        return true;
                    }
                    target = guessB.getLocation();
                }
            } else {
                if(!hasPermission(sender, "DomsCommands.teleport.coordinates")) {
                    sendMessage(sender, ChatError + "You don't have permission to TP to Coordinates.");
                    return this.noPermission(sender, cmd, label, args);
                }
            

                target = guessLoc;
                player = (guessA == null ? (guessB == null ? DomsPlayer.getPlayer(sender) : guessB) : guessA);
                
                if(!player.compare(sender) && !hasPermission(sender, "DomsCommands.teleport.coordinates.others")) {
                    sendMessage(sender, ChatError + "You can't teleport other players to coordinates.");
                    return true;
                }
            }
        }
        
        if(player == null || player.isConsole() || !player.isOnline(sender)) {
            sendMessage(sender, ChatError + "Couldn't find player.");
            return true;
        }
        
        if(target == null) {
            sendMessage(sender, ChatError + "Invalid location.");
            return true;
        }
        if(target.getWorld() == null) target.setWorld(player.getLocation().getWorld());
        
        if(!target.isWorldLoaded()) {
            sendMessage(sender, ChatError + "Invalid location.");
            return true;
        }
        
        player.setBackLocation(player.getLocation());
        player.teleport(target);
        player.sendMessage("Teleporting you to " + ChatImportant + target.toHumanString() + ChatDefault + ".");
        if(!player.compare(sender)) {
            sendMessage(sender, "Teleporting " + ChatImportant + player.getDisplayName() + ChatDefault + " to " + ChatImportant + target.toHumanString() + ChatDefault + ".");
        }
        return true;
    }
}
