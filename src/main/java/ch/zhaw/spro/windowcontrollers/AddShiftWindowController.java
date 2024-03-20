package ch.zhaw.spro.windowcontrollers;

import ch.zhaw.spro.models.ShiftType;
import ch.zhaw.spro.models.SystemDirector;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import lombok.Setter;
import org.controlsfx.control.PopOver;
import javafx.fxml.FXML;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Controller for the AddShift PopOver
 */
public class AddShiftWindowController {

    @FXML
    private Button btnSave;
    @FXML
    private ToggleGroup shiftTime;
    @FXML
    private ComboBox<String > shiftTypeComboBox;
    @FXML
    private Label lblDate;
    @Setter
    private PopOver popOver;
    private LocalDate currentShiftDate;


    @FXML
    public void btnClose() {
        popOver.hide();
    }

    public void setCurrentShift(LocalDate date) {currentShiftDate = date;}

    /**
     * Sets the SystemDirector and adds the ShiftTypes to the ComboBox
     * @param systemDirector The SystemDirector object from the SetUpUI class
     */
    public void setSystemDirector(SystemDirector systemDirector) {
        if (systemDirector.getShiftType() != null){
            for (ShiftType shiftTypeName : systemDirector.getShiftType()) {
                shiftTypeComboBox.getItems().add(shiftTypeName.getName());
            }
        }
        String dateString = currentShiftDate.format(DateTimeFormatter.ofPattern("E dd.MM.yyyy"));
        lblDate.setText(dateString);
        btnSave.setOnAction(e -> {
            if (shiftTime.getSelectedToggle() == null || shiftTypeComboBox.getValue() == null){
                return;
            }
            String shiftTypeId = "";
            for (ShiftType shiftType : systemDirector.getShiftType()) {
                if (shiftType.getName().equals(shiftTypeComboBox.getValue())){
                    shiftTypeId = shiftType.getId();
                    break;
                }
            }
            if (((ToggleButton)shiftTime.getSelectedToggle()).getText().equals("Full Day")){
                systemDirector.addShift(currentShiftDate, "Morning Shift", shiftTypeId);
                systemDirector.addShift(currentShiftDate, "Lunch Shift", shiftTypeId);
            }
            else {
                systemDirector.addShift(currentShiftDate, ((ToggleButton)shiftTime.getSelectedToggle()).getText(), shiftTypeId);
            }

            popOver.hide();
        });
    }
}
