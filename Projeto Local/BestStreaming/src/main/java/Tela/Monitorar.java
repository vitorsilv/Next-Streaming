/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tela;

import Database.DatabaseConnection;
import LoginScreen.LoginClass;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import oshi.SystemInfo;
import oshi.hardware.*;
import oshi.software.os.*;
import oshi.util.FormatUtil;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import oshi.util.Util;

/**
 *
 * @author vitor_silva
 */
public class Monitorar extends LoginClass {
    //CONEXAO DE BANCO
    static Database.DatabaseConnection conn;
    //OSHI
    private SystemInfo si = new SystemInfo();
    private OperatingSystem os = si.getOperatingSystem();
    private FileSystem fs = os.getFileSystem();
    private HardwareAbstractionLayer hal = si.getHardware();
    private GlobalMemory memory = hal.getMemory();
    private final CentralProcessor cpu = hal.getProcessor();
    //private Double cpu;
    //Processos
    private String nomeProcesso;
    private int PID;
    protected List<oshi.software.os.OSProcess> procsTotal;
    //RAM
    private Double ram;
    private double totalDisponivel;
    private double totalRamUsado;
    private double porcentagemBarra;
    //CPU
    private double user;
    private double system;
    private double iowait;
    private String tempoDeUso;
    private String cpuName;
    private Double totalUsadoCPU;
    //DATA
    private Date dataHora;
    
    public Monitorar(){
        conn = new DatabaseConnection();
    }
    
    public void monitoramento(){
        //Pegar os 10 primeiros dados de processos de acordo com a memoria
        try{
            procsTotal = Arrays.asList(os.getProcesses(0, OperatingSystem.ProcessSort.MEMORY));
            
            
            for(int i = 0; i < procsTotal.size(); i++){
                
                oshi.software.os.OSProcess p = procsTotal.get(i);
           
                this.nomeProcesso = p.getName();
                this.PID = p.getProcessID();
                
                usoCPU();
                usoRAM();
                
                this.dataHora = new Date();
                
                //inserirDados();
            }
        }catch(Exception e){
            
        }
    }
    
    private void usoCPU(){
        
        cpuName = cpu.getName();
        
        long[] cpuTicks;
        long[] prevCpuTicks;
        prevCpuTicks = cpu.getSystemCpuLoadTicks();
        Util.sleep(5000);
        
        cpuTicks = cpu.getSystemCpuLoadTicks();
        
        long user = (cpuTicks[CentralProcessor.TickType.USER.getIndex()] - prevCpuTicks[CentralProcessor.TickType.USER.getIndex()]);
        long sys = (cpuTicks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevCpuTicks[CentralProcessor.TickType.SYSTEM.getIndex()]);
        long iowait = (cpuTicks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevCpuTicks[CentralProcessor.TickType.IOWAIT.getIndex()]);
        
        long nice = (cpuTicks[CentralProcessor.TickType.NICE.getIndex()] - prevCpuTicks[CentralProcessor.TickType.NICE.getIndex()]);
        long idle = (cpuTicks[CentralProcessor.TickType.IDLE.getIndex()] - prevCpuTicks[CentralProcessor.TickType.IDLE.getIndex()]);      
        long irq = (cpuTicks[CentralProcessor.TickType.IRQ.getIndex()] - prevCpuTicks[CentralProcessor.TickType.IRQ.getIndex()]);
        long softirq = (cpuTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevCpuTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()]);
        long steal = (cpuTicks[CentralProcessor.TickType.STEAL.getIndex()] - prevCpuTicks[CentralProcessor.TickType.STEAL.getIndex()]);
        
        long totalcpu = user + nice + sys + idle + iowait + irq + softirq + steal;
        
        this.user = (100d * user) / totalcpu;
        this.system = (100d * sys) / totalcpu;
        this.iowait = (100d * iowait) / totalcpu;
        
        totalUsadoCPU = (100d * (user + sys + iowait)) / totalcpu;
        
        dataHora = new Date();
        
    }
    
    private void usoRAM(){
        
        
        this.totalDisponivel = memory.getTotal();
        double ramLivre = memory.getAvailable();
        this.totalRamUsado = totalDisponivel - ramLivre;
        
        this.porcentagemBarra = (100d * totalRamUsado) / totalDisponivel;
        
        this.dataHora = new Date();
        
    }
    
    protected void processosAtivos(){
        try{
            procsTotal = Arrays.asList(os.getProcesses(0, OperatingSystem.ProcessSort.MEMORY));
            
            
            for(int i = 0; i < procsTotal.size(); i++){
                
                oshi.software.os.OSProcess p = procsTotal.get(i);
          
                this.nomeProcesso = p.getName();
                this.PID = p.getProcessID();
            }
        }catch(Exception e){
            
        }
    }
    
    public Boolean inserirDados(){
        Connection connection = conn.getConnection();

        String selectSql = "INSERT INTO monitoramento ('pid','nomeProcesso') "
                + "values ('"+this.PID+"', '"+this.nomeProcesso+"')";
            
        try {
            PreparedStatement ps = connection.prepareStatement(selectSql);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
