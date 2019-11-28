/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tela;

import Database.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import oshi.SystemInfo;
import oshi.hardware.*;
import oshi.hardware.CentralProcessor.TickType;
import oshi.software.os.*;
import oshi.software.os.OperatingSystem.ProcessSort;
import oshi.util.FormatUtil;
import oshi.util.Util;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author vitor_silva
 */
public class Monitorar {
    static Database.DatabaseConnection conn;
    public Monitorar(){
        conn = new DatabaseConnection();
        
        conn.openConnection();
    }
    
    public void monitoramento(){
        
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        OperatingSystem os = si.getOperatingSystem();
        GlobalMemory memory = hal.getMemory();
        System.out.println("Processes: " + os.getProcessCount() + ", Threads: " + os.getThreadCount());
        // Sort by highest CPU
        List<oshi.software.os.OSProcess> procs = Arrays.asList(os.getProcesses(5, OperatingSystem.ProcessSort.CPU));

        System.out.println("   PID  %CPU %MEM       VSZ       RSS Name");
        for (int i = 0; i < procs.size(); i++) {
            oshi.software.os.OSProcess p = procs.get(i);
            p.getProcessID();
            System.out.format(" %5d %5.1f %4.1f %9s %9s %s%n", p.getProcessID(),
                    100d * (p.getKernelTime() + p.getUserTime()) / p.getUpTime(),
                    100d * p.getResidentSetSize() / memory.getTotal(), FormatUtil.formatBytes(p.getVirtualSize()),
                    FormatUtil.formatBytes(p.getResidentSetSize()), p.getName());
        }
    }
}
