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
import com.domsplace.DomsCommands.Events.PlayerLeaveGameEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author      Dominic
 * @since       11/10/2013
 */
public class CustomPlayerListener extends DomsListener {
    @EventHandler(ignoreCancelled=true)
    public void handlePlayerLeaveEvent(PlayerQuitEvent e) {
        PlayerLeaveGameEvent event = new PlayerLeaveGameEvent(e.getPlayer());
        event.fireEvent();
    }
    
    @EventHandler(ignoreCancelled=true)
    public void handlePlayerLeaveEventKicked(PlayerKickEvent e) {
        PlayerLeaveGameEvent event = new PlayerLeaveGameEvent(e.getPlayer());
        event.fireEvent();
    }
}
