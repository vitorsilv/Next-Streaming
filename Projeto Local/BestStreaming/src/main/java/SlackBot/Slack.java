

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SlackBot;

import Tela.Monitorar;
import java.io.DataOutputStream;
import java.util.Arrays;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
/**
 *
 * @author vitor_silva
 */
public class Slack {
    public void enviarMensagem(String msg){
        try{
            URL url = new URL("https://hooks.slack.com/services/TR8LFA6KX/"
                    + "BQVSYQQJE/PL5x66S7b57xAlAQOm9yi9nU");
            
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            
            connection.setDoOutput(true);
            
            DataOutputStream post = new DataOutputStream(connection.getOutputStream());
            
            post.writeBytes(String.format("{'text': '%s'}",msg));
            post.flush();
            post.close();
            
            if(connection.getResponseCode()==200){
                System.out.println("Enviou mensagem");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
