package com.example.colorartist;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class ColorArtistApp extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        // Load custom fonts
        Font.loadFont(getClass().getResourceAsStream("fonts/Poppins-Bold.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("fonts/Poppins-Regular.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("fonts/Poppins-SemiBold.ttf"), 14);

        showMenu();
    }

    public static void showMenu() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ColorArtistApp.class.getResource("menu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 768);
        scene.getStylesheets().add(ColorArtistApp.class.getResource("styles.css").toExternalForm());
        primaryStage.setTitle("Color Artist");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }

    public static void showGame(int levelIndex) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ColorArtistApp.class.getResource("game-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 768);
        scene.getStylesheets().add(ColorArtistApp.class.getResource("styles.css").toExternalForm());

        GameController controller = fxmlLoader.getController();
        controller.loadLevel(levelIndex);

        primaryStage.setScene(scene);
    }

    public static void showVictory(int levelIndex, int totalRegions) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ColorArtistApp.class.getResource("victory-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 768);
        scene.getStylesheets().add(ColorArtistApp.class.getResource("styles.css").toExternalForm());

        VictoryController controller = fxmlLoader.getController();
        controller.setup(levelIndex, totalRegions);

        primaryStage.setScene(scene);
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch();
    }
}
