package model.imageManipulation.edits;

import javafx.scene.image.PixelWriter;

/**
 * All instructions should implement this method.
 * 
 * Images can be manipulated using WritableImage through PixelWriter. ImageBuilder holds the WritableImage and is 
 * edited indirectly using WritableImage's PixelWriter. ImageBuilder looks for this interface. See more instructions
 * on that class.
 *
 * @see model.imageManipulation.edits.ImageBuilder
 * @see javafx.scene.image.WritableImage
 * @see javafx.scene.image.PixelWriter
 * @author William Cao
 * @since 1.0
 */
interface Edit{
	/**
	 * This method will be the one directly working with PixelWriter from ImageBuilder. It should also be the only
	 * package-private method (other than the constructor). Everything else should be private (fields, methods, ect.) 
	 */
	void change(PixelWriter pixelWriter);
}