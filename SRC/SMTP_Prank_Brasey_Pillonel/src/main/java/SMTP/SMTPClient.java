package SMTP;
import bot.mail.Email;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SMTPClient {
    private static final Logger LOG = Logger.getLogger(SMTPClient.class.getName());
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;

    private boolean error        = false;
    private boolean endOfReading = false;


    public SMTPClient(String srvAddr, int port){
        try {
            socket = new Socket(srvAddr, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(),StandardCharsets.UTF_8));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),StandardCharsets.UTF_8));
        }catch(IOException e){
            LOG.log(Level.SEVERE, null, e);
            this.close();
        }
    }
    public void close(){
        try {
            if (out != null) out.close();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.toString(), ex);
        }
        try {
            if (in != null) in.close();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.toString(), ex);
        }
        try {
            if (socket != null) socket.close();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.toString(), ex);
        }
    }
    public void sendMail(Email email) throws IOException{
        endOfReading = false;
        Thread t1 = new Thread(new Socket_Reader(this));
        t1.start();
        String endline = "\r\n";

        String formatReciever = new String();
        formatReciever = email.getReciever().get(0);
        for(int i = 1; i < email.getReciever().size(); ++i){
            formatReciever += ", " + email.getReciever().get(i);
        }
        formatReciever += endline;

        out.write("EHLO SMTPManager" + endline);
        out.flush();
        if(error)return;

        out.write("MAIL FROM: "+ email.getSender() + endline);
        out.flush();
        if(error)return;

        out.write("RCPT TO: "+ formatReciever);
        out.flush();
        if(error)return;

        out.write("DATA" + endline);
        out.write("Content-Type: text/plain; charset=utf-8" + endline);
        out.flush();
        if(error)return;
        out.write(  "From: " + email.getSender() + endline +
                        "To: " + formatReciever + endline +
                        "Subject: " + email.getSubject() + endline +
                        email.getContent().replace("\n", endline) +
                        endline + "." + endline);
        out.flush();
        endOfReading = true;
        try{
            t1.join();
        }catch(Exception ex){
            LOG.log(Level.SEVERE, ex.toString(), ex);
        }
    }

    private class Socket_Reader implements Runnable{
        SMTPClient parent;
        private Socket_Reader(SMTPClient parent){
            this.parent = parent;
        }
        @Override
        public void run() {
            String response;
            try {
                do {
                    response = parent.in.readLine();
                    if(response == null)break;
                    LOG.info(response);
                    response = response.substring(0,3);
                    if(!(response.equals("250") || response.equals("220")|| response.equals("354")))parent.error = true;
                } while (!parent.error && !parent.endOfReading);
            }catch (IOException e){
                LOG.log(Level.SEVERE, e.toString(), e);
                parent.close();
            }
        }
    }
}
