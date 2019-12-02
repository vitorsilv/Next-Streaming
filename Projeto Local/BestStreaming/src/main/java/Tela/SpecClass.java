/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tela;

import LoginScreen.LoginClass;
import static LoginScreen.LoginClass.getConn;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

/**
 *
 * @author carlos
 */
public class SpecClass extends LoginClass{
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
        
    public void atualizarSpecs(){
        try{
            Database.DatabaseConnection conn = getConn();
            Connection connection = conn.getConnection();
            
            
            String insertSql = "INSERT INTO maquina (cpu,ram,disco,idStreamer) "
            + "VALUES (?, ?, ?, ?)";
           
            PreparedStatement ps = connection.prepareStatement(insertSql);
            ps.setString(1, "AA");
            ps.setBoolean(2, true);
            ps.setInt(3, getIdMaquina());
                
            ps.execute();
                
            JOptionPane.showMessageDialog(null, "Inserido com sucesso"); 
            
        }catch(Exception e){
            
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
}
