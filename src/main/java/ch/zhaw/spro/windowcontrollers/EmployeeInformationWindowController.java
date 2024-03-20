package ch.zhaw.spro.windowcontrollers;


import ch.zhaw.spro.models.Absence;
import ch.zhaw.spro.models.Employee;
import ch.zhaw.spro.models.Qualification;
import ch.zhaw.spro.models.SystemDirector;
import com.jfoenix.controls.JFXCheckBox;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.image.ImageView;
import lombok.Setter;
import org.controlsfx.control.PopOver;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the EmployeeInformation PopOver
 * The user can see the information of an employee
 */
public class EmployeeInformationWindowController {
    @FXML
    public MenuButton btnChooseAbsences;
    @FXML
    public Button btnSave;
    @FXML
    private ImageView avatar;

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
    private Label labelAHV;

    @FXML
    private Label labelAddress;

    @FXML
    private Label labelFirstName;

    @FXML
    private Label labelLastName;

    @Setter
    private PopOver popOver;
    private SystemDirector systemDirector;
    private final ChooseAbsencesMenu chooseAbsencesMenu = new ChooseAbsencesMenu();
    private Employee employee;
    private final ArrayList<String> qualificationList = new ArrayList<>();
    private  List<JFXCheckBox> checkBoxList = new ArrayList<>();

    @FXML
    public void btnClose() {
        popOver.hide();
    }

    /**
     * Sets the current employee and displays the information
     * @param employee the employee to be displayed
     */
    public void setCurrentEmployee(Employee employee) {
        this.employee = employee;
        labelFirstName.setText(employee.getFirstName());
        labelLastName.setText(employee.getLastName());
        labelAddress.setText(employee.getStreet());
        labelAHV.setText(employee.getAhvNumber());
        ImageUtils imageUtils = new ImageUtils();
        imageUtils.displayImage(employee.getImage(), avatar);
        for (Qualification qualification : systemDirector.getQualificationsFromEmployee(employee)){
            for (CheckBox checkBox : checkBoxList){
                if (qualification.getName().equals(checkBox.getText())){
                    checkBox.setSelected(true);
                }
            }
        }
        for (Absence absence : systemDirector.getAbsences(employee)){
            chooseAbsencesMenu.addAbsence(absence);
        }

    }
    /**
     * Sets the SystemDirector object
     * Sets the checkboxes to the qualifications of the employee
     * @param systemDirector the SystemDirector object
     */
    public void setSystemDirector(SystemDirector systemDirector) {
        this.systemDirector = systemDirector;
        checkBoxList = List.of(cbQ1, cbQ2, cbQ3, cbQ4, cbQ5, cbQ6);
        WindowControllerUtils.initializeQualificationsCheckboxes(checkBoxList, systemDirector);

    }

    @FXML
    public void btnSaveOnAction() {
        checkQualifications();
        systemDirector.editAbsenceAndQualificationsOfEmployee(employee, chooseAbsencesMenu.getSelectedAbsenceDates(), qualificationList);
        popOver.hide();
    }
    private void checkQualifications(){
        for (CheckBox checkBox : checkBoxList){
            if (checkBox.isSelected()){
                qualificationList.add(checkBox.getText());
            }
        }
    }

    @FXML
    private void initialize() {
        btnChooseAbsences.getItems().clear();
        btnChooseAbsences.setOnShowing(event -> chooseAbsencesMenu.showAbsencesMenu(btnChooseAbsences));
    }
}
