package ch.zhaw.spro.windowcontrollers;

import ch.zhaw.spro.SetUpUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Controller for the AddShiftTypeWindow.
 * This class handles the user interactions for adding a new shift type.
 * It is responsible for managing the UI elements and actions related to the AddShiftTypeWindow.
 */
public class AddShiftTypeWindowController {

    private SetUpUI setUpUI;
    @FXML
    private Button btnClose;
    @FXML
    public void closeAction() {
        setUpUI.showAddShiftTypeWindow(btnClose);
    }

    public void setSetUpUI(SetUpUI setUpUI) {
        this.setUpUI = setUpUI;
    }

}
