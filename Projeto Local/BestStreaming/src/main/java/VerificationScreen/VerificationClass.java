
package VerificationScreen;


public class VerificationClass {
    
    public static void main(String[] args) {
        
     InitScreen.Init ini = new InitScreen.Init();
     Verification ver = new Verification();
        ver.setVisible(true);
        try {
            for(int i = 0;i<=100;i++){
                Thread.sleep(50);
                if(i==100){
                    ver.setVisible(false);
                    ini.setVisible(true);
                }
            }
        } catch (Exception e) {
        }
    }

   
}
