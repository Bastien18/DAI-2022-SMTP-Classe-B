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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class FileConfigurer implements IFileConfigurer{
    private static final Logger LOG = Logger.getLogger(FileConfigurer.class.getName());
    private static final String COMMA_DELIMITER         = ",",
                                EMAIL_ADDRESS_CSV_PATH  = "ConfigFile/email_address.csv",
                                EMAIL_CONTENT_JSON_PATH = "ConfigFile/email_content.json",
                                CONFIG_PROPERTIES_PATH  = "ConfigFile/config.properties",
                                SUBJECT                 = "Subject",
                                CONTENT                 = "Content",
                                REGEX                   = "^(.+)@(.+)$";

    private String smtpServerAddress;
    private int smtpServerPort,
                numberOfgroup;
    private EmailAddr witnessToCC;

    private List<EmailAddr> victims;
    private List<EmailContent> contents;

    public FileConfigurer(){
        victims     = getVictimsFromCSV();
        contents    = getContentFromJSON();
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

    public List<EmailContent> getContentFromJSON() throws RuntimeException{
        List<EmailContent> listContent = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();

        try(FileReader reader = new FileReader(EMAIL_CONTENT_JSON_PATH, StandardCharsets.UTF_8)){
            Object obj = jsonParser.parse(reader);
            JSONArray messages = (JSONArray) obj;

            for(int i = 0; i < messages.size(); ++i){
                // Parsing every messages to get both subject and content
                JSONObject msg       = (JSONObject) messages.get(i);
                JSONObject msgObject = (JSONObject) msg.get("message");
                String  subject = (String) msgObject.get(SUBJECT),
                        content = (String) msgObject.get(CONTENT);

                if(subject == null || content == null)
                    throw new RuntimeException("Missing subject or content field in " + EMAIL_CONTENT_JSON_PATH);

                listContent.add(new EmailContent(subject, content));
            }

        }catch(Exception ex){
            LOG.log(Level.SEVERE, ex.toString(), ex);
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
