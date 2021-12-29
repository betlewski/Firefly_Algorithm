package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("view/view.fxml"));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("icons/firefly.png")));
        stage.setTitle("Glowworm Swarm Optimization");
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.show();
        stage.setOnCloseRequest(e -> System.exit(0));
    }

    public static void main(String[] args) {
        launch(args);
    }

}