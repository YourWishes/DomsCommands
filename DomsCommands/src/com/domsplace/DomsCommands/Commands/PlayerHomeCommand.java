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
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import com.domsplace.DomsCommands.Objects.Home;
import com.domsplace.DomsCommands.Objects.SubCommandOption;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author      Dominic
 * @since       08/05/2013
 */
public class PlayerHomeCommand extends BukkitCommand {
    public PlayerHomeCommand() {
        super("playerhome");
        this.addSubCommandOption(new SubCommandOption(SubCommandOption.PLAYERS_OPTION, SubCommandOption.HOMES_OPTION));
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length < 1)  {
            sendMessage(sender, ChatError + "Please enter a player name.");
            return false;
        }
        
        DomsPlayer player = DomsPlayer.guessPlayer(sender, args[0]);
        if(player == null) {
            sendMessage(sender, ChatError + "Couldn't not find player by that name.");
            return true;
        }
        
        if(args.length == 1 || !isPlayer(sender)) {
            sendMessage(sender, new String[] {
                ChatImportant + player.getDisplayName() + ChatImportant + "'s Homes: (" + player.getHomes().size() + ")",
                ChatDefault + Base.listToString(player.getHomes(), ChatDefault + ", ")
            });
            return true;
        }
        
        String name = "home";
        if(args.length > 1) {
            name = args[1];
        }
        
        Home home = player.getHome(name);
        if(home == null) {
            sendMessage(sender, ChatError + player.getDisplayName() + ChatError + " doesn't have \"" + name + "\" set.");
            return true;
        }
        
        if(!home.getLocation().isWorldLoaded()) {
            sendMessage(sender, ChatError + "This world is no longer available.");
            return true;
        }
        
        DomsPlayer senderPlayer = DomsPlayer.getPlayer(sender);
        senderPlayer.setBackLocation(senderPlayer.getLocation());
        senderPlayer.teleport(home.getLocation());
        sendMessage(sender, "Going to " + ChatImportant + player.getDisplayName() + ChatDefault + "'s home; \"" + ChatImportant + home + ChatDefault + "\".");
        return true;
    }
}
