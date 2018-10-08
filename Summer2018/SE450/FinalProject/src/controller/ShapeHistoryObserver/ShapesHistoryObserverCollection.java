package controller.ShapeHistoryObserver;

import controller.DrawShape.IShape;
import model.persistence.ApplicationState;
import view.gui.PaintCanvas;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ShapesHistoryObserverCollection {
    private List<IShape> currentShapeList = new ArrayList<IShape>();
    private List<IShape> selectedList = new ArrayList<IShape>();

    public void registerCurrentShape(IShape shape){
        currentShapeList.add(shape);
    }

    public void registerSelectedShape(IShape shape){
        selectedList.add(shape);
    }

    public List<IShape> getCurrentShapeList() {
        return currentShapeList;
    }

    public List<IShape> getSelectedShapeList(){
        return selectedList;
    }

    public void clearSelectedShapeList(){
        selectedList = new ArrayList<IShape>();
    }

    public void redrawAll(PaintCanvas paintCanvas){
        Graphics2D canvas = paintCanvas.getGraphics2D();
        canvas.setColor(Color.WHITE);
        canvas.fillRect(0,0, 1000000, 1000000);
        for (IShape shape :
                currentShapeList) {
            shape.drawShape(paintCanvas);
        }
    }
}
