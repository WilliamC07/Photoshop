package views;

import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import project.Project;

public class ToolsComponent extends VBox {
    private final MainDisplay parent;
    private final Project project;
    private Color lastUsedColor;

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
        getChildren().addAll(shapeButton(), lineButton());
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

    private ColorPicker makeColorPicker(){
        ColorPicker colorPicker = new ColorPicker(lastUsedColor == null ? Color.BLACK : lastUsedColor);
        colorPicker.setOnAction(e -> lastUsedColor = colorPicker.getValue());
        return colorPicker;
    }
}
