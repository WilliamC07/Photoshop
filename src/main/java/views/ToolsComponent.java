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
        getChildren().addAll(shapeButton());
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

}
