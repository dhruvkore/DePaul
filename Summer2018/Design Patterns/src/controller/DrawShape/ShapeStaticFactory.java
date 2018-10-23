package controller.DrawShape;

import model.ShapeType;
import model.persistence.ApplicationState;

public class ShapeStaticFactory {
    public static IShape getShape(ApplicationState applicationState){
        ShapeType shapeName = applicationState.getActiveShapeType();
        IShape shape;
        switch(shapeName){
            case ELLIPSE:
                shape = new Ellipse(applicationState);
                break;
            case RECTANGLE:
                shape = new Rectangle(applicationState);
                break;
            case TRIANGLE:
                shape = new Triangle(applicationState);
                break;
            default:
                shape = new Rectangle(applicationState);
        }
        return shape;
    }
}
