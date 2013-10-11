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

package com.domsplace.DomsCommands.Listeners;

import com.domsplace.DomsCommands.Bases.DomsListener;
import com.domsplace.DomsCommands.Events.PreCommandEvent;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

/**
 * @author      Dominic
 * @since       08/10/2013
 */
public class CustomEventCommandListener extends DomsListener {
    
    @EventHandler(ignoreCancelled=true)
    public void handlePreCommandEvent(PlayerCommandPreprocessEvent e) {
        if(e.isCancelled()) return;
        if(e.getPlayer() == null) return;
        if(e.getMessage().equalsIgnoreCase(" ")) return;
        
        if(!e.getMessage().replaceAll(" ", "").startsWith("/")) return;
        
        String[] parts = e.getMessage().split(" ");
        if(parts.length < 1) return;
        
        String command = parts[0].replaceFirst("/", "");
        List<String> args = new ArrayList<String>();
        if(parts.length > 1) {for(int i = 1; i < parts.length; i++) {args.add(parts[i]);}}
        
        PreCommandEvent event = new PreCommandEvent(e.getPlayer(), command, args);
        event.fireEvent();
        if(event.isCancelled()) e.setCancelled(event.isCancelled());
    }
    
    @EventHandler(ignoreCancelled=true)
    public void handlePreServerCommandEvent(ServerCommandEvent e) {
        if(e.getSender() == null) return;
        if(e.getCommand().equalsIgnoreCase(" ")) return;
        
        //All Server stuffs are commans ;)
        //if(!e.getCommand().replaceAll(" ", "").startsWith("/")) return;
        
        String[] parts = e.getCommand().split(" ");
        if(parts.length < 1) return;
        
        String command = parts[0];
        List<String> args = new ArrayList<String>();
        if(parts.length > 1) {for(int i = 1; i < parts.length; i++) {args.add(parts[i]);}}
        
        PreCommandEvent event = new PreCommandEvent(e.getSender(), command, args);
        event.fireEvent();
        if(event.isCancelled()) {
            //TODO: Need a better way to cancel server commands..
            e.setCommand("donothing");
        }
    }
}
