package views;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.information.InputCheck;
import network.Server;
import project.Project;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetworkComponent extends VBox {
    private final Project project;
    private final ListView<String> collabList = new ListView<>();

    NetworkComponent(Project project){
        this.project = project;
        Label header = new Label("Network:");
        this.getChildren().add(header);
        // Only able to host the server if the user is not connected to a server
        if(!project.isConnectedToServer()){
            setUpServerComponents();
        }
    }

    private void setUpServerComponents(){
        Label usernameLabel = new Label("Your username:");
        TextField usernameField = new TextField();
        Button hostServerButton = new Button("Host Server");
        Label errorLabel = new Label();

        hostServerButton.setOnAction(e -> {
            if(project.getOriginalImage() != null){
                if(InputCheck.checkUsername(errorLabel, usernameField.getText())){
                    // remove nodes created by this method
                    getChildren().removeAll(usernameLabel, usernameField, hostServerButton, errorLabel);

                    setUpServer();
                    project.setMyUsername(usernameField.getText());
                    updateCollabList(project.getCollaborators());
                }
            }else{
                errorLabel.setText("Please choose an image before sharing");
            }
        });

        getChildren().addAll(usernameLabel, usernameField, hostServerButton, errorLabel);
    }

    private void setUpServer(){
        Label ipLabel = new Label();
        Label portLabel = new Label();
        Server server = new Server(project);
        try{
            ipLabel.setText("IP: " + InetAddress.getLocalHost().getHostAddress());
        }catch (UnknownHostException e){
            e.printStackTrace();
            return;
        }
        // Static port
        portLabel.setText("Port: 5000");
        getChildren().addAll(ipLabel, portLabel);
        project.setServer(server);
    }

    public void updateCollabList(String[] collabs){
        // add the list to the view if it doesn't already exist
        if(!getChildren().contains(collabList)){
            getChildren().add(collabList);
        }


        // clear list to refresh
        collabList.getItems().clear();

        for(String collab : collabs){
            if(collab.equals(project.getMyUsername())){
                collabList.getItems().add(collab + " (you)");
            }else{
                collabList.getItems().add(collab);
            }
        }
    }
}
