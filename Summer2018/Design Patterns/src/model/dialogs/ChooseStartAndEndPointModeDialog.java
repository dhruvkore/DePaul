package model.dialogs;

import model.StartAndEndPointMode;
import model.interfaces.IApplicationState;
import view.interfaces.IDialogChoice;

public class ChooseStartAndEndPointModeDialog implements IDialogChoice<StartAndEndPointMode> {
    private final IApplicationState applicationState;

    public ChooseStartAndEndPointModeDialog(IApplicationState applicationState) {

        this.applicationState = applicationState;
    }

    @Override
    public String getDialogTitle() {
        return "Start and End Point Mode";
    }

    @Override
    public String getDialogText() {
        return "Select a shading type from the menu below:";
    }

    @Override
    public StartAndEndPointMode[] getDialogOptions() {
        return StartAndEndPointMode.values();
    }

    @Override
    public StartAndEndPointMode getCurrentSelection() {
        return applicationState.getActiveStartAndEndPointMode();
    }
}
