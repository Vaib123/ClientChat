package controllers;

import Common.HandleChat;
import Common.HandleFile;
import Common.Login;
import Common.Message;
import gui.Main;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static java.lang.Thread.sleep;

public class Controller_Start  {

    String userid;
    ArrayList<String>activeusers;
    @FXML
    private Button connect;
    @FXML
    public  TextArea status;
    @FXML
    private Button disconnect;
    @FXML
    private ContextMenu contextmenu;

    @FXML
    private ListView<String> listview;
    @FXML
    private MenuItem message;
    @FXML
    private MenuItem sendfile;
    @FXML
    private PasswordField password;
    @FXML
    private TextField username;
    @FXML
    void disconnect(ActionEvent event) {
            Socket s=Main.clientsocket;
            String disconnected="Disconnect "+username.getText();
            System.out.println(disconnected);
            if(s!=null) {
                try {
                    Main.clientoutputstream.writeObject(disconnected);
                    Main.clientoutputstream.flush();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            status.setText("You are disconnected now\n");
                            Stage st=(Stage)disconnect.getScene().getWindow();
                            PauseTransition delay = new PauseTransition(Duration.seconds(1));
                            delay.setOnFinished( event -> st.close() );
                            delay.play();


                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else
            {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        status.setText("You are not logged in .You cant disconnect\n");
                    }
                });
            }
    }

    @FXML
    void onlogin(ActionEvent event) {
        this.userid=username.getText();
        Login lgn=new Login(username.getText(),password.getText(),"LOGIN");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Socket s = new Socket(Main.serverip, Main.portno);      //creating sockets on login
                        ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
                        ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());

                        Socket clientfilesocket=new Socket(Main.serverip,Main.fileportno);
                        ObjectInputStream fois=new ObjectInputStream(clientfilesocket.getInputStream());
                        ObjectOutputStream foos=new ObjectOutputStream(clientfilesocket.getOutputStream());

                        foos.writeObject(userid);
                        foos.flush();
                        oos.writeObject(lgn);
                        oos.flush();

                        Object ob=ois.readObject();
                        String received=(String)ob;
                        System.out.println(received);

                        if(received.equals("ACCESS GRANTED")){  //successful login

                            Main.clientsocket=s;
                            Main.clientinputstream=ois;
                            Main.clientoutputstream=oos;
                            Main.fileClientSocket=clientfilesocket;
                            Main.clientfileinputstream=fois;
                            Main.clientfileoutputstream=foos;


                            new Thread(new HandleFile()).start();  //handling file events
                            status.setText("You are successfully logged in "+userid+"\n");



                            /*this thread is for updating online list of users*/
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    while(true){
                                        try {
                                            Main.clientoutputstream.writeObject("GETTHELIST");
                                            Main.clientoutputstream.flush();


                                                activeusers = (ArrayList<String>) ((ArrayList<String>) (Main.clientinputstream.readObject())).clone();
                                                Platform.runLater(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        listview.getItems().clear();
                                                        if (activeusers.size() == 1)
                                                            status.setText("NO OTHER USER IS CURRENTLY ONLINE " + userid + "\n");
                                                        for (int i = 0; i < activeusers.size(); i++) {
                                                            if (activeusers.get(i).equals(userid))
                                                                continue;
                                                            else {
                                                                listview.getItems().add(activeusers.get(i));
                                                                System.out.println(activeusers.get(i));
                                                            }
                                                        }
                                                    }
                                                });


                                        } catch (IOException|ClassNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            sleep(10000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }).start();
                        }
                        else  //Wrong password
                        {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    status.setText(status.getText()+"\nRetry to login.Ask your administrator the correct password\n");
                                }
                            });

                        }

                        username.setText("");
                        password.setText("");








                    }catch(IOException | ClassNotFoundException e){
                        e.printStackTrace();
                    }
                }
            }).start();


    }



    @FXML
    public  void openChatWindow() {

            Stage primaryStage = (Stage) username.getScene().getWindow();
            Scene old = username.getScene();
            Stage chatstage = new Stage();
            String title = listview.getSelectionModel().selectedItemProperty().getValue();
            Parent root = null;
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/chatwindow.fxml"));
                root = loader.load();
                Controller_ChatWindow cw = loader.getController();
                cw.setDetails(userid, title, old);
            } catch (IOException en) {
                en.printStackTrace();
            }
            chatstage.initModality(Modality.NONE);
            chatstage.initOwner(primaryStage);
            chatstage.setTitle(userid.toUpperCase() + "  TO  "+ title.toUpperCase());
            chatstage.setScene(new Scene(root, 401, 512));
            chatstage.show();


    }


        @FXML

    public void sendFile(ActionEvent actionEvent) {


            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Stage filechoose=(Stage)username.getScene().getWindow();
                    String receiver = listview.getSelectionModel().selectedItemProperty().getValue();
                    Parent root = null;
                    try {

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/filechoose.fxml"));
                        root = loader.load();
                        Controller_FileChoose fc = loader.getController();
                        fc.setDetails(userid, receiver,username.getScene());
                    } catch (IOException en) {
                        en.printStackTrace();
                    }

                    filechoose.setTitle("FileChooser");
                    filechoose.setScene(new Scene(root, 600, 400));

                }
            });


    }



}
