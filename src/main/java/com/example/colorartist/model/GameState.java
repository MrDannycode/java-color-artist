package com.example.colorartist.model;

import javafx.scene.paint.Color;

/**
 * Tracks the current game state during gameplay.
 */
public class GameState {

    private LevelData currentLevel;
    private int selectedColorNumber = -1;
    private Color selectedColor = null;
    private int levelIndex;

    public GameState(LevelData level, int levelIndex) {
        this.currentLevel = level;
        this.levelIndex = levelIndex;
    }

    public LevelData getCurrentLevel() {
        return currentLevel;
    }

    public int getLevelIndex() {
        return levelIndex;
    }

    public int getSelectedColorNumber() {
        return selectedColorNumber;
    }

    public Color getSelectedColor() {
        return selectedColor;
    }

    public void selectColor(int number, Color color) {
        this.selectedColorNumber = number;
        this.selectedColor = color;
    }

    public boolean hasSelectedColor() {
        return selectedColor != null;
    }

    public double getProgress() {
        if (currentLevel.getTotalRegions() == 0) return 1.0;
        return (double) currentLevel.getColoredCount() / currentLevel.getTotalRegions();
    }

    public boolean isComplete() {
        return currentLevel.isComplete();
    }

    public void reset() {
        currentLevel.resetAll();
        selectedColor = null;
        selectedColorNumber = -1;
    }
}
