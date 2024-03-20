package ch.zhaw.spro.windowcontrollers;

import ch.zhaw.spro.SetUpUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;


/**
 * The {@code SettingWindowController} class is responsible for controlling the behavior of the setting window in the UI.
 * It interacts with the {@link SetUpUI} class to manage the display and actions within the setting window.
 */
public class SettingWindowController {

    private SetUpUI setUpUI;
    @FXML
    private Button btnClose;
    @FXML
    public void closeAction() {
        setUpUI.showSettingWindow(btnClose);
    }

    public void setSetUpUI(SetUpUI setUpUI) {
        this.setUpUI = setUpUI;
    }

}
