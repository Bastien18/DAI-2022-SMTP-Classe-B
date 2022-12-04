package FileConfigurer;

import bot.mail.EmailAddr;
import bot.mail.EmailContent;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileConfigurer implements IFileConfigurer{
    private static final Logger LOG = Logger.getLogger(FileConfigurer.class.getName());
    private static final String COMMA_DELIMITER         = ",",
                                EMAIL_ADDRESS_CSV_PATH  = "ConfigFile/email_address.csv",
                                EMAIL_CONTENT_CSV_PATH  = "ConfigFile/email_content.csv",
                                CONFIG_PROPERTIES_PATH  = "ConfigFile/config.properties",
                                SUBJECT                 = "Subject:",
                                CONTENT                 = "Content:",
                                REGEX                   = "^(.+)@(.+)$";

    private String smtpServerAddress;
    private int smtpServerPort,
                numberOfgroup;
    private EmailAddr witnessToCC;

    private List<EmailAddr> victims;
    private List<EmailContent> contents;

    public FileConfigurer(){
        victims     = getVictimsFromCSV();
        contents    = getContentFromCSV();
        getProperties("ConfigFile/config.properties");
        verifyEmailAddress();
    }

    public List<EmailAddr> getVictimsFromCSV(){
        List<List<String>> dataEmails = getDataFromCSV(EMAIL_ADDRESS_CSV_PATH);
        List<EmailAddr> listEmail = new ArrayList<>();

        for(List<String> ls : dataEmails){
            for(String s : ls){
                listEmail.add(new EmailAddr(s));
            }
        }

        return listEmail;
    }

    public List<EmailContent> getContentFromCSV() throws RuntimeException{
        List<List<String>> dataEmails = getDataFromCSV(EMAIL_CONTENT_CSV_PATH);
        List<EmailContent> listContent = new ArrayList<>();

        for(List<String> ls : dataEmails){
            for(String s : ls){
                String  subject = new String(),
                        content = new String();

                if(!s.contains(SUBJECT) || !s.contains(CONTENT))
                    throw new RuntimeException("Format incorrect must match the following sequence \"Subject:<your subject>Content:<your content>\"");

                subject = s.split(SUBJECT)[1].split(CONTENT)[0];
                content = s.split(CONTENT)[1];
                listContent.add(new EmailContent(subject, content));
            }
        }

        return listContent;
    }

    public void getProperties(String filename) throws RuntimeException{
        BufferedReader in;
        try{
            in = new BufferedReader(new FileReader(filename, StandardCharsets.UTF_8));
            Properties prop = new Properties();
            prop.load(in);

            smtpServerAddress   = prop.getProperty("smtpServerAddress");
            smtpServerPort      = Integer.parseInt(prop.getProperty("smtpServerPort"));
            numberOfgroup       = Integer.parseInt(prop.getProperty("numberOfGroups"));
            witnessToCC         = new EmailAddr(prop.getProperty("witnessesToCC"));

            if(smtpServerAddress == null || smtpServerPort < 1 || numberOfgroup < 0)
                throw new RuntimeException("Incorrect properties loaded");

        }catch(Exception ex){
            LOG.log(Level.SEVERE, ex.toString(), ex);
        }
    }

    List<List<String>> getDataFromCSV(String filename){
        List<List<String>> records = new ArrayList<>();
        BufferedReader in;

        try{
            in = new BufferedReader(new FileReader(filename, StandardCharsets.UTF_8));
            String line;
            while((line = in.readLine()) != null){
                String[] values = line.split(COMMA_DELIMITER);
                records.add(Arrays.asList(values));
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.toString(), ex);
        }

        return records;
    }

    public void verifyEmailAddress() throws RuntimeException{
        Pattern pattern = Pattern.compile(REGEX);

        List<EmailAddr> completeAddressList = new ArrayList<>(victims);
        completeAddressList.add(witnessToCC);

        for(EmailAddr emailAddr : completeAddressList){
            Matcher matcher = pattern.matcher(emailAddr.getEmailAddress());
            if(!matcher.matches())
                throw new RuntimeException("Email address invalid");
        }
    }

    public static void main(String[] args){
        List<EmailAddr> dataAddr = new ArrayList<>();
        List<EmailContent> dataContent = new ArrayList<>();
        FileConfigurer fg = new FileConfigurer();

        dataAddr = fg.getVictimsFromCSV();
        dataContent = fg.getContentFromCSV();

        for(EmailAddr addr : dataAddr){
            System.out.println(addr.getEmailAddress());
        }

        for(EmailContent cont : dataContent){
            System.out.println(cont.getSubject() + " " + cont.getContent());
        }
    }

    public String getSmtpServerAddress() {
        return smtpServerAddress;
    }

    public int getSmtpServerPort() {
        return smtpServerPort;
    }

    public int getNumberOfgroup() {
        return numberOfgroup;
    }

    public EmailAddr getWitnessToCC() {
        return witnessToCC;
    }

    public List<EmailAddr> getVictims() {
        return victims;
    }

    public List<EmailContent> getContents() {
        return contents;
    }
}
