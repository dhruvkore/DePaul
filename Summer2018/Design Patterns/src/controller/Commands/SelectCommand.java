package controller.Commands;

import controller.DrawShape.IShape;
import model.persistence.ApplicationState;
import java.awt.Rectangle;
import java.util.List;

public class SelectCommand implements ICommand, IUndoable {
    private ApplicationState applicationState;

    @Override
    public void execute(ApplicationState applicationState) {
        this.applicationState = applicationState;
        List<IShape> currentList = applicationState.getShapesHistoryObserverCollection().getCurrentShapeList();
        applicationState.getShapesHistoryObserverCollection().clearSelectedShapeList();
        List<IShape> selectedList = applicationState.getShapesHistoryObserverCollection().getSelectedShapeList();

        int startX = applicationState.getStartPoint().x;
        int startY = applicationState.getStartPoint().y;
        int endX = applicationState.getEndPoint().x;
        int endY = applicationState.getEndPoint().y;

        int topLeftX = Math.min(endX, startX);
        int topLeftY = Math.min(endY, startY);
        int x = Math.abs(endX - startX);
        int y = Math.abs(endY - startY);

        Rectangle selectedRectangle = new Rectangle(topLeftX, topLeftY, x, y);
        Rectangle currentShape;

        for(IShape shape : currentList){
            int topLeftcurrentX = Math.min(shape.getEndX(), shape.getStartX());
            int topLeftcurrentY = Math.min(shape.getEndY(), shape.getStartY());
            int xcurrent = Math.abs(shape.getEndX() - shape.getStartX());
            int ycurrent = Math.abs(shape.getEndY() - shape.getStartY());
            currentShape = new Rectangle(topLeftcurrentX, topLeftcurrentY, xcurrent, ycurrent);
            if(topLeftX < topLeftcurrentX + xcurrent
                    && topLeftX + x > topLeftcurrentX
                    && topLeftY < topLeftcurrentY + ycurrent
                    && topLeftcurrentY + y > topLeftcurrentY){
                selectedList.add(shape);
            }
        }
    }

    @Override
    public void undo() {
        //Not Used
    }

    @Override
    public void redo() {
        //Not Used
    }
}
