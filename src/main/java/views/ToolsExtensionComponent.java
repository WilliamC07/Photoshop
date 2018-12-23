package views;

import javafx.scene.Node;
import javafx.scene.layout.HBox;

class ToolsExtensionComponent extends HBox {

    ToolsExtensionComponent(){
        // Sizing
        final double size = 30d;
        setMaxHeight(size);
        setMinHeight(size);
    }

    /**
     * Clears all the previous (if any) elements and adds the given.
     * Do not access getChildren().
     * @param elements Nodes to be added to this component
     */
    void setContents(Node... elements){
        this.getChildren().setAll(elements);
    }
}
