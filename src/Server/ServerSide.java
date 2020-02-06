package Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServerSide extends Thread {
    private final int port;
    ServerSocket ss;
    private ArrayList<HandleClient> clientlist=new ArrayList();
    private ArrayList<String> users=new ArrayList<>();
    private HashMap<String,Socket> map=new HashMap<>();
  //  private HashMap<String,Socket> clientmap=new HashMap<>();
   // private HashMap<String, ObjectOutputStream> mapout=new HashMap<>();
    public ServerSide(int port)
    {
        this.port=port;
    }
    public ArrayList<HandleClient> getList(){
        return clientlist;
    }

    public void fillHashmap(String user,Socket clientsocket)
    {
        if(!map.containsKey(user)) {
            map.put(user, clientsocket);
            users.add(user);
        }
    }
  
    public HashMap<String, Socket> getMap() {
        return map;
    }
    /*public HashMap<String,Socket> getClientmap(){
        return clientmap;
    }
    public HashMap<String,ObjectOutputStream> getMapout(){
        return mapout;
    }*/

    public ArrayList<String> getUsers()
    {
        for(int i=0;i<users.size();i++)
            System.out.println(users.get(i));
        return users;

    }
    @Override
    public void run() {

        try {
            ss = new ServerSocket(port);
            while (true) {
                Socket clientsocket = ss.accept();
                System.out.println("Client connected  " + clientsocket);
                HandleClient handleclient = new HandleClient(this,clientsocket);
                clientlist.add(handleclient);
                handleclient.start();
            }
        }catch(IOException e){
            e.printStackTrace();
        }

    }
}
