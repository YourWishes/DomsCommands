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

import com.domsplace.DomsCommands.Bases.DomsCancellableEvent;
import com.domsplace.DomsCommands.Objects.DomsLocation;
import com.domsplace.DomsCommands.Objects.DomsPlayer;

/**
 * @author      Dominic
 * @since       08/10/2013
 */
public class DomsMoveEvent extends DomsCancellableEvent {
    private DomsLocation from;
    private DomsLocation to;
    private DomsPlayer player;
    
    private long lastMoveTime;
    
    public DomsMoveEvent(DomsLocation from, DomsLocation to, DomsPlayer player, long lastMoveTime) {
        this.from = from;
        this.to = to;
        this.player = player;
        this.lastMoveTime = lastMoveTime;
    }
    
    public DomsPlayer getPlayer() {return player;}
    public DomsLocation getFrom() {return this.from;}
    public DomsLocation getTo() {return this.to;}
    public long getLastMoveTime() {return this.lastMoveTime;}
}
