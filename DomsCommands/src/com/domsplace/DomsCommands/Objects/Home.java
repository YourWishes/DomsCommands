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

/**
 *
 * @author Dominic Masters
 */
public class Home {
    private String name;
    private DomsLocation location;
    private DomsPlayer player;
    
    public Home(String name, DomsLocation location, DomsPlayer player) {
        this.name = name;
        this.location = location;
        this.player = player;
    }
    
    public String getName() {return this.name;}
    public DomsLocation getLocation() {return this.location;}
    public DomsPlayer getPlayer() {return this.player;}
}
