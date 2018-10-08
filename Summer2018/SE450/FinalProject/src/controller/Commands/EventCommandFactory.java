package controller.Commands;

import model.persistence.ApplicationState;
import view.EventName;

public class EventCommandFactory {
    public static ICommand getEventCommand(ApplicationState applicationState, EventName eventName)
    {
        ICommand command;
        IUndoable undoCommand;
        switch (eventName){
            case PASTE:
                command = new PasteCommand();
                undoCommand = (IUndoable) command;
                applicationState.getUndoList().add(undoCommand);
                break;
            case DELETE:
                command = new DeleteCommand();
                undoCommand = (IUndoable) command;
                applicationState.getUndoList().add(undoCommand);
                break;
            default:
                command = new PasteCommand();
                undoCommand = (IUndoable) command;
                applicationState.getUndoList().add(undoCommand);
                break;
        }

        return command;
    }
}
