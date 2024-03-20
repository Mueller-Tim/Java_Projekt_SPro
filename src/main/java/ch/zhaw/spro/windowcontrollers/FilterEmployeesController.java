package ch.zhaw.spro.windowcontrollers;

import ch.zhaw.spro.SetUpUI;
import ch.zhaw.spro.models.SystemDirector;
import com.jfoenix.controls.JFXCheckBox;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the FilterEmployee PopOver
 * The user can filter the employees by their qualifications
 */
public class FilterEmployeesController {

    @FXML
    private Button btnApply;

    @FXML
    private Button btnClose;

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

    private SetUpUI setUpUI;
    private SystemDirector systemDirector;
    private List<JFXCheckBox> checkBoxList;
    private static final List<String> lastSelectedQualifications = new ArrayList<>();

    @FXML
    public void btnApply() {
        List<String> selectedQualifications = new ArrayList<>();
        for (JFXCheckBox checkBox : checkBoxList){
            if (checkBox.isSelected()){
                selectedQualifications.add(checkBox.getText());
                lastSelectedQualifications.add(checkBox.getText());
            }
            if (!checkBox.isSelected()){
                lastSelectedQualifications.remove(checkBox.getText());
            }
        }
        systemDirector.searchEmployeeByQualifications(selectedQualifications);
        setUpUI.showAddEmployeePopOver(btnApply);
    }
    @FXML
    public void btnClose() {
        setUpUI.showAddEmployeePopOver(btnClose);
    }

    /**
     * Sets the SetUpUI and SystemDirector object
     * Creates a list of all CheckBoxes
     * Sets the text of the CheckBoxes to the qualifications of the employees
     * If the lastSelectedQualifications list is not empty, the CheckBoxes will be selected according to the lastSelectedQualifications list
     * @param setUpUI The SetUpUI object from the Main class
     * @param systemDirector The SystemDirector object from the SetUpUI class
     */
    public void setSetupUIAndSystemDirector(SetUpUI setUpUI, SystemDirector systemDirector) {
        this.setUpUI = setUpUI;
        this.systemDirector = systemDirector;
        checkBoxList = List.of(cbQ1, cbQ2, cbQ3, cbQ4, cbQ5, cbQ6);
        WindowControllerUtils.initializeQualificationsCheckboxes(checkBoxList, systemDirector);
        setLastSelectedQualifications();
    }

    private void setLastSelectedQualifications() {
        if (!lastSelectedQualifications.isEmpty()){
            for (JFXCheckBox checkBox : checkBoxList){
                checkBox.setSelected(lastSelectedQualifications.contains(checkBox.getText()));
            }
        }
    }
}
