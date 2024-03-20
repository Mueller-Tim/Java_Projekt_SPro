package ch.zhaw.spro.windowcontrollers;

import ch.zhaw.spro.models.Employee;
import ch.zhaw.spro.models.SystemDirector;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * Class responsible for creating a horizontal box (HBox) layout for each employee.
 * This layout is used in the context of deleting an employee, containing the employee's details and a delete button.
 */
public class DeleteEmployeeHBox {

    /**
     * Creates and returns an HBox containing details of a given employee and a delete button.
     * The HBox includes the employee's image, name, AHV number, address, and a button to trigger deletion.
     *
     * @param employee The employee whose information is to be displayed.
     * @param systemDirector The system director instance for handling employee deletion.
     * @return A fully populated HBox ready to be used in a UI context.
     */
    public HBox createDeleteEmployeeHBox(Employee employee, SystemDirector systemDirector) {
        HBox hbox = WindowControllerUtils.getEmployeehBox();
        ImageView imageView = WindowControllerUtils.getEmployeeImageView(employee);
        imageView.setTranslateX(10);

        Label nameLabel = new Label(" " + employee.getFirstName() + " " + employee.getLastName());
        nameLabel.setMinWidth(150);
        setNodeStyle(nameLabel);

        Label ahvNr = new Label(employee.getAhvNumber());
        ahvNr.setMinWidth(100);
        setNodeStyle(ahvNr);

        Label address = new Label(employee.getCity() + " " + employee.getStreet());
        address.setMinWidth(250);
        setNodeStyle(address);

        Button deleteEmployee = getButton(employee, systemDirector);
        deleteEmployee.setTranslateX(-10);

        hbox.getChildren().addAll(imageView, nameLabel, ahvNr, address, deleteEmployee);
        return hbox;
    }

    private Button getButton(Employee employee, SystemDirector systemDirector) {
        Button deleteEmployee = new Button();
        deleteEmployee.setMinWidth(20);

        Image deleteIcon = new Image("/images/trash_can_icon.png");
        ImageView deleteIconView = new ImageView(deleteIcon);
        deleteIconView.setFitWidth(20);
        deleteIconView.setFitHeight(20);
        deleteEmployee.setGraphic(deleteIconView);

        deleteEmployee.setOnMouseClicked(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ConfirmDeleteEmployee.fxml"));
                VBox deleteEmployeeAnchorPane = loader.load();
                ConfirmDeleteEmployeeWindowController confirmDeleteEmployeeWindowController = loader.getController();
                WindowControllerUtils.showPopOver(deleteEmployeeAnchorPane, confirmDeleteEmployeeWindowController, deleteEmployee, employee, systemDirector);
            } catch (IOException ex) {
                throw new IllegalStateException("Could not load ConfirmDeleteEmployee.fxml", ex);
            }
        });
        return deleteEmployee;
    }

    private void setNodeStyle(Node node) {
        node.setStyle("-fx-font-size: 14px; -fx-font-weight: normal; -fx-text-fill: black; -fx-opacity: 1");
    }

}
