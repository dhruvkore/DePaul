package controller.Commands;

import javafx.application.Application;
import model.persistence.ApplicationState;

public interface ICommand {
    void execute(ApplicationState applicationState);
}
