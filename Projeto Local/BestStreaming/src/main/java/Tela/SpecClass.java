/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tela;

import LoginScreen.LoginClass;
import static LoginScreen.LoginClass.getConn;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;

/**
 *
 * @author carlos
 */
public class SpecClass extends LoginClass {
    
    public SpecClass() throws IOException{
        
    }
    SystemInfo sysInfo = new SystemInfo();
    
    OperatingSystem sysOpe = sysInfo.getOperatingSystem();
    HardwareAbstractionLayer hardwareLayer = sysInfo.getHardware();
    
    private String sistemaOperacional = sysOpe.getFamily();
    private OperatingSystem versaoSistemaOperacional = sysOpe;
    private String fabricanteComputador = sysInfo.getOperatingSystem().getManufacturer();
    private String marcaComputador = sysInfo.getHardware().getComputerSystem().getManufacturer();
    //private String modeloPlacaMae = sysInfo.getHardware().getComputerSystem().getBaseboard().getModel();
    private long modeloPlacaMae = sysInfo.getHardware().getMemory().getAvailable();
    
    private String modeloProcessador = sysInfo.getHardware().getProcessor().getModel();
    private String ghzProcessador = sysInfo.getHardware().getProcessor().getName();
    private String geracaoProcessador = sysInfo.getHardware().getProcessor().getFamily();
    
    private long totalMemoria = sysInfo.getHardware().getMemory().getTotal(); 
    private Integer qtdMonitores = sysInfo.getHardware().getDisplays().length;
    
    private String frequenciaTotal = FormatUtil.formatHertz(this.hardwareLayer.getProcessor().getMaxFreq());
    
    private String discoTotal = setDiscoTotal();
    
    public Boolean verificarMaquina() throws IOException{
        try{
            Database.DatabaseConnection conn = getConn();
            Connection connection = conn.getConnection();
            String dadosSql = "SELECT * FROM streamer "
                  + "INNER JOIN maquina ON streamer.idStreamer = maquina.idStreamer "
            + "WHERE streamer.idStreamer = "+getIdStreamer();  
          
            PreparedStatement ps = connection.prepareStatement(dadosSql);
            ResultSet rs = ps.executeQuery();
            
            if(!rs.next()){
                return false;
            }else{
                return true;
            }
        }catch(Exception e){
            GeracaoLog.GerarLog.GravarLog("Erro ao recuperar maquina do streamer: "+e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public void criarSpecs() throws IOException{
        
        try{
            Database.DatabaseConnection conn = new Database.DatabaseConnection();
            Connection connection = conn.getConnection();
            
            
            String insertSql = "INSERT INTO maquina (cpu,ram,disco,sistemaOperacional,qtdMonitores,infoProcessador,fabricanteMaquina,idStreamer) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
           
            PreparedStatement ps = connection.prepareStatement(insertSql);
            ps.setDouble(1, getFrequenciaTotal());
            ps.setDouble(2, getTotalMemoria());
            ps.setString(3, getDiscoTotal());
            ps.setString(4, getSistemaOperacional());
            ps.setInt(5, getQtdMonitores());
            ps.setString(6, getGhzProcessador());
            ps.setString(7, getMarcaComputador());
            ps.setInt(8, getIdStreamer());
                
            ps.execute();
                
            JOptionPane.showMessageDialog(null, "Inserido com sucesso");
        }catch(Exception e){
            GeracaoLog.GerarLog.GravarLog("Erro ao atulaizar specs: "+e.getMessage());
        }
    }
    
    public void atualizarSpecs() throws IOException{
        
        try{
            Database.DatabaseConnection conn = new Database.DatabaseConnection();
            Connection connection = conn.getConnection();
            
            
            String insertSql = "UPDATE maquina "
                    + "SET cpu = ?, ram = ?, disco = ?, sistemaOperacional = ?, qtdMonitores = ?, infoProcessador = ?, fabricanteMaquina = ? "
            + "WHERE idStreamer = ?";
           
            PreparedStatement ps = connection.prepareStatement(insertSql);
            ps.setDouble(1, getFrequenciaTotal());
            ps.setDouble(2, getTotalMemoria());
            ps.setString(3, getDiscoTotal());
            ps.setString(4, getSistemaOperacional());
            ps.setInt(5, getQtdMonitores());
            ps.setString(6, getGhzProcessador());
            ps.setString(7, getMarcaComputador());
            ps.setInt(8, getIdStreamer());
                
            ps.execute();
                
            JOptionPane.showMessageDialog(null, "Atualizado com sucesso!"); 
            
        }catch(Exception e){
            GeracaoLog.GerarLog.GravarLog("Erro ao atulaizar specs: "+e.getMessage());
        }
    }
    
    public SystemInfo getSysInfo() {
        return sysInfo;
    }

    public OperatingSystem getSysOpe() {
        return sysOpe;
    }

    public HardwareAbstractionLayer getHardwareLayer() {
        return hardwareLayer;
    }

    public String getSistemaOperacional() {
        return sistemaOperacional;
    }

    public String getModeloProcessador() {
        return modeloProcessador;
    }

    public String getGhzProcessador() {
        return ghzProcessador;
    }

    public String getGeracaoProcessador() {
        return geracaoProcessador;
    }

    public long getTotalMemoria() {
        return totalMemoria;
    }

    public Integer getQtdMonitores() {
        return qtdMonitores;
    }

    public String getMarcaComputador() {
        return marcaComputador;
    }

    public long getModeloPlacaMae() {
        return modeloPlacaMae;
    }

    public OperatingSystem getVersaoSistemaOperacional() {
        return versaoSistemaOperacional;
    }

    public String getFabricanteComputador() {
        return fabricanteComputador;
    }
    
    public Double getFrequenciaTotal(){
        String auxiliar[] = this.frequenciaTotal.split(" ");
        String resposta = auxiliar[0].replaceAll(",",".");
        return Double.valueOf(resposta);
    }
    
    public String getDiscoTotal(){
        return this.discoTotal;
    }
    
    public String setDiscoTotal(){
        String hd = "";
        for (HWDiskStore atual : hardwareLayer.getDiskStores()) {
            
            if(atual.getSize() > 0){
                hd = FormatUtil.formatBytesDecimal((atual.getSize()));
            }
        }   
        
        return hd;
    }
}
