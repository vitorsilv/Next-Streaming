

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SlackBot;

import java.io.DataOutputStream;
import java.util.Arrays;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
/**
 *
 * @author vitor_silva
 */
public class Slack {
    public void enviarMensagem(){
        try{
            URL url = new URL("https://hooks.slack.com/services/TR8LFA6KX/"
                    + "BQVSYQQJE/PL5x66S7b57xAlAQOm9yi9nU");
            
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            
            connection.setDoOutput(true);
            
            DataOutputStream post = new DataOutputStream(connection.getOutputStream());
            String aa= "HELLO AMIGO ESTOU AQ";
            post.writeBytes(String.format("{'text': '%s'}",aa));
            post.flush();
            post.close();
            
            if(connection.getResponseCode()==200){
                System.out.println("ENVIOU");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        Slack s = new Slack();
        s.enviarMensagem();
    }
}
