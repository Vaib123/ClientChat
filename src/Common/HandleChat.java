package Common;

import gui.Main;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

public class HandleChat implements Runnable, Serializable {

    public static String actualmessage = "";
    public static String owner = "";
    public static int flag=0;

    //thread for continuously checking message received
    @Override
    public void run() {
        while (true) {
               try {
                    Object ob=Main.clientchatinputstream.readObject();
                    if(ob instanceof Message){
                        Message m=(Message)ob;
                        actualmessage=m.getText();
                        owner=m.getFrom();
                        flag=1;  //setting flag 1 to ovoid multiple times writing
                        System.out.println("Message received from\t"+owner+"\t"+actualmessage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

