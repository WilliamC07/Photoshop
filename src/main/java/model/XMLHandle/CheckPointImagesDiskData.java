package model.XMLHandle;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.transform.stream.StreamResult;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

public class CheckPointImagesDiskData extends XMLHandler<ArrayList<Path>> {
    /**
     * Creates a xml file if one doesn't exist or reads data from it.
     * @param path Directory of where the xml file is located or will be located
     */
    public CheckPointImagesDiskData(Path path){
        super(path.resolve("images.xml"));
    }

    @Override
    void createBlankXML(StreamResult streamResult) {
        Document document = getEmptyDocument();
        Element root = document.createElement("information");

        // Contains the path to the original image
        root.appendChild(document.createElement("original"));

        // Contains multiple other elements that each contains a path to the respective checkpoint image
        Element checkpoints = document.createElement("checkpoints");
        checkpoints.setAttribute("amount", "0"); // Counter of how many checkpoint images exists
        root.appendChild(checkpoints);

        // Contains the path to the most recently saved image
        root.appendChild(document.createElement("recent"));

        document.appendChild(root);
        super.saveFile(document, streamResult);
    }

    @Override
    public ArrayList<Path> readData() {
        return null;
    }

    @Override
    public void writeData(ArrayList<Path> data) {

    }
}
