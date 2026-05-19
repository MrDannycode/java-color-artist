package com.example.colorartist.model;

import javafx.scene.paint.Color;

import java.util.List;
import java.util.Map;

/**
 * Holds all data for a single game level.
 */
public class LevelData {

    private final String title;
    private final String difficulty;
    private final List<ColorRegion> regions;
    private final Map<Integer, Color> colorPalette; // number -> color
    private final double canvasWidth;
    private final double canvasHeight;

    public LevelData(String title, String difficulty, List<ColorRegion> regions,
                     Map<Integer, Color> colorPalette, double canvasWidth, double canvasHeight) {
        this.title = title;
        this.difficulty = difficulty;
        this.regions = regions;
        this.colorPalette = colorPalette;
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
    }

    public String getTitle() {
        return title;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public List<ColorRegion> getRegions() {
        return regions;
    }

    public Map<Integer, Color> getColorPalette() {
        return colorPalette;
    }

    public double getCanvasWidth() {
        return canvasWidth;
    }

    public double getCanvasHeight() {
        return canvasHeight;
    }

    public int getTotalRegions() {
        return regions.size();
    }

    public long getColoredCount() {
        return regions.stream().filter(ColorRegion::isColored).count();
    }

    public boolean isComplete() {
        return regions.stream().allMatch(ColorRegion::isColored);
    }

    public void resetAll() {
        regions.forEach(ColorRegion::reset);
    }
}
