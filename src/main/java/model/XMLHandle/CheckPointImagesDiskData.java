package model.XMLHandle;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.transform.stream.StreamResult;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

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
        ArrayList<Path> imagePaths = new ArrayList<>();
        Document document = getReadDocument();
        Element root = document.getDocumentElement();

        // Get the original image path
        String originalImagePathString = root.getElementsByTagName("original").item(0).getTextContent();
        if(originalImagePathString.isBlank()){
            // If the original image doesn't exist, checkpoint images and most recent image cannot
            imagePaths.addAll(Arrays.asList(null, null, null));
            return imagePaths;
        }else{
            imagePaths.add(Paths.get(originalImagePathString));
        }

        // Get the checkpoint images
        Element checkpointImage = (Element) root.getElementsByTagName("checkpoints").item(0);
        NodeList checkPointImages = checkpointImage.getElementsByTagName("checkpoint");
        if(checkPointImages.getLength() == 0){
            // No checkpoint images
            imagePaths.add(null);
            return imagePaths;
        }else{
            // Checkpoint images do exists
            for(int i = 0; i < checkPointImages.getLength(); i++){
                Element element = (Element) checkPointImages.item(i);
                imagePaths.add(Paths.get(element.getTextContent()));
            }
        }

        // There will be a recent image since if we reached this point
        String recentImagePathString = root.getElementsByTagName("recent").item(0).getTextContent();
        imagePaths.add(Paths.get(recentImagePathString));

        return imagePaths;
    }

    @Override
    public void writeData(ArrayList<Path> data) {

    }
}
