package controller.Commands;

import controller.DrawShape.IShape;
import model.persistence.ApplicationState;

import java.util.ArrayList;
import java.util.List;

public class MoveCommand implements ICommand, IUndoable {
    private int moveX;
    private int moveY;
    private ApplicationState applicationState;
    private List<IShape> movedShapes = new ArrayList<IShape>();

    @Override
    public void execute(ApplicationState applicationState) {
        this.applicationState = applicationState;
        moveX = applicationState.getEndPoint().x - applicationState.getStartPoint().x;
        moveY = applicationState.getEndPoint().y - applicationState.getStartPoint().y;
        for (IShape shape :
                this.applicationState.getShapesHistoryObserverCollection().getSelectedShapeList()) {
            shape.move(moveX, moveY);
            movedShapes.add(shape);
        }
        redrawAll();
    }

    @Override
    public void undo() {
        for (IShape shape :
                movedShapes) {
            shape.move(-moveX, -moveY);
        }
        redrawAll();
    }

    @Override
    public void redo() {
        for (IShape shape :
                movedShapes) {
            shape.move(moveX, moveY);
        }
        redrawAll();
    }

    public void redrawAll(){
        applicationState.getShapesHistoryObserverCollection().redrawAll(applicationState.getPaintCanvas());
    }
}
