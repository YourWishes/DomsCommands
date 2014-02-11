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

package com.domsplace.DomsCommands.Threads;

import com.domsplace.DomsCommands.Bases.DataManager;
import com.domsplace.DomsCommands.Bases.DomsThread;
import com.domsplace.DomsCommands.Objects.DomsPlayer;

/**
 * @author      Dominic
 * @since       13/10/2013
 */
public class ConfigSaveThread extends DomsThread {
    public ConfigSaveThread() {
        super(300, 600, true);
    }
    
    @Override
    public void run() {
        //log("Saving data...");
        for(DomsPlayer player : DomsPlayer.getOnlinePlayers()) {
            DataManager.PLAYER_MANAGER.savePlayer(player);
        }
    }
}
