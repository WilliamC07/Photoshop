package views;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;

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
        Button createShapeButton = new Button("Shape");
        createShapeButton.setOnAction(this::createShape);


        getChildren().addAll(createShapeButton);
    }

    private void createShape(ActionEvent actionEvent){
        parent.makeRectangle();
    }
}
