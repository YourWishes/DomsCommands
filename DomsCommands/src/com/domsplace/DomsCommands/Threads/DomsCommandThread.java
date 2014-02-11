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

package com.domsplace.DomsCommands.Threads;

import com.domsplace.DomsCommands.Bases.BukkitCommand;
import com.domsplace.DomsCommands.Bases.DomsThread;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Dominic Masters
 */
public class DomsCommandThread extends DomsThread {
    //THIS IS AN EXPERIMENTAL COMMAND API TO LESSEN LOADS ON SERVERS
    public static String COMMAND_SUCCESS = "CALLBACKFROMTHREADYES";
    public static String COMMAND_FAILED = "CALLBACKFROMTHREADNO";
    
    private final BukkitCommand command;
    private final CommandSender sender;
    private final Command cmd;
    private final String label;
    private final String[] args;
    
    public DomsCommandThread(BukkitCommand command, CommandSender sender, Command cmd, String label, String[] args) {
        super(0, Integer.MAX_VALUE, false);
        //Should not repeat
        
        this.command = command;
        this.sender = sender;
        this.cmd = cmd;
        this.label = label;
        this.args = args;
    }
    
    @Override
    public void run() {
        try {
            boolean successful = false;

            if(isPlayer(this.sender) && !(getPlayer(sender).isOnline())) return;

            try {
                //debug("Success: " + this.command.cmd(sender, cmd, label, args));
                successful = this.command.cmd(sender, cmd, label, args);
            } catch(Throwable e) {
                debug("Error!");
                this.command.onError(sender, cmd, label, args, e);
                successful = false;
            }

            if(!successful) {
                this.command.commandFailed(sender, cmd, label, args);
            } else {
                this.command.commandSuccess(sender, cmd, label, args);
            }

            String x = label + " " + (successful ? COMMAND_SUCCESS : COMMAND_FAILED);

            Bukkit.dispatchCommand(sender, x);
        } catch(Throwable e) {}
        
        try {this.stopThread();} catch(Throwable e) {}
        try {this.deregister();} catch(Throwable e) {}
    }
}
