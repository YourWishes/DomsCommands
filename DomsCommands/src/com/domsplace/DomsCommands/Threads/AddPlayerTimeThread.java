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

import com.domsplace.DomsCommands.Bases.DomsThread;
import com.domsplace.DomsCommands.Objects.DomsPlayer;

/**
 * @author      Dominic
 * @since       13/10/2013
 */
public class AddPlayerTimeThread extends DomsThread {
    public AddPlayerTimeThread() {
        super(1, 1, true);
    }
    
    @Override
    public void run() {
        //Adds 1000 milliseconds to a players play time (1 second)
        for(DomsPlayer plyr : DomsPlayer.getOnlinePlayers()) {
            plyr.addPlayTime(1000);
        }
    }
}
