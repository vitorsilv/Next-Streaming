/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author vitor_silva
 */
public class DatabaseConnection {
    protected static Connection connection = null;
    public static void openConnection(){
        try{
            String url = String.format("jdbc:sqlserver://banco01191117.database.windows.net:1433;"
                    + "database=Banco01191117;user=Gustavo01191117@banco01191117;"
                    + "password=#Gf50422207802;encrypt=true;trustServerCertificate=false;"
                    + "hostNameInCertificate=*.database.windows.net;loginTimeout=30;");
            connection = DriverManager.getConnection(url);
           
            System.out.println("Conectado com sucesso");
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void closeConnection(){
        try{
            connection.close();
            System.out.println("Conexão fechada!");
        }catch(Exception e){
            
        }
    }

    public static Connection getConnection() {
        return connection;
    }
    
}
