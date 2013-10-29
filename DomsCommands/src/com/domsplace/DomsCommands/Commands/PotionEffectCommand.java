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
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import com.domsplace.DomsCommands.Objects.SubCommandOption;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * @author      Dominic
 * @since       28/10/2013
 */
public class PotionEffectCommand extends BukkitCommand {
    public PotionEffectCommand() {
        super("potioneffect");
        this.addSubCommandOption(SubCommandOption.POTION_OPTION);
        this.addSubCommandOption(new SubCommandOption(SubCommandOption.PLAYERS_OPTION, SubCommandOption.POTION_OPTION));
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length < 2 && !isPlayer(sender)) {
            sendMessage(sender, ChatError + "Please enter a player name.");
            return true;
        }
        
        if(args.length < 1) {
            sendMessage(sender, ChatError + "Please enter a potion effect!");
            return true;
        }
        
        DomsPlayer player = DomsPlayer.getPlayer(sender);
        int duration = 3600;
        int amp = 1;
        PotionEffectType pet = null;
        
        if(args.length == 1) {
            pet = PotionEffectType.getByName(args[0].toUpperCase());
        } else if(args.length == 2) {
            DomsPlayer guess = DomsPlayer.guessPlayer(sender, args[0]);
            if(guess != null) {
                player = guess;
                pet = PotionEffectType.getByName(args[1].toUpperCase());
            } else {
                pet = PotionEffectType.getByName(args[0].toUpperCase());
                if(!isInt(args[1])) {
                    sendMessage(sender, ChatError + "Duration must be a number");
                    return true;
                }
                duration = getInt(args[1]);
            }
        } else if(args.length == 3) {
            DomsPlayer guess = DomsPlayer.guessPlayer(sender, args[0]);            
            if(guess != null) {
                player = guess;
                pet = PotionEffectType.getByName(args[1].toUpperCase());
            } else {
                if(!isInt(args[1])) {
                    sendMessage(sender, ChatError + "Duration must be a number");
                    return true;
                }
                if(!isInt(args[2])) {
                    sendMessage(sender, ChatError + "Amplifier must be a number");
                    return true;
                }
                pet = PotionEffectType.getByName(args[0].toUpperCase());
                duration = getInt(args[1]);
                amp = getInt(args[2]);
            }
        } else if(args.length > 3) {
            DomsPlayer guess = DomsPlayer.guessPlayer(sender, args[0]);
            pet = PotionEffectType.getByName(args[1]);
            if(!isInt(args[2])) {
                sendMessage(sender, ChatError + "Duration must be a number");
                return true;
            }
            if(!isInt(args[3])) {
                sendMessage(sender, ChatError + "Amplifier must be a number");
                return true;
            }    
            duration = getInt(args[2]);
            amp = getInt(args[3]);
        }
        
        if(player == null || !player.isOnline(sender) || player.isConsole()) {
            sendMessage(sender, ChatError + "Couldn't find player!");
            return true;
        }
        
        if(duration < 0) {
            sendMessage(sender, ChatError + "Duration must be 0 or more.");
            return true;
        }
        
        if(amp < 0) {
            sendMessage(sender, ChatError + "Potion Duration me be 0 or more");
            return true;
        }
        
        if(pet == null) {
            sendMessage(sender, ChatError + "Invalid Potion Type.");
            return true;
        }
        
        PotionEffect pe = new PotionEffect(pet, duration, amp);
        player.getOnlinePlayer().addPotionEffect(pe, true);
        sendMessage(player, "Got effect " + ChatImportant + pe.getType().getName()
                + (amp > 1 ? ChatDefault + ", level " + ChatImportant + amp : "")
                + ChatDefault + " for " + ChatImportant + (duration / 20) + 
                " second" + ((duration/20) !=  1 ? "s" : "") + ChatDefault + ".");
        if(!DomsPlayer.getPlayer(sender).equals(player)) {
            sendMessage(sender, "Gave effect" + ChatImportant + pe.getType().getName()
                + (amp > 1 ? ChatDefault + ", level " + ChatImportant + amp : "")
                + ChatDefault + " for " + ChatImportant + (duration / 20) + 
                " second" + ((duration/20) !=  1 ? "s" : "") + ChatDefault + " to " + ChatImportant + 
                player.getDisplayName() + ChatDefault + ".");
        }
        return true;
    }
}
