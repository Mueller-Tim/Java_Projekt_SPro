package ch.zhaw.spro.windowcontrollers;

import ch.zhaw.spro.models.*;
import ch.zhaw.spro.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Window;
import org.controlsfx.control.PopOver;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller class for ShiftTableWindow.fxml
 * This class is responsible for showing the ShiftTable.
 * The ShiftTable shows the shifts of all employees for the next month
 * The user can export the ShiftTable as a PDF
 */
public class ShiftTableWindowController {

    private SetUpUI setUpUI;

    @FXML
    private Button buttonLegend;

    @FXML
    public Button buttonSettingId;

    @FXML
    public Button buttonExportPDFId;

    @FXML
    public Button pageBackId;

    @FXML
    private GridPane tableViewShift;
    private static final String LEGEND_WINDOW_FXML = "/fxml/Legend.fxml";
    private static final int CELL_WIDTH = 140;
    private static final String MORNING_SHIFT = "Morning Shift";
    private static final String LUNCH_SHIFT = "Lunch Shift";
    private static final String FULL_DAY = "Full Day";
    private static final String MORNING_SHIFT_TIME = "08:00 - 12:00";
    private static final String LUNCH_SHIFT_TIME = "13:00 - 16:00";
    private static final String ABSENCE_COLOR = "#999999";
    private SystemDirector systemDirector;
    private PopOver popOver;
    private Window window;
    private GridPaneTable gridPaneTable;
    private Month nextMonth;
    private int nextMonthLength;
    private LocalDate firstDayOfTheNextMonth;
    private int employeeColumnIndex;
    private int employeeRowIndex;
    private int dateColumnIndex;
    private int dateRowIndex;
    private List<Shift> generatedShiftList;
    private List<ShiftType> shiftTypeList;
    private Map<String, Integer> employeeRowMap;
    private Map<String, Integer> dateColumnMap;


    @FXML
    public void buttonExportPDF() {
        setUpUI.exportPDF();
        setUpUI.showPDFExportWindow(buttonExportPDFId);

    }

    @FXML
    public void buttonLegend() {
        boolean isShowing = popOver.isShowing();
        if (isShowing) {
            popOver.hide();
        } else {
            popOver.show(window);
        }
    }
    @FXML
    public void buttonExit() {
        setUpUI.closeBackend();
        Platform.exit();
    }

    @FXML
    public void buttonSetting() {
        setUpUI.showSettingWindow(buttonSettingId);
    }

    @FXML
    public void pageBack() {
        try {
            setUpUI.changeScene("configuration");
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Could not load Configuration.fxml" , e);
        }
    }

    /**
     * Sets the SystemDirector and initializes the ShiftTable
     * @param systemDirector The SystemDirector object from the SetUpUI class
     */
    public void setSetupUIAndSystemDirector(SetUpUI setUpUI, SystemDirector systemDirector) {
        this.systemDirector =  systemDirector;
        this.setUpUI = setUpUI;
        showLegendPopOver();
    }
    public void setGeneratedShiftList(List<Shift> generatedShiftList) {
        this.generatedShiftList = generatedShiftList;
        configureShiftTable();
    }

    private void configureShiftTable() {
        initializeBasicComponents();
        createColumnsForDates();
        populateShifts();
        addRowConstraints();
    }

    private void initializeBasicComponents() {
        gridPaneTable = new GridPaneTable(tableViewShift);
        final String employeeLabelString = "Employee";
        final String shiftTimeLabelString = "Shift Time";
        gridPaneTable.getGridPane().setStyle("-fx-border-color: black; -fx-border-width: 1.5; -fx-border-radius: 5; -fx-background-radius: 5; -fx-background-color: #fcfafa; -fx-padding: 10;");


        Label employeeLabel = new Label(employeeLabelString);
        employeeLabel.setAlignment(Pos.CENTER);
        employeeLabel.setPadding(new Insets(0, 0, 0, 10));
        Label shiftTimeLabel = new Label(shiftTimeLabelString);
        shiftTimeLabel.setAlignment(Pos.CENTER);
        shiftTimeLabel.setPadding(new Insets(0, 0, 0, 10));
        gridPaneTable.getGridPane().add(employeeLabel, 0, 0);
        gridPaneTable.getGridPane().add(shiftTimeLabel, 1, 0);

        LocalDate currentDate = LocalDate.now();
        nextMonth = currentDate.getMonth().plus(1);
        nextMonthLength = nextMonth.maxLength();
        int currentYear = determineCurrentYear(currentDate, nextMonth);
        firstDayOfTheNextMonth = LocalDate.of(currentYear, nextMonth, 1);

        employeeColumnIndex = 0;
        employeeRowIndex = 1;
        dateColumnIndex = 2;
        dateRowIndex = 0;


        shiftTypeList = systemDirector.getShiftType();
        employeeRowMap = new HashMap<>();
        dateColumnMap = new HashMap<>();
    }

    private void createColumnsForDates() {
        for (int i = 0; i < nextMonthLength; i++) {
            LocalDate date = firstDayOfTheNextMonth.plusDays(i);
            if (isWeekday(date)) {
                addDateColumn(date);
            }
        }
    }

    private boolean isWeekday(LocalDate date) {
        return date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY;
    }

    private void addDateColumn(LocalDate date) {
        gridPaneTable.addColumn();
        ColumnConstraints columnConstraints = createColumnConstraints();
        gridPaneTable.getGridPane().getColumnConstraints().add(columnConstraints);

        Label dateLabel = createDateLabel(date);

        gridPaneTable.getGridPane().add(dateLabel, dateColumnIndex, dateRowIndex);
        dateColumnMap.put(date.toString(), dateColumnIndex);
        dateColumnIndex++;
    }

    private ColumnConstraints createColumnConstraints() {
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setMinWidth(CELL_WIDTH);
        return columnConstraints;
    }

    private Label createDateLabel(LocalDate date) {
        final String datePattern = "dd.MM.yyyy";
        String dateAsString = date.format(DateTimeFormatter.ofPattern(datePattern));
        Label dateLabel = new Label(dateAsString + "\n" + date.getDayOfWeek());
        dateLabel.setMinWidth(CELL_WIDTH);
        dateLabel.setAlignment(Pos.CENTER);
        return dateLabel;
    }

    private int determineCurrentYear(LocalDate currentDate, Month nextMonth) {
        return nextMonth.getValue() == 1 ? currentDate.getYear() + 1 : currentDate.getYear();
    }

    private void populateShifts() {
        for (Shift shift : generatedShiftList) {
            if (!employeeRowMap.containsKey(shift.getEmployeeId())) {
                addEmployeeRow(shift);
            }
            if (employeeRowMap.containsKey(shift.getEmployeeId())) {
                addAbsenceToGrid();
                addShiftToGrid(shift);
            }
        }
    }

    private void addEmployeeRow(Shift shift) {
        Employee employee = systemDirector.getEmployee(shift.getEmployeeId());
        HBox hbox = new EmployeeHBox().createEmployeeHBox(employee, systemDirector);
        hbox.getChildren().remove(2);
        gridPaneTable.addTwoRows();
        gridPaneTable.getGridPane().add(hbox, employeeColumnIndex, employeeRowIndex);
        gridPaneTable.rowSpan(hbox, 2);

        hbox.toFront();

        Label morningShiftLabel = new Label(MORNING_SHIFT_TIME);
        Label lunchShiftLabel = new Label(LUNCH_SHIFT_TIME);
        morningShiftLabel.setMinWidth(CELL_WIDTH);
        lunchShiftLabel.setMinWidth(CELL_WIDTH);
        morningShiftLabel.setAlignment(Pos.CENTER);
        lunchShiftLabel.setAlignment(Pos.CENTER);

        gridPaneTable.getGridPane().add(morningShiftLabel, employeeColumnIndex + 1, employeeRowIndex);
        gridPaneTable.getGridPane().add(lunchShiftLabel, employeeColumnIndex + 1, employeeRowIndex + 1);

        employeeRowMap.put(shift.getEmployeeId(), employeeRowIndex);
        employeeRowIndex += 2;
    }

    private void addShiftToGrid(Shift shift) {
        if (!isShiftInNextMonth(shift)) {
            return;
        }

        int row = employeeRowMap.get(shift.getEmployeeId());
        int column = dateColumnMap.get(shift.getDate().toString());

        Pane cellColorMorning = new Pane();
        Pane cellColorLunch = new Pane();

        for (ShiftType shiftType : shiftTypeList) {
            if (shift.getShiftTypeId().equals(shiftType.getId())) {
                addShiftToGridPane(shift, cellColorMorning, cellColorLunch, column, row);
                setCellColor(cellColorMorning, cellColorLunch, shiftType.getColor());
                break;
            }
        }
    }
    private void addAbsenceToGrid() {
        List<Absence> absenceDates = systemDirector.getAllAbsences();
        for (Absence absence : absenceDates) {
            if (absence.getDate().getMonth() == nextMonth) {
                int row ;
                if (employeeRowMap.get(absence.getEmployeeId()) != null) {
                    row = employeeRowMap.get(absence.getEmployeeId());
                    int column = dateColumnMap.get(absence.getDate().toString());
                    Pane absencePaneMorning = new Pane();
                    Pane absencePaneLunch = new Pane();
                    gridPaneTable.getGridPane().add(absencePaneMorning, column, row);
                    gridPaneTable.getGridPane().add(absencePaneLunch, column, row + 1);
                    setCellColor(absencePaneMorning, absencePaneLunch, ABSENCE_COLOR);
                }
            }
        }

    }

    private boolean isShiftInNextMonth(Shift shift) {
        LocalDate shiftDate = shift.getDate();
        return shiftDate.getMonth() == nextMonth && !shiftDate.getDayOfWeek().equals(DayOfWeek.SATURDAY) && !shiftDate.getDayOfWeek().equals(DayOfWeek.SUNDAY);
    }

    private void setCellColor(Pane cellColorMorning, Pane cellColorLunch, String color) {
        String cellColorStyle = "-fx-background-color: " + color + ";";
        cellColorMorning.setStyle(cellColorStyle);
        cellColorLunch.setStyle(cellColorStyle);
        cellColorMorning.toBack();
        cellColorLunch.toBack();
    }

    private void addShiftToGridPane(Shift shift, Pane cellColorMorning, Pane cellColorLunch, int column, int row) {
        switch (shift.getTime()) {
            case MORNING_SHIFT:
                gridPaneTable.getGridPane().add(cellColorMorning, column, row);
                break;
            case LUNCH_SHIFT:
                gridPaneTable.getGridPane().add(cellColorLunch, column, row + 1);
                break;
            case FULL_DAY:
                gridPaneTable.getGridPane().add(cellColorMorning, column, row);
                gridPaneTable.getGridPane().add(cellColorLunch, column, row + 1);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + shift.getTime());
        }
    }

    private void addRowConstraints() {
        int cellHeight = 37;
        for (int i = 0; i < gridPaneTable.getGridPane().getRowCount(); i++) {
            RowConstraints rowConstraints = new RowConstraints(cellHeight);
            gridPaneTable.getGridPane().getRowConstraints().add(rowConstraints);
        }

    }

    private void showLegendPopOver(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(LEGEND_WINDOW_FXML));
            AnchorPane legendAnchorPane = loader.load();
            LegendWindowController legendWindowController = loader.getController();
            popOver = new PopOver(legendAnchorPane);
            legendWindowController.setShiftTypeNamesAndColors(systemDirector.getShiftType());
            buttonLegend.setOnMouseEntered(event -> {
                window = buttonLegend.getScene().getWindow();
                configureLegendPopOver();
                popOver.show(window);
            });
            buttonLegend.setOnMouseExited(event -> popOver.hide());

        } catch (IOException e) {
            throw new IllegalStateException("Could not load Legend.fxml" , e);
        }
    }

    private void configureLegendPopOver() {
        Bounds boundsInScreen = buttonLegend.localToScreen(buttonLegend.getBoundsInLocal());
        double xOnScreen = boundsInScreen.getMinX();
        double yOnScreen = boundsInScreen.getMinY();
        final int popOverWidth = 100;
        final int popOverHeight = 10;

        popOver.setAnchorX(xOnScreen + popOverWidth);
        popOver.setAnchorY(yOnScreen - popOverHeight);
        popOver.setCornerRadius(20);
        popOver.setArrowLocation(PopOver.ArrowLocation.LEFT_TOP);
        popOver.setDetachable(false);
    }
}
