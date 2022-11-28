package org.example;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SMTPManager {
    private static final Logger LOG = Logger.getLogger(SMTPManager.class.getName());
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;

    private boolean error = false;


    SMTPManager(String srvAddr, int port){
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
    public void sendMail(String from, String to,String subject,String content){
        try {
            Thread t1 =new Thread(new Socket_Reader(this));
            t1.start();
            String endline = "\r\n";
            out.write("EHLO SMTPManager" + endline);
            out.flush();
            if(error)return;
            out.write("MAIL FROM: "+ from + endline);
            out.flush();
            if(error)return;
            out.write("RCPT TO: "+ to + endline);
            out.flush();
            if(error)return;
            out.write("DATA" + endline);
            out.flush();
            if(error)return;
            out.write(  "From: " + from + endline +
                            "To: " + to + endline +
                            "Subject: " + subject + endline +
                            content.replace("\n", endline) +
                            endline + "." + endline);
            out.flush();
            t1.stop();
        }catch (IOException e){
            LOG.log(Level.SEVERE, null, e);
            this.close();
        }
    }

    private class Socket_Reader implements Runnable{
        SMTPManager parent;
        private Socket_Reader(SMTPManager parent){
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
                } while (!parent.error);
            }catch (IOException e){
                LOG.log(Level.SEVERE, null, e);
                parent.close();
            }
        }
    }
}
