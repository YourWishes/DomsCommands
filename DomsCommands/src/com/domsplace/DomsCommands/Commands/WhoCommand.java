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
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author      Dominic
 * @since       26/10/2013
 */
public class WhoCommand extends BukkitCommand {
    public WhoCommand() {
        super("who");
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(!DomsPlayer.getPlayer(sender).isVisible()) sendMessage(sender, "You are invisible, you won't appear on this.");
        sendMessage(sender, new String[] {
            ChatImportant + getConfigManager().format("There are currently {NUMPLAYERS}/{TOTALPLAYERS} players online."),
            getConfigManager().format("{PLAYERS}")
        });
        return true;
    }
}
