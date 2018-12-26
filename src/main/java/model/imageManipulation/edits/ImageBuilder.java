package model.imageManipulation.edits;

import javafx.scene.image.WritableImage;
import javafx.scene.image.Image;
import project.Project;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * This collects all the instructions and perform it on the WritableImage. 
 *
 * This builder is the only way to edit the image. You only need PixelWriter to edit an image, you do not need direct
 * access to WritableImage.
 *
 * @see javafx.scene.image.WritableImage
 * @see javafx.scene.image.PixelWriter
 * @see model.imageManipulation.edits.Edit
 * @author William Cao
 * @since 1.0
 */
public final class ImageBuilder{
	private WritableImage writableImage;
	private final LinkedList<Edit> edits_done = new LinkedList<>();
	private final Project project;

	/**
	 * When the image is first loaded onto the program, it should run this method to allow edits to the image.
	 * @param image Image loaded from disk
	 */
	public ImageBuilder(Project project, Image image){
		this.project = project;
        writableImage = new WritableImage(image.getPixelReader(), (int) image.getWidth(), (int) image.getHeight());
	}

	/**
	 * To optimize undo, call this method to jump straight into a previously saved state of the WRITABLE_IMAGE.
	 */
	public ImageBuilder(Project project, WritableImage writableImage){
		this.project = project;
        this.writableImage = writableImage;
	}

	/**
	 * Builder method to make a change onto the image.
	 * @param instruction What edit to make on the image.
	 * @param isNewEdit True if you want to make checkpoint images, false if you don't
	 * @return Newly edited image
	 */
	public ImageBuilder edit(Edit instruction, boolean isNewEdit){
		instruction.change(writableImage.getPixelWriter());
		instruction.change(this);
		edits_done.add(instruction);
		System.out.println(edits_done.size());

		// Make a checkpoint image every 10 edits
		if(isNewEdit && edits_done.size() % 10 == 0){
			int checkpointNumber = (edits_done.size() / 10) - 1;
			project.makeCheckpoint(checkpointNumber, writableImage);
		}
		return this;
	}

	/**
	 * Builder method to make multiple changes onto the image.
	 * Calls {@link #edit(Edit, boolean)} on each element of the array.
	 * @param instructions array of instructions to perform on the image
	 * @return Newly edited image
	 */
	public ImageBuilder edit(Edit[] instructions){
		for(Edit instruction : instructions){
			// Reading from disk, so don't recreate
			edit(instruction, false);
		}

		return this;
	}

	void setWritableImage(WritableImage writableImage){
	    this.writableImage = writableImage;
    }

    /**
     * This should only be used to display the image on the screen. Use the other methods in this class to perform
     * edits on it.
     * @return Image currently being worked on (same instance, not a copy)
     */
	public WritableImage getWritableImage(){
	    return writableImage;
    }

    public Edit[] getEdits(){
		return edits_done.toArray(new Edit[0]);
	}
}