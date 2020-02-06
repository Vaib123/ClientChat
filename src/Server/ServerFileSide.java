package Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ServerFileSide extends  Thread{
    private final int port;
    ServerSocket fss;

    private HashMap<String, Socket> map=new HashMap<>();

    private HashMap<String, ObjectOutputStream> mapout=new HashMap<>();







    public HashMap<String, Socket> getMap() {
        return map;
    }


    public HashMap<String, ObjectOutputStream> getMapout() {
        return mapout;
    }

    public void fillFileHashmap(String user,Socket clientfilesocket)
    {
        if(!map.containsKey(user))
            map.put(user,clientfilesocket);
    }
    public void fillOutputHashmap(String user,ObjectOutputStream oos)
    {
        if(!mapout.containsKey(user))
            mapout.put(user,oos);
    }

    public ServerFileSide (int port){
        this.port=port;
    }
    @Override
    public void run() {
        try {
            fss=new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(true){
            Socket clientfilesocket= null;
            try {
                clientfilesocket = fss.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Client connected  " + clientfilesocket);
            HandleClientFile handleclientfile = new HandleClientFile(this,clientfilesocket);

            handleclientfile.start();
        }

    }
}


