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

/**
 *
 * @author vitor_silva
 */
public class OtimizarProcessos extends LoginClass{
    
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
                
            JOptionPane.showMessageDialog(null, "Inserido com sucesso"); 
            return true;
        }catch(Exception e){
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
                dadosBanco.addElement(rs.getInt("pid")+"-"+rs.getString("nomeProcesso"));
            }
            return dadosBanco;
        }catch(Exception e){
            e.printStackTrace();
            
            return dadosBanco = new DefaultListModel();
        }
    }
}
