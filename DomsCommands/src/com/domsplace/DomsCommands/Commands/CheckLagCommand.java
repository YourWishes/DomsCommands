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

package com.domsplace.DomsCommands.Commands;

import com.domsplace.DomsCommands.Bases.BukkitCommand;
import com.domsplace.DomsCommands.Threads.ServerTPSThread;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Dominic Masters
 */
public class CheckLagCommand extends BukkitCommand {
    public CheckLagCommand() {
        super("checklag");
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> msgs = new ArrayList<String>();
        
        msgs.add(ChatDefault + "TPS: " + ChatImportant + twoDecimalPlaces(ServerTPSThread.getTPS()) + ChatDefault + "/" + ChatImportant + "20.00");
        
        Runtime r = Runtime.getRuntime();
        
        double mb = 1024d*1024d;
        
        double used = (r.totalMemory() - r.freeMemory()) / mb;
        double total = r.totalMemory() / mb;
        double max = r.maxMemory() / mb;
        double perc = used/total*100;
        msgs.add(ChatDefault + "Memory Usage: " + ChatImportant + twoDecimalPlaces(used) + "MB " + ChatDefault + "(" + ChatImportant + twoDecimalPlaces(perc) + "%" + ChatDefault + ")");
        msgs.add(ChatDefault + "Total Memory: " + ChatImportant + twoDecimalPlaces(total) + "MB");
        msgs.add(ChatDefault + "Max Memory: " + ChatImportant + twoDecimalPlaces(max) + "MB");
        
        for(World w : Bukkit.getWorlds()) {
            msgs.add(ChatDefault + w.getName() + ": " + ChatImportant + w.getEntities().size() + ChatDefault + " entities - " + ChatImportant + w.getLoadedChunks().length + ChatDefault + " chunks.");
        }
        
        r.gc();
        used = (r.totalMemory() - r.freeMemory()) / mb;
        total = r.totalMemory() / mb;
        perc = used/total*100;
        msgs.add(ChatDefault + "Memory Usage (Post GC): " + ChatImportant + twoDecimalPlaces(used) + "MB " + ChatDefault + "(" + ChatImportant + twoDecimalPlaces(perc) + "%" + ChatDefault + ")");
        sendMessage(sender, msgs);
        return true;
    }
}
