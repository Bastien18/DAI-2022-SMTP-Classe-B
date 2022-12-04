package bot.mail;

public class EmailContent {
    String subject;
    String content;

    public EmailContent(String subject, String content){
        this.subject = subject;
        this.content = content;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }
}
