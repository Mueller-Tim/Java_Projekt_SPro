package ch.zhaw.spro.windowcontrollers;

import ch.zhaw.spro.models.Absence;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuButton;
import javafx.scene.control.SeparatorMenuItem;
import lombok.Getter;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for the absences' menu.
 * It is used to show the absences' menu and to add absences to the list of selected absences.
 */
@Getter
public class ChooseAbsencesMenu {

    private final List<String> selectedAbsenceDates = new ArrayList<>();

    /**
     * Adds an absence to the list of selected absences.
     * @param absence The absence to add.
     */
    public void addAbsence(Absence absence) {
        selectedAbsenceDates.add(String.valueOf(absence.getDate()));
    }

    /**
     * Shows the absences menu with all the days of the next month.
     */
    public void showAbsencesMenu(MenuButton btnChooseAbsences) {
        if (btnChooseAbsences.getItems().isEmpty()) {
            LocalDate currentDate = LocalDate.now();
            Month nextMonth = currentDate.getMonth().plus(1);
            int nextMonthLength = nextMonth.maxLength();
            int currentYear = nextMonth.getValue() == 1 ? currentDate.getYear() + 1 : currentDate.getYear();
            LocalDate firstDayOfTheMonth = LocalDate.of(currentYear, nextMonth, 1);
            for (int i = 0; i < nextMonthLength; i++) {
                LocalDate date = firstDayOfTheMonth.plusDays(i);
                if (date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY) {
                    addCheckMenuItem(date, btnChooseAbsences);
                }
            }
            checkIfDateIsSelected(btnChooseAbsences);
        }
    }
    private void checkIfDateIsSelected(MenuButton btnChooseAbsences){
        for (int i = 0; i < btnChooseAbsences.getItems().size(); i++) {
            for (String date : selectedAbsenceDates) {
                if(!btnChooseAbsences.getItems().isEmpty() && (btnChooseAbsences.getItems().get(i).getText() != null && (btnChooseAbsences.getItems().get(i).getText().split(" ")[1].equals(date)))){
                    ((CheckMenuItem) btnChooseAbsences.getItems().get(i)).setSelected(true);
                }
            }
        }
    }
    private void addCheckMenuItem(LocalDate date, MenuButton btnChooseAbsences){
        String day = DayOfWeek.from(date).toString();
        CheckMenuItem item = new CheckMenuItem(day + " " + date);
        item.setOnAction(event -> handleWorkdaySelection(item));
        btnChooseAbsences.getItems().add(item);
        SeparatorMenuItem separator = new SeparatorMenuItem();
        btnChooseAbsences.getItems().add(separator);
    }

    private void handleWorkdaySelection(CheckMenuItem item) {
        if (item.isSelected()) {
            selectedAbsenceDates.add(item.getText().split(" ")[1]);
        }
    }
}
