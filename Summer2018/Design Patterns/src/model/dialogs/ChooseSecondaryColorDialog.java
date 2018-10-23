package model.dialogs;

import model.ShapeColor;
import model.interfaces.IApplicationState;
import view.interfaces.IDialogChoice;

public class ChooseSecondaryColorDialog implements IDialogChoice<ShapeColor> {

    private final IApplicationState applicationState;

    public ChooseSecondaryColorDialog(IApplicationState applicationState) {
        this.applicationState = applicationState;
    }

    @Override
    public String getDialogTitle() {
        return "Secondary Color";
    }

    @Override
    public String getDialogText() {
        return "Select a secondary color from the menu below:";
    }

    @Override
    public ShapeColor[] getDialogOptions() {
        return ShapeColor.values();
    }

    @Override
    public ShapeColor getCurrentSelection() {
        return applicationState.getActiveSecondaryColor();
    }
}
