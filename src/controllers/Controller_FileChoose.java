package controllers;

import Common.FileEvent;
import gui.Main;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

public class Controller_FileChoose extends Window {

    private String sender,receiver;
    private Scene scene;
    private FileEvent fileEvent;
    @FXML
    private FileChooser fil_chooser;
    @FXML
    private ProgressBar progressbar;
    @FXML
    private Label label;

    @FXML
    private Button open;

    @FXML
    private Button save;

    @FXML
    void OpenDialog(ActionEvent event) throws InterruptedException { //selecting file to be sent
         fil_chooser = new FileChooser();
        // get the file selected
        File file = fil_chooser.showOpenDialog(this);

        if (file != null) {

            label.setText(file.getAbsolutePath()
                    + "  selected");
            progressbar.setVisible(true);
            progressbar.setProgress(1); //progress bar to show uploading
            Thread.sleep(1000);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);  //alert for confirmation want to send or not
            alert.setTitle("File Sending");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to send this file to "+this.receiver.toUpperCase() +"?");
            Optional<ButtonType> result= alert.showAndWait();  //button presed by user

          if(!result.isPresent());
          else if(result.get()==ButtonType.OK){  //if it is ok
              String destinationPath="/home/harry/Downloads/";  //default path
              fileEvent = new FileEvent();
              String fileName = file.getName();
              String path = file.getAbsolutePath().substring(0,file.getAbsolutePath().lastIndexOf("/")+1);

              //sending file by creating file event object and initializing it with required fields
              fileEvent.setDestinationDirectory(destinationPath);
              fileEvent.setFilename(fileName);
              fileEvent.setSourceDirectory(file.getAbsolutePath());
              fileEvent.setSender(this.sender);
              fileEvent.setReceiver(this.receiver);
              if (file.isFile()) {
                  try {
                      DataInputStream diStream = new DataInputStream(new FileInputStream(file));
                      long len = (int) file.length();
                      byte[] fileBytes = new byte[(int) len];
                      int read = 0;
                      int numRead = 0;
                      while (read < fileBytes.length && (numRead = diStream.read(fileBytes, read, fileBytes.length - read)) >= 0) {
                          read = read + numRead;
                      } //reading file
                      fileEvent.setFileSize(len);
                      fileEvent.setFileData(fileBytes);
                      fileEvent.setStatus("Success");
                  } catch (Exception e) {
                      e.printStackTrace();
                      fileEvent.setStatus("Error");
                  }
              } else {
                  System.out.println("path specified is not pointing to a file");
                  fileEvent.setStatus("Error");  //in case of error
              }
              try {
                  Main.clientfileoutputstream.writeObject(fileEvent);
                  Thread.sleep(3000);
              } catch (IOException e) {
                  e.printStackTrace();
              }
              Stage stage=(Stage)label.getScene().getWindow();
              stage.setScene(this.scene);
          }

        }
    }

    @FXML
    void SaveDialog(ActionEvent event) {
        File file = fil_chooser.showSaveDialog(this);

        if (file != null) {
            label.setText(file.getAbsolutePath()
                    + "  selected");
        }
    }
/*passing info from previous to this window*/
    public void setDetails(String sender, String receiver, Scene scene){
        this.sender=sender;
        this.receiver=receiver;
        this.scene=scene;
        progressbar.setVisible(false);
    }

}
