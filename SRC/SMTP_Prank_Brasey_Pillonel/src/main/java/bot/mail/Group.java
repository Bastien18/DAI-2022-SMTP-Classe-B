package bot.mail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Group {
    List<EmailAddr> reciever;
    EmailAddr       sender;

    public Group(List<EmailAddr> group){
        if(group == null)
            throw new RuntimeException("Group can't be null");

        Collections.shuffle(group);
        sender = group.get(0);

        reciever = new ArrayList<>();
        for(int i = 1; i < group.size(); ++i)
            reciever.add(group.get(i));
    }

    public List<EmailAddr> getReciever(){
        return reciever;
    }

    public EmailAddr getSender(){
        return sender;
    }

    public void addReciever(EmailAddr reciever){
        if(reciever == null)
            throw new RuntimeException("Receveur ne peut pas être null");

        this.reciever.add(reciever);
    }

    public void addReciever(List<EmailAddr> recievers){
        if(recievers == null)
            throw new RuntimeException("Receveur ne peut pas être null");

        this.reciever.addAll(recievers);
    }

    public void deleteReciever(EmailAddr emailToDelete){
        this.reciever.remove(emailToDelete);
    }
}
