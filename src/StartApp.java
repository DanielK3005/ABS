import mainWindow.ABSController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class StartApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/mainWindow/resource/main-fxml.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());
        primaryStage.setTitle("Alternative Banking System");
        ABSController ABSController = fxmlLoader.getController();
        ABSController.setPrimaryStage(primaryStage);
        Scene scene = new Scene(root, 1200, 700);
        scene.getStylesheets().add("/mainWindow/resource/main.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
