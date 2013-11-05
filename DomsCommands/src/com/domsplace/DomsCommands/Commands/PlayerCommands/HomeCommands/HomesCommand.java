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

package com.domsplace.DomsCommands.Commands.PlayerCommands.HomeCommands;
    
import com.domsplace.DomsCommands.Bases.BukkitCommand;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import com.domsplace.DomsCommands.Objects.Home;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author      Dominic
 * @since       24/10/2013
 */
public class HomesCommand extends BukkitCommand {
    public HomesCommand() {
        super("homes");
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(!isPlayer(sender) && args.length < 1) {
            sendMessage(sender, ChatError + "Please enter a player name.");
            return true;
        }
        
        DomsPlayer player = DomsPlayer.getPlayer(sender);
        if(args.length > 0) {
            player = DomsPlayer.guessOnlinePlayer(sender, args[0]);
        }
        
        if(player == null || player.isConsole()) {
            sendMessage(sender, ChatError + "Player not found.");
            return true;
        }
        
        List<String> msgs = new ArrayList<String>();
        msgs.add(ChatImportant + "Homes: (" + player.getHomes().size() + ")");
        
        String s = "";
        List<Home> homes = player.getHomes();
        for(int i = 0; i < homes.size(); i++) {
            Home h = homes.get(i);
            s += h.getName();
            if(i < (homes.size() -1)) s  += ", ";
        }
        msgs.add(s);
        sendMessage(sender, msgs);
        return true;
    }
}
