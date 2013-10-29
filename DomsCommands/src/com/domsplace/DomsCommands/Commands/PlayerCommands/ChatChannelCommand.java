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
import com.domsplace.DomsCommands.Objects.DomsChannel;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import com.domsplace.DomsCommands.Objects.SubCommandOption;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author      Dominic
 * @since       12/10/2013
 */
public class ChatChannelCommand extends BukkitCommand {
    public ChatChannelCommand() {
        super("chatchannel");
        this.addSubCommandOption(SubCommandOption.CHANNELS_OPTION);
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length < 1) {
            sendMessage(sender, ChatError + "Please enter a chat channel.");
            return false;
        }
        
        DomsChannel channel = DomsChannel.getChannel(args[0]);
        if(channel == null) channel = DomsChannel.getChannelByCommand(args[0]);
        
        if(channel == null) {
            sendMessage(sender, ChatError + "Couldn't find chat channel.");
            return true;
        }
        
        DomsPlayer player = DomsPlayer.getPlayer(sender);
        if(!player.hasPermisson(channel.getChatPermission())) return this.noPermission(sender, cmd, label, args);
        
        if(args.length == 1) {
            boolean t = player.toggleChannel(channel);
            if(t) sendMessage(player, "Joined channel " + ChatImportant + channel.getName());
            else sendMessage(player, "Left the channel " + ChatImportant + channel.getName());
            return true;
        }
        
        String message = "";
        for(int i = 1; i < args.length; i++) {
            message += args[i];
            if(i < args.length - 1) message += " ";
        }
        channel.chat(player, message);
        return true;
    }
}
