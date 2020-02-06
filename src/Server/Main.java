package Server;

public class Main {
    public static void main(String []args) {
        ServerSide s = new ServerSide(6987);        //Three threads at differnt port nos for file handling for chatting for other purposes
        ServerChatSide scs=new ServerChatSide(7211);
        ServerFileSide sfs=new ServerFileSide(6880);
        s.start();                   //server can respond to all clients at same time
        scs.start();
        sfs.start();
    }
}
