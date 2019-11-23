package InitScreen;
import java.text.Format;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;
import oshi.util.GlobalConfig;


public class InitScreenClass{  
    public String Inicio() {
            
     SystemInfo s = new SystemInfo();
     
     OperatingSystem p = s.getOperatingSystem();
     HardwareAbstractionLayer h = s.getHardware();
        System.out.println(p);
        return memoria(h.getMemory());
        
    }

    
    public static String memoria(GlobalMemory memo){
        
    long disp = memo.getAvailable();
    long total = memo.getTotal();
        
        String ex = String.format("Disponivel %s \n"+"Total %s",FormatUtil.formatBytes(disp),FormatUtil.formatBytes(total));
        
        return ex;
    
    }
    
    
    
    
}
