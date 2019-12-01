package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Project extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/project.fxml"));
        primaryStage.setTitle("Solar System Planet Trajectories");
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add("/fxml/project.css");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(this.getClass().getResource("/icons/sunIcon.png").toString()));
        primaryStage.show();
    }
}
