package views;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import model.information.ScreenDimensions;
import project.ProjectFactory;
import views.wrappers.*;

import java.io.File;
import java.io.IOException;

class WelcomeScreen extends VBox {
    private final int TITLE_FONT_SIZE = 30;
    private final int HEADER_FONT_SIZE = 20;
    private final int TEXT_SIZE = 13;

    WelcomeScreen(){
        this.setAlignment(Pos.CENTER);
        this.setPrefSize(ScreenDimensions.welcomeWidth, ScreenDimensions.welcomeHeight);

        makeTitle();
        makeCreatorOption();
        makeOpenerOption();
    }

    /**
     * Makes the title label and a separator at the top of the view.
     */
    private void makeTitle(){
        Label welcomeLabel = new Label("Photoshop Java");
        welcomeLabel.setFont(new Font(TITLE_FONT_SIZE));

        Separator separator = new Separator(Orientation.HORIZONTAL);
        separator.setMaxWidth(Double.MAX_VALUE);  // Takes up the entire width

        getChildren().addAll(welcomeLabel, separator);
    }

    /**
     * Makes the section that allows the user to make a project and a separator from the other container.
     * This goes below {@link #makeTitle()}.
     */
    private void makeCreatorOption(){
        // Section that allows the user to make a project
        Label header = new Label("Create a new project");
        Label inputLabel = new Label("Project name:");
        TextField inputField = new TextField();
        Button makeProjectButton = new Button(">");
        Label errorLabel = new Label();
        HBox makerContainer = new HBox(inputLabel, inputField, makeProjectButton);

        // header design
        header.setFont(new Font(HEADER_FONT_SIZE));

        // inputLabel design
        inputLabel.setFont(new Font(TEXT_SIZE));

        // Input field design and action
        inputField.setFont(new Font(TEXT_SIZE));
        inputField.setOnKeyPressed(k -> {
            if (k.getCode() == KeyCode.ENTER) {
                createProject(inputField.getText(), errorLabel);
            }
        });

        // makeProjectButton design and action
        makeProjectButton.setOnAction(e -> createProject(inputField.getText(), errorLabel));

        // container design
        makerContainer.setPadding(new Insets(5));  // The nodes won't touch each other and appear clumped together
        makerContainer.setAlignment(Pos.CENTER);
        VBox.setVgrow(makerContainer, Priority.ALWAYS); // Divide the WelcomeScreen screen space evenly with other containers

        // Divider to separate the project maker from the other section
        Separator s1 = new Separator();
        Label orLabel = new Label(" or "); // Spaces so separators don't touch the word
        Separator s2 = new Separator();
        HBox orSeparatorContainer = new HBox(s1, orLabel, s2);

        // Make the separators take up the greatest amount of space while being the same size
        HBox.setHgrow(s1, Priority.ALWAYS);
        HBox.setHgrow(s2, Priority.ALWAYS);

        // Make the separators in the middle of orLabel
        orSeparatorContainer.setAlignment(Pos.CENTER);

        // Or text design
        orLabel.setFont(new Font(20));

        getChildren().addAll(makerContainer, errorLabel, orSeparatorContainer);
    }

    /**
     * Makes the section that allows the user to either open an already existing project or connect to a server.
     */
    private void makeOpenerOption(){
        Label header = new Label("Open an existing project");

        Button openDirectoryChooserButton = new Button("Choose Project");
        // Need a wrapper to allow centering of the button
        VBox openDirectoryContainer = new VBox(openDirectoryChooserButton);

        Separator separator = new Separator(Orientation.VERTICAL);

        TextField ipInputField = new TextField();
        TextField portInputField = new TextField();
        Button connectButton = new Button("Connect");
        VBox connectToServerContainer = new VBox(ipInputField, portInputField, connectButton);

        HBox headContainer = new HBox(openDirectoryContainer, separator, connectToServerContainer);

        // header design
        header.setFont(new Font(HEADER_FONT_SIZE));

        // Centering openDirectoryChooserButton
        openDirectoryContainer.setAlignment(Pos.CENTER);

        // Centering items for connectToServerContainer
        connectToServerContainer.setAlignment(Pos.CENTER);

        // Connecting to server text fonts and text
        ipInputField.setFont(new Font(TEXT_SIZE));
        ipInputField.setPromptText("IP address");
        ipInputField.setMaxWidth(ScreenDimensions.welcomeWidth / 4.0);
        portInputField.setFont(new Font(TEXT_SIZE));
        portInputField.setPromptText("Port");
        portInputField.setMaxWidth(ScreenDimensions.welcomeWidth / 8.0);

        // Head container spacing nodes
        // Have to set pref width to actually make it center (connectToServerContainer would take up too much space)
        headContainer.setAlignment(Pos.CENTER);
        HBox.setHgrow(connectToServerContainer, Priority.ALWAYS);
        connectToServerContainer.setPrefWidth(ScreenDimensions.welcomeWidth / 2.0);
        HBox.setHgrow(openDirectoryContainer, Priority.ALWAYS);
        openDirectoryContainer.setPrefWidth(ScreenDimensions.welcomeWidth / 2.0);


        // Make the container take up space equally
        VBox.setVgrow(headContainer, Priority.ALWAYS);

        getChildren().addAll(header, headContainer);
    }

    private void createProject(String projectName, Label errorLabel){
        if(projectName.isBlank()){
            errorLabel.setText("Enter a project name");
            return;
        }

        try{
            ProjectFactory.createProject(projectName);
        }catch(IOException e){
            errorLabel.setText("Please enter a unique valid project name");
        }
    }

    /**
     * Opens the DirectoryChooser to allow the user to open a project. If the directory is a valid project, it will
     * open that project. If it is not, it will inform the user through the label given
     * @param errorLabel Label to output the error message to the user
     */
    private void showDirectoryChooser(Label errorLabel){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(this.getScene().getWindow());
    }
}
