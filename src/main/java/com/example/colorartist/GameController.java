package com.example.colorartist;

import com.example.colorartist.levels.LevelGenerator;
import com.example.colorartist.model.ColorRegion;
import com.example.colorartist.model.GameState;
import com.example.colorartist.model.LevelData;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class GameController {

    @FXML
    private Label levelTitleLabel;
    @FXML
    private Label progressLabel;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private StackPane canvasContainer;
    @FXML
    private HBox paletteBar;
    
    private GameCanvas gameCanvas;
    private ColorPaletteView paletteView;
    
    private GameState gameState;

    @FXML
    public void initialize() {
        gameCanvas = new GameCanvas();
        canvasContainer.getChildren().add(gameCanvas);
        
        gameCanvas.setOnRegionColored(this::onRegionColored);
    }

    public void loadLevel(int levelIndex) {
        LevelData levelData = LevelGenerator.getLevel(levelIndex);
        gameState = new GameState(levelData, levelIndex);
        
        levelTitleLabel.setText(levelData.getTitle());
        
        // Setup Canvas
        gameCanvas.setLevelData(levelData);
        
        // Setup Palette
        paletteView = new ColorPaletteView(levelData.getColorPalette(), this::onColorSelected);
        paletteBar.getChildren().clear();
        paletteBar.getChildren().add(paletteView);
        
        updateProgress();
    }

    private void onColorSelected(int colorNumber) {
        LevelData levelData = gameState.getCurrentLevel();
        javafx.scene.paint.Color color = levelData.getColorPalette().get(colorNumber);
        gameState.selectColor(colorNumber, color);
        gameCanvas.setSelectedColor(colorNumber, color);
    }

    private void onRegionColored() {
        updateProgress();
        checkColorCompletion();
        
        if (gameState.isComplete()) {
            onLevelComplete();
        }
    }
    
    private void checkColorCompletion() {
        if (!gameState.hasSelectedColor()) return;
        
        int currentNumber = gameState.getSelectedColorNumber();
        LevelData levelData = gameState.getCurrentLevel();
        
        boolean allOfColorDone = levelData.getRegions().stream()
            .filter(r -> r.getColorNumber() == currentNumber)
            .allMatch(ColorRegion::isColored);
            
        if (allOfColorDone) {
            paletteView.markColorCompleted(currentNumber);
            gameState.selectColor(-1, null);
            gameCanvas.setSelectedColor(-1, null);
            paletteView.reset(); // clear selection styling, keeps disabled state
            
            // Re-apply disabled state correctly
            for(int num : levelData.getColorPalette().keySet()) {
                boolean done = levelData.getRegions().stream()
                    .filter(r -> r.getColorNumber() == num)
                    .allMatch(ColorRegion::isColored);
                if (done) paletteView.markColorCompleted(num);
            }
        }
    }

    private void updateProgress() {
        double progress = gameState.getProgress();
        progressBar.setProgress(progress);
        
        LevelData levelData = gameState.getCurrentLevel();
        progressLabel.setText(levelData.getColoredCount() + " / " + levelData.getTotalRegions());
    }
    
    private void onLevelComplete() {
        try {
            ColorArtistApp.showVictory(gameState.getLevelIndex(), gameState.getCurrentLevel().getTotalRegions());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onBackClick() {
        try {
            ColorArtistApp.showMenu();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onResetClick() {
        gameState.reset();
        paletteView.reset();
        gameCanvas.setSelectedColor(-1, null);
        gameCanvas.draw();
        updateProgress();
    }
}
