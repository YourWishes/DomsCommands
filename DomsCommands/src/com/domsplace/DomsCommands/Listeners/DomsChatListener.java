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

package com.domsplace.DomsCommands.Listeners;

import com.domsplace.DomsCommands.Bases.DomsListener;
import com.domsplace.DomsCommands.Events.DomsChatEvent;
import com.domsplace.DomsCommands.Objects.DomsChannel;
import com.domsplace.DomsCommands.Objects.DomsChatFormat;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 *
 * @author Dominic Masters
 */
public class DomsChatListener extends DomsListener {
    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    public void handleChat(AsyncPlayerChatEvent e) {
        e.setCancelled(true);
        Player p = e.getPlayer();
        
        DomsPlayer player = DomsPlayer.getPlayer(p);
        DomsChannel channel = player.getChannel();
        DomsChatFormat format = channel.getFormat(player);
        
        DomsChatEvent event = new DomsChatEvent(player, format, channel);
        event.fireEvent();
        if(event.isCancelled()) return;
        
        channel.chat(player, event.getFormat(), e.getMessage());
    }
}
