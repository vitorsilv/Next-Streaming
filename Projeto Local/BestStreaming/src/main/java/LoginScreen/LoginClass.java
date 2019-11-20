package LoginScreen;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;

public class LoginClass extends Login{
    public static void main(String[] args) {
                // Connect to database
        String hostName = "banco01191117.database.windows.net";
        String dbName = "Banco01191117";
        String user = "Gustavo01191117";
        String password = "#Gf50422207802";
        String url = String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;"
            + "hostNameInCertificate=*.database.windows.net;loginTimeout=30;", hostName, dbName, user, password);
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(url);
            String schema = connection.getSchema();
            System.out.println("Successful connection - Schema: " + schema);

            System.out.println("Query data example:");
            System.out.println("=========================================");

            // Create and execute a SELECT SQL statement.
            String selectSql = "SELECT *"
                + "FROM streamer as s "  
                + "INNER JOIN enderecoStreamer as eS ON s.idStreamer = eS.idStreamer "
                + "INNER JOIN endereco as e ON e.idEndereco = eS.idEndereco";

            try (Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectSql)) {

                // Print results from select statement
                System.out.println("Top 20 categories:");
                while (resultSet.next())
                {
                    System.out.println(resultSet.getString(1) + " "
                        + resultSet.getString(2));
                }
                connection.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        Login lg = new Login();
        
    }
    
}
