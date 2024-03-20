package ch.zhaw.spro.windowcontrollers;


import ch.zhaw.spro.models.Employee;
import ch.zhaw.spro.models.SystemDirector;
import javafx.fxml.FXML;
import org.controlsfx.control.PopOver;

/**
 * This class is responsible for the logic of the ConfirmDeleteEmployeeView.
 * It is used to confirm the deletion of an employee.
 */
public class ConfirmDeleteEmployeeWindowController {

    private PopOver popOver;
    private SystemDirector systemDirector;
    private Employee currentEmployee;

    @FXML
    public void cancelAction() {
        popOver.hide();
    }

    @FXML
    public void deleteAction() {
        if (currentEmployee != null) {
            systemDirector.deleteEmployee(currentEmployee);
        }
        popOver.hide();
    }

    public void setPopOver(PopOver popOver) {
        this.popOver = popOver;
    }

    public void setSystemDirector(SystemDirector systemDirector) {
        this.systemDirector = systemDirector;
    }
    public void setCurrentEmployee(Employee employee) {
        this.currentEmployee = employee;
    }
}

