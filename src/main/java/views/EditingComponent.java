package views;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import model.imageManipulation.edits.Point;
import project.Project;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

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
    private double scaleFactor = 1;
    /**
     * Maximum scale factor the {@link #imageView} can be for width
     */
    private final double maxScaleFactorWidth = 2;
    /**
     * Minimum scale factor the {@link #imageView} can be
     */
    private final double minScaleFactor = .8;

    private final Project project;

    private final MainDisplay parent;

    EditingComponent(Project project, MainDisplay parent){
        this.project = project;
        this.parent = parent;
        setAlignment(Pos.CENTER);

        if(project.getOriginalImage() != null){
            generateView();
        }else{
            // Allow the user to choose a file
            System.out.println("original image has not been found -----------");
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
        VBox container = new VBox();
        container.getChildren().addAll(openExistingComponent(temp -> getChildren().remove(container)));
        container.getChildren().add(separatorComponent());
        container.getChildren().add(makeNewImageComponent(temp -> getChildren().remove(container)));
        container.setAlignment(Pos.CENTER);
        container.setSpacing(10);

        getChildren().add(container);
    }

    private Node[] openExistingComponent(Consumer<Object> removeView){
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
                removeView.accept(null);
                // show the image on the screen
                generateView();
            }else{
                errorLabel.setText("Please choose a valid image (.png files only)");
            }
        });
        return new Node[]{directions, chooseFile, errorLabel};
    }

    private HBox separatorComponent(){
        Separator separator1 = new Separator(Orientation.HORIZONTAL);
        Label or = new Label(" or "); // spaces so the separators don't touch the text
        Separator separator2 = new Separator(Orientation.HORIZONTAL);

        or.setFont(new Font(30));

        // expand fully
        HBox.setHgrow(separator1, Priority.ALWAYS);
        HBox.setHgrow(separator2, Priority.ALWAYS);

        HBox container = new HBox(separator1, or, separator2);
        container.setAlignment(Pos.CENTER);

        return container;
    }

    private VBox makeNewImageComponent(Consumer<Object> removeView){
        TextField widthNode = new TextField();
        TextField heightNode = new TextField();
        ColorPicker backgroundNode = new ColorPicker(Color.BLACK);
        Button createNode = new Button("Create");
        Label errorNode = new Label();

        errorNode.setFont(new Font(20));
        widthNode.setPromptText("width (pixels)");
        heightNode.setPromptText("height (pixels)");

        HBox chooser = new HBox(widthNode, heightNode, backgroundNode, createNode);
        chooser.setAlignment(Pos.CENTER);
        chooser.setSpacing(5);
        VBox container = new VBox(chooser, errorNode);
        container.setAlignment(Pos.CENTER);

        createNode.setOnAction(e -> {
            int width = 500;
            int height = 500;
            Color colorChosen = backgroundNode.getValue();

            if(colorChosen.equals(Color.TRANSPARENT)){
                errorNode.setText("No support for transparent images");
                return;
            }

            try {
                width = Integer.parseInt(widthNode.getText());
                height = Integer.parseInt(heightNode.getText());

                if (width <= 0 || height <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException nfe) {
                errorNode.setText("Width and height must be positive integers");
                return;
            }
            // make new image
            WritableImage writableImage = new WritableImage(width, height);
            PixelWriter writer = writableImage.getPixelWriter();
            // fill in of the chosen color
            for(int w = 0; w < width; w++){
                for(int h = 0; h < height; h++){
                    writer.setColor(w, h, colorChosen);
                }
            }

            try{
                File temp = File.createTempFile("photoshopJava" + System.currentTimeMillis(), ".png", null);
                System.out.println(temp.toPath().toString());
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);
                ImageIO.write(bufferedImage, "png", temp);
                project.setOriginalImage(temp);
            }catch(IOException ioe) {
                errorNode.setText("failed to make file, try again");
                return;
            }

            // remove this and begin the view
            removeView.accept(null);
            generateView();
        });

        return container;
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
            if (!(zoomFactor * viewWidth >= image.getWidth() * maxScaleFactorWidth ||
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
            parent.supplyPoints(new Point(e.getX(), e.getY(), scaleFactor));
        });

        getChildren().clear();
        getChildren().add(imageWrapper);

        // TODO: Show multiple pages --  see Google docs for more information
    }
}
