package view.interfaces;

public interface IDialogChoice<T> {
    String getDialogTitle();

    String getDialogText();

    T[] getDialogOptions();

    T getCurrentSelection();
}
