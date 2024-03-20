package ch.zhaw.spro.windowcontrollers;

import ch.zhaw.spro.models.Shift;
import ch.zhaw.spro.models.SystemDirector;
import javafx.scene.image.Image;
import org.controlsfx.control.PopOver;
import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controller for the ShiftInfo PopOver
 */
public class ShiftInfoWindowController {

    @FXML
    private ImageView calenderImageView;

    @FXML
    private Label dateLabel;

    @FXML
    private JFXListView<String> shiftsListView;
    private PopOver popOver;
    private String currentShiftDate;
    private List<Shift> createdShiftList;
    private SystemDirector systemDirector;

    @FXML
    public void btnClose() {
        popOver.hide();
    }

    @FXML
    void btnDeleteShift() {
        String selectedShift = shiftsListView.getSelectionModel().getSelectedItem();
        Shift shiftToDelete = null;
        for (Shift shift : createdShiftList) {
            String formattedDate = getFormattedDate(shift);
            if (formattedDate.equals(currentShiftDate) && (selectedShift.equals(shift.getShiftType().getName() + "  -  " + shift.getTime()))){
                    shiftToDelete = shift;
            }
        }
        deleteShift(shiftToDelete);
    }

    /**
     * Sets the current selected date
     * @param date The selected date
     */
    public void setCurrentShiftDate(String date) {
        calenderImageView.setImage(new Image("/images/calendar_icon.png"));
        dateLabel.setText(date);
        this.currentShiftDate = date;
    }

    /**
     * Sets the PopOver
     * @param popOver The PopOver object of the ShiftInfo PopOver
     */
    public void setPopOver(PopOver popOver) {
        this.popOver = popOver;
    }

    /**
     * Sets the SystemDirector and shows the shifts for the selected date
     * @param systemDirector The SystemDirector object from the SetUpUI class
     */
    public void setSystemDirector(SystemDirector systemDirector) {
        this.createdShiftList = systemDirector.getAllShifts();
        this.systemDirector = systemDirector;
        showShifts();
    }

    private void showShifts() {
        shiftsListView.getItems().clear();
        if (createdShiftList != null) {

            for (Shift shift : createdShiftList) {
                String formattedDate = getFormattedDate(shift);
                if (formattedDate.equals(currentShiftDate)){
                    shiftsListView.getItems().add(shift.getShiftType().getName() + "  -  " + shift.getTime());
                }
            }
        }
    }

    private String getFormattedDate(Shift shift){
        String pattern = "E dd.MM.yyyy";
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(pattern);
        return dateFormat.format(shift.getDate());
    }

    private void deleteShift(Shift shiftToDelete) {
        if (shiftToDelete != null){
            createdShiftList.remove(shiftToDelete);
            systemDirector.deleteShift(shiftToDelete);
            showShifts();
        }
    }

}
