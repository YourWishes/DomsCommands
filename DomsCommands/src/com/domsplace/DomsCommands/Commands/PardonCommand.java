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
import com.domsplace.DomsCommands.Enums.PunishmentType;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import com.domsplace.DomsCommands.Objects.Punishment;
import com.domsplace.DomsCommands.Objects.SubCommandOption;
import org.bukkit.Bukkit;
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
        
        DomsPlayer rel;
        if(isIP(args[0])) {
            rel = DomsPlayer.getPlayerByIP(args[0]);
            if(rel == null) {
                Bukkit.unbanIP(args[0]);
                sendMessage(sender, "Unbanned " + args[0]);
                return true;
            }
        } else {
            rel = DomsPlayer.guessPlayerExactly(sender, args[0], true);
        }
        
        if(rel.isConsole() || !rel.isBanned()) {
            sendMessage(sender, ChatError + rel.getDisplayName() + ChatError + " isn't banned.");
            return true;
        }
        
        for(Punishment p : rel.getPunishmentsOfType(PunishmentType.BAN)) {
            p.isPardoned(true);
        }
        
        rel.getOfflinePlayer().setBanned(false);
        if(isIP(args[0])) Bukkit.unbanIP(rel.getLastIP());
        sendMessage(sender, "Pardoned " + ChatImportant + rel.getDisplayName() + ChatDefault + ".");    
        return true;
    }
}
