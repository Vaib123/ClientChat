package Server;

import Common.Login;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;


public class HandleClient extends Thread {
    Socket clientsocket;
    ServerSide server;
    ObjectOutputStream oos=null;
    ObjectInputStream ois=null;
    HandleClient(ServerSide server,Socket clientsocket)
    {
        this.server=server;
        this.clientsocket=clientsocket;
        try {
            oos=new ObjectOutputStream(this.clientsocket.getOutputStream());
            ois=new ObjectInputStream(this.clientsocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        while(true) {
            Object ob=null;
            try {

                 ob = ois.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            if (ob!=null && ob instanceof Login) {
                Login lgn = (Login) ob;
                if (lgn.getPass().equals("abcd")) {   //for successful login
                    try {
                        server.fillHashmap(lgn.getUsername(), clientsocket);
                        oos.writeObject("ACCESS GRANTED");
                        oos.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {   //unsuccesful login
                    try {
                        oos.writeObject("ACCESS DENIED");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (ob!=null && ob instanceof String) {   //for disconnecting the user
                String s = (String) ob;
                System.out.println(s + "Hello");
                String val=s.substring(0,10);
                System.out.println(val);
                boolean st=s.substring(0,10).equals("Disconnect");
                System.out.println(st);
                if ((s.substring(0, 10)).equals("Disconnect")) {
                    String user = s.substring(11);
                    System.out.println(user);
                    server.getUsers().remove(user);
                    server.getMap().remove(user);   //removing user
                    server.getList().remove(clientsocket); //removing socket from list

                }
                else if(s.equals("GETTHELIST")){       //for fetching list of online users
                    try {
                        oos.writeObject((ArrayList<String>)(server.getUsers().clone()));
                        oos.flush();
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }

        }
    }
}
