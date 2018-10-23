package controller.Commands;

import controller.DrawShape.IShape;
import controller.ShapeHistoryObserver.ShapesHistoryObserverCollection;
import model.persistence.ApplicationState;

import java.util.ArrayList;
import java.util.List;

public class DeleteCommand implements ICommand, IUndoable {
    ApplicationState applicationState;
    List<IShape> deletedShapes = new ArrayList<IShape>();

    @Override
    public void execute(ApplicationState applicationState) {
        this.applicationState = applicationState;
        List<IShape> currentShapes = applicationState.getShapesHistoryObserverCollection().getCurrentShapeList();
        for (IShape shape :
                applicationState.getShapesHistoryObserverCollection().getSelectedShapeList()) {
            deletedShapes.add(shape);
            currentShapes.remove(shape);
        }
        applicationState.getShapesHistoryObserverCollection().redrawAll(applicationState.getPaintCanvas());
    }

    @Override
    public void undo() {
        ShapesHistoryObserverCollection shapesHistoryObserverCollection = applicationState.getShapesHistoryObserverCollection();
        for (IShape shape:
                deletedShapes) {
            shapesHistoryObserverCollection.registerCurrentShape(shape);
        }
        applicationState.getShapesHistoryObserverCollection().redrawAll(applicationState.getPaintCanvas());
    }

    @Override
    public void redo() {
        List<IShape> currentShapes = applicationState.getShapesHistoryObserverCollection().getCurrentShapeList();
        for (IShape shape:
                deletedShapes) {
            currentShapes.remove(shape);
        }
        applicationState.getShapesHistoryObserverCollection().redrawAll(applicationState.getPaintCanvas());
    }
}
