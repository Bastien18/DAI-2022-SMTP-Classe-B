package bot.prank;

import FileConfigurer.FileConfigurer;
import FileConfigurer.IFileConfigurer;
import bot.mail.EmailAddr;
import bot.mail.EmailContent;
import bot.mail.Group;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PrankGenerator {
    private final IFileConfigurer fc;
    private final static Random RANDOM          = new Random();
    private final static int MIN_VICTIMS_NBR    = 3;

    public PrankGenerator(IFileConfigurer fc){
        this.fc = fc;
    }

    public List<Group> createGroup(){
        List<Group> groups = new ArrayList<>();
        List<EmailAddr> emailAddrs = fc.getVictims();
        int victimsNbr = emailAddrs.size();

        for(int i = 0; i < fc.getNumberOfgroup(); ++i){

            // Choose randomly distinct email addresses for a group
            int victimsNbrForAGroup = MIN_VICTIMS_NBR + RANDOM.nextInt(victimsNbr - MIN_VICTIMS_NBR - 1);
            int victimsFoundNbr = 0;
            List<EmailAddr> chosenVictims = new ArrayList<>();

            while(victimsFoundNbr < victimsNbrForAGroup){
                int index = RANDOM.nextInt(emailAddrs.size());
                if(!chosenVictims.contains(emailAddrs.get(index))){
                    chosenVictims.add(emailAddrs.get(index));
                    ++victimsFoundNbr;
                }
            }

            groups.add(new Group(chosenVictims));
        }

        return groups;
    }

    public List<Prank> generatePrank(){
        List<Prank> pranks = new ArrayList<>();
        List<Group> groups = createGroup();
        List<EmailContent> contents = fc.getContents();

        for(Group group : groups){
            pranks.add(new Prank(group, contents.get(RANDOM.nextInt(contents.size()))));
        }

        return pranks;
    }
}
