package model.XMLHandle;

import model.Savable;
import model.imageManipulation.edits.DiskToEdit;
import model.imageManipulation.edits.Edit;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.nio.file.Path;

public class EditsDoneXML implements Savable {
    private Document document;
    private final Path fileLocation;

    public EditsDoneXML(Path fileLocation){
        this.fileLocation = fileLocation;
        if(fileLocation.toFile().isFile()){
            document = XMLHelper.getDocument(fileLocation);
        }else{
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
        // Occurs when no edits have been done
        if(!document.getDocumentElement().hasChildNodes()){
            return null;
        }
        NodeList nodeElements = document.getDocumentElement().getElementsByTagName("edit");
        Edit[] edits = new Edit[nodeElements.getLength()];
        for(int i = 0; i < nodeElements.getLength(); i++){
            Node text = nodeElements.item(i);
            edits[i] = DiskToEdit.getEdit(text.getTextContent());
        }
        return edits;
    }


    @Override
    public void save() {
        XMLHelper.saveFile(fileLocation, document);
    }
}
