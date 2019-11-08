package LoginScreen;

import Database.DatabaseConnection;
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
    public LoginClass(){

         conn = new DatabaseConnection();
        
        conn.openConnection();
    }
    
    public boolean logar(String email, String senha){
        Connection connection = conn.getConnection();
        
        String selectSql = "SELECT email, senha FROM streamer "
                + "WHERE email='"+email+"' AND senha='"+senha+"'";
        
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
