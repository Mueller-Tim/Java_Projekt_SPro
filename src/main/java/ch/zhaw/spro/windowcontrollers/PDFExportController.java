package ch.zhaw.spro.windowcontrollers;

import ch.zhaw.spro.SetUpUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * This class is the windowcontrollers for the PDFExportWindow.fxml file.
 * It is used to display the PDF export Information.
 */
public class PDFExportController {

    private SetUpUI setUpUI;
    @FXML
    private Button btnClose;
    @FXML
    private Label lblExportInformation;
    @FXML
    public void closeAction() {
        setUpUI.showPDFExportWindow(btnClose);
    }

    public void setSetUpUI(SetUpUI setUpUI) {
        this.setUpUI = setUpUI;
        lblExportInformation.setText(setUpUI.getPDFExportError());
    }

}
