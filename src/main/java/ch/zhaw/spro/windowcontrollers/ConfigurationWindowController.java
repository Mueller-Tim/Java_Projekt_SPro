package ch.zhaw.spro.windowcontrollers;

import ch.zhaw.spro.models.Employee;
import ch.zhaw.spro.SetUpUI;
import ch.zhaw.spro.models.SystemDirector;
import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.List;

/**
 * This class is the windowControllers for the configuration window.
 * Here can Employees be added, deleted, filtered and searched.
 * The shift types are also shown here.
 * The user can also generate the shift table from here.
 */
public class ConfigurationWindowController {
    @FXML
    public AnchorPane configurationAnchorPane;
    @FXML
    public Button buttonAddEmployeeId;
    @FXML
    public TextField fieldSearchEmployee;
    @FXML
    public Button buttonFilterEmployee;
    @FXML
    public Button buttonDeleteEmployeeId;
    @FXML
    public Button buttonInformationId;
    @FXML
    public Button buttonSearchEmployee;
    @FXML
    public Button buttonCalendarId;
    @FXML
    public Button buttonSettingId;
    @FXML
    public Button buttonAddShiftTypeId;
    @FXML
    public ScrollPane scrollPaneShiftType;
    @FXML
    private JFXListView<HBox> listAvailableEmployee;

    @FXML
    private Label lblError;

    private SetUpUI setUpUI;
    private SystemDirector systemDirector;

    @FXML
    public void buttonAddEmployee() {
        setUpUI.showAddEmployeePopOver(buttonAddEmployeeId);
    }
    @FXML
    public void buttonDeleteEmployee() {
        setUpUI.showDeleteEmployeePopOver(buttonDeleteEmployeeId);
    }

    @FXML
    public void buttonInformation() {
        setUpUI.showInformationWindow(buttonInformationId);
    }

    @FXML
    public void buttonSetting() {
        setUpUI.showSettingWindow(buttonSettingId);
    }

    @FXML
    public void buttonCalendar() {
        setUpUI.showCalendarWindow(buttonCalendarId);
    }
    @FXML
    public void buttonAddShiftType() {
        setUpUI.showAddShiftTypeWindow(buttonAddShiftTypeId);
    }

    @FXML
    public void buttonFilterEmployeeOnAction() {
        setUpUI.showEmployeesFilter(buttonFilterEmployee);
        systemDirector.addListener(() -> {
            listAvailableEmployee.getItems().clear();
            showSearchResult(systemDirector.getSearchResult());
        });
    }

    @FXML
    public void buttonSearchEmployeeOnAction() {
        List<Employee> searchResultList = systemDirector.searchEmployeeByName(fieldSearchEmployee.getText());
        showSearchResult(searchResultList);
    }
    private void showSearchResult(List<Employee> searchResultList) {
        listAvailableEmployee.getItems().clear();
        if (searchResultList.isEmpty()) {
            listAvailableEmployee.getItems().add(new HBox(new Label("No results found")));
        } else {
            for (Employee employee : searchResultList) {
                initializeEmployeeHBox(employee);
            }
        }
    }

   @FXML
   public void buttonGenerate() {
       setUpUI.showLoadingScreen();
       Task<Void> task = new Task<>() {
           @Override
           protected Void call()  {
               setUpUI.generateShiftPlan();
               return null;
           }
       };

       task.setOnSucceeded(e -> setUpUI.changeScene("shiftTable"));

       task.setOnFailed(e -> {
           showErrorMessage("Error: " + task.getException().getMessage());
           setUpUI.hideLoadingScreen();
       });

       new Thread(task).start();
   }

    @FXML
    public void buttonExit() {
        setUpUI.closeBackend();
        Platform.exit();
    }

    /**
     * Sets the SetUpUI and SystemDirector object
     * Adds a listener to the SystemDirector object
     * Prints all available employees to the view list
     * Prints all shift types to the view list
     * @param setUpUI The SetUpUI object from the Main class
     * @param systemDirector The SystemDirector object from the SetUpUI class
     */
    public void setSetupUIAndSystemDirector(SetUpUI setUpUI, SystemDirector systemDirector) {
        this.setUpUI = setUpUI;
        this.systemDirector = systemDirector;
        systemDirector.addListener(() -> {
            listAvailableEmployee.getItems().clear();
            printAvailableEmployeeToViewList();
            printShiftTypesToViewList();
        });
        printAvailableEmployeeToViewList();
        printShiftTypesToViewList();
    }
    private void showErrorMessage(String message) {
        lblError.setText(message);
        lblError.setVisible(true);
        lblError.setStyle("-fx-text-fill: red;");
    }
    private void initializeEmployeeHBox(Employee employee) {
        EmployeeHBox employeeHBox = new EmployeeHBox();
        HBox hbox = employeeHBox.createEmployeeHBox(employee, systemDirector);
        listAvailableEmployee.getItems().add(hbox);
    }
    private void initializeShiftTypeVBox(List<LocalDate> availableDateList) {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        int numRows = 5;
        int numCols = 5;

        for (int i = 0; i < availableDateList.size(); i++) {
            LocalDate date = availableDateList.get(i);

            ShiftTypeVBox shiftTypeVBox = new ShiftTypeVBox();
            VBox shiftVBox = shiftTypeVBox.createShiftTypeVBox(date, systemDirector);

            int col = i % numCols;
            int row = i / numCols % numRows;

            gridPane.add(shiftVBox, col, row);
        }
        scrollPaneShiftType.setFitToWidth(true);
        scrollPaneShiftType.setContent(gridPane);
    }
    private void printShiftTypesToViewList() {
        List<LocalDate> dateListOfShifts = systemDirector.getDatesOfNextMonth();
        initializeShiftTypeVBox(dateListOfShifts);
    }
    private void printAvailableEmployeeToViewList() {
        List<Employee> employeesList = systemDirector.getAllEmployee();
        for (Employee employee : employeesList) {
            initializeEmployeeHBox(employee);
        }
    }
}
