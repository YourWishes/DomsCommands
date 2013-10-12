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

import com.domsplace.DomsCommands.Bases.BukkitCommand;
import com.domsplace.DomsCommands.Enums.PunishmentType;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import com.domsplace.DomsCommands.Objects.Punishment;
import com.domsplace.DomsCommands.Objects.SubCommandOption;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author      Dominic
 * @since       12/10/2013
 */
public class PardonCommand extends BukkitCommand {
    public PardonCommand() {
        super("pardon");
        this.addSubCommandOption(SubCommandOption.PLAYERS_OPTION);
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length < 1) {
            sendMessage(sender, ChatError + "Please enter a player name to pardon.");
            return false;
        }
        
        OfflinePlayer op = getOfflinePlayer(sender, args[0]);
        DomsPlayer rel = DomsPlayer.getPlayer(op);
        
        if(!rel.isBanned()) {
            sendMessage(sender, ChatError + rel.getDisplayName() + ChatError + " isn't banned.");
            return true;
        }
        
        for(Punishment p : rel.getPunishmentsOfType(PunishmentType.BAN)) {
            p.isPardoned(true);
        }
        
        sendMessage(sender, "Pardoned " + ChatImportant + rel.getDisplayName() + ChatDefault + ".");    
        return true;
    }
}
