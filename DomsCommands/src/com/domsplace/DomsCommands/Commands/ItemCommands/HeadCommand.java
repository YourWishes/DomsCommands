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

package com.domsplace.DomsCommands.Commands.ItemCommands;

import com.domsplace.DomsCommands.Bases.Base;
import com.domsplace.DomsCommands.Bases.BukkitCommand;
import com.domsplace.DomsCommands.Objects.DomsItem;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import com.domsplace.DomsCommands.Objects.SubCommandOption;
import java.util.List;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Dominic Masters
 */
public class HeadCommand extends BukkitCommand {
    public HeadCommand() {
        super("head");
        this.addSubCommandOption(SubCommandOption.PLAYERS_OPTION);
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(!isPlayer(sender)) {
            sendMessage(sender, ChatError + "Only players can run this command.");
            return true;
        }
        
        if(args.length < 1) {
            sendMessage(sender, ChatError + "Please enter a player name.");
            return true;
        }
        
        DomsPlayer p = DomsPlayer.getPlayer(sender);
        DomsItem held = DomsItem.createItem(p.getOnlinePlayer().getItemInHand());
        if(held == null || !held.isHead()) {
            sendMessage(sender, ChatError + "Can't apply head to this type.");
            return true;
        }
        
        OfflinePlayer player = Base.getOfflinePlayer(sender, args[0]);
        if(player == null || player.getName() == null) {
            sendMessage(sender, ChatError + "Couldn't find that player.");
            return true;
        }
        held.setPlayerHead(player);
        
        List<DomsItem> item = DomsItem.multiply(held, p.getOnlinePlayer().getItemInHand().getAmount());
        p.getOnlinePlayer().setItemInHand(DomsItem.createItem(item));
        sendMessage(sender, "Giving you " + ChatImportant + held.getPlayerHead().getName() + ChatDefault + "'s head.");
        return true;
    }
}
