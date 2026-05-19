package com.example.colorartist;

import com.example.colorartist.levels.LevelGenerator;
import com.example.colorartist.model.ColorRegion;
import com.example.colorartist.model.GameState;
import com.example.colorartist.model.LevelData;
import com.example.colorartist.patterns.singleton.GameManager;
import com.example.colorartist.patterns.observer.GameObserver;
import com.example.colorartist.patterns.command.Command;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.Stack;

public class GameController implements GameObserver {

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
    @FXML
    private Button undoBtn;
    
    private GameCanvas gameCanvas;
    private ColorPaletteView paletteView;
    
    private GameState gameState;
    
    // Command History (Undo Stack)
    private final Stack<Command> commandHistory = new Stack<>();

    @FXML
    public void initialize() {
        gameCanvas = new GameCanvas();
        canvasContainer.getChildren().add(gameCanvas);
        
        gameCanvas.setOnCommandExecuted(this::onCommandExecuted);
    }

    public void loadLevel(int levelIndex) {
        LevelData levelData = LevelGenerator.getLevel(levelIndex);
        gameState = new GameState(levelData, levelIndex);
        
        // Atasam observatorul
        gameState.attach(this);
        
        levelTitleLabel.setText(levelData.getTitle());
        
        // Setup Canvas
        gameCanvas.setLevelData(levelData);
        
        // Setup Palette
        paletteView = new ColorPaletteView(levelData.getColorPalette(), this::onColorSelected);
        paletteBar.getChildren().clear();
        paletteBar.getChildren().add(paletteView);
        
        // Initial trigger for UI
        gameState.notifyObservers();
    }

    private void onColorSelected(int colorNumber) {
        LevelData levelData = gameState.getCurrentLevel();
        javafx.scene.paint.Color color = levelData.getColorPalette().get(colorNumber);
        gameState.selectColor(colorNumber, color);
        gameCanvas.setSelectedColor(colorNumber, color);
    }

    private void onCommandExecuted(Command command) {
        // Salvam comanda in istoric
        commandHistory.push(command);
        undoBtn.setDisable(false);
        
        // Notificam observatorii despre schimbare
        gameState.notifyObservers();
        checkColorCompletion();
    }
    
    // Metoda primita de la GameObserver
    @Override
    public void onGameStateChanged(int coloredRegions, int totalRegions, boolean isComplete) {
        double progress = totalRegions == 0 ? 1.0 : (double) coloredRegions / totalRegions;
        progressBar.setProgress(progress);
        progressLabel.setText(coloredRegions + " / " + totalRegions);
        
        if (isComplete) {
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
    
    @FXML
    protected void onUndoClick() {
        if (!commandHistory.isEmpty()) {
            Command lastCommand = commandHistory.pop();
            lastCommand.undo();
            undoBtn.setDisable(commandHistory.isEmpty());
            
            // Resetam stilurile din paleta, fiindca poate un Undo a anulat completarea unei culori
            paletteView.reset();
            LevelData levelData = gameState.getCurrentLevel();
            for(int num : levelData.getColorPalette().keySet()) {
                boolean done = levelData.getRegions().stream()
                    .filter(r -> r.getColorNumber() == num)
                    .allMatch(ColorRegion::isColored);
                if (done) paletteView.markColorCompleted(num);
            }
            
            gameCanvas.draw();
            gameState.notifyObservers();
        }
    }

    private void onLevelComplete() {
        GameManager.getInstance().navigateToVictory(gameState.getLevelIndex(), gameState.getCurrentLevel().getTotalRegions());
    }

    @FXML
    protected void onBackClick() {
        GameManager.getInstance().navigateToMenu();
    }

    @FXML
    protected void onResetClick() {
        gameState.reset();
        paletteView.reset();
        commandHistory.clear();
        undoBtn.setDisable(true);
        gameCanvas.setSelectedColor(-1, null);
        gameCanvas.draw();
    }
}
