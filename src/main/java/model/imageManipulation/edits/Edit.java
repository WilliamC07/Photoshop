package model.imageManipulation.edits;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

/**
 * All instructions should extend this class.
 * 
 * Images can be manipulated using WritableImage through PixelWriter. ImageBuilder holds the WritableImage and is 
 * edited indirectly using WritableImage's PixelWriter. ImageBuilder looks for this abstract method. See more
 * instructions on that class.
 *
 * The constructor for the subclasses must have a constructor that accepts the string representation of the object.
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
	abstract void change(PixelWriter pixelWriter, PixelReader pixelReader);

	/**
	 * When writing the Edit onto a xml file, this method will return what data to write. This allows the object to be
	 * recreated from the xml file.
	 *
	 * The xml file can be sent through sockets (between users and server) or it can be written to disk.
	 * @return String representation of the object to be written onto an xml file.
	 */
	public abstract String getStringRepresentation();

	/**
	 * Two Edits are the same if their String Representation is equal. This is a pretty ineffective way as com
	 * @param o Object to be compared to
	 * @return True if the two edits are the same, false otherwise
	 */
	@Override
	public boolean equals(Object o){
		return o instanceof Edit && ((Edit) o).getStringRepresentation().equals(this.getStringRepresentation());
	}
}