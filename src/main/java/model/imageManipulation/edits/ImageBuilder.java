package model.imageManipulation.edits;

import javafx.scene.image.WritableImage;
import javafx.scene.image.Image;

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
final class ImageBuilder{
	private final WritableImage WRITABLE_IMAGE;

	/**
	 * When the image is first loaded onto the program, it should run this method to allow edits to the image.
	 * @param image Image loaded from disk
	 */
	ImageBuilder(Image image){
		WRITABLE_IMAGE = new WritableImage(image.getPixelReader(), (int) image.getWidth(), (int) image.getHeight());
	}

	/**
	 * To optimize undo, call this method to jump straight into a previously saved state of the WRITABLE_IMAGE.
	 */
	ImageBuilder(WritableImage WritableImage){
		WRITABLE_IMAGE = WritableImage;
	}

	/**
	 * Builder method to make a change onto the image.
	 * @param instruction What edit to make on the image.
	 * @return Newly edited image
	 */
	ImageBuilder edit(Edit instruction){
		instruction.change(WRITABLE_IMAGE.getPixelWriter());
		return this;
	}

	/**
	 * Builder method to make multiple changes onto the image.
	 * Calls {@link #edit(Edit)} on each element of the array.
	 * @param instructions array of instructions to perform on the image
	 * @return Newly edited image
	 */
	ImageBuilder edit(Edit[] instructions){
		for(Edit instruction : instructions){
			edit(instruction);
		}

		return this;
	}
}