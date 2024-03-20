package ch.zhaw.spro.windowcontrollers;

import ch.zhaw.spro.models.Employee;
import ch.zhaw.spro.models.SystemDirector;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;


/**
 * This class is responsible for creating a horizontal box (HBox) layout for each employee.
 * The layout includes the employee's image, name, and an information label with a clickable icon.
 */
public class EmployeeHBox {

    /**
     * Creates an HBox for an employee, displaying their image, name, and an information label.
     *
     * @param employee The employee whose information is to be displayed.
     * @param systemDirector The SystemDirector instance, used to handle system-level operations.
     * @return An HBox containing the employee's details and information label.
     */
    public HBox createEmployeeHBox(Employee employee, SystemDirector systemDirector) {
        HBox hbox = WindowControllerUtils.getEmployeehBox();
        ImageView imageView = WindowControllerUtils.getEmployeeImageView(employee);


        Label nameLabel = new Label(employee.getFirstName() + " " + employee.getLastName());
        nameLabel.setMinWidth(200);
        setNodeStyle(nameLabel);

        Label informationLabel = getInformationLabel(employee, systemDirector);

        hbox.getChildren().addAll(imageView, nameLabel, informationLabel);
        return hbox;
    }

    private Label getInformationLabel(Employee employee, SystemDirector systemDirector) {
        ImageView imageView = new ImageView(new Image("/images/info_button_icon.png"));
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);

        Label informationLabel = new Label("", imageView);
        informationLabel.setMinWidth(30);
        informationLabel.setMinHeight(30);
        informationLabel.setAlignment(Pos.CENTER);

        informationLabel.setOnMouseClicked(e -> loadEmployeeInformationWindow(employee, systemDirector, informationLabel));
        return informationLabel;
    }

    private void loadEmployeeInformationWindow(Employee employee, SystemDirector systemDirector, Label informationLabel) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EmployeeInformation.fxml"));
            AnchorPane employeeInformationAnchorPane = loader.load();
            EmployeeInformationWindowController employeeInformationWindowController = loader.getController();
            WindowControllerUtils.showPopOver(employeeInformationAnchorPane, employeeInformationWindowController, informationLabel, employee, systemDirector);
        } catch (IOException ex) {
            throw new IllegalStateException("Could not load EmployeeInformation.fxml" , ex);
        }
    }

    private void setNodeStyle(Node node) {
        node.setStyle("-fx-font-size: 14px; -fx-font-weight: normal; -fx-text-fill: black; -fx-opacity: 1");
    }
}
