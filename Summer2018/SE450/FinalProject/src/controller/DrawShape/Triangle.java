package controller.DrawShape;

import controller.ColorMap;
import model.ShapeColor;
import model.ShapeShadingType;
import model.ShapeType;
import model.persistence.ApplicationState;
import view.gui.PaintCanvas;

import java.awt.*;

public class Triangle implements IShape {
    private int startX;
    private int startY;
    private int endX;
    private int endY;
    private ShapeColor activePrimaryColor;
    private ShapeColor activeSecondaryColor;
    private ShapeShadingType activeShapeShadingType;

    public Triangle(ApplicationState applicationState){
        this.startX = applicationState.getStartPoint().x;
        this.startY = applicationState.getStartPoint().y;
        this.endX = applicationState.getEndPoint().x;
        this.endY = applicationState.getEndPoint().y;
        this.activePrimaryColor = applicationState.getActivePrimaryColor();
        this.activeSecondaryColor = applicationState.getActiveSecondaryColor();
        this.activeShapeShadingType = applicationState.getActiveShapeShadingType();
    }

    public Triangle(int x, int y, int width, int height, ShapeColor activePrimaryColor, ShapeColor activeSecondaryColor, ShapeShadingType activeShapeShadingType) {
        this.startX = x;
        this.startY = y;
        this.endX = x + width;
        this.endY = y + height;
        this.activePrimaryColor = activePrimaryColor;
        this.activeSecondaryColor = activeSecondaryColor;
        this.activeShapeShadingType = activeShapeShadingType;
    }

    @Override
    public void drawShape(PaintCanvas pc) {
        int topLeftX = Math.min(this.endX, this.startX);
        int topLeftY = Math.min(this.endY, this.startY);
        int x = Math.abs(this.endX - this.startX);
        int y = Math.abs(this.endY - startY);
        Point top = new Point(topLeftX + (x / 2), topLeftY);
        Point bottomLeft = new Point(topLeftX, topLeftY + y);
        Point bottomRight = new Point(topLeftX + x, topLeftY + y);

        Graphics2D graphics = pc.getGraphics2D();
        graphics.setColor(ColorMap.getColor(this.activePrimaryColor.toString()));
        graphics.setStroke(new BasicStroke(5));

        if(this.activeShapeShadingType.equals(ShapeShadingType.OUTLINE)){
            graphics.drawPolygon(new int[] {top.x, bottomLeft.x, bottomRight.x}, new int[]{top.y, bottomLeft.y, bottomRight.y}, 3);
        }
        else if(this.activeShapeShadingType.equals(ShapeShadingType.FILLED_IN)) {
            graphics.fillPolygon(new int[]{top.x, bottomLeft.x, bottomRight.x}, new int[]{top.y, bottomLeft.y, bottomRight.y}, 3);
        }
        else if(this.activeShapeShadingType.equals(ShapeShadingType.OUTLINE_AND_FILLED_IN)){
            graphics.fillPolygon(new int[]{top.x, bottomLeft.x, bottomRight.x}, new int[]{top.y, bottomLeft.y, bottomRight.y}, 3);
            graphics.setColor(ColorMap.getColor(this.activeSecondaryColor.toString()));
            graphics.drawPolygon(new int[] {top.x, bottomLeft.x, bottomRight.x}, new int[]{top.y, bottomLeft.y, bottomRight.y}, 3);
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
        return new Triangle(0, 0, Math.abs(this.endX - this.startX), Math.abs(this.endY - this.startY),
                activePrimaryColor, activeSecondaryColor, activeShapeShadingType);
    }
}
