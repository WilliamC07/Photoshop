package views;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import network.Server;
import project.Project;

import java.io.File;
import java.net.Inet4Address;
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
        getItems().addAll(new Button("App"), new EditPane(), new RightSide());
        setDividerPositions(0.2f, 0.6f);
    }

    /**
     * Goes on the left of the screen.
     * This provides all the tools to use to edit the image.
     */
    private class Tools extends VBox{

    }

    /**
     * Goes on the center of the screen
     * The image being edited goes here. If no image is selected, this should prompt the user to choose a file
     * or use a blank canvas.
     */
    private class EditPane extends VBox{
        EditPane(){
            // If there is no original image, the user most choose one. If there is one, use the most recent image to
            // display.
            if(!project.hasOriginalImage()){
                showRequestMode();
            }else{
                showEditMode();
            }

            // Basic designs
            setAlignment(Pos.CENTER);

            // TODO: Code multiple pages (see google docs for more information)
        }

        /**
         * Sets the view up to ask the user to choose the base picture of the project. If the user chooses a valid image
         * (defined by project.Project), the view will change using {@link #showEditMode()} and remove all the nodes
         * created by this method.
         */
        private void showRequestMode(){
            Label directions = new Label("Open a image to get started");
            Button chooseFile = new Button("Pick an image");
            Label errorLabel = new Label();

            // directions label design
            directions.setFont(new Font(30));

            // chooseFile functionality
            chooseFile.setOnAction(e -> {
                FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showOpenDialog(this.getScene().getWindow());
                // Check null to make sure the user did choose a file and didn't click close/cancel
                if(file != null && project.setOriginalImage(file)){
                    // User can now start editing the image
                    // Remove the nodes created by this method
                    this.getChildren().removeAll(directions, chooseFile, errorLabel);
                    // show the image on the screen
                    showEditMode();
                }else{
                    errorLabel.setText("Please choose a valid image (.png files only)");
                }
            });

            this.getChildren().addAll(directions, chooseFile, errorLabel);
        }

        /**
         * Sets the view up to allow the user to edit the image.
         */
        private void showEditMode(){
            ImageView imageView = new ImageView();
        }
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
                try{
                    ipLabel.setText("IP address: " + InetAddress.getLocalHost().getHostAddress());
                }catch(UnknownHostException error){
                    error.printStackTrace();
                }
                portLabel.setText("Port: " + server.getPort());
                container.getChildren().remove(hostServer);
                container.getChildren().addAll(ipLabel, portLabel);
                Project.getInstance().setServer(server);
            });

            container.getChildren().addAll(header, hostServer);
            return container;
        }

    }
}
