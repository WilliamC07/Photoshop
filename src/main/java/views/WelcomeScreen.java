package views;

import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
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
    WelcomeScreen(){
        this.setAlignment(Pos.CENTER);
        this.setPrefSize(ScreenDimensions.welcomeWidth, ScreenDimensions.welcomeHeight);
        generateView();
    }

    private void generateView(){
        // Title
        Label welcomeLabel = new Label("Photoshop Java");
        welcomeLabel.setFont(new Font(30));
        Separator separator = new Separator(Orientation.HORIZONTAL);
        Utility.resizeWidth(separator, ScreenDimensions.welcomeWidth);

        int containersHeight = ScreenDimensions.welcomeHeight / 2 - 50;

        // Create a new project
        Label newProjectHeader = new Label("Create a new project");
        newProjectHeader.setFont(new Font(20));
        Label newProjectDirection = new Label("Project name:");
        TextField newProjectName = new TextField();
        Button newProjectButton = new Button(">");
        HBox newProjectChooserContainer = new WHBox(newProjectDirection, newProjectName, newProjectButton);
        newProjectChooserContainer.setAlignment(Pos.CENTER);
        Utility.resizeHeight(newProjectChooserContainer, containersHeight);
        Label newProjectError = new Label();

        // Or separator
        int separatorWidth = ScreenDimensions.welcomeWidth / 2 - 25;
        Separator s1 = new Separator();
        Utility.resizeWidth(s1, separatorWidth);
        Label orLabel = new Label(" or "); // Spaces so separators don't touch the word
        orLabel.setFont(new Font(20));
        Separator s2 = new Separator();
        Utility.resizeWidth(s2, separatorWidth);
        HBox orSeparatorContainer = new HBox(s1, orLabel, s2);
        Utility.resizeWidth(orSeparatorContainer, ScreenDimensions.welcomeWidth);
        orSeparatorContainer.setAlignment(Pos.CENTER);

        // Open existing project
        Label oldProjectHeader = new Label("Open existing project");
        oldProjectHeader.setFont(new Font(20));
        Button oldProjectChooser = new Button("Choose file");
        Label chosenFile = new Label();
        oldProjectChooser.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Open existing project");
            File file = directoryChooser.showDialog(this.getScene().getWindow());
            chosenFile.setText(file.getAbsolutePath());
        });
        Button openOldProject = new Button(">");

        HBox oldProjectContainer = new HBox(oldProjectChooser, chosenFile, openOldProject);
        oldProjectContainer.setAlignment(Pos.CENTER);
        Utility.resizeHeight(oldProjectContainer, containersHeight);


        newProjectButton.setOnAction(e -> createProject(newProjectName.getText(), newProjectError));

        this.getChildren().addAll(welcomeLabel, separator,
                                  newProjectHeader, newProjectChooserContainer, newProjectError,
                                  orSeparatorContainer,
                                  oldProjectHeader, oldProjectContainer);
    }

    private void createProject(String projectName, Label errorLabel){
        try{
            ProjectFactory.createProject(projectName);
        }catch(IOException e){
            e.printStackTrace();
            errorLabel.setText("Please enter a unique valid project name");
        }
    }
}
