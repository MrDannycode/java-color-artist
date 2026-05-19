package com.example.colorartist.model;

import javafx.scene.paint.Color;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import com.example.colorartist.patterns.decorator.DrawableRegion;

/**
 * Represents a single colorable region (polygon) in a level.
 */
public class ColorRegion implements DrawableRegion {

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

    @Override
    public void draw(GraphicsContext gc, double panX, double panY, double zoom, int selectedColorNumber, Color selectedColor) {
        // Fill
        if (isColored()) {
            gc.setFill(getCurrentColor());
        } else if (selectedColorNumber >= 0 && getColorNumber() == selectedColorNumber) {
            gc.setFill(Color.web("#e8e8f0")); // Highlight hint
        } else {
            gc.setFill(Color.WHITE);
        }
        gc.fillPolygon(xPoints, yPoints, xPoints.length);

        // Border
        gc.setStroke(Color.web("#333333", 0.6));
        gc.setLineWidth(1.2 / zoom);
        gc.strokePolygon(xPoints, yPoints, xPoints.length);

        // Number label (only if not colored)
        if (!isColored()) {
            double cx = getCenterX();
            double cy = getCenterY();

            double fontSize = Math.max(10, 14 / Math.sqrt(zoom > 0.5 ? 1 : zoom));
            gc.setFont(Font.font("Arial", FontWeight.BOLD, fontSize));
            gc.setTextAlign(TextAlignment.CENTER);

            // Background circle for number
            double circleR = fontSize * 0.7;
            gc.setFill(Color.rgb(255, 255, 255, 0.85));
            gc.fillOval(cx - circleR, cy - circleR, circleR * 2, circleR * 2);

            gc.setStroke(Color.web("#999999"));
            gc.setLineWidth(0.5);
            gc.strokeOval(cx - circleR, cy - circleR, circleR * 2, circleR * 2);

            gc.setFill(Color.web("#333333"));
            gc.fillText(String.valueOf(getColorNumber()), cx, cy + fontSize * 0.35);
        }
    }
}
