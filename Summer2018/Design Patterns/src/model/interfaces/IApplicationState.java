package model.interfaces;

import controller.DrawShape.IShape;
import controller.ShapeHistoryObserver.ShapesHistoryObserverCollection;
import model.ShapeColor;
import model.ShapeShadingType;
import model.ShapeType;
import model.StartAndEndPointMode;
import view.gui.PaintCanvas;

import java.awt.Point;
import java.util.List;

public interface IApplicationState {
    void setActiveShape();

    void setActivePrimaryColor();

    void setActiveSecondaryColor();

    void setActiveShadingType();

    void setActiveStartAndEndPointMode();

    void setStartPoint(int x, int y);

    void setEndPoint(int x, int y);

    ShapeType getActiveShapeType();

    ShapeColor getActivePrimaryColor();

    ShapeColor getActiveSecondaryColor();

    ShapeShadingType getActiveShapeShadingType();

    StartAndEndPointMode getActiveStartAndEndPointMode();

    Point getStartPoint();

    Point getEndPoint();

    PaintCanvas getPaintCanvas();

    ShapesHistoryObserverCollection getShapesHistoryObserverCollection();

    List<IShape> getCopyList();

    void undo();

    void redo();

    void copy();

    void paste();

    void delete();

    void clearRedoList();
}
