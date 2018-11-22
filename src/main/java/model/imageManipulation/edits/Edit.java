package model.imageManipulation.edits;

import javafx.scene.image.PixelWriter;

/**
 * All instructions should extend this class.
 * 
 * Images can be manipulated using WritableImage through PixelWriter. ImageBuilder holds the WritableImage and is 
 * edited indirectly using WritableImage's PixelWriter. ImageBuilder looks for this abstract method. See more
 * instructions on that class.
 *
 * @see model.imageManipulation.edits.ImageBuilder
 * @see javafx.scene.image.WritableImage
 * @see javafx.scene.image.PixelWriter
 * @author William Cao
 * @since 1.0
 */
public abstract class Edit{
	/**
	 * This method will be the one directly working with PixelWriter from ImageBuilder. It should also be the only
	 * package-private method (other than the constructor). Everything else should be private (fields, methods, ect.) 
	 */
	abstract void change(PixelWriter pixelWriter);
}