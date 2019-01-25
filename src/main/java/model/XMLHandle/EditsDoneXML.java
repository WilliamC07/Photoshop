package model.XMLHandle;

import model.Savable;
import model.imageManipulation.edits.DiskToEdit;
import model.imageManipulation.edits.Edit;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class EditsDoneXML implements Savable {
    private Document document;
    private final Path fileLocation;

    public EditsDoneXML(Path fileLocation){
        this.fileLocation = fileLocation;
        // Make sure the file exists
        if(Files.exists(fileLocation)){
            document = XMLHelper.getDocument(fileLocation);
            System.out.println("File existts");
        }else{
            // Create the file if it doesn't exist
            document = XMLHelper.createDocument();
            document.appendChild(document.createElement("edits"));
        }
    }

    public void writeData(Edit[] edits){
        // Writing is basically deleting all the old ones and rewriting it all
        document = XMLHelper.createDocument();
        document.appendChild(document.createElement("edits"));
        Node rootNode = document.getDocumentElement();
        for(Edit edit : edits){
            Node sub = document.createElement("edit");
            sub.appendChild(document.createTextNode(edit.getStringRepresentation()));
            rootNode.appendChild(sub);
        }
    }

    public Edit[] getData(){
        System.out.println("ran the getData()");
        // Occurs when no edits have been done
        if(!document.getDocumentElement().hasChildNodes()){
            //If there are no nodes, that means no edits had been done
            return new Edit[0];
        }
        NodeList nodeElements = document.getDocumentElement().getElementsByTagName("edit");
        Edit[] edits = new Edit[nodeElements.getLength()];
        for(int i = 0; i < nodeElements.getLength(); i++){
            Node text = nodeElements.item(i);
            edits[i] = DiskToEdit.getEdit(text.getTextContent());
        }
        System.out.println("read from xml+ " + Arrays.toString(edits));
        return edits;
    }


    @Override
    public void save() {
        XMLHelper.saveFile(fileLocation, document);
    }
}
