/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenwich.chat;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 *
 * @author emmascott
 */
public class SetupSocket extends Thread {
    private int port;
    private String ip;
    private int id;
    Scanner in;
    PrintWriter out;
    ReceiverManager receiver;
    UserManager info;
    Display1 display;

    public SetupSocket(User user, ReceiverManager receiver, UserManager info, Display1 display) {
        this.id = user.id;
        this.ip = user.ip;
        this.port = user.port;
        this.receiver = receiver;
        this.info = info;
        this.display = display;
    }

    public void run() {

            initialiseSocket(this.ip, this.port);
            
            info.addWriter(out);
            info.addWriterBK(id, out);
                        
            registerInfo(this.in, this.out, this.receiver);
 

    }
    
    public PrintWriter initialiseSocket(String ip, int port){
        try {
            System.out.println("You're connected");
            Socket socket = new Socket(ip, port);
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);     
        } catch (IOException e){
            System.out.println("Your connection was not successful");
        }

        return out;
    }

 
    
    public void registerInfo(Scanner in, PrintWriter out, ReceiverManager receiver){
        while (in.hasNextLine()) {
                String line = in.nextLine();
                if (line.startsWith("SUBMIT-ID")) {
                    out.println(receiver.getRecId());
                } else if (line.startsWith("SUBMIT-IP")) {
                    out.println(receiver.getRecIp());
                } else if (line.startsWith("SUBMIT-PORT")){
                    out.println(receiver.getRecPort());       
                } else if (line.startsWith("DUPLICATE-ID")){
                    JOptionPane.showMessageDialog(display, "Someone else already has your id, please leave and try again with another id");
                }
            }
    }

}
