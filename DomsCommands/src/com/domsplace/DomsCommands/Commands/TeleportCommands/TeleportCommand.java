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
import com.domsplace.DomsCommands.Objects.DomsLocation;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import com.domsplace.DomsCommands.Objects.SubCommandOption;
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
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length < 1) {
            sendMessage(sender, ChatError + "Please enter a player name, or coordinates.");
            return false;
        }
        
        DomsLocation dest = null;
        DomsPlayer plyr = null;
        
        if(args.length < 2 && !isPlayer(sender)) {
            sendMessage(sender, ChatError + "Please enter a player name, or coordinates to teleport to.");
            return false;
        } else if(args.length == 1 && isPlayer(sender)) {
            plyr = DomsPlayer.getPlayer(sender);
        }
        
        DomsPlayer guess = DomsPlayer.guessPlayer(sender, args[0]);
        if(guess == null) { 
            if(!isPlayer(sender)) {
                sendMessage(sender, ChatError + args[0] + " wasn't found.");
                return true;
            }
            
            //Try to get as coords
            String c = "";
            for(int i = 0; i < args.length; i++) {
                if(DomsPlayer.guessPlayer(sender, args[i]) != null) continue;
                String[] x = args[i].split(",");
                for(int e = 0; e < x.length; e++) {
                    String f = x[e].replaceAll(",", "").replaceAll(" ", "");
                    if(f == null || f.equalsIgnoreCase("")) continue;
                    c += f + ",";
                }
            }
            
            DomsLocation guessLocation = DomsLocation.guessLocation(c);
            if(guessLocation == null) {
                sendMessage(sender, ChatError + "Please enter valid coords.");
                return true;
            }
            
            if(!hasPermission(sender, "DomsCommands.teleport.coordinates")) {
                return this.noPermission(sender, cmd, label, args);
            }
            
            dest = guessLocation;
            plyr = DomsPlayer.getPlayer(sender);
        } else {
            if(args.length == 1) {
                if(isPlayer(sender) && guess.equals(DomsPlayer.getPlayer(sender))) {
                    sendMessage(sender, ChatError + "You can't teleport to yourself");
                    return true;
                }
                dest = guess.getLocation();
            } else {
                plyr = guess;
            }
        }
        
        if(args.length > 1) {
            DomsPlayer guess2 = DomsPlayer.guessPlayer(sender, args[1]);
            if(guess2 == null) {
                //Try to get as coords
                String c = "";
                for(int i = 0; i < args.length; i++) {
                    if(DomsPlayer.guessPlayer(sender, args[i]) != null) continue;
                    c += args[i].replaceAll(",", "").replaceAll(" ", "") + ",";
                }

                DomsLocation guessLocation = DomsLocation.guessLocation(c);
                if(guessLocation == null) {
                    sendMessage(sender, ChatError + "Please enter valid coords.");
                    return true;
                }
            
                if(!hasPermission(sender, "DomsCommands.teleport.coordinates.others")) {
                    return this.noPermission(sender, cmd, label, args);
                }

                dest = guessLocation;
            } else {
                if(!hasPermission(sender, "DomsCommands.teleport.others")) {
                    return this.noPermission(sender, cmd, label, args);
                }
                
                if(guess != null && guess2.equals(guess)) {
                    sendMessage(sender, ChatError + "You can't teleport to the same player.");
                }
                
                dest = guess2.getLocation();
            }
        }
        
        if(dest == null) {
            sendMessage(sender, ChatError + "Please enter a valid destination.");
            return true;
        }
        
        if(plyr == null || !plyr.isOnline(sender) || plyr.isConsole()) {
            sendMessage(sender, ChatError + "Please enter a player to teleport");
            return true;
        }
        
        if(dest.getWorld() == null) {
            dest.setWorld(plyr.getLocation().getWorld());
        }
        
        if(dest.getBukkitWorld() == null) {
            sendMessage(sender, ChatError + "Invalid world.");
            return true;
        }
        
        plyr.setBackLocation(plyr.getLocation());
        plyr.teleport(dest);
        sendMessage(sender, ChatDefault + "Teleporting...");
        return true;
    }
}
