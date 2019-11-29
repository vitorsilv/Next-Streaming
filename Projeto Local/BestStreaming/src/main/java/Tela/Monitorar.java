/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tela;

import Database.DatabaseConnection;
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

/**
 *
 * @author vitor_silva
 */
public class Monitorar {
    
    static Database.DatabaseConnection conn;
    
    private SystemInfo si = new SystemInfo();
    private OperatingSystem os = si.getOperatingSystem();
    private FileSystem fs = os.getFileSystem();
    private HardwareAbstractionLayer hal = si.getHardware();
    
    private Integer idStreamer;
    private String nomeProcesso;
    private String tempoDeUso;
    private Date dataCapturada;
    private int idMaquina;
    private int PID;
    private Double cpu;
    private Double ram;
    private String cpuName;
    private Double totalUsadoCPU;
    
    
    private GlobalMemory memory = hal.getMemory();
    
    public Monitorar(){
        conn = new DatabaseConnection();
        
        conn.openConnection();
    }
    
    public void monitoramento(Integer id){
        this.idStreamer = id;
        //Pegar os 10 primeiros dados de processos de acordo com a memoria
        try{
            List<oshi.software.os.OSProcess> procs = Arrays.asList(os.getProcesses(0, OperatingSystem.ProcessSort.MEMORY));
            
            
            for(int i = 0; i < procs.size(); i++){
                
                oshi.software.os.OSProcess p = procs.get(i);
           
                this.nomeProcesso = p.getName();
                this.PID = p.getProcessID();
                this.cpu = 100d * (p.getKernelTime() + p.getUserTime()) / p.getUpTime();
                
                long minutos = (p.getUpTime() / 60000) % 60;
                long horas = (p.getUpTime() / 3600000);
                String upTime = String.format("%3d:%02d", horas, minutos);
            
                this.tempoDeUso = upTime;
                this.dataCapturada = new Date();
                
                inserirDados();
            }
        }catch(Exception e){
            
        }
    }
    
    
    public Boolean inserirDados(){
        Connection connection = conn.getConnection();

        String selectSql = "INSERT INTO processos ('pid','nomeProcesso') "
                + "values ('"+this.nomeProcesso+"', '"+this.nomeProcesso+"')";
            
        try {
            PreparedStatement ps = connection.prepareStatement(selectSql);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return true;
            }else{
                return false;
            }     
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
