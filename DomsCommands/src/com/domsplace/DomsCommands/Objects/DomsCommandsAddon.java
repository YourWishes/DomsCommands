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

package com.domsplace.DomsCommands.Objects;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author Dominic Masters
 */
public abstract class DomsCommandsAddon {
    public static final List<DomsCommandsAddon> ADDONS = new ArrayList<DomsCommandsAddon>();
    
    public static void invoke() {
        //Silly bukkit
        int x = (int)Math.random();
    }
    
    private final Plugin plugin;
    
    public DomsCommandsAddon(Plugin p) {this.plugin = p; ADDONS.add(this);}
    public Plugin getPlugin() {return this.plugin;}
    public abstract void disable();
}
