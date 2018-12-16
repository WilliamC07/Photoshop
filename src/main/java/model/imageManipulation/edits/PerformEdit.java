package model.imageManipulation.edits;

import project.Project;

public class PerformEdit {
    private final Project project;

    public PerformEdit(Project project) {
        this.project = project;
    }

    public RectangleFactory createRectangle(){
        return new RectangleFactory(project.getImageBuilder());
    }
}
