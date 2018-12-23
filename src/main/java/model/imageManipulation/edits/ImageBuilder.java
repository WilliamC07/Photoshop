package model.imageManipulation.edits;

import javafx.scene.image.WritableImage;
import javafx.scene.image.Image;

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

	/**
	 * When the image is first loaded onto the program, it should run this method to allow edits to the image.
	 * @param image Image loaded from disk
	 */
	public ImageBuilder(Image image){
        writableImage = new WritableImage(image.getPixelReader(), (int) image.getWidth(), (int) image.getHeight());
	}

	/**
	 * To optimize undo, call this method to jump straight into a previously saved state of the WRITABLE_IMAGE.
	 */
	public ImageBuilder(WritableImage writableImage){
        this.writableImage = writableImage;
	}

	/**
	 * Builder method to make a change onto the image.
	 * @param instruction What edit to make on the image.
	 * @return Newly edited image
	 */
	public ImageBuilder edit(Edit instruction){
		instruction.change(writableImage.getPixelWriter());
		instruction.change(this);
		edits_done.add(instruction);
		return this;
	}

	/**
	 * Builder method to make multiple changes onto the image.
	 * Calls {@link #edit(Edit)} on each element of the array.
	 * @param instructions array of instructions to perform on the image
	 * @return Newly edited image
	 */
	public ImageBuilder edit(Edit[] instructions){
		for(Edit instruction : instructions){
			edit(instruction);
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
}