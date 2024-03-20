package ch.zhaw.spro.windowcontrollers;

import ch.zhaw.spro.SetUpUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Controller for the Calendar Window in the application.
 * This class manages the interactions related to the calendar view, including closing the calendar window.
 */
public class CalendarWindowController {

    private SetUpUI setUpUI;
    @FXML
    private Button btnClose;
    @FXML
    public void closeAction() {
        setUpUI.showCalendarWindow(btnClose);
    }

    public void setSetUpUI(SetUpUI setUpUI) {
        this.setUpUI = setUpUI;
    }

}
