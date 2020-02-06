package Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerChatSide extends Thread {
    private final int port;
    ServerSocket css;

    private HashMap<String,Socket> map=new HashMap<>();      //hashmap for message received at server reaches to correct end

    private HashMap<String, ObjectOutputStream> mapout=new HashMap<>();



    public HashMap<String, Socket> getMap() {
        return map;
    }


    public HashMap<String, ObjectOutputStream> getMapout() {
        return mapout;
    }

    public void fillChatHashmap(String user,Socket clientchatsocket)
    {
        if(!map.containsKey(user))
            map.put(user,clientchatsocket);
    }
   public void fillOutputHashmap(String user,ObjectOutputStream oos)
    {
        if(!mapout.containsKey(user))
            mapout.put(user,oos);
    }

    public ServerChatSide (int port){
        this.port=port;
    }
    @Override
    public void run() {
        try {
            css=new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(true){
            Socket clientchatsocket= null;
            try {
                clientchatsocket = css.accept();    //creating connection
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Client connected  " + clientchatsocket);
            HandleClientChat handleclientchat = new HandleClientChat(this,clientchatsocket); //threads for multiple clients

            handleclientchat.start();
        }

    }
}
