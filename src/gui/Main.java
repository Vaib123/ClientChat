package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Main extends Application {


    /*Taking all necessary streams,sockets ,ports static so that they remain same for 1 client*/

    public static Socket clientsocket ;
    public static ObjectOutputStream clientoutputstream;
    public static ObjectInputStream clientinputstream;
    public static OutputStream os;
    public static InputStream is;
    public static String serverip="127.0.0.1";
    public static int portno=6987;
    public static int chatportno=7211;
    public static int fileportno=6880;
    public static Socket chatClientSocket;
    public static ObjectOutputStream clientchatoutputstream;
    public static ObjectInputStream clientchatinputstream;
    public static Socket fileClientSocket;
    public static ObjectOutputStream clientfileoutputstream;
    public static ObjectInputStream clientfileinputstream;



    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Chat Window");
        primaryStage.setScene(new Scene(root, 600, 575));
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
