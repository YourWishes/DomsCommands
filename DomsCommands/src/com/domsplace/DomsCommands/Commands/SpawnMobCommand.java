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
import com.domsplace.DomsCommands.Objects.DomsEntity;
import com.domsplace.DomsCommands.Objects.DomsLocation;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import com.domsplace.DomsCommands.Objects.SubCommandOption;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author      Dominic
 * @since       12/10/2013
 */
public class SpawnMobCommand extends BukkitCommand {
    public SpawnMobCommand() {
        super("spawnmob");
        this.addSubCommandOption(new SubCommandOption(SubCommandOption.PLAYERS_OPTION, SubCommandOption.MOB_OPTION));
        this.addSubCommandOption(SubCommandOption.MOB_OPTION);
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length < 1) {
            sendMessage(sender, ChatError + "Please enter a player or mob name.");
            return true;
        }
        
        DomsPlayer player = DomsPlayer.getPlayer(sender);
        DomsEntity entity;
        int amt = 1;
        
        if(args.length == 1) {
            entity = DomsEntity.craftEntity(args[0], player);
        } else if(args.length == 2) {
            if(isInt(args[1])) {
                entity = DomsEntity.craftEntity(args[0], player);
                amt = getInt(args[1]);
            } else {
                player = DomsPlayer.guessPlayer(sender, args[0]);
                if(player == null) {
                    sendMessage(sender, ChatError + "Amount must be a number!");
                    return true;
                }
                entity = DomsEntity.craftEntity(args[1], player);
            }
        } else {
            player = DomsPlayer.guessPlayer(sender, args[0]);
            entity = DomsEntity.craftEntity(args[1], player);
            if(!isInt(args[2])) {
                sendMessage(sender, ChatError + "Amount must be a number.");
                return true;
            }
            amt = getInt(args[2]);
        }
        
        if(player == null || player.isConsole() || !player.isOnline(sender)) {
            sendMessage(sender, ChatError + "Couldn't find player.");
            return true;
        }
        
        if(player.getTargetBlock() == null) {
            sendMessage(sender, ChatError + "Can't spawn mobs in air!");
            return true;
        }
        
        if(entity == null || entity.getType() == null) {
            sendMessage(sender, ChatError + "Invalid Mob type.");
            return false;
        }
        
        DomsLocation targetLocation = new DomsLocation(player.getTargetBlock());
        entity.spawn(targetLocation);
        
        sendMessage(sender, "Spawned " + amt + " " + ChatImportant + entity.toHumanString() + ChatDefault + ".");
        return true;
    }
}
