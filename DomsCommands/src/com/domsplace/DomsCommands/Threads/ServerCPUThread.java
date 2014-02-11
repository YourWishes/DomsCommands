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
import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 *
 * @author Dominic Masters
 */
public class ServerCPUThread extends DomsThread {
    private static double cpu = 20.0;
    
    public static double getCPU() {
        return cpu;
    }
    
    public ServerCPUThread() {
        super(1, 10);
    }
    
    @Override
    public void run() {
        try {
            OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
            RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
            int availableProcessors = operatingSystemMXBean.getAvailableProcessors();
            long prevUpTime = runtimeMXBean.getUptime();
            long prevProcessCpuTime = operatingSystemMXBean.getProcessCpuTime();
            
            double cpuUsage;
            
            try {
                Thread.sleep(10);
            } catch(Exception e) {}
            
            operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
            long upTime = runtimeMXBean.getUptime();
            long processCpuTime = operatingSystemMXBean.getProcessCpuTime();
            long elapsedCpu = processCpuTime - prevProcessCpuTime;
            long elapsedTime = upTime - prevUpTime;

            cpuUsage = Math.min(99F, elapsedCpu / (elapsedTime * 10000F * availableProcessors));
            if(Math.floor(cpuUsage) <= 0.0d) return;
            cpu = cpuUsage;
        } catch(Exception e) {return;}
    }
}
