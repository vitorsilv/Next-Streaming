
package LoginScreen;

import Database.DatabaseConnection;
import Tela.Telas;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class LoginClass extends Login{
    
    Login lg = new Login();
    static Database.DatabaseConnection conn;
    static Integer idStreamer;
    static Integer idMaquina = 0;
    public LoginClass() throws IOException{

        conn = new DatabaseConnection();
        
        conn.openConnection();
    }
    
    public boolean logar(String email, String senha) throws IOException{
        Connection connection = conn.getConnection();
        
        String selectSql = "SELECT * FROM streamer "
                + "WHERE email='"+email+"' AND senha='"+senha+"'";
        Boolean  f = false;
        try {
            PreparedStatement ps = connection.prepareStatement(selectSql);
            ResultSet rs = ps.executeQuery();           
            while(rs.next()){
                this.idStreamer = rs.getInt("idStreamer");
                String maquina = "SELECT * FROM maquina "
                + "WHERE idStreamer="+this.idStreamer;
                PreparedStatement psMaquina = connection.prepareStatement(maquina);
                ResultSet rsMaquina = psMaquina.executeQuery();
                while(rsMaquina.next()){
                    if(rsMaquina.getInt("idMaquina")!=0){
                        this.idMaquina = rsMaquina.getInt("idMaquina");
                    }else{
                        this.idMaquina = 0;
                    }
                }
                f = true;
            } 
            return f;
        }catch (Exception e) {
            GeracaoLog.GerarLog.GravarLog("Erro ao logar: "+e.getMessage());
            e.printStackTrace();
            return false;
        } 
    }

    public Integer getIdStreamer() {
        return idStreamer;
    }

    public Integer getIdMaquina() {
        return idMaquina;
    }
    
    public Integer setIdMaquina() {
        return idMaquina;
    }

    public static DatabaseConnection getConn() {
        return conn;
    }
    
    
}