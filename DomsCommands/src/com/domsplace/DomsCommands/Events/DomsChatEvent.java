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

package com.domsplace.DomsCommands.Events;

import com.domsplace.DomsCommands.Bases.DomsCancellableEvent;
import com.domsplace.DomsCommands.Objects.DomsChannel;
import com.domsplace.DomsCommands.Objects.DomsChatFormat;
import com.domsplace.DomsCommands.Objects.DomsPlayer;

/**
 *
 * @author Dominic Masters
 */
public class DomsChatEvent extends DomsCancellableEvent {
    private final DomsPlayer player;
    private DomsChatFormat format;
    private DomsChannel channel;
    
    public DomsChatEvent(DomsPlayer player, DomsChatFormat format, DomsChannel channel) {
        this.player = player;
        this.format = format;
        this.channel = channel;
    }
    
    public DomsPlayer getPlayer() {return this.player;}
    public DomsChatFormat getFormat() {return this.format;}
    public DomsChannel getChannel() {return this.channel;}
    
    public void setFormat(DomsChatFormat format) {this.format = format;}
    public void setChannel(DomsChannel channel) {this.channel = channel;}
}
