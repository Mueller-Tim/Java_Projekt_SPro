package ch.zhaw.spro.windowcontrollers;

import ch.zhaw.spro.models.SystemDirector;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import org.controlsfx.control.PopOver;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * This class is responsible for creating the VBox for the Shift
 * Here can the user add a shift or see the information of the shift
 */
public class ShiftTypeVBox {

    /**
     * Creates a VBox with the date, the add shift button and the info button
     *
     * @param date           The selected date of the shift
     * @param systemDirector The SystemDirector object
     * @return The created VBox
     */
    VBox createShiftTypeVBox(LocalDate date, SystemDirector systemDirector) {
        VBox shiftTypeVBox = getShiftvBox();
        String formattedDate = getFormattedDate(date);
        Button addShiftButton = getAddShiftButton(date, systemDirector);
        HBox iconDayDateHBox = createDateIconHBox(formattedDate, systemDirector);
        shiftTypeVBox.getChildren().addAll(iconDayDateHBox, addShiftButton);
        return shiftTypeVBox;
    }

    private static String getFormattedDate(LocalDate date) {
        String pattern = "E dd.MM.yyyy";
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(pattern);
        return dateFormat.format(date);
    }

    private static VBox getShiftvBox() {
        VBox shiftTypeVBox = new VBox();
        shiftTypeVBox.setAlignment(Pos.CENTER);
        shiftTypeVBox.setSpacing(10);
        shiftTypeVBox.setStyle("-fx-background-color: #fcfafa; -fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: black; -fx-border-width: 1.5");
        shiftTypeVBox.setPadding(new Insets(10, 10, 10, 10));
        shiftTypeVBox.setPrefWidth(210);
        shiftTypeVBox.setPrefHeight(100);
        shiftTypeVBox.setMaxHeight(300);
        shiftTypeVBox.setMaxWidth(300);
        return shiftTypeVBox;
    }

    private static void configureAndShowPopover(Scene nodeScene, PopOver popOver) {
        Window window = nodeScene.getWindow();
        popOver.setArrowSize(0);
        popOver.setDetachable(false);
        popOver.setCornerRadius(20);
        popOver.show(window);
    }

    private Button getAddShiftButton(LocalDate date, SystemDirector systemDirector) {
        Button addShiftButton = new Button("Add Shift");
        addShiftButton.setMaxSize(100, 30);
        addShiftButton.setMinSize(100, 30);

        ImageView imageView = new ImageView(new Image("/images/plus_icon.png"));
        imageView.setFitWidth(15);
        imageView.setFitHeight(15);

        addShiftButton.setGraphic(imageView);
        addShiftButton.setContentDisplay(ContentDisplay.LEFT);
        addShiftButton.setGraphicTextGap(10);

        setNodeStyleButton(addShiftButton);

        addShiftButton.setOnMouseClicked(e -> {
            try {
                loadAddShiftWindow(date, systemDirector, addShiftButton);
            } catch (Exception ex) {
                throw new IllegalStateException("Could not load AddShift.fxml", ex);
            }
        });
        return addShiftButton;
    }

    private void loadAddShiftWindow(LocalDate date, SystemDirector systemDirector, Button addShiftButton) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddShift.fxml"));
        AnchorPane addShiftAnchorPane = loader.load();
        AddShiftWindowController addShiftWindowController = loader.getController();
        PopOver popOver = new PopOver(addShiftAnchorPane);
        addShiftWindowController.setCurrentShift(date);
        addShiftWindowController.setPopOver(popOver);
        addShiftWindowController.setSystemDirector(systemDirector);
        configureAndShowPopover(addShiftButton.getScene(), popOver);
    }

    private HBox createDateIconHBox(String date, SystemDirector systemDirector) {
        HBox dateIconHBox = new HBox();
        dateIconHBox.setAlignment(Pos.CENTER);
        dateIconHBox.setSpacing(5);

        ImageView calendarIcon = new ImageView(new Image("/images/calendar_icon.png"));
        calendarIcon.setFitWidth(40);
        calendarIcon.setFitHeight(40);

        Label dateLabel = new Label(date);
        dateLabel.setMaxSize(100, 25);
        dateLabel.setMinSize(100, 25);
        setNodeStyleText(dateLabel);

        Label infoLabel = getInfoLabel(date, systemDirector);


        dateIconHBox.getChildren().addAll(calendarIcon, dateLabel, infoLabel);
        return dateIconHBox;
    }

    private Label getInfoLabel(String date, SystemDirector systemDirector) {
        ImageView imageView = new ImageView(new Image("/images/info_button_icon.png"));
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);

        Label infoLabel = new Label("", imageView);
        infoLabel.setMinWidth(25);
        infoLabel.setMinHeight(25);
        infoLabel.setAlignment(Pos.CENTER);

        infoLabel.setOnMouseClicked(e ->
            loadShiftInformationWindow(date, systemDirector, infoLabel)
        );
        return infoLabel;
    }

    private void loadShiftInformationWindow(String date, SystemDirector systemDirector, Label infoLabel)  {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ShiftInformation.fxml"));
            AnchorPane shiftInformationAnchorPane = loader.load();
            ShiftInfoWindowController shiftInfoWindowController = loader.getController();
            PopOver popOver = new PopOver(shiftInformationAnchorPane);
            shiftInfoWindowController.setCurrentShiftDate(date);
            shiftInfoWindowController.setSystemDirector(systemDirector);
            shiftInfoWindowController.setPopOver(popOver);
            configureAndShowPopover(infoLabel.getScene(), popOver);
        } catch (Exception ex) {
            throw new IllegalStateException("Could not load ShiftInformation.fxml", ex);
        }
    }

    private void setNodeStyleText(Node node) {
        node.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: black; -fx-opacity: 1");
    }

    private void setNodeStyleButton(Node node) {
        node.setStyle("-fx-font-size: 12px; -fx-font-weight: normal; -fx-text-fill: black; -fx-opacity: 1");
    }
}
