package view.interfaces;

import view.EventName;

public interface IUiModule {
    void addEvent(EventName eventName, IEventCallback command);
    <T> T getDialogResponse(IDialogChoice dialogChoice);
}
