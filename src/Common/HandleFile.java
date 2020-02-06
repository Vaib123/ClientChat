package Common;

import gui.Main;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class HandleFile implements Runnable, Serializable {

    @Override
    public void run() {
        while(true){
            try {
                Object ob = Main.clientfileinputstream.readObject();
                if(ob instanceof FileEvent) {
                    FileEvent fileEvent = (FileEvent) ob;
                    if (fileEvent.getStatus().equalsIgnoreCase("Error")) {  //error in file receiving
                        System.out.println("Error occurred ..So exiting");
                    }
                    //setting received file directory location inside users home directory
                    fileEvent.setDestinationDirectory(System.getProperty("user.home")+File.separator+"ReceivedFiles/");

                    String outputFile = fileEvent.getDestinationDirectory() + fileEvent.getFilename(); //getting required file
                    if (!new File(fileEvent.getDestinationDirectory()).exists()) { //if file doesnt exist
                        new File(fileEvent.getDestinationDirectory()).mkdirs();  //file exists
                    }
                    File dstFile = new File(outputFile);
                    FileOutputStream fileOutputStream = new FileOutputStream(dstFile);
                    fileOutputStream.write(fileEvent.getFileData());  //writing data to file
                    System.out.println("Output file : " + outputFile + " is successfully saved "); //for showing that file is received

                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }catch(IOException|ClassNotFoundException e){
                e.printStackTrace();;
            }
        }
    }
}
