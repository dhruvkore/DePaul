package controller.Commands;

import model.persistence.ApplicationState;

public class CommandFactory {
    public static ICommand getCommand(ApplicationState applicationState){
        ICommand command;
        IUndoable undoCommand;
        switch(applicationState.getActiveStartAndEndPointMode()) {
            case DRAW:
                command = new DrawCommand();
                undoCommand = (IUndoable) command;
                applicationState.getUndoList().add(undoCommand);
                break;
            case MOVE:
                command = new MoveCommand();
                undoCommand = (IUndoable) command;
                applicationState.getUndoList().add(undoCommand);
                break;
            case SELECT:
                command = new SelectCommand();
                break;
            default:
                command = new DrawCommand();
                undoCommand = (IUndoable) command;
                applicationState.getUndoList().add(undoCommand);
                break;
        }

        return command;
    }
}
