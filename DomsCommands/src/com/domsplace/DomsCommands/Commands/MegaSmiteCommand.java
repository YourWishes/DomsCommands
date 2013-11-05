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
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author      Dominic
 * @since       30/10/2013
 */
public class MegaSmiteCommand extends BukkitCommand {
    public MegaSmiteCommand() {
        super("megasmite");
        this.addSubCommandOption(SubCommandOption.PLAYERS_OPTION);
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(!isPlayer(sender) && args.length < 1) {
            sendMessage(sender, ChatError + "Please enter a player name");
            return true;
        }
        
        DomsLocation target = null;
        DomsPlayer targetPlayer = null;
        if(args.length > 0) {
            DomsPlayer guess = DomsPlayer.guessOnlinePlayer(sender, args[0]);
            if(guess == null || guess.isConsole() || !guess.isOnline(sender)) {
                sendMessage(sender, ChatError + "Can't smite this player.");
                return true;
            }
            target = guess.getLocation();
            targetPlayer = guess;
        } else {
            target = DomsPlayer.getPlayer(sender).getTarget();
        }
        
        if(target == null || !target.isWorldLoaded()) {
            sendMessage(sender, ChatError + "Invalid Location");
            return true;
        }
        
        if(targetPlayer != null) {
            sendMessage(targetPlayer, "Thou hath been smitten!");
            if(!targetPlayer.compare(sender)) {
                sendMessage(sender, "Smote " + ChatImportant + targetPlayer.getOnlinePlayer());
            }
        }
        for(int i = 0; i < 15; i++) {
            target.getBukkitWorld().strikeLightning(target.toLocation());
        }
        return true;
    }
}
