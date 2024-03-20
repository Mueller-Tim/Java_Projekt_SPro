package ch.zhaw.spro.windowcontrollers;

import ch.zhaw.spro.SetUpUI;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

/**
 * Controller class for StartWindow.fxml
 * This class is responsible for showing the start window (first window the user sees)
 */
public class StartWindowController {

    @FXML
    public AnchorPane startAnchorPane;

    private SetUpUI setUpUI;

    @FXML
    public void buttonCreatePlan() {
        setUpUI.changeScene("configuration");
    }

    @FXML
    public void buttonExit() {
        setUpUI.closeBackend();
        Platform.exit();
    }
    public void setSetupUI(SetUpUI setUpUI) {
        this.setUpUI = setUpUI;
    }
}
