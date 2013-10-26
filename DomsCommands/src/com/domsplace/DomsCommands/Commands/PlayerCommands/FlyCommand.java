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

package com.domsplace.DomsCommands.Commands.PlayerCommands;
    
import com.domsplace.DomsCommands.Bases.BukkitCommand;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author      Dominic
 * @since       17/10/2013
 */
public class FlyCommand extends BukkitCommand {
    public FlyCommand() {
        super("fly");
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        DomsPlayer player = DomsPlayer.getPlayer(sender);
        if(args.length > 0) {
            player = DomsPlayer.guessPlayer(sender, args[0]);
        }
        
        if(player == null || !player.isOnline(sender) || player.isConsole()) {
            if(player != null && player.isConsole()) {
                sendMessage(sender, ChatError + "Can't change Console's Flight mode.");
                return true;
            }
            sendMessage(sender, ChatError + "Couldn't find player.");
            return true;
        }
        
        DomsPlayer send = DomsPlayer.getPlayer(sender);
        if(!send.equals(player) && !send.hasPermisson("DomsCommands.fly.others")) return this.noPermission(sender, cmd, label, args);
        if(!send.equals(player) && player.hasPermisson("DomsCommands.fly.exempt")) {
            sendMessage(sender, ChatError + "You don't have permission to change " + player.getDisplayName() + ChatError + "'s flight mode.");
            return true;
        }
        
        boolean f = player.isFlightMode();
        if(f) {
            player.getOnlinePlayer().setAllowFlight(false);
            player.sendMessage("Fly mode disabled.");
            if(!player.equals(send)) {
                sendMessage(sender, "Turned " + ChatImportant + player.getDisplayName() + ChatDefault + "'s fly mode off.");
            }
        } else {
            player.getOnlinePlayer().setAllowFlight(true);
            player.sendMessage("Fly mode enabled.");
            if(!player.equals(DomsPlayer.getPlayer(sender))) {
                sendMessage(sender, "Turned " + ChatImportant + player.getDisplayName() + ChatDefault + "'s fly mode on.");
            }
        }
        return true;
    }
}
