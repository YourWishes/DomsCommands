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
import com.domsplace.DomsCommands.Exceptions.InvalidItemException;
import com.domsplace.DomsCommands.Objects.DomsItem;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import com.domsplace.DomsCommands.Objects.SubCommandOption;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;

/**
 *
 * @author Dominic Masters
 */
public class GetIDCommand extends BukkitCommand {
    public GetIDCommand() {
        super("getid");
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length < 1 && !isPlayer(sender)) {
            sendMessage(sender, ChatError + "Please enter an Item Name or ID");
            return false;
        }
        
        DomsItem item;
        
        if(args.length > 0) {
            String itm = "";
            for(int i = 0; i < args.length; i++) {
                itm += args[i];
                if(i < args.length - 1) itm += " ";
            }
            
            try {
                item = DomsItem.guessItem(itm);
            } catch (InvalidItemException e) {
                sendMessage(sender, ChatError + "Invalid item.");
                return true;
            }
        } else {
            DomsPlayer player = DomsPlayer.getPlayer(sender);
            try {
                item = DomsItem.itemStackToDomsItems(player.getOnlinePlayer().getItemInHand()).get(0);
            } catch(Exception e) {
                sendMessage(sender, ChatError + "You're holding an invalid item.");
                return true;
            }
        }
        
        if(item == null) {
            sendMessage(sender, ChatError + "Invalid item.");
            return true;
        }
        
        List<String> info = new ArrayList<String>();
        
        if(item.isAir()) {
            info.add(ChatImportant + "Information about " + ChatDefault + "Air");
        } else {
            info.add(ChatImportant + "Information about " + ChatDefault + item.getTypeName());
        }
        
        info.add(ChatImportant + "ID: " + ChatDefault + item.getID());
        
        if(item.hasData()) {
            info.add(ChatImportant + "Data: " + ChatDefault + item.getData());
        }
        
        if(item.getName() != null) {
            info.add(ChatImportant + "Name: " + ChatDefault + item.getName());
        }
        
        if(item.isBook()) {
            String auth = item.getBookAuthor();
            List<String> s = item.getBookPages();
            if(auth != null && !auth.equals("")) info.add(ChatImportant + "Author: " + ChatDefault + auth);
            if(s != null && s.size() > 0) info.add(ChatImportant + "Pages: " + ChatDefault + s.size());
        }
        
        if(item.getEnchantments() != null) {
            for(Enchantment e : item.getEnchantments().keySet()) {
                int l = item.getEnchantments().get(e);
                info.add(ChatImportant + "Enchantment: " + ChatDefault + e.getName() + " lvl " + l);
            }
        }
        
        if(item.getLores() != null && item.getLores().size() > 0) {
            info.add(ChatImportant + "Lores: " + ChatDefault + item.getLores().size());
        }
        
        sendMessage(sender, info);
        return true;
    }
}
