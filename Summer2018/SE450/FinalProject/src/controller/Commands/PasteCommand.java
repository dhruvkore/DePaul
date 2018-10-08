package controller.Commands;

import controller.DrawShape.IShape;
import controller.ShapeHistoryObserver.ShapesHistoryObserverCollection;
import model.persistence.ApplicationState;

import java.util.ArrayList;
import java.util.List;

public class PasteCommand implements ICommand, IUndoable {
    ApplicationState applicationState;
    List<IShape> pastedShapes = new ArrayList<IShape>();

    @Override
    public void execute(ApplicationState applicationState) {
        this.applicationState = applicationState;
        for (IShape shape :
                applicationState.getCopyList()) {
            IShape shapeClone = shape.clone();
            pastedShapes.add(shapeClone);
            applicationState.getShapesHistoryObserverCollection().registerCurrentShape(shapeClone);
        }
        applicationState.getShapesHistoryObserverCollection().redrawAll(applicationState.getPaintCanvas());
    }

    @Override
    public void undo() {
        List<IShape> currentShapes = applicationState.getShapesHistoryObserverCollection().getCurrentShapeList();
        for (IShape shape:
             pastedShapes) {
            currentShapes.remove(shape);
        }
        applicationState.getShapesHistoryObserverCollection().redrawAll(applicationState.getPaintCanvas());
    }

    @Override
    public void redo() {
        ShapesHistoryObserverCollection shapesHistoryObserverCollection = applicationState.getShapesHistoryObserverCollection();
        for (IShape shape:
                pastedShapes) {
            shapesHistoryObserverCollection.registerCurrentShape(shape);
        }
        applicationState.getShapesHistoryObserverCollection().redrawAll(applicationState.getPaintCanvas());
    }
}
