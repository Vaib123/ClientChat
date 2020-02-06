package controllers;

import Common.HandleChat;
import Common.Message;
import gui.Main;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller_ChatWindow implements Initializable {

    String fromuser;
    String touser;
    Scene scene;
    @FXML
    private TextArea chatdetails;

    @FXML
    private TextArea actualmessage;

    @FXML
    private Button send;

    @FXML
    void sendMessage(ActionEvent event) {


        if(actualmessage.getText().length()>0){  //if user has written a message
            try {
                Main.clientchatoutputstream.writeObject(new Message(this.fromuser, this.touser, actualmessage.getText()));
                Main.clientchatoutputstream.flush();
                actualmessage.setText("");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION); //alert to show message transmission successsful
                        alert.setTitle("Message sent successfully");
                        alert.setHeaderText(null);
                        alert.setContentText(null);
                        alert.setWidth(100);
                        alert.setHeight(100);
                        alert.showAndWait();
                    }
                });

            }catch (IOException en){
                en.printStackTrace();
            }
        }
    }


    public void setDetails(String fromuser, String touser, Scene scene){
        this.fromuser=fromuser;
        this.touser=touser;
        this.scene=scene;
        try {
            Main.clientchatoutputstream.writeObject(this.fromuser);
            Main.clientchatoutputstream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
      Socket s= null;
        try {

         s = new Socket(Main.serverip,Main.chatportno);  //creting sockets for message passing

        ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
        ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
        Main.chatClientSocket=s;
        Main.clientchatinputstream=ois;
        Main.clientchatoutputstream=oos;





        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread t=new Thread(new HandleChat());  //thread for handling chats
        t.start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true)
                {
                    if(HandleChat.flag==1 && HandleChat.owner.length()>0 && HandleChat.actualmessage.length()>0){
                    chatdetails.setText(chatdetails.getText()+"\n"+HandleChat.owner+":"+HandleChat.actualmessage); //display chat to usr
                    HandleChat.flag=0;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }
}

