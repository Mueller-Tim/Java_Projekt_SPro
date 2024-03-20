package ch.zhaw.spro.windowcontrollers;

import ch.zhaw.spro.SetUpUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Controller for managing the Information Window in the application.
 * This class is responsible for handling user interactions within the Information Window,
 * such as closing the window.
 */
public class InformationWindowController {

    private SetUpUI setUpUI;
    @FXML
    private Button btnClose;
    @FXML
    public void closeAction() {
        setUpUI.showInformationWindow(btnClose);
    }

    public void setSetupUIAndSystemDirector(SetUpUI setUpUI) {
        this.setUpUI = setUpUI;
    }

}
