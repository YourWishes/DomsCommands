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

package com.domsplace.DomsCommands.Objects;

import com.domsplace.DomsCommands.Enums.TeleportRequestType;

/**
 * @author      Dominic
 * @since       09/10/2013
 */
public class TeleportRequest {
    private DomsPlayer from;
    private DomsPlayer to;
    private TeleportRequestType type;
    
    public TeleportRequest(DomsPlayer from, DomsPlayer to, TeleportRequestType type) {
        this.from = from;
        this.to = to;
        this.type = type;
    }
    
    public DomsPlayer getFrom() {return this.from;}
    public DomsPlayer getTo() {return this.to;}
    public TeleportRequestType getType() {return this.type;}
}
