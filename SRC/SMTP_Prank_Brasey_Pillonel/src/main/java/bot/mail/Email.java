package bot.mail;

import javax.management.BadAttributeValueExpException;
import java.util.ArrayList;
import java.util.List;

public class Email {
    EmailAddr sender;
    List<EmailAddr> reciever;

    EmailContent content;

    public Email(EmailAddr sender, List<EmailAddr> reciever, EmailContent content){
        if(sender == null || reciever.size() == 0 || content == null)
            throw new RuntimeException("Can't have one of the three argument null or empty");

        this.sender = sender;
        this.reciever = reciever;
        this.content = content;
    }

    public String getSender() {
        return sender.emailAddress;
    }

    public List<String> getReciever() {
        ArrayList<String> listAddressReciever = new ArrayList<String>();
        for(EmailAddr e : reciever)
            listAddressReciever.add(e.emailAddress);
        return listAddressReciever;
    }

    public String getSubject(){
        return content.subject;
    }

    public String getContent() {
        return content.content;
    }
}
