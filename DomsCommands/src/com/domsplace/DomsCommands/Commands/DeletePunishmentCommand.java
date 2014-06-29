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

import com.domsplace.DomsCommands.Bases.BukkitCommand;
import com.domsplace.DomsCommands.Objects.Punishment;
import com.domsplace.DomsCommands.Objects.SubCommandOption;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Dominic Masters
 */
public class DeletePunishmentCommand extends BukkitCommand {    
    public DeletePunishmentCommand() {
        super("deletepunishment");
        this.addSubCommandOption(SubCommandOption.PLAYERS_OPTION);
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length < 1) {
            sendMessage(sender, ChatError + "Please enter a warn/kick/ban/mute ID.");
            return false;
        }
        
        if(!isLong(args[0])) {
            sendMessage(sender, ChatError + "ID must be a number!");
            return false;
        }
        
        Punishment tryPun = Punishment.getByID(getLong(args[0]));
        if(tryPun == null) {
            sendMessage(sender, ChatError + "No Punishment by that ID exists.");
            return true;
        }
        
        tryPun.delete();
        sendMessage(sender, ChatImportant + "Removed Punishment!");
        tryPun.getPlayer().save();
        return true;
    }
}
