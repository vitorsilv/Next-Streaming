
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
    static Integer idMaquina;
    public LoginClass() throws IOException{

        conn = new DatabaseConnection();
        
        conn.openConnection();
    }
    
    public boolean logar(String email, String senha) throws IOException{
        Connection connection = conn.getConnection();
        
        String selectSql = "SELECT * FROM streamer "
                + "INNER JOIN maquina ON streamer.idStreamer = maquina.idStreamer "
                + "WHERE email='"+email+"' AND senha='"+senha+"'";
        
        try {
            PreparedStatement ps = connection.prepareStatement(selectSql);
            ResultSet rs = ps.executeQuery();           
            if(rs.next()){
                this.idStreamer = rs.getInt("idStreamer");
                this.idMaquina = rs.getInt("idMaquina");
                
                return true;
            }else{
                return false;
            }     
        }catch (Exception e) {
            GeracaoLog.GerarLog.GravarLog("Erro ao logar: "+e.getMessage());
            return false;
        }
    }

    public Integer getIdStreamer() {
        return idStreamer;
    }

    public Integer getIdMaquina() {
        return idMaquina;
    }

    public static DatabaseConnection getConn() {
        return conn;
    }
    
    
}