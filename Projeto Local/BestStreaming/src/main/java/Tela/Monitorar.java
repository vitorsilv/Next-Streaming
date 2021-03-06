/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tela;

import Database.DatabaseConnection;
import LoginScreen.LoginClass;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import oshi.SystemInfo;
import oshi.hardware.*;
import oshi.software.os.*;
import oshi.util.FormatUtil;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
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
    private long user;
    private long system;
    private long iowait;
    private String tempoDeUso;
    private String cpuName;
    private Double totalUsadoCPU;
    //HD
    private double espacoTotal = 0;
    private double espacoUsavel = 0;
    private double espacoUsado = 0;
    //DATA
    private Date dataHora;
    
    DecimalFormat df = new DecimalFormat("#.##");
    
    SlackBot.Slack slack = new SlackBot.Slack();
    
    public Monitorar() throws IOException{
        conn = getConn();
    }
    
    public void monitoramento() throws IOException{
        //Pegar os 10 primeiros dados de processos de acordo com a memoria
        try{
            procsTotal = Arrays.asList(os.getProcesses(0, OperatingSystem.ProcessSort.MEMORY));
            
            usoCPU();
            usoRAM();
            usoHD();
                
            this.dataHora = new Date();
            
            if(getIdMaquina()!=0){
                Boolean isSaved = inserirMonitoramento();
                if(isSaved){
                    for(int i = 0; i < 10; i++){

                        oshi.software.os.OSProcess p = procsTotal.get(i);

                        this.nomeProcesso = p.getName();
                        this.PID = p.getProcessID();
                        inserirProcessos();
                    }
                }
            }else{
               JOptionPane.showMessageDialog(null, "Por favor, cadastre sua maquina na tela Specs!");
            }
                
        }catch(Exception e){
            GeracaoLog.GerarLog.GravarLog("Erro no monitoramento: "+e.getMessage());
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    private void usoCPU() throws IOException{
        try{
            cpuName = cpu.getName();

            long[] cpuTicks;
            long[] prevCpuTicks;
            prevCpuTicks = cpu.getSystemCpuLoadTicks();
            Util.sleep(5000);

            cpuTicks = cpu.getSystemCpuLoadTicks();

            user = (cpuTicks[CentralProcessor.TickType.USER.getIndex()] - prevCpuTicks[CentralProcessor.TickType.USER.getIndex()]);
            system = (cpuTicks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevCpuTicks[CentralProcessor.TickType.SYSTEM.getIndex()]);
            iowait = (cpuTicks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevCpuTicks[CentralProcessor.TickType.IOWAIT.getIndex()]);

            long nice = (cpuTicks[CentralProcessor.TickType.NICE.getIndex()] - prevCpuTicks[CentralProcessor.TickType.NICE.getIndex()]);
            long idle = (cpuTicks[CentralProcessor.TickType.IDLE.getIndex()] - prevCpuTicks[CentralProcessor.TickType.IDLE.getIndex()]);      
            long irq = (cpuTicks[CentralProcessor.TickType.IRQ.getIndex()] - prevCpuTicks[CentralProcessor.TickType.IRQ.getIndex()]);
            long softirq = (cpuTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevCpuTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()]);
            long steal = (cpuTicks[CentralProcessor.TickType.STEAL.getIndex()] - prevCpuTicks[CentralProcessor.TickType.STEAL.getIndex()]);

            long totalcpu = user + nice + system + idle + iowait + irq + softirq + steal;

            this.user =   (100 * user) / totalcpu;
            this.system = (100 * system) / totalcpu;
            this.iowait = (100 * iowait) / totalcpu;

            totalUsadoCPU =   (100d * (user + system + iowait)) / totalcpu;
            
            
            totalUsadoCPU = Double.valueOf(df.format(totalUsadoCPU).replaceAll(",", "."));
            
            dataHora = new Date();
        }catch(Exception e){
            GeracaoLog.GerarLog.GravarLog("Erro ao pegar dados da CPU: "+e.getMessage());
        }
    }
    
    private void usoRAM() throws IOException{
        try{
            this.totalDisponivel = memory.getTotal();
            double ramLivre = memory.getAvailable();
            this.totalRamUsado = (totalDisponivel - ramLivre)/1000000000;

            this.porcentagemBarra = (100d * totalRamUsado) / totalDisponivel;
            
            this.totalRamUsado = Double.valueOf(df.format(this.totalRamUsado).replaceAll(",", "."));
            this.totalDisponivel = Double.valueOf(df.format(this.totalDisponivel).replaceAll(",", "."));
                    
            this.dataHora = new Date();
        }catch(Exception e){
            GeracaoLog.GerarLog.GravarLog("Erro ao pegar dados de RAM: "+e.getMessage());
        }
        
    }
    
    private void usoHD() throws IOException{
        try{
            OSFileStore[] teste = fs.getFileStores();

            for (OSFileStore teste1 : teste) {
                espacoTotal += teste1.getTotalSpace();
                espacoUsavel += teste1.getUsableSpace();
            }

            espacoTotal = (((espacoTotal/1024)/1024)/1024);
            espacoUsavel = (((espacoUsavel/1024)/1024)/1024);
            espacoUsado = espacoTotal-espacoUsavel;
            
            espacoUsado = Double.valueOf(df.format(espacoUsado).replaceAll(",", "."));
            espacoTotal = Double.valueOf(df.format(espacoTotal).replaceAll(",", "."));

            dataHora = new Date();
        }catch(Exception e){
            GeracaoLog.GerarLog.GravarLog("Erro ao pegar dados de HD: "+e.getMessage());
        }
        
    }
    
    protected void processosAtivos() throws IOException{
        try{
            procsTotal = Arrays.asList(os.getProcesses(0, OperatingSystem.ProcessSort.MEMORY));
            
            
            for(int i = 0; i < procsTotal.size(); i++){
                
                oshi.software.os.OSProcess p = procsTotal.get(i);
          
                this.nomeProcesso = p.getName();
                this.PID = p.getProcessID();
            }
        }catch(Exception e){
            GeracaoLog.GerarLog.GravarLog("Erro ao pegar processos ativos: "+e.getMessage());
        }
    }
    
    private Boolean inserirMonitoramento() throws IOException{
        Connection connection = conn.getConnection();

        String sql = "INSERT INTO monitoramento (cpu, ram, disco, dataHora, idMaquina) "
                + "values (?,?,?,?,?)";
         
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            
            Object dataSql = new java.sql.Timestamp(this.dataHora.getTime());
            
            ps.setDouble(1, this.totalUsadoCPU);
            ps.setDouble(2, this.totalRamUsado);
            ps.setInt(3,(int) Math.round(this.espacoUsado));
            ps.setObject(4, dataSql);
            ps.setInt(5, getIdMaquina());
            
            ps.execute();
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            GeracaoLog.GerarLog.GravarLog("Erro ao inserir monitoramento: "+e.getMessage());
            return false;
        }
    }
    
    private Boolean inserirProcessos() throws IOException{
        Connection connection = conn.getConnection();
        
        String selectSql = "SELECT TOP 1 idMonitoramento FROM monitoramento "
                + "WHERE idMaquina="+getIdMaquina()+" ORDER BY idMonitoramento DESC";
        
        String selectProcessosBlack = "SELECT nomeProcesso FROM processos "
                + "WHERE idMaquina="+getIdMaquina()+" AND blackList=1";
        
        String insert = "INSERT INTO processos (pid, nomeProcesso, idMaquina, idMonitoramento, blackList) "
                + "values (?, ?, ?, ?, ?)";    
        try {
            //SELECT DO ULTIMO MONITORAMENTO
            PreparedStatement ps = connection.prepareStatement(selectSql);
            ResultSet rs = ps.executeQuery();
            //PROCESSOS EM BLACKLIST
            PreparedStatement psProc = connection.prepareStatement(selectProcessosBlack);
            ResultSet rsProc = psProc.executeQuery();
            //INSERINDO PROCESSOS
            PreparedStatement psInsert = connection.prepareStatement(insert);
            psInsert.setInt(1, this.PID);
            psInsert.setString(2, this.nomeProcesso);
            psInsert.setInt(3, getIdMaquina());
            while(rs.next()){
                psInsert.setInt(4, rs.getInt("idMonitoramento"));
            }
            psInsert.setInt(5, 0);
            if(rsProc.next()){
                while(rsProc.next()){
                    if(rsProc.getString("nomeProcesso").equals(this.nomeProcesso)){
                        psInsert.setInt(5, 1);
                    }else{
                        psInsert.setInt(5, 0);
                    }
                }
            }
            
            psInsert.execute();
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            GeracaoLog.GerarLog.GravarLog("Erro ao inserir processos: "+e.getMessage());
            return false;
        }
    }
    
    public void alerta(){
        Connection connection = conn.getConnection();
        try{
            String selectMoni = "SELECT TOP 1 "
                    + "monitoramento.idMonitoramento ,maquina.idMaquina ,streamer.idStreamer,"
                    + "maquina.cpu as CPUTotal, monitoramento.cpu as CPUUso,"
                    + "maquina.ram as RAMTotal, monitoramento.ram as RAMUso,"
                    + "maquina.disco as HDTotal, monitoramento.disco as HDUso,"
                    + "monitoramento.dataHora "
                    + "FROM streamer "
                    + "INNER JOIN maquina ON streamer.idStreamer = maquina.idStreamer "
                    + "INNER JOIN monitoramento ON maquina.idMaquina = monitoramento.idMaquina "
                + "WHERE monitoramento.idMaquina="+getIdMaquina()+" "
                    + "AND maquina.idStreamer = "+getIdStreamer()+" "
                    + "ORDER BY idMonitoramento DESC";
            PreparedStatement psMoni = connection.prepareStatement(selectMoni);
            ResultSet rs = psMoni.executeQuery();
            
            while(rs.next()){
                
                String dataHora = rs.getDate("dataHora").toString();
                Double cpuTotal = rs.getDouble("CPUTotal");
                Double cpuUso = rs.getDouble("CPUUso");
                Double ramTotal = rs.getDouble("RAMTotal");
                Double ramUso = rs.getDouble("RAMUso");
                Integer discoTotal = rs.getInt("HDTotal");
                Integer discoUso = rs.getInt("HDUso");
                
                Double porcentagemCPU = (cpuUso/cpuTotal)*100;
                Double porcentagemRAM = (ramUso/ramTotal)*100;
                Integer porcentagemHD = (discoUso/discoTotal)*100;
                
                if(porcentagemRAM > 50){
                    slack.enviarMensagem("A sua RAM esta acima de 50% fique atento");
                }else if(porcentagemRAM > 75){
                    slack.enviarMensagem("A sua RAM esta acima de 75% verifique o motivo");
                }else if(porcentagemRAM > 90){
                    slack.enviarMensagem("A sua RAM esta acima de 90%, veja em seu PC o que esta acontecendo urgentemente");
                }
                if(porcentagemCPU > 50){
                    slack.enviarMensagem("A sua CPU esta acima de 50% fique atento");
                }else if(porcentagemCPU > 75){
                    slack.enviarMensagem("A sua CPU esta acima de 75% verifique o motivo");
                }else if(porcentagemCPU > 90){
                    slack.enviarMensagem("A sua CPU esta acima de 90%, veja em seu PC o que esta acontecendo urgentemente");
                }
                if(porcentagemCPU > 70){
                    slack.enviarMensagem("A seu HD esta acima de 70% fique atento");
                }else if(porcentagemCPU > 80){
                    slack.enviarMensagem("A seu HD esta acima de 80% sugerimos uma nova fonte de armazenamento");
                }else if(porcentagemCPU > 90){
                    slack.enviarMensagem("A seu HD esta acima de 90% procure uma fonte de armazenamento");
                }
                
            }
        }catch(Exception e){
            
        }
    }
}
