package Server;

import Common.FileEvent;
import Common.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class HandleClientFile extends Thread {
    Socket clientfilesocket;
    ServerFileSide server;
    ObjectOutputStream foos=null;
    ObjectInputStream fois=null;
    HandleClientFile(ServerFileSide server,Socket clientfilesocket)
    {
        this.server=server;
        this.clientfilesocket=clientfilesocket;
        try {
            foos=new ObjectOutputStream(this.clientfilesocket.getOutputStream());
            fois=new ObjectInputStream(this.clientfilesocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        while(true)
        {
            try{
                Object ob=fois.readObject();

                if(ob instanceof String)           //mapping user with socket and its outputstream
                {
                    String user=(String)ob;
                    server.fillFileHashmap(user,clientfilesocket);
                    System.out.println("FILESOCKET"+clientfilesocket);
                    server.fillOutputHashmap(user,foos);
                    System.out.println("Printing \n");
                    System.out.println(server.getMapout());

                }
                else if(ob instanceof FileEvent){  //file event object contains all details of the file
                    FileEvent fileEvent=(FileEvent) ob;
                    ObjectOutputStream oos=server.getMapout().get(fileEvent.getReceiver());
                    System.out.println("Transferring file\n");
                    oos.writeObject(fileEvent);
                    oos.flush();
                }


            }catch(IOException|ClassNotFoundException e){
                e.printStackTrace();
            }
        }
    }
}
