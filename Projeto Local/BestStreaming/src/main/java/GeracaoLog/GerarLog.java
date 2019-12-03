/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GeracaoLog;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vitor_silva
 */
public class GerarLog {
    public static void GravarLog(String mensagem) throws IOException {
        
        String pasta = "C:\\NextStreaming";
        
        File diretorio = new File(pasta);
            diretorio.mkdir();
        File arquivo = new File(pasta + "\\log-"+LocalDate.now()+".txt");

        if (!arquivo.exists()) {
            arquivo.createNewFile();
        } 
        List<String> lista = new ArrayList<>();
        lista.add("[" + LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)) + "] " + mensagem);
        Files.write(Paths.get(arquivo.getPath()), lista, StandardOpenOption.APPEND);
    }
}
