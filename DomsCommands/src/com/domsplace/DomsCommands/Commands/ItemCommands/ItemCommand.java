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

import com.domsplace.DomsCommands.Bases.BukkitCommand;
import com.domsplace.DomsCommands.Exceptions.InvalidItemException;
import com.domsplace.DomsCommands.Objects.DomsItem;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import com.domsplace.DomsCommands.Objects.SubCommandOption;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Dominic Masters
 */
public class ItemCommand extends BukkitCommand {
    public ItemCommand() {
        super("item");
        this.addSubCommandOption(
            new SubCommandOption(SubCommandOption.ITEM_OPTION, "amount")
        );
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(!isPlayer(sender)) {
            sendMessage(sender, ChatError + "Only players can run this command.");
            return true;
        }
        
        if(args.length < 1) {
            sendMessage(sender, ChatError + "Please enter an item name/item id.");
            return true;
        }
        
        DomsPlayer player = DomsPlayer.getPlayer(sender);
        
        int arrEnd = args.length;
        short data = DomsItem.BAD_DATA;
        
        int size = 64;
        if(args.length > 1) {
            if(args.length > 2 && isShort(args[args.length-1]) && isInt(args[args.length - 2])) {
                size = getInt(args[args.length - 2]);
                data = getShort(args[args.length-1]);
                arrEnd -= 2;
            } else {
                if(isInt(args[args.length - 1])) {
                    size = getInt(args[args.length - 1]);
                    arrEnd -= 1;
                }
            }
        }
        
        String itemStr = "";
        for(int i = 0; i < arrEnd; i++) {
            itemStr += args[i];
            if(i < arrEnd-1) itemStr += " ";
        }
        
        DomsItem item;
        try {
            item = DomsItem.guessItem(itemStr);
            if(item.isAir()) throw new InvalidItemException(item.toString());
        } catch(InvalidItemException e) {
            sendMessage(sender, ChatError + "Not a valid item.");
            return true;
        }
        
        if(data != DomsItem.BAD_DATA && data >= 0) {
            item.setData(data);
        }
        
        player.addItems(DomsItem.multiply(item, size));
        sendMessage(player, ChatDefault + "You have been given " + ChatImportant + size + ChatDefault + " of " + ChatImportant + item.toHumanString()+ ChatDefault + ".");
        return true;
    }
}
