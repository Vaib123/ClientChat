package Server;

import Common.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class HandleClientChat extends Thread {
    Socket clientchatsocket;
    ServerChatSide server;
    ObjectOutputStream coos=null;
    ObjectInputStream cois=null;
    HandleClientChat(ServerChatSide server,Socket clientchatsocket)
    {
        this.server=server;
        this.clientchatsocket=clientchatsocket;
        try {
            coos=new ObjectOutputStream(this.clientchatsocket.getOutputStream());
            cois=new ObjectInputStream(this.clientchatsocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        while(true){
            try{
                Object ob=cois.readObject();

                if(ob instanceof String)                          //receiving userid for chatting
                {
                    String user=(String)ob;
                    server.fillChatHashmap(user,clientchatsocket);
                    System.out.println(clientchatsocket);
                    server.fillOutputHashmap(user,coos);
                    System.out.println("Printing \n");
                    System.out.println(server.getMapout());

                }
                else if(ob instanceof Message){
                    Message m=(Message)ob;
                    System.out.println("Message sent\t"+m.getFrom()+m.getText());
                    ObjectOutputStream oos=server.getMapout().get(m.getTo());  //getting the required output stream of receiver server end
                    oos.writeObject(m);
                    oos.flush();
                }

            }catch(IOException|ClassNotFoundException e){
                e.printStackTrace();
            }
        }
    }
}
