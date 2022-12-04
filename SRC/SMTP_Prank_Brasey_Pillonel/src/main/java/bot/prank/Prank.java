package bot.prank;

import bot.mail.*;

public class Prank {
    final Group group;
    final EmailContent content;

    public Prank(Group group, EmailContent content){
        this.group      = group;
        this.content    = content;
    }

    public Email generateEmail(){
        return new Email(group.getSender(), group.getReciever(), content);
    }

}
