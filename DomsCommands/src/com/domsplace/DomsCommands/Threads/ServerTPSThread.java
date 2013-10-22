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

package com.domsplace.DomsCommands.Threads;

import com.domsplace.DomsCommands.Bases.DomsThread;

/**
 *
 * @author Dominic Masters
 */
public class ServerTPSThread extends DomsThread {
    private static double tps = 20.0;
    
    public static double getTPS() {
        return tps;
    }
    
    //Instance
    private long lastCheck = getNow();
    
    public ServerTPSThread() {
        super(1, 1);
    }
    
    @Override
    public void run() {
        long now = getNow();
        
        double diff = now - lastCheck;
        try {
            tps = (tps + (20d / (diff / 1000d)))/2d;
        } catch(Exception e) {return;}
        
        lastCheck = now;
    }
}
