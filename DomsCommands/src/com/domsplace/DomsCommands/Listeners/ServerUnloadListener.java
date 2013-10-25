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
import com.domsplace.DomsCommands.Bases.PluginHook;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;

/**
 *
 * @author Dominic Masters
 */
public class ServerUnloadListener extends DomsListener {
    @EventHandler(ignoreCancelled=true)
    public void handlePluginUnloaded(PluginDisableEvent e) {
        if(e.getPlugin() == null) return;
        PluginHook hook = PluginHook.getHookFromPlugin(e.getPlugin());
        if(hook == null) return;
        hook.unHook();
    }
    
    @EventHandler(ignoreCancelled=true)
    public void handlePluginLoaded(PluginEnableEvent e) {
        if(e.getPlugin() == null) return;
        PluginHook hook = PluginHook.getHookFromPlugin(e.getPlugin());
        if(hook == null) return;
        hook.hook();
    }
}
