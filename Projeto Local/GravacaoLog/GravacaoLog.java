package GravacaoLog;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

public class GravacaoLog {
    
        public void GravarLog(String mensagem) throws IOException {
        
        String pasta = "C:\\NextStreaming";
        
        File diretorio = new File(pasta);
            diretorio.mkdir();
        File arquivo = new File(pasta + "\\LogsdeExecução.txt");

        if (!arquivo.exists()) {
            arquivo.createNewFile();
        } 
        List<String> lista = new ArrayList<>();
        lista.add("[" + LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)) + "] " + mensagem);
        Files.write(Paths.get(arquivo.getPath()), lista, StandardOpenOption.APPEND);
    }
}
