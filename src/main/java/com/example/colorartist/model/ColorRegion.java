package com.example.colorartist.model;

import javafx.scene.paint.Color;

/**
 * Represents a single colorable region (polygon) in a level.
 */
public class ColorRegion {

    private final int id;
    private final int colorNumber; // The number displayed in the region (corresponds to palette)
    private final double[] xPoints;
    private final double[] yPoints;
    private final Color targetColor;

    private boolean colored = false;
    private Color currentColor = null;

    public ColorRegion(int id, int colorNumber, double[] xPoints, double[] yPoints, Color targetColor) {
        this.id = id;
        this.colorNumber = colorNumber;
        this.xPoints = xPoints;
        this.yPoints = yPoints;
        this.targetColor = targetColor;
    }

    public int getId() {
        return id;
    }

    public int getColorNumber() {
        return colorNumber;
    }

    public double[] getXPoints() {
        return xPoints;
    }

    public double[] getYPoints() {
        return yPoints;
    }

    public Color getTargetColor() {
        return targetColor;
    }

    public boolean isColored() {
        return colored;
    }

    public Color getCurrentColor() {
        return currentColor;
    }

    public void setColor(Color color) {
        this.currentColor = color;
        this.colored = true;
    }

    public boolean isCorrectlyColored() {
        return colored && currentColor != null && currentColor.equals(targetColor);
    }

    public void reset() {
        this.colored = false;
        this.currentColor = null;
    }

    /**
     * Returns the centroid of the polygon (for label placement).
     */
    public double getCenterX() {
        double sum = 0;
        for (double x : xPoints) sum += x;
        return sum / xPoints.length;
    }

    public double getCenterY() {
        double sum = 0;
        for (double y : yPoints) sum += y;
        return sum / yPoints.length;
    }

    /**
     * Checks if a point is inside this polygon using ray casting algorithm.
     */
    public boolean containsPoint(double px, double py) {
        boolean inside = false;
        int n = xPoints.length;
        for (int i = 0, j = n - 1; i < n; j = i++) {
            if ((yPoints[i] > py) != (yPoints[j] > py) &&
                    px < (xPoints[j] - xPoints[i]) * (py - yPoints[i]) / (yPoints[j] - yPoints[i]) + xPoints[i]) {
                inside = !inside;
            }
        }
        return inside;
    }
}
