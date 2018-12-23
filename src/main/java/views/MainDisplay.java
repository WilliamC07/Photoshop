package views;

import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import model.imageManipulation.edits.*;
import project.Project;


/**
 * This is the view that is always shown to the user. The components are:
 * 1. TODO Editing information
 * 2. TODO Editing board
 * 3. TODO Chat
 * 4. TODO Switch to different pages
 * <p>
 * Should only be initialized once in Head. We are not having multiple windows, instead we have pages.
 * This is a SplitPane to allow the photo editor to choose how big he/she wants the sections to be.
 * <p>
 * Use a SplitPane to allow the user to switch
 *
 * @author William Cao
 * @see Head
 * @since 1.0
 */
final class MainDisplay extends SplitPane {
    private final Project project;
    private final PerformEdit performEdit;
    private ToolsComponent toolsComponent;
    private ToolsExtensionComponent toolsExtensionComponent;
    private EditingComponent editingComponent;
    private NetworkComponent networkComponent;
    private HistoryComponent historyComponent;

    private RequirePoints requirePoints;

    MainDisplay(Project project) {
        this.project = project;
        this.performEdit = new PerformEdit(project);
        // Screen is divided into 3 different parts
        this.toolsComponent = new ToolsComponent(this, project);
        this.toolsExtensionComponent = new ToolsExtensionComponent();
        this.editingComponent = new EditingComponent(project, this);

        VBox center = new VBox(toolsExtensionComponent, editingComponent);

        SplitPane rightSide = generateRightSide();
        getItems().addAll(toolsComponent, center, rightSide);
        setDividerPositions(0.2f, 0.6f);
    }

    void makeRectangle(ColorPicker colorPicker){
        RectangleFactory rectangleFactory = performEdit.createRectangle();
        rectangleFactory.setColorPicker(colorPicker);
        requirePoints = rectangleFactory;
        System.out.println("made rectangle factory");
    }

    void resize(int width, int height){
        new ResizeFactory(width, height, project.getImageBuilder());
        editingComponent.generateView();
    }

    void supplyPoints(Point point){
        if(requirePoints != null)
            requirePoints.addPoint(point);
    }

    /**
     * Creates the right side view
     */
    private SplitPane generateRightSide(){
        // Top is always network and bottom is always history
        this.networkComponent = new NetworkComponent(project);
        this.historyComponent = new HistoryComponent();
        SplitPane splitPane = new SplitPane(networkComponent, historyComponent);
        splitPane.setOrientation(Orientation.VERTICAL);

        return splitPane;
    }

    ToolsExtensionComponent getToolsExtensionComponent(){
        return toolsExtensionComponent;
    }
}
