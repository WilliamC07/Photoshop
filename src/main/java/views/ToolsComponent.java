package views;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import project.Project;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

public class ToolsComponent extends VBox {
    private final MainDisplay parent;
    private final Project project;
    private Color lastUsedColor;

    ToolsComponent(MainDisplay parent, Project project){
        this.parent = parent;
        this.project = project;
        this.setAlignment(Pos.CENTER);
        // TODO: Design
        // Buttons and such
        createButtons();
    }

    /**
     * Tools for:
     * Creating shapes
     */
    private void createButtons(){
        getChildren().addAll(shapeButton(), lineButton(), blurButton(), saveAs());
    }

    /**
     * Creates a rectangle on the image.
     * @return Button to click
     */
    private Button shapeButton(){
        // Button to select the tool
        Button button = new Button("Shape");
        button.setOnAction(e -> {
            // Tool bar
            ColorPicker colorPicker = makeColorPicker();
            parent.getToolsExtensionComponent().setContents(colorPicker);

            parent.makeRectangle(colorPicker);
        });

        return button;
    }

    private Button lineButton(){
        Button button = new Button("line");
        button.setOnAction(e -> {
            // tool bar
            ColorPicker colorPicker = makeColorPicker();
            TextField width = new TextField();
            width.setPromptText("width (> 0)");
            parent.getToolsExtensionComponent().setContents(colorPicker, width);

            parent.makeLine(colorPicker, width);
        });
        return button;
    }

    private Button saveAs(){
        Button button = new Button("save");
        button.setOnAction(event -> {
            // tool bar
            TextField nameField = new TextField(project.getProjectName());
            Button saveLocationButton = new Button("Pick location");
            Label error = new Label();

            nameField.setPromptText("Name of the file");

            saveLocationButton.setOnAction(saveEvent -> {
                String fileName = nameField.getText();
                DirectoryChooser directoryChooser = new DirectoryChooser();
                Path directoryToSaveTo = directoryChooser.showDialog(this.getScene().getWindow()).toPath();
                if(directoryToSaveTo == null){
                    error.setText("Please choose a director to save to");
                }else if(fileName.isBlank()){
                    error.setText("Please choose a file name");
                }else{
                    try{
                        Path fileLocation = directoryToSaveTo.resolve(fileName+".png");
                        // save the file
                        project.copyRecentImage(fileLocation);
                    }catch(InvalidPathException e){
                        error.setText("Invalid file name or location to save to");
                    }catch(IOException e){
                        // cannot make a copy
                        error.setText("Cannot save project, try again");
                    }
                }
            });

            parent.getToolsExtensionComponent().setContents(nameField, saveLocationButton, error);
        });

        return button;
    }

    private Button blurButton(){
      Button button = new Button("blur");
      button.setOnAction(e -> {
        parent.makeBlurredRectangle();
      });
      return button;
    }

    private ColorPicker makeColorPicker(){
        ColorPicker colorPicker = new ColorPicker(lastUsedColor == null ? Color.BLACK : lastUsedColor);
        colorPicker.setOnAction(e -> lastUsedColor = colorPicker.getValue());
        return colorPicker;
    }
}
