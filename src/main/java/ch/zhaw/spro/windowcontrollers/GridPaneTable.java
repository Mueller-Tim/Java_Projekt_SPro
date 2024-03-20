package ch.zhaw.spro.windowcontrollers;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import lombok.Getter;

/**
 * Extends the GridPane class to create a custom GridPaneTable.
 * This class is designed for displaying a generated shift plan in a grid format.
 * It provides methods to manipulate rows and columns of the grid, as well as setting spans for elements.
 */
@Getter
public class GridPaneTable extends GridPane {

    private final GridPane gridPane;

    /**
     * Constructor for GridPaneTable.
     * Initializes a new GridPaneTable with a given GridPane instance.
     *
     * @param gridPane The GridPane instance to be used for table creation.
     */
    public GridPaneTable(GridPane gridPane) {
        this.gridPane = gridPane;
    }

    /**
     * Adds two rows to the grid pane.
     * Specifically, it adds rows at positions 0 and 1.
     */
    public void addTwoRows() {
        gridPane.addRow(0);
        gridPane.addRow(1);
    }

    /**
     * Adds a column to the grid pane at position 0.
     */
    public void addColumn() {
        gridPane.addColumn(0);
    }

    /**
     * Sets the row span for a given HBox in the grid pane.
     * This determines how many rows the HBox will span in the grid layout.
     *
     * @param hbox The HBox for which to set the row span.
     * @param rowSpan The number of rows the HBox should span.
     */
    public void rowSpan(HBox hbox, int rowSpan) {
        GridPane.setRowSpan(hbox, rowSpan);
    }
}
