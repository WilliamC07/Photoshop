package model.XMLHandle;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.nio.file.Path;

/**
 * Any class that deals with reading or writing to an XML file should
 * @param <T>
 */
abstract class XMLHandler<T>{
    /**
     * File of the XML.
     */
    private File file;

    /**
     * Constructs this abstract class.
     * @param path Path to the XML (will create the XML if it doesn't exists)
     */
    XMLHandler(Path path){
        file = new File(path.toUri());
    }

    /**
     * Creates the XML content with only the root element (no other elements or data).
     * @param rootName Name of the root element (Cannot be null value)
     */
    final void createBlankXML(String rootName){
        Document document = getEmptyDocument();

        document.appendChild(document.createElement(rootName));
        saveFile(document, new StreamResult(file));
    }

    /**
     * Read the data in {@link #file} into the given data structure.
     * @return Data structure to represent the read data
     */
    public abstract T readData();

    /**
     * Writes the data of the given parameter into the xml file in {@link #file}.
     * Should eventually call {@link #saveFile(Document, StreamResult)} to write to disk.
     * @param data
     */
    public abstract void writeData(T data);

    /**
     * Writes the document to disk.
     * @param document Document to be written into data.
     * @param streamResult StreamResult to write the data to.
     */
    final void saveFile(Document document, StreamResult streamResult){
        try{
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            DOMSource source = new DOMSource(document);
            transformer.transform(source, streamResult);
        }catch(TransformerException e){
            e.printStackTrace();
        }
    }

    /**
     * Creates a Document with no nodes.
     * @return An empty document.
     */
    final Document getEmptyDocument() {
        try{
            return DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        }catch(ParserConfigurationException e){
            e.printStackTrace();
        }
        return null;  // Should never reach this point
    }
}
