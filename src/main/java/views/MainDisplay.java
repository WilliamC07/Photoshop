package views;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.imageManipulation.edits.PerformEdit;
import model.imageManipulation.edits.Point;
import model.imageManipulation.edits.RectangleFactory;
import model.imageManipulation.edits.RequirePoints;
import network.Server;
import project.Project;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * This is the view that is always shown to the user. The components are:
 * 1. TODO Editing information
 * 2. TODO Editing board
 * 3. TODO Chat
 * 4. TODO Switch to different pages
 * <p>
 * Should only be initialized once in Head. We are not having multiple windows, instead we have pages.
 * This is a SplitPane to allow the photo editor to choose how big he/she wants the sections to be.
 * <p>
 * Use a SplitPane to allow the user to switch
 *
 * @author William Cao
 * @see Head
 * @since 1.0
 */
final class MainDisplay extends SplitPane {
    private final Project project;
    private final PerformEdit performEdit;
    private RequirePoints requirePoints;

    MainDisplay(Project project) {
        this.project = project;
        this.performEdit = new PerformEdit(project);
        // Screen is divided into 3 different parts
        getItems().addAll(new ToolsComponent(this), new EditingComponent(project, this), new RightSide());
        setDividerPositions(0.2f, 0.6f);
    }

    void makeRectangle(){
        RectangleFactory rectangleFactory = performEdit.createRectangle();
        rectangleFactory.setColor(Color.BLACK);
        requirePoints = rectangleFactory;
        System.out.println("made rectangle factory");
    }

    void supplyPoints(Point point){
        if(requirePoints != null)
            requirePoints.addPoint(point);
    }

    /**
     * Goes on the right of the screen.
     * Allows the user to host a server and view the collaborators and information on how to join (give ip address and
     * port). Below this, the user can see the history of all the edits done on the image.
     */
    private class RightSide extends SplitPane {
        RightSide() {
            // Sets up the SplitPane
            // Network half
            getItems().add(setNetworkDisplay());
            // History half
            getItems().add(new Rectangle(20, 20)); // Replace with history section
        }

        private VBox setNetworkDisplay() {
            VBox container = new VBox();
            Label header = new Label("Network:");
            Button hostServer = new Button("Host server");
            Label ipLabel = new Label();
            Label portLabel = new Label();
            Label errorLabel = new Label();
            // Hosts the server for others to connect
            hostServer.setOnAction(e -> {
                if (project.getOriginalImage() != null) {
                    Server server = new Server(project);
                    try {
                        ipLabel.setText("IP address: " + InetAddress.getLocalHost().getHostAddress());
                    } catch (UnknownHostException error) {
                        error.printStackTrace();
                    }
                    portLabel.setText("Port: 5000");
                    container.getChildren().removeAll(hostServer, errorLabel);
                    container.getChildren().addAll(ipLabel, portLabel);
                    project.setServer(server);
                } else {
                    errorLabel.setText("Please choose an image before sharing");
                }
            });

            container.getChildren().addAll(header, hostServer, errorLabel);
            return container;
        }

    }
}
