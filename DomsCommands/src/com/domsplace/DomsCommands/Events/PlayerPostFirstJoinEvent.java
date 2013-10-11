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

package com.domsplace.DomsCommands.Events;

import com.domsplace.DomsCommands.Bases.DomsEvent;
import com.domsplace.DomsCommands.Objects.DomsPlayer;

/**
 * @author      Dominic
 * @since       08/10/2013
 */
public class PlayerPostFirstJoinEvent extends DomsEvent {
    private DomsPlayer player;
    
    public PlayerPostFirstJoinEvent(DomsPlayer player) {
        this.player = player;
    }
    
    public DomsPlayer getPlayer() {return this.player;}
}
