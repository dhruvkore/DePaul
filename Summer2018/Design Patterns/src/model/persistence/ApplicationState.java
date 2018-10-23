package model.persistence;

import controller.Commands.EventCommandFactory;
import controller.Commands.ICommand;
import controller.Commands.IUndoable;
import controller.DrawShape.IShape;
import controller.ShapeHistoryObserver.ShapesHistoryObserverCollection;
import model.ShapeColor;
import model.ShapeShadingType;
import model.ShapeType;
import model.StartAndEndPointMode;
import model.dialogs.DialogProvider;
import model.interfaces.IApplicationState;
import model.interfaces.IDialogProvider;
import view.EventName;
import view.gui.PaintCanvas;
import view.interfaces.IPaintCanvas;
import view.interfaces.IUiModule;

import javax.swing.*;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ApplicationState implements IApplicationState {
    private final IUiModule uiModule;
    private final IDialogProvider dialogProvider;

    private ShapeType activeShapeType;
    private ShapeColor activePrimaryColor;
    private ShapeColor activeSecondaryColor;
    private ShapeShadingType activeShapeShadingType;
    private StartAndEndPointMode activeStartAndEndPointMode;
    private PaintCanvas paintCanvas;
    private Point startPoint;
    private Point endPoint;
    private ShapesHistoryObserverCollection shapesHistoryObserverCollection;
    private Stack<IUndoable> undoList;
    private Stack<IUndoable> redoList;
    private List<IShape> copyList;

    public ApplicationState(IUiModule uiModule, PaintCanvas paintCanvas) {
        this.uiModule = uiModule;
        this.dialogProvider = new DialogProvider(this);
        this.paintCanvas = paintCanvas;
        this.shapesHistoryObserverCollection = new ShapesHistoryObserverCollection();
        this.undoList = new Stack<IUndoable>();
        this.redoList = new Stack<IUndoable>();
        this.copyList = new ArrayList<IShape>();
        setDefaults();
    }

    @Override
    public void setActiveShape() {
        activeShapeType = uiModule.getDialogResponse(dialogProvider.getChooseShapeDialog());
    }

    @Override
    public void setActivePrimaryColor() {
        activePrimaryColor = uiModule.getDialogResponse(dialogProvider.getChoosePrimaryColorDialog());
    }

    @Override
    public void setActiveSecondaryColor() {
        activeSecondaryColor = uiModule.getDialogResponse(dialogProvider.getChooseSecondaryColorDialog());
    }

    @Override
    public void setActiveShadingType() {
        activeShapeShadingType = uiModule.getDialogResponse(dialogProvider.getChooseShadingTypeDialog());
    }

    @Override
    public void setActiveStartAndEndPointMode() {
        activeStartAndEndPointMode = uiModule.getDialogResponse(dialogProvider.getChooseStartAndEndPointModeDialog());
    }

    @Override
    public void setStartPoint(int x, int y) {
        startPoint = new Point(x, y);
    }

    @Override
    public void setEndPoint(int x, int y) {
        endPoint = new Point(x, y);
    }

    @Override
    public ShapeType getActiveShapeType() {
        return activeShapeType;
    }

    @Override
    public ShapeColor getActivePrimaryColor() {
        return activePrimaryColor;
    }

    @Override
    public ShapeColor getActiveSecondaryColor() {
        return activeSecondaryColor;
    }

    @Override
    public ShapeShadingType getActiveShapeShadingType() {
        return activeShapeShadingType;
    }

    @Override
    public StartAndEndPointMode getActiveStartAndEndPointMode() {
        return activeStartAndEndPointMode;
    }

    @Override
    public Point getStartPoint() {
        return startPoint;
    }

    @Override
    public Point getEndPoint() {
        return endPoint;
    }

    @Override
    public PaintCanvas getPaintCanvas() {
        return paintCanvas;
    }

    @Override
    public ShapesHistoryObserverCollection getShapesHistoryObserverCollection() {
        return shapesHistoryObserverCollection;
    }

    @Override
    public List<IShape> getCopyList() {
        return copyList;
    }

    @Override
    public void undo() {
        if(undoList.size() < 1){
            return;
        }
        IUndoable command = undoList.pop();
        redoList.add(command);
        command.undo();
        shapesHistoryObserverCollection.redrawAll(paintCanvas);
    }

    @Override
    public void redo() {
        if(redoList.size() < 1){
            return;
        }
        IUndoable command = redoList.pop();
        undoList.add(command);
        command.redo();
        shapesHistoryObserverCollection.redrawAll(paintCanvas);
    }

    @Override
    public void copy(){
        copyList = new ArrayList<IShape>();
        for (IShape shape :
                getShapesHistoryObserverCollection().getSelectedShapeList()) {
            copyList.add(shape.clone());
        }
    }

    @Override
    public void paste(){
        ICommand command = EventCommandFactory.getEventCommand(this, EventName.PASTE);
        command.execute(this);
        IUndoable undoable = (IUndoable)command;
        undoList.add(undoable);
    }

    @Override
    public void delete(){
        ICommand command = EventCommandFactory.getEventCommand(this, EventName.DELETE);
        command.execute(this);
        IUndoable undoable = (IUndoable)command;
        undoList.add(undoable);
    }

    public Stack<IUndoable> getUndoList() {
        return undoList;
    }

    public Stack<IUndoable> getRedoList() {
        return redoList;
    }

    @Override
    public void clearRedoList(){
        redoList = new Stack<IUndoable>();
    }

    private void setDefaults() {
        activeShapeType = ShapeType.ELLIPSE;
        activePrimaryColor = ShapeColor.BLUE;
        activeSecondaryColor = ShapeColor.GREEN;
        activeShapeShadingType = ShapeShadingType.FILLED_IN;
        activeStartAndEndPointMode = StartAndEndPointMode.DRAW;
    }
}
