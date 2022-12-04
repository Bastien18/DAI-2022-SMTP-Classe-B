package FileConfigurer;

import bot.mail.EmailAddr;
import bot.mail.EmailContent;

import java.util.List;

public interface IFileConfigurer {
    public String getSmtpServerAddress();
    public int getSmtpServerPort();
    public int getNumberOfgroup();
    public EmailAddr getWitnessToCC();
    public List<EmailAddr> getVictims();
    public List<EmailContent> getContents();
}
