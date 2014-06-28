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

import com.domsplace.DomsCommands.Bases.Base;
import com.domsplace.DomsCommands.Bases.DomsListener;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.SignChangeEvent;

/**
 *
 * @author Dominic Masters
 */
public class SignListener extends DomsListener {
    @EventHandler
    public void handleSignColor(SignChangeEvent e) {
        if(e.getPlayer() == null) return;
        if(e.getBlock() == null) return;
        String[] lines = e.getLines();
        for(int i = 0; i < lines.length; i++) {
            lines[i] = Base.coloriseByPermission(lines[i], DomsPlayer.getDomsPlayerFromPlayer(e.getPlayer()), "DomsCommands.sign.colors.");
            e.setLine(i, lines[i]);
        }
    }
}
