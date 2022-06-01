package message;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MessageController {

    @FXML
    private ImageView MessageImage;

    @FXML
    private Label MessageLabel;

    public void setMessageText(String Text) {
        MessageLabel.setText(Text);
    }

    public void setMessageImage(Image messageImage) {
        MessageImage.setImage(messageImage);
    }
}
