package model.XMLHandle;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Helps in creating and reading an xml file from the disk.
 */
final class XMLHelper {
    private static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    /**
     * Creates a blank document.
     * @return A blank document.
     */
    static Document createDocument(){
        DocumentBuilder builder;
        try{
             builder = factory.newDocumentBuilder();
             return builder.newDocument();
        }catch(ParserConfigurationException e){
            // Do nothing
            e.printStackTrace();
        }
        return null; // Error reached at this point
    }

    /**
     * Gets the document representation of the given path.
     * @param path
     * @return
     */
    static Document getDocument(Path path){
        File file = path.toFile();
        if(!file.isFile()){
            throw new IllegalArgumentException("File doesn't exist");
        }

        try{
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            document.normalizeDocument();
            return document;
        }catch(ParserConfigurationException | IOException | SAXException e){
            // Do nothing
            e.printStackTrace();
        }

        return null;  // Error reached at this point
    }

    static void saveFile(Path saveLocation, Document document){
        try{
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            DOMSource source = new DOMSource(document);
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // So the xml isn't one long line
            transformer.transform(source, new StreamResult(saveLocation.toFile()));
        }catch(TransformerException e){
            e.printStackTrace();
            //Do nothing
        }
    }
}
