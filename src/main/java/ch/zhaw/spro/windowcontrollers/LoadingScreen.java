package ch.zhaw.spro.windowcontrollers;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Class responsible for creating and managing a loading screen.
 * This loading screen is displayed as a modal dialog with a spinning loading indicator.
 * It uses a Stage to show the loading animation in a separate window.
 */
public class LoadingScreen {

    private final Stage dialogStage;

    /**
     * Constructor for LoadingScreen.
     * Initializes a new loading screen with a spinning loading animation.
     * Sets up the stage with a transparent background and fixed size, and adds the loading GIF to it.
     */
    public LoadingScreen() {
        dialogStage = new Stage();
        ImageView imageView = new ImageView();
        Image image = new Image("images/loading_screen_animation.gif");
        imageView.setFitHeight(180);
        imageView.setFitWidth(180);
        imageView.setImage(image);

        dialogStage.initStyle(StageStyle.TRANSPARENT);
        dialogStage.setResizable(false);
        dialogStage.initModality(Modality.APPLICATION_MODAL);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(imageView);
        stackPane.setMaxSize(220, 220);
        stackPane.setOpacity(0.95);
        stackPane.setStyle("-fx-background-color: #2b2b2b; -fx-background-radius: 30px;");

        Scene scene = new Scene(stackPane, 50, 50);
        scene.setFill(Color.TRANSPARENT);
        dialogStage.setScene(scene);
    }

    /**
     * Gets the root node of the scene containing the loading screen.
     * This can be used for additional configuration or manipulation of the loading screen's scene.
     *
     * @return The root node of the loading screen's scene.
     */
    public Node getScene() {
        return dialogStage.getScene().getRoot();
    }
}
