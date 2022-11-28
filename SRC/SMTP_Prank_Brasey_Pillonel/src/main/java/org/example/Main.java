package org.example;

public class Main {
    public static void main(String[] args) {
        SMTPManager test = new SMTPManager("127.0.0.1",25000);
        test.sendMail("from","to","test", "content");
    }
}