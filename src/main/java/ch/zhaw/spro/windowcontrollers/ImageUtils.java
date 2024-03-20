package ch.zhaw.spro.windowcontrollers;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;

/**
 * Utility class for image-related operations in the application.
 * Provides methods to handle and display images, specifically for ImageViews.
 */
public class ImageUtils {

    /**
     * Displays an image in a given ImageView.
     * If the provided image data is null or empty, a default avatar image is shown.
     * Otherwise, the image data is converted to an Image and displayed in the ImageView.
     *
     * @param imageData The byte array containing image data. Can be null or empty.
     * @param avatar The ImageView where the image is to be displayed.
     */
    public void displayImage(byte[] imageData, ImageView avatar) {
        if (imageData == null || imageData.length == 0) {
            avatar.setImage(new Image("/images/avatar_icon.png"));
            return;
        }
        ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
        Image image = new Image(bis);
        avatar.setImage(image);
    }
}