package views;

import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import project.Project;

public class ToolsComponent extends FlowPane {
    private final MainDisplay parent;
    private final Project project;

    ToolsComponent(MainDisplay parent, Project project){
        this.parent = parent;
        this.project = project;
        // TODO: Design
        // Buttons and such
        createButtons();
    }

    /**
     * Tools for:
     * Creating shapes
     */
    private void createButtons(){
        getChildren().addAll(shapeButton(), resizeButton());
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
            ColorPicker colorPicker = new ColorPicker(Color.BLACK);
            parent.getToolsExtensionComponent().setContents(colorPicker);

            parent.makeRectangle(colorPicker);
        });

        return button;
    }

    /**
     * Resize the image. This is a toolbar command
     * @return Button to click
     */
    private Button resizeButton(){
        Button button = new Button("Resize");
        button.setOnAction(e -> {
            // Tool bar
            TextField widthInput = new TextField();
            TextField heightInput = new TextField();
            Button push = new Button("Make changes");
            Label error = new Label();

            widthInput.setPromptText("Width");
            widthInput.setText(String.valueOf((int) project.getImageBuilder().getWritableImage().getWidth()));
            heightInput.setPromptText("Height");
            heightInput.setText(String.valueOf((int) project.getImageBuilder().getWritableImage().getHeight()));

            push.setOnAction(x -> {
                try{
                    int width = Integer.valueOf(widthInput.getText());
                    int height = Integer.valueOf(heightInput.getText());

                    if(width < 0 || height < 0){
                        throw new NumberFormatException();
                    }

                    parent.resize(width, height);
                }catch(NumberFormatException exc){
                    exc.printStackTrace();
                    error.setText("Must be a positive number");
                }
            });

            parent.getToolsExtensionComponent().setContents(widthInput, heightInput, push, error);
        });

        return button;
    }
}
