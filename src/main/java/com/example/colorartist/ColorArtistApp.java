package com.example.colorartist;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

import com.example.colorartist.patterns.singleton.GameManager;

public class ColorArtistApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Load custom fonts
        Font.loadFont(getClass().getResourceAsStream("fonts/Poppins-Bold.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("fonts/Poppins-Regular.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("fonts/Poppins-SemiBold.ttf"), 14);

        // Initializare Singleton pentru managerul de joc
        GameManager.getInstance().init(stage);
        
        // Pornire joc la ecranul de meniu
        GameManager.getInstance().navigateToMenu();
    }

    // Aceste metode raman aici temporar ca utility pentru GameManager
    public static void showMenu() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ColorArtistApp.class.getResource("menu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 768);
        scene.getStylesheets().add(ColorArtistApp.class.getResource("styles.css").toExternalForm());
        
        Stage stage = GameManager.getInstance().getPrimaryStage();
        if (stage != null) {
            stage.setTitle("Color Artist");
            stage.setScene(scene);
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.show();
        }
    }

    public static void showGame(int levelIndex) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ColorArtistApp.class.getResource("game-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 768);
        scene.getStylesheets().add(ColorArtistApp.class.getResource("styles.css").toExternalForm());

        GameController controller = fxmlLoader.getController();
        controller.loadLevel(levelIndex);

        Stage stage = GameManager.getInstance().getPrimaryStage();
        if (stage != null) stage.setScene(scene);
    }

    public static void showVictory(int levelIndex, int totalRegions) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ColorArtistApp.class.getResource("victory-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 768);
        scene.getStylesheets().add(ColorArtistApp.class.getResource("styles.css").toExternalForm());

        VictoryController controller = fxmlLoader.getController();
        controller.setup(levelIndex, totalRegions);

        Stage stage = GameManager.getInstance().getPrimaryStage();
        if (stage != null) stage.setScene(scene);
    }

    public static void main(String[] args) {
        launch();
    }
}
