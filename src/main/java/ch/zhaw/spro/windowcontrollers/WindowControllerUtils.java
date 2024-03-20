package ch.zhaw.spro.windowcontrollers;

import ch.zhaw.spro.models.Employee;
import ch.zhaw.spro.models.SystemDirector;
import com.jfoenix.controls.JFXCheckBox;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.stage.Window;
import org.controlsfx.control.PopOver;

import java.util.List;

/**
 * This class is used to show a PopOver window.
 * It is used in the following classes:
 * - EmployeeInformationWindowController
 * - ConfirmDeleteEmployeeWindowController
 */
public class WindowControllerUtils {

    /**
     * This method shows a PopOver window with the given parameters.
     * @param anchorPane The anchorPane to show the PopOver on.
     * @param controller The windowControllers to Window Controller to use.
     * @param triggerLabel The label to trigger the PopOver.
     * @param employee The employee to show the PopOver for.
     * @param systemDirector The systemDirector Object.
     */
    public static void showPopOver(Node anchorPane, Object controller, Node triggerLabel, Employee employee, SystemDirector systemDirector) {
        PopOver popOver = new PopOver(anchorPane);

        if (controller instanceof EmployeeInformationWindowController specificController) {
            specificController.setPopOver(popOver);
            specificController.setSystemDirector(systemDirector);
            specificController.setCurrentEmployee(employee);
        } else if (controller instanceof ConfirmDeleteEmployeeWindowController specificController) {
            specificController.setPopOver(popOver);
            specificController.setSystemDirector(systemDirector);
            specificController.setCurrentEmployee(employee);
        }

        Window window = triggerLabel.getScene().getWindow();
        popOver.setArrowSize(0);
        popOver.setDetachable(false);
        popOver.setCornerRadius(20);
        popOver.show(window);
    }

    /**
     * This method creates a HBox for the employee.
     * @return The created HBox.
     */
    public static HBox getEmployeehBox() {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(30);
        hbox.setPrefHeight(45);
        hbox.setMinHeight(45);
        hbox.setStyle("-fx-background-color: #fcfafa;" + "-fx-border-style: solid inside;" + "-fx-border-width: 1;" + "-fx-border-insets: 1.5;" + "-fx-border-radius: 5;" + "-fx-border-color: black;");
        return hbox;
    }

    /**
     * This method creates an ImageView for the employee.
     * @param employee The employee to create the ImageView for.
     * @return The created ImageView.
     */
    public static ImageView getEmployeeImageView(Employee employee) {
        ImageUtils imageUtils = new ImageUtils();
        ImageView imageView = new ImageView();
        imageUtils.displayImage(employee.getImage(), imageView);
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);
        imageView.setClip(new Circle(15, 15, 15));
        return imageView;
    }

    /**
     * This method initializes the checkboxes for the qualifications.
     * @param checkBoxList The list of checkboxes to initialize.
     * @param systemDirector The systemDirector Object.
     */
    public static void initializeQualificationsCheckboxes(List<JFXCheckBox> checkBoxList, SystemDirector systemDirector) {
        List<String> qualifications = systemDirector.getQualificationsFromEmployee();
        for (int i = 0; i < qualifications.size(); i++) {
            checkBoxList.get(i).setText(qualifications.get(i));
        }
    }
}