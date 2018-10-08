package controller.Commands;

import controller.DrawShape.IShape;
import controller.DrawShape.ShapeStaticFactory;
import model.persistence.ApplicationState;

public class DrawCommand implements ICommand, IUndoable {
    private ApplicationState applicationState;
    private IShape shape;

    @Override
    public void execute(ApplicationState applicationState) {
        this.applicationState = applicationState;
        this.shape = ShapeStaticFactory.getShape(applicationState);
        this.shape.drawShape(applicationState.getPaintCanvas());
        applicationState.getShapesHistoryObserverCollection().registerCurrentShape(shape);
        applicationState.clearRedoList();
    }

    @Override
    public void undo() {
        applicationState.getShapesHistoryObserverCollection().getCurrentShapeList().remove(shape);
    }

    @Override
    public void redo() {
        applicationState.getShapesHistoryObserverCollection().registerCurrentShape(shape);
    }
}
