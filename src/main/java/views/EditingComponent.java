package views;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import model.imageManipulation.edits.Point;
import project.Project;

import java.io.File;

/**
 * If the user chose the original image for the project or one already exists, this should be be shown the the user.
 * This is where the user sees image being worked on and can click on parts of this to make changes.
 *
 * @see MainDisplay
 */
class EditingComponent extends VBox {
    private ImageView imageView;
    private ScrollPane imageWrapper;
    /**
     * The width of the fit width of {@link #imageView}
     */
    private int viewWidth;
    /**
     * Width of the image (not of the {@link #imageView})
     */
    private int imageWidth;
    /**
     * Height of the image (not of the {@link #imageView})
     */
    private int imageHeight;
    /**
     * The height of the fit height of {@link #imageView}
     */
    private int viewHeight;
    /**
     * Zoom factor of how zoomed in or zoomed out the {@link #imageView} is
     */
    private double scaleFactor;
    /**
     * Maximum scale factor the {@link #imageView} can be
     */
    private final double maxScaleFactor = 2;
    /**
     * Minimum scale factor the {@link #imageView} can be
     */
    private final double minScaleFactor = .5;

    private final Project project;

    private final MainDisplay parent;

    EditingComponent(Project project, MainDisplay parent){
        this.project = project;
        this.parent = parent;
        // TODO: should be located in a css for design
        setAlignment(Pos.CENTER);

        if(project.getOriginalImage() != null){
           generateView();
        }else{
            // Allow the user to choose a file
            showRequestMode();
        }
    }

    /**
     * Sets the view up to ask the user to choose the base picture of the project. If the user chooses a valid image
     * (defined by project.Project), the view will change using {@link #generateView()} and remove all the nodes
     * created by this method.
     *
     * TODO: This section should be taken out and be made into a separate class (IntroductionComponent.java) in the same
     *
     */
    private void showRequestMode(){
        Label directions = new Label("Open a image to get started");
        Button chooseFile = new Button("Pick an image");
        Label errorLabel = new Label();

        // directions label design
        directions.setFont(new Font(30));

        // chooseFile functionality
        chooseFile.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(this.getScene().getWindow());
            // Check null to make sure the user did choose a file and didn't click close/cancel
            if(file != null && project.setOriginalImage(file)){
                // User can now start editing the image
                // Remove the nodes created by this method
                this.getChildren().removeAll(directions, chooseFile, errorLabel);
                // show the image on the screen
                generateView();
            }else{
                errorLabel.setText("Please choose a valid image (.png files only)");
            }
        });

        this.getChildren().addAll(directions, chooseFile, errorLabel);
    }

    /**
     * Sets the view up to allow the user to edit the image.
     * Call this to refresh the view.
     */
    public void generateView(){
        WritableImage image = project.getImageBuilder().getWritableImage();
        viewWidth = (int) image.getWidth();
        imageHeight = (int) image.getHeight();

        imageView = new ImageView(image);
        imageWrapper = new ScrollPane(imageView);

        imageView.setPreserveRatio(true);

        imageView.setOnZoom((ZoomEvent z) -> {
            double zoomFactor = z.getZoomFactor();

            // Only resize the view if the zoom in or zoom out fits the requirements
            if (!(zoomFactor * viewWidth >= image.getWidth() * maxScaleFactor ||
                  zoomFactor * viewWidth <= image.getWidth() * minScaleFactor)) {
                viewWidth *= zoomFactor;
                imageHeight *= zoomFactor;

                scaleFactor = imageView.getLayoutBounds().getWidth() / image.getWidth();

                // Update the view to show how zoomed in the image is
                imageView.setFitWidth(viewWidth);
                imageView.setFitHeight(imageHeight);

                // Relocate the positioning of the imageWrapper to actually zoom in on the cursor
                Point centerPoint = new Point(z.getX(), z.getY(), scaleFactor);
                imageWrapper.setHvalue(centerPoint.getX() / image.getWidth());
                imageWrapper.setVvalue(centerPoint.getY() / image.getWidth());
            }
        });

        imageView.setOnMouseClicked(e -> {
            int xCord = (int) (e.getX() / scaleFactor);
            int yCord = (int) (e.getY() / scaleFactor);
            System.out.println(xCord);
            System.out.println(yCord);
        });

        // Only add if it isn't already being shown
        if(!getChildren().contains(imageWrapper)){
            getChildren().add(imageWrapper);
        }

        // TODO: Show multiple pages --  see Google docs for more information
    }
}
