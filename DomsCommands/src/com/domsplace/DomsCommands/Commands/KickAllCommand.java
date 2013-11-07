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

import com.domsplace.DomsCommands.Bases.Base;
import com.domsplace.DomsCommands.Bases.BukkitCommand;
import com.domsplace.DomsCommands.Enums.PunishmentType;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import com.domsplace.DomsCommands.Objects.Punishment;
import com.domsplace.DomsCommands.Objects.SubCommandOption;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author      Dominic
 * @since       07/11/2013
 */
public class KickAllCommand extends BukkitCommand {
    public KickAllCommand() {
        super("kickall");
        this.addSubCommandOption(new SubCommandOption("reason"));
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        String reason = "Kicked by an operator";
        if(args.length > 0) {
            reason = Base.arrayToString(args, " ");
        }
        
        DomsPlayer s = DomsPlayer.getPlayer(sender);
        for(DomsPlayer p : DomsPlayer.getOnlinePlayers()) {
            if(p.compare(sender)) continue;
            Punishment pun = new Punishment(s, PunishmentType.KICK, reason);
            p.addPunishment(pun);
            p.kickPlayer(ChatDefault + "You have been kicked for " + ChatImportant + colorise(reason) + ChatDefault + ".");
        }
        
        sendMessage(sender, "Kicked all players for " + ChatImportant + colorise(reason) + ChatDefault + "!");
        return true;
    }
}
