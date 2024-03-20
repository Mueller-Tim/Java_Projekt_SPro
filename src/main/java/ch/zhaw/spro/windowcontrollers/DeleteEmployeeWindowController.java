package ch.zhaw.spro.windowcontrollers;

import ch.zhaw.spro.models.Employee;
import ch.zhaw.spro.SetUpUI;
import ch.zhaw.spro.models.SystemDirector;
import javafx.fxml.FXML;
import javafx.scene.control.Button;


import com.jfoenix.controls.JFXListView;
import javafx.scene.layout.HBox;

/**
 * Controller class for ConfirmDeleteEmployee.fxml
 * Manages the user interface for deleting employees. It displays a list of employees and handles the deletion process.
 */
public class DeleteEmployeeWindowController {

    @FXML
    private JFXListView<HBox> employeeListView;

    @FXML
    private Button btnClose;

    private SetUpUI setUpUI;

    private SystemDirector systemDirector;

    @FXML
    public void btnClose() {
        setUpUI.showDeleteEmployeePopOver(btnClose);
    }

    /**
     * Initializes the windowcontrollers with SetUpUI and SystemDirector instances.
     * Sets up the windowcontrollers to display the list of employees and manages the listener for employee data updates.
     * @param setUpUI The SetUpUI instance for managing UI interactions.
     * @param systemDirector The SystemDirector instance for handling business logic related to employees.
     */
    public void setSetupUIAndSystemDirector(SetUpUI setUpUI, SystemDirector systemDirector) {
        this.setUpUI = setUpUI;
        this.systemDirector = systemDirector;
        systemDirector.addListener(this::showEmployeeList);
        showEmployeeList();
    }

    private void showEmployeeList() {
        employeeListView.getItems().clear();
        for (Employee employee : systemDirector.getAllEmployee()) {
            DeleteEmployeeHBox deleteEmployeeHBox = new DeleteEmployeeHBox();
            employeeListView.getItems().add(deleteEmployeeHBox.createDeleteEmployeeHBox(employee, systemDirector));
        }
    }
}
