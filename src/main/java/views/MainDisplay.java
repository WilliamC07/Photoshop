package views;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
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
public final class MainDisplay extends SplitPane {
    private final Project project;
    private ToolsComponent toolsComponent;
    private ToolsExtensionComponent toolsExtensionComponent;
    private EditingComponent editingComponent;
    private NetworkComponent networkComponent;

    private RequirePoints requirePoints;

    MainDisplay(Project project) {
        this.project = project;
        // Screen is divided into 3 different parts
        this.toolsComponent = new ToolsComponent(this, project);
        this.toolsExtensionComponent = new ToolsExtensionComponent();
        this.editingComponent = new EditingComponent(project, this);

        VBox center = new VBox(toolsExtensionComponent, editingComponent);
        center.setAlignment(Pos.CENTER);

        getItems().addAll(toolsComponent, center, generateRightSide());
        setDividerPositions(0.1f, 0.8f);
    }

    void makeRectangle(ColorPicker colorPicker){
        RectangleBuilder builder = new RectangleBuilder(project.getImageBuilder(), colorPicker);
        requirePoints = builder;
    }

    void makeLine(ColorPicker colorPicker, TextField width){
        LineBuilder lineBuilder = new LineBuilder(project.getImageBuilder(), colorPicker, width);
        requirePoints = lineBuilder;
    }

    void makeBlurredRectangle(){
      BlurredRectangleBuilder blurBuilder = new BlurredRectangleBuilder(project.getImageBuilder());
      requirePoints = blurBuilder;
    }

    void makeBrightness(boolean makeBrighter){
        new BrightnessBuilder(project.getImageBuilder(), makeBrighter).makeEdit();
    }

    void supplyPoints(Point point){
        if(requirePoints != null) {
            requirePoints.addPoint(point);
            if(!requirePoints.needMorePoints()){
                requirePoints = null;
            }
        }
    }

    /**
     * Creates the right side view
     */
    private NetworkComponent generateRightSide(){
        // Top is always network and bottom is always history
        this.networkComponent = new NetworkComponent(project);

        return this.networkComponent;
    }

    ToolsExtensionComponent getToolsExtensionComponent(){
        return toolsExtensionComponent;
    }

    public NetworkComponent getNetworkComponent() {
        return networkComponent;
    }

    public void regenerateEditingView(){
        editingComponent.updateView();
    }
}
