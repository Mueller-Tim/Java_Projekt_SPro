package ch.zhaw.spro;

import ch.zhaw.spro.models.Shift;
import ch.zhaw.spro.models.SystemDirector;
import ch.zhaw.spro.windowcontrollers.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.controlsfx.control.PopOver;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Objects;

/**
 * The SetUpUI class is responsible for setting up the UI.
 * It loads the different windows and shows them.
 */
public class SetUpUI extends Application {
    private static final String START_WINDOW_FXML = "/fxml/StartWindow.fxml";
    private static final String CONFIGURATION_WINDOW_FXML = "/fxml/ConfigurationWindow.fxml";
    private static final String SHIFT_TABLE_WINDOW_FXML = "/fxml/ShiftTableWindow.fxml";
    private static final String ADD_EMPLOYEE_WINDOW_FXML = "/fxml/AddEmployee.fxml";
    private static final String DELETE_EMPLOYEE_WINDOW_FXML = "/fxml/DeleteEmployee.fxml";
    private static final String FILTER_EMPLOYEES_WINDOW_FXML = "/fxml/FilterEmployees.fxml";
    private static final String LOGO = "/images/s_pro_icon.png";
    private static final String CALENDAR_WINDOW_FXML = "/fxml/CalendarWindow.fxml";
    private static final String SETTING_WINDOW_FXML = "/fxml/SettingWindow.fxml";
    private static final String PDF_EXPORT_WINDOW = "/fxml/PDFExportWindow.fxml";
    private static final String INFORMATION_WINDOW_FXML = "/fxml/InformationWindow.fxml";
    private static final String ADD_SHIFT_TYPE_WINDOW_FXML = "/fxml/AddShiftTypeWindow.fxml";

    private Stage primaryStage;
    private StackPane stackPane;
    private AnchorPane configurationPane;
    private AnchorPane startAnchorPane;
    private AnchorPane shiftTablePane;
    private FXMLLoader loader;
    private PopOver popOver;
    private SystemDirector systemDirector;
    private List<Shift> shiftList;
    private String pdfExportInformation;

    /**
     * The main method of the application.
     * @param args The arguments passed to the application.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the application.
     * @param primaryStage The primary stage of the application.
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            if (!isInternetAvailable()) {
                showAlert("No internet connection available.");
                return;
            }
            systemDirector = new SystemDirector();
            this.primaryStage = primaryStage;
            stackPane = new StackPane();
            loadMainWindow();
            showStartWindow();
        } catch (Exception e) {
            showAlert("An error has occurred: " + e.getMessage());
        }
    }

    /**
     * Changes the scene to the given scene name.
     * @param sceneName The name of the scene to change to.
     */
    public void changeScene(String sceneName) {
        switch (sceneName) {
            case "start":
                showStartWindow();
                break;
            case "configuration":
                loadConfigurationWindow();
                showConfigurationWindow();
                break;
            case "shiftTable":
                loadShiftTableWindow();
                showShiftTableWindow();
                break;
            default:
                throw new IllegalArgumentException("Unknown scene: " + sceneName);
        }
    }
    /**
     * Generates the shift plan.
     */
    public void generateShiftPlan() {
        try {
            shiftList = systemDirector.generateShiftPlan();
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    /**
     * Exports the shift plan as PDF.
     */
    public void exportPDF() {
        try {
           List<String> exportPaths = systemDirector.exportPDF();
           StringBuilder allPaths = new StringBuilder("The following PDFs are exported: \n");
              for(String path : exportPaths){
                allPaths.append(path).append("\n");
              }
            pdfExportInformation = allPaths.toString();
        } catch (IllegalArgumentException e) {
            pdfExportInformation = e.getMessage();
        }
    }
    public String getPDFExportError() {
        return pdfExportInformation;
    }
    /**
     * Shows the loading screen.
     */
    public void showLoadingScreen(){
        LoadingScreen loadingScreen = new LoadingScreen();
        stackPane.getChildren().add(loadingScreen.getScene());
    }
    /**
     * Hides the loading screen.
     */
    public void hideLoadingScreen(){
        stackPane.getChildren().remove(1);
    }
    /**
     * Shows the information window.
     * @param informationButton The informationButton to show the pop over next to.
     */
    public void showInformationWindow(Button informationButton) {
        showPopOver(informationButton, INFORMATION_WINDOW_FXML, new InformationWindowController());
    }
    /**
     * Shows the calendar window.
     * @param calendarButton The calendarButton to show the pop over next to.
     */
    public void showCalendarWindow(Button calendarButton) {
        showPopOver(calendarButton, CALENDAR_WINDOW_FXML, new CalendarWindowController());
    }
    /**
     * Shows the setting window.
     * @param settingButton The settingButton to show the pop over next to.
     */
    public void showSettingWindow(Button settingButton) {
        showPopOver(settingButton, SETTING_WINDOW_FXML, new SettingWindowController());
    }
    /**
     * Shows the PDF export window.
     * @param pdfExportButton The pdfExportButton to show the pop over next to.
     */
    public void showPDFExportWindow(Button pdfExportButton) {
        showPopOver(pdfExportButton, PDF_EXPORT_WINDOW, new PDFExportController());
    }

    /**
     * Shows the add employee pop over.
     * @param addEmployeeButton The addEmployeeButton to show the pop over next to.
     */
    public void showAddEmployeePopOver(Button addEmployeeButton) {
        showPopOver(addEmployeeButton, ADD_EMPLOYEE_WINDOW_FXML, new AddEmployeeWindowController());
    }

    /**
     * Shows the Window to delete an employee.
     * @param deleteEmployeeButton The deleteEmployeeButton to show the pop over next to.
     */
    public void showDeleteEmployeePopOver(Button deleteEmployeeButton) {
        showPopOver(deleteEmployeeButton, DELETE_EMPLOYEE_WINDOW_FXML, new DeleteEmployeeWindowController());
    }

    /**
     * Shows the Window to filter employees.
     * @param buttonAddShiftType The button to show the pop over next to.
     */
    public void showAddShiftTypeWindow(Button buttonAddShiftType) {
        showPopOver(buttonAddShiftType, ADD_SHIFT_TYPE_WINDOW_FXML, new AddShiftTypeWindowController());
    }

    /**
     * Shows the Window to filter employees.
     * @param buttonFilterEmployee The button to show the pop over next to.
     */
    public void showEmployeesFilter(Button buttonFilterEmployee) {
        showPopOver(buttonFilterEmployee, FILTER_EMPLOYEES_WINDOW_FXML, new FilterEmployeesController());
    }
    /**
     * Closes the backend.
     */
    public void closeBackend() {
        SystemDirector.runningSpringBootApplication.close();
    }
    private void showPopOver(Button button, String fxmlPath, Object controllerType) {
        if (popOver != null) {
            popOver.hide();
            popOver = null;
        } else {
            try {
                loader = new FXMLLoader(getClass().getResource(fxmlPath));
                AnchorPane pane = loader.load();
                Bounds boundsInScreen = button.localToScreen(button.getBoundsInLocal());
                double popoverX = boundsInScreen.getMaxX();
                double popoverY = boundsInScreen.getMinY();
                popOver = new PopOver(pane);
                initializePopOver(popOver);
                if (controllerType instanceof InformationWindowController) {
                    InformationWindowController controller = loader.getController();
                    controller.setSetupUIAndSystemDirector(this);
                    popoverY = popoverY - 10;
                    popoverX = popoverX + 10;
                } else if (controllerType instanceof CalendarWindowController) {
                    CalendarWindowController controller = loader.getController();
                    controller.setSetUpUI(this);
                    popoverY = popoverY - 10;
                    popoverX = popoverX + 10;
                } else if (controllerType instanceof DeleteEmployeeWindowController) {
                    DeleteEmployeeWindowController controller = loader.getController();
                    controller.setSetupUIAndSystemDirector(this, systemDirector);
                    popoverY = popoverY - 10;
                    popoverX = popoverX + 10;
                } else if (controllerType instanceof FilterEmployeesController) {
                    FilterEmployeesController controller = loader.getController();
                    controller.setSetupUIAndSystemDirector(this, systemDirector);
                    popoverX = popoverX + 10;
                    popoverY = popoverY - 30;
                } else if (controllerType instanceof AddEmployeeWindowController) {
                    AddEmployeeWindowController controller = loader.getController();
                    controller.setSetupUIAndSystemDirector(this, systemDirector);
                    popoverX = popoverX + 10;
                    popOver.setArrowSize(0);
                } else if (controllerType instanceof SettingWindowController) {
                    SettingWindowController controller = loader.getController();
                    controller.setSetUpUI(this);
                    popoverX = popoverX + 10;
                    popoverY = popoverY - 315;
                    popOver.setArrowLocation(PopOver.ArrowLocation.LEFT_BOTTOM);
                } else if(controllerType instanceof PDFExportController){
                    PDFExportController controller = loader.getController();
                    controller.setSetUpUI(this);
                    popoverX = popoverX + 10;
                    popoverY = popoverY - 5;
                } else if(controllerType instanceof AddShiftTypeWindowController){
                    AddShiftTypeWindowController controller = loader.getController();
                    controller.setSetUpUI(this);
                    popoverX = popoverX + 10;
                    popoverY = popoverY - 5;
                }
                Window window = button.getScene().getWindow();
                popOver.show(window, popoverX, popoverY);
            } catch (Exception e) {
                throw new IllegalStateException("Could not load window.", e);
            }
        }
    }
    /**
     * Requests the focus of the primary stage.
     */
    public void requestFocusOfPrimaryStage() {
        primaryStage.requestFocus();
    }
    private void initializePopOver(PopOver popOver) {
        popOver.setDetachable(false);
        popOver.setHideOnEscape(true);
        popOver.setCornerRadius(20);
        popOver.setArrowSize(10);
        popOver.setArrowLocation(PopOver.ArrowLocation.LEFT_TOP);
    }
    private void loadMainWindow() {
        loadMainStackPane();
        loadStartWindow();

    }

    private boolean isInternetAvailable() {
        try {
            final URL url = new URL("https://www.google.ch");
            final HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("HEAD");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (IOException e) {
            return false;
        }
    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Connection error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        String cssStyle = "-fx-font-family: 'Arial'; "
                + "-fx-font-size: 14px; "
                + "-fx-font-weight: bold; "
                + "-fx-text-fill: #2A5058;";

        alert.getDialogPane().setStyle(cssStyle);
        alert.showAndWait();
        closeBackend();
    }

    private void loadMainStackPane() {
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream(LOGO)));
        primaryStage.getIcons().add(icon);
        Scene scene = new Scene(stackPane);
        URL stylesheet = getClass().getResource("/css/style.css");
        assert stylesheet != null;
        scene.getStylesheets().add(stylesheet.toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1200);
        primaryStage.setMinHeight(700);
        primaryStage.setWidth(1200);
        primaryStage.setHeight(700);
        primaryStage.setTitle("S-Pro");
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> closeBackend());
    }

    private void loadStartWindow() {
        try {
            loader = new FXMLLoader(getClass().getResource(START_WINDOW_FXML));
            startAnchorPane = loader.load();
            StartWindowController startWindowController = loader.getController();
            startWindowController.setSetupUI(this);
        } catch (IOException e) {
            throw new IllegalStateException("Could not load start window.", e);
        }
    }

    private void loadConfigurationWindow() {
        try {
            loader = new FXMLLoader(getClass().getResource(CONFIGURATION_WINDOW_FXML));
            configurationPane = loader.load();
            ConfigurationWindowController configurationWindowController = loader.getController();
            configurationWindowController.setSetupUIAndSystemDirector(this, systemDirector);
        } catch (IOException e) {
            throw new IllegalStateException("Could not load configuration window.", e);
        }

    }

    private void loadShiftTableWindow() {
        try {
            loader = new FXMLLoader(getClass().getResource(SHIFT_TABLE_WINDOW_FXML));
            shiftTablePane = loader.load();
            ShiftTableWindowController shiftTableWindowController = loader.getController();
            shiftTableWindowController.setSetupUIAndSystemDirector(this, systemDirector);
            shiftTableWindowController.setGeneratedShiftList(shiftList);
        } catch (IOException e) {
            throw new IllegalStateException("Could not load shift table window.", e);
        }
    }

    private void showStartWindow() {
        stackPane.getChildren().clear();
        stackPane.getChildren().add(startAnchorPane);
    }

    private void showConfigurationWindow() {
        stackPane.getChildren().clear();
        stackPane.getChildren().add(configurationPane);
    }

    private void showShiftTableWindow() {
        stackPane.getChildren().clear();
        stackPane.getChildren().add(shiftTablePane);
    }

}
