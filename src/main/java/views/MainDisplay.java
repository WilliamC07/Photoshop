package views;

import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import network.Server;
import project.Project;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * This is the view that is always shown to the user. The components are:
 * 1. TODO Editing information
 * 2. TODO Editing board
 * 3. TODO Chat
 * 4. TODO Switch to different pages
 *
 * Should only be initialized once in ApplicationStart. We are not having multiple windows, instead we have pages.
 * This is a SplitPane to allow the photo editor to choose how big he/she wants the sections to be.
 *
 * Use a SplitPane to allow the user to switch
 *
 *
 * @see ApplicationStart
 * @author William Cao
 * @since 1.0
 */
final class MainDisplay extends SplitPane {
    private Project project = Project.getInstance();

    MainDisplay(){
        // Screen is divided into 3 different parts
        getItems().addAll(new Button("App"), new EditingComponent(), new RightSide());
        setDividerPositions(0.2f, 0.6f);

    }

    /**
     * Goes on the left of the screen.
     * This provides all the tools to use to edit the image.
     */
    private class Tools extends VBox{

    }

    private Tools showTools() {
      Tools toolmenu = new Tools();
      Button crop = new Button("Crop");
      Button brush = new Button("Brush");
      Button shape = new Button("Shape");
      Button text = new Button("Text");
      Button erase = new Button("Erase");
      toolmenu.getChildren().addAll(crop,brush,shape,text,erase);
      return toolmenu;

    }

    /**
     * Goes on the right of the screen.
     * Allows the user to host a server and view the collaborators and information on how to join (give ip address and
     * port). Below this, the user can see the history of all the edits done on the image.
     */
     private class RightSide extends SplitPane{
         RightSide(){
             // Sets up the SplitPane
             // Network half
             getItems().add(setNetworkDisplay());
             // History half
             getItems().add(new Rectangle(20, 20)); // Replace with history section
         }

         private VBox setNetworkDisplay(){
             VBox container = new VBox();
             Label header = new Label("Network:");
             Button hostServer = new Button("Host server");
             Label ipLabel = new Label();
             Label portLabel = new Label();
             // Hosts the server for others to connect
             hostServer.setOnAction(e -> {
                 Server server = new Server();
                 if (project.hasOriginalImage()){
                 try{
                     ipLabel.setText("IP address: " + InetAddress.getLocalHost().getHostAddress());
                 }catch(UnknownHostException error){
                     error.printStackTrace();
                 }
                 portLabel.setText("Port: 5000");
                 container.getChildren().remove(hostServer);
                 container.getChildren().addAll(ipLabel, portLabel);
                 Project.getInstance().setServer(server);
             }
             else{
               header.setText("pick a valid image");
               hostServer.setText("don't click me");
             }});

             container.getChildren().addAll(header, hostServer);
             return container;
         }

     }
     }
