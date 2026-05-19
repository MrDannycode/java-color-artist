package com.example.colorartist.patterns.singleton;

import com.example.colorartist.ColorArtistApp;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * GameManager implementeaza sablonul SINGLETON (Creational).
 * Garanteaza ca exista o singura instanta de management la nivel global
 * pentru starea jocului, preferinte si navigare intre scene.
 */
public class GameManager {
    
    // Instanta unica statica
    private static GameManager instance;
    
    private Stage primaryStage;
    
    // Constructor privat pentru a impiedica instantierea externa
    private GameManager() {
    }
    
    // Metoda publica statica pentru a obtine instanta unica
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }
    
    // Initializarea sistemului de navigare
    public void init(Stage stage) {
        this.primaryStage = stage;
    }
    
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    
    // Functionalitati globale gestionate de Singleton
    public void navigateToMenu() {
        try {
            ColorArtistApp.showMenu();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void navigateToGame(int levelIndex) {
        try {
            ColorArtistApp.showGame(levelIndex);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void navigateToVictory(int levelIndex, int totalRegions) {
        try {
            ColorArtistApp.showVictory(levelIndex, totalRegions);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
