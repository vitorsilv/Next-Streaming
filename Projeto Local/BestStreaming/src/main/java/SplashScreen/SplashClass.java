package SplashScreen;

import LoginScreen.Login;

public class SplashClass {
    public static void main(String[] args) {
        Splash sp = new Splash();
        LoginScreen.Login lg  = new Login();
        sp.setVisible(true);
        try {
            for(int i = 0;i<101;i++){   
                Thread.sleep(50);
                sp.lblLoading.setText(Integer.toString(i)+"%");
                sp.progLoading.setValue(i);
                if(i == 100){
                    sp.setVisible(false);
                    lg.setVisible(true);
                }
            }
        } catch (Exception e) {
        }
    }
}
