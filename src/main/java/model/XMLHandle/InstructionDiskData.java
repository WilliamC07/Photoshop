package model.XMLHandle;

import model.imageManipulation.edits.Edit;
import java.nio.file.Path;
import java.util.ArrayList;

public class InstructionDiskData extends XMLHandler<ArrayList<Edit>> {
    public InstructionDiskData(Path location){
        super(location);
    }

    @Override
    public ArrayList<Edit> readData() {
        return null;
    }

    @Override
    public void writeData(ArrayList<Edit> data) {

    }
}
