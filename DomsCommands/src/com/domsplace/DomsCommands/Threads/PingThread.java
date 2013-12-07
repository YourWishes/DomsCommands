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

import com.domsplace.DomsCommands.Bases.DomsThread;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import org.bukkit.entity.Player;

/**
 *
 * @author Dominic Masters
 */
public class PingThread extends DomsThread {
    private DomsPlayer player;
    
    public PingThread(DomsPlayer player) {
        super(1,Long.MAX_VALUE, true);
        this.player = player;
    }
    
    @Override
    public void run() {
        this.tick();
        this.stopThread();
    }
    
    public void tick() {
        if(this.player == null) return;
        try {
            
            //Sleep Thread
            Player p = player.getOnlinePlayer();
            
            long before = getNow();
            p.getAddress().getAddress().isReachable(5000);
            long after = getNow();
            
            long diff = after - before;
            sendMessage(this.player, "Your ping: " + ChatImportant + diff + "ms" + ChatDefault + ".");
        } catch(Exception e) {
            sendMessage(this.player, ChatError + "Failed to get ping.");
            e.printStackTrace();
            return;
        }
    }
}
