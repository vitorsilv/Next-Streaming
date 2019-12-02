/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tela;

import LoginScreen.LoginClass;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;


/**
 *
 * @author vitor_silva
 */
public class OtimizarProcessos extends LoginClass{
    
    List<String> processosKill = new ArrayList<>();
    
    public Boolean salvarBlackList(Integer PID,String nomeProcesso){
        try{
            Database.DatabaseConnection conn = getConn();
            Connection connection = conn.getConnection();
            
            String insertSql = "INSERT INTO processos (pid,nomeProcesso,blacklist,idMaquina) "
            + "VALUES (?, ?, ?, ?)";
           
            PreparedStatement ps = connection.prepareStatement(insertSql);
            
            ps.setInt(1, PID);
            ps.setString(2, nomeProcesso);
            ps.setBoolean(3, true);
            ps.setInt(4, getIdMaquina());
                
            ps.execute();
                
            return true;
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e); 
            return false;
        }    
    }
    
    public DefaultListModel blackListBanco(){
        Database.DatabaseConnection conn = getConn();
        Connection connection = conn.getConnection();
        
        DefaultListModel dadosBanco = new DefaultListModel();
        
        String dados = "SELECT * FROM processos "
                + "WHERE blackList=1 AND idMaquina="+getIdMaquina();
        try{
            PreparedStatement ps = connection.prepareStatement(dados);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                dadosBanco.addElement(rs.getInt("pid")+" - "+rs.getString("nomeProcesso"));
            }
            return dadosBanco;
        }catch(Exception e){
            e.printStackTrace();
            
            return dadosBanco = new DefaultListModel();
        }
    }
    
    public void deletarProcessos(){
        Database.DatabaseConnection conn = getConn();
        Connection connection = conn.getConnection();
        
        String dados = "SELECT nomeProcesso FROM processos "
                + "WHERE blackList=1 AND idMaquina="+getIdMaquina();
        try{
            PreparedStatement ps = connection.prepareStatement(dados);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                processosKill.add(rs.getString("nomeProcesso"));
            }
            
            for (int i = 0; i < processosKill.size(); i++) {
                kill(processosKill.get(i));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static boolean kill(String process) {    
        try {    
            String line;    
            Process p = Runtime.getRuntime().exec("tasklist.exe /fo csv /nh");    
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));    
            while ((line = input.readLine()) != null) {    
                if (!line.trim().equals("")) {    
                    if (!line.substring(1, line.indexOf("\"", 1)).equalsIgnoreCase(process)) {    
                    } else {
                        Runtime.getRuntime().exec("taskkill /F /IM " + line.substring(1, line.indexOf("\"", 1)));    
                        return true;
                    }    
                }    
            }    
            input.close();    
        } catch (Exception err) {    
            err.printStackTrace();    
        }    
        return false;    
    } 
}
