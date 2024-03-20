package ch.zhaw.spro.windowcontrollers;

import ch.zhaw.spro.models.ShiftType;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * Controller for the Legend.fxml file.
 * Responsible for displaying the legend of shift types in the application,
 * showing each shift type along with its associated color.
 */
public class LegendWindowController {

    @FXML
    private VBox shiftTypesVbox;

    public void setShiftTypeNamesAndColors(List<ShiftType> shiftTypes) {
        for (ShiftType shiftType : shiftTypes) {
            createShiftTypeHBox(shiftType.getName(), shiftType.getColor());
        }
        createShiftTypeHBox("Absent", "#999999");
    }
    private void createShiftTypeHBox(String name, String color) {
        Label shiftTypeName = new Label(name);
        Pane shiftTypeColor = new Pane();
        setPaneConfiguration(color, shiftTypeColor);
        HBox shiftTypeHBox = new HBox(shiftTypeColor, shiftTypeName);
        setHBoxConfiguration(shiftTypeHBox);
        shiftTypesVbox.getChildren().add(shiftTypeHBox);
    }

    private static void setHBoxConfiguration(HBox shiftTypeHBox) {
        shiftTypeHBox.setSpacing(10);
        shiftTypeHBox.setPadding(new Insets(5,5,5,5));
        shiftTypeHBox.setAlignment(Pos.CENTER_LEFT);
    }

    private static void setPaneConfiguration(String color, Pane shiftTypeColor) {
        shiftTypeColor.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 10; -fx-border-radius: 10;");
        shiftTypeColor.setPrefSize(30, 30);
        shiftTypeColor.setMinSize(30, 30);
        shiftTypeColor.setMaxSize(30, 30);
    }


}
