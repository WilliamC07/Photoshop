package views;

import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;

public class ToolsComponent extends FlowPane {
    private final MainDisplay parent;

    ToolsComponent(MainDisplay parent){
        this.parent = parent;

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

    private Button shapeButton(){
        // Tool bar

        // Button to select the tool
        Button button = new Button("Shape");
        button.setOnAction(e -> {
            ColorPicker colorPicker = new ColorPicker(Color.BLACK);
            parent.getToolsExtensionComponent().setContents(colorPicker);

            parent.makeRectangle(colorPicker);
        });

        return button;
    }
}
