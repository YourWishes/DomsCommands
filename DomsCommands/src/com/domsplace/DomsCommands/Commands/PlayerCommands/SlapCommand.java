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

package com.domsplace.DomsCommands.Commands.PlayerCommands;

import com.domsplace.DomsCommands.Bases.BukkitCommand;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import com.domsplace.DomsCommands.Objects.SubCommandOption;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.Vector;

/**
 *
 * @author Dominic Masters
 */
public class SlapCommand extends BukkitCommand {
    private static final double SLAP_FORCE = 1.5;
    
    public SlapCommand() {
        super("slap");
        this.addSubCommandOption(SubCommandOption.PLAYERS_OPTION);
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length < 1) {
            sendMessage(sender, ChatError + "Please enter a player name.");
            return true;
        }
        
        DomsPlayer player = DomsPlayer.getPlayer(sender);
        DomsPlayer target = DomsPlayer.guessPlayer(sender, args[0]);
        if(target == null || !target.isOnline(sender)) {
            sendMessage(sender, ChatError + args[0] + " isn't online.");
            return true;
        }
        
        Vector v = target.getOnlinePlayer().getVelocity();
        v.setX(v.getX() + diceDouble(-SLAP_FORCE, SLAP_FORCE));
        v.setY(v.getY() + diceDouble(0, SLAP_FORCE));
        v.setZ(v.getZ() + diceDouble(-SLAP_FORCE, SLAP_FORCE));
        target.getOnlinePlayer().setVelocity(v);
        
        broadcast(ChatImportant + player.getDisplayName() + ChatDefault + 
                " slapped " + ChatImportant + target.getDisplayName() + 
                ChatDefault + "!");
        return true;
    }
}
