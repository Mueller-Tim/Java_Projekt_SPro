package ch.zhaw.spro.windowcontrollers;

import ch.zhaw.spro.SetUpUI;
import ch.zhaw.spro.models.SystemDirector;
import com.jfoenix.controls.JFXCheckBox;
import javafx.fxml.FXML;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the Add Employee Window.
 * Manages the UI interactions for adding a new employee, including capturing employee details, image selection, and validation.
 */
public class AddEmployeeWindowController {
    @FXML
    public Label lblError;
    @FXML
    public MenuButton btnChooseAbsences;
    @FXML
    private ImageView avatar;

    @FXML
    private Button btnClose;

    @FXML
    private Button btnSave;

    @FXML
    private JFXCheckBox cbQ1;

    @FXML
    private JFXCheckBox cbQ2;

    @FXML
    private JFXCheckBox cbQ3;

    @FXML
    private JFXCheckBox cbQ4;

    @FXML
    private JFXCheckBox cbQ5;

    @FXML
    private JFXCheckBox cbQ6;

    @FXML
    private TextField fldAHV;

    @FXML
    private TextField fldStreet;
    @FXML
    private TextField fldCity;

    @FXML
    private TextField fldFirstName;

    @FXML
    private TextField fldLastName;
    @FXML
    private ScrollPane errorScrollPane;
    private SetUpUI setUpUI;
    private SystemDirector systemDirector;
    private List<JFXCheckBox> checkBoxList ;
    private final ChooseAbsencesMenu chooseAbsencesMenu = new ChooseAbsencesMenu();
    private final List<String> qualificationList = new ArrayList<>();
    private byte[] imageBytes;

    @FXML
    public void btnClose() {
        setUpUI.showAddEmployeePopOver(btnClose);
    }

    @FXML
    public void btnSave() {
            try {
                checkQualifications();
                systemDirector.addEmployee(imageBytes, fldFirstName.getText(),
                fldLastName.getText(), fldStreet.getText(), fldCity.getText(), fldAHV.getText(), qualificationList, chooseAbsencesMenu.getSelectedAbsenceDates());
                setUpUI.showAddEmployeePopOver(btnSave);
            } catch (IllegalArgumentException e) {
                lblError.setVisible(true);
                errorScrollPane.setVisible(true);
                lblError.setStyle("-fx-text-fill: red;");
                lblError.setText(e.getMessage());
                lblError.setWrapText(true);
                lblError.setMaxWidth(280);
                errorScrollPane.setMaxWidth(300);
            }
    }

    @FXML
    public void avatarClicked(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image", "*.png", "*.jpg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(((Node) mouseEvent.getTarget()).getScene().getWindow());
        setUpUI.requestFocusOfPrimaryStage();
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            imageBytes = fileToByteArray(selectedFile);
            avatar.setImage(image);
        }

    }

    @FXML
    private void initialize() {
        btnChooseAbsences.getItems().clear();
        btnChooseAbsences.setOnShowing(event -> chooseAbsencesMenu.showAbsencesMenu(btnChooseAbsences));
    }
    /**
     * Sets the SetUpUI and SystemDirector objects from the SetUpUI class
     * and sets the text of the CheckBoxes to the qualifications from the SystemDirector
     * @param setUpUI The SetUpUI object from the SetUpUI class
     * @param systemDirector The SystemDirector object from the SetUpUI class
     */
    public void setSetupUIAndSystemDirector(SetUpUI setUpUI, SystemDirector systemDirector) {
        this.setUpUI = setUpUI;
        this.systemDirector = systemDirector;
        checkBoxList = List.of(cbQ1, cbQ2, cbQ3, cbQ4, cbQ5, cbQ6);
        WindowControllerUtils.initializeQualificationsCheckboxes(checkBoxList, systemDirector);
    }
    private byte[] fileToByteArray(File file) {
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            return new byte[0];
        }
    }
    private void checkQualifications(){
        for (CheckBox checkBox : checkBoxList){
            if (checkBox.isSelected()){
                qualificationList.add(checkBox.getText());
            }
        }
    }
}
