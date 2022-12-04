import FileConfigurer.IFileConfigurer;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import FileConfigurer.*;
import SMTP.SMTPClient;
import bot.prank.Prank;
import bot.prank.PrankGenerator;
import java.util.concurrent.TimeUnit;

public class MailRobot {
    private static final Logger LOG = Logger.getLogger(MailRobot.class.getName());
    public static void main(String[] args) {
        IFileConfigurer fc;
        try{
            fc = new FileConfigurer();
        }catch(Exception ex){
            LOG.log(Level.SEVERE, ex.toString(), ex);
            return;
        }

        PrankGenerator generator = new PrankGenerator(fc);
        List<Prank> pranks = generator.generatePrank();
        SMTPClient client = new SMTPClient(fc.getSmtpServerAddress(), fc.getSmtpServerPort());

        try{
            for(Prank prank : pranks){
                client.sendMail(prank.generateEmail());
                TimeUnit.MILLISECONDS.sleep(500);
            }
        }catch(Exception ex){
            LOG.log(Level.SEVERE, ex.toString(), ex);
        }
    }
}