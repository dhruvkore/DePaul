package controller.MouseHandlers;

import controller.Commands.CommandFactory;
import controller.Commands.ICommand;
import model.persistence.ApplicationState;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DrawHandler extends MouseAdapter {

    ApplicationState applicationState;

    public void setApplicationState(ApplicationState applicationState){
        this.applicationState = applicationState;
    }

    public void mousePressed(MouseEvent mouseEvent){
        System.out.println(mouseEvent.getX());
        applicationState.setStartPoint(mouseEvent.getX(), mouseEvent.getY());
    }

    public void mouseReleased(MouseEvent mouseEvent){
        applicationState.setEndPoint(mouseEvent.getX(), mouseEvent.getY());
        ICommand command = CommandFactory.getCommand(applicationState);
        command.execute(applicationState);
    }
}
