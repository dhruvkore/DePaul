package view.interfaces;

import view.EventName;

import javax.swing.*;

public interface IGuiWindow {
    JButton getButton(EventName eventName);
}
