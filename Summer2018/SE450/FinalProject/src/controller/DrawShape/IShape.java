package controller.DrawShape;

import model.persistence.ApplicationState;
import view.gui.PaintCanvas;

public interface IShape {
    void drawShape(PaintCanvas pc);
    int getStartX();
    int getStartY();
    int getEndX();
    int getEndY();

    void move(int x, int y);

    IShape clone();
}
