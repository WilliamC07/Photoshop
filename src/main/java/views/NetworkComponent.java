package views;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import network.Server;
import project.Project;

import java.net.InetAddress;
import java.net.UnknownHostException;

class NetworkComponent extends VBox {
    private final Project project;

    NetworkComponent(Project project){
        this.project = project;
        setupView();
    }

    private void setupView(){
        Label header = new Label("Network:");
        Button hostServer = new Button("Host server");

        Label errorLabel = new Label();
        // Hosts the server for others to connect
        hostServer.setOnAction(e -> {
            if(connectToServer(errorLabel)){
                getChildren().removeAll(hostServer, errorLabel);
            }
        });

        getChildren().addAll(header, hostServer, errorLabel);
    }

    /**
     * Attempts to create a server to connect to
     * @param errorLabel Label to print error to the user
     * @return True if server created, false otherwise
     */
    private boolean connectToServer(Label errorLabel){
        Label ipLabel = new Label();
        Label portLabel = new Label();

        if (project.getOriginalImage() != null) {
            Server server = new Server(project);
            try {
                ipLabel.setText("IP address: " + InetAddress.getLocalHost().getHostAddress());
            } catch (UnknownHostException error) {
                error.printStackTrace();
                return false;
            }
            portLabel.setText("Port: 5000");
            getChildren().addAll(ipLabel, portLabel);
            project.setServer(server);
            return true;
        } else {
            errorLabel.setText("Please choose an image before sharing");
        }
        return false;
    }
}
