package controller.DrawShape;

import controller.ColorMap;
import model.ShapeColor;
import model.ShapeShadingType;
import model.ShapeType;
import model.persistence.ApplicationState;
import view.gui.PaintCanvas;

import java.awt.*;

public class Ellipse implements IShape {
    private int startX;
    private int startY;
    private int endX;
    private int endY;
    private ShapeColor activePrimaryColor;
    private ShapeColor activeSecondaryColor;
    private ShapeShadingType activeShapeShadingType;

    public Ellipse(ApplicationState applicationState){
        this.startX = applicationState.getStartPoint().x;
        this.startY = applicationState.getStartPoint().y;
        this.endX = applicationState.getEndPoint().x;
        this.endY = applicationState.getEndPoint().y;
        this.activePrimaryColor = applicationState.getActivePrimaryColor();
        this.activeSecondaryColor = applicationState.getActiveSecondaryColor();
        this.activeShapeShadingType = applicationState.getActiveShapeShadingType();
    }

    public Ellipse(int startX, int startY, int endX, int endY,
                   ShapeColor activePrimaryColor, ShapeColor activeSecondaryColor, ShapeShadingType activeShapeShadingType) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.activePrimaryColor = activePrimaryColor;
        this.activeSecondaryColor = activeSecondaryColor;
        this.activeShapeShadingType = activeShapeShadingType;
    }

    @Override
    public void drawShape(PaintCanvas pc) {
        int topLeftX = Math.min(this.endX, this.startX);
        int topLeftY = Math.min(this.endY, this.startY);
        int x = Math.abs(this.endX - this.startX);
        int y = Math.abs(this.endY - this.startY);
        Graphics2D graphics = pc.getGraphics2D();
        graphics.setColor(ColorMap.getColor(this.activePrimaryColor.toString()));
        graphics.setStroke(new BasicStroke(5));

        if(this.activeShapeShadingType.equals(ShapeShadingType.OUTLINE)){
            graphics.drawOval(topLeftX, topLeftY, x, y);
        }
        else if(this.activeShapeShadingType.equals(ShapeShadingType.FILLED_IN)) {
            graphics.fillOval(topLeftX, topLeftY, x, y);
        }
        else if(this.activeShapeShadingType.equals(ShapeShadingType.OUTLINE_AND_FILLED_IN)){
            graphics.fillOval(topLeftX, topLeftY, x, y);
            graphics.setColor(ColorMap.getColor(this.activeSecondaryColor.toString()));
            graphics.drawOval(topLeftX, topLeftY, x, y);
        }
    }

    @Override
    public int getStartX() {
        return startX;
    }

    @Override
    public int getStartY() {
        return startY;
    }

    @Override
    public int getEndX() {
        return endX;
    }

    @Override
    public int getEndY() {
        return endY;
    }

    @Override
    public void move(int x, int y) {
        startX += x;
        endX += x;
        startY += y;
        endY += y;
    }

    @Override
    public IShape clone() {
        return new Ellipse(0, 0, Math.abs(this.endX - this.startX), Math.abs(this.endY - this.startY),
                activePrimaryColor, activeSecondaryColor, activeShapeShadingType);
    }
}
