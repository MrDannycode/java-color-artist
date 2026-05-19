package com.example.colorartist;

import com.example.colorartist.model.ColorRegion;
import com.example.colorartist.model.LevelData;
import javafx.animation.FadeTransition;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

/**
 * Custom canvas component for rendering and interacting with the coloring game.
 * Supports zoom, pan, and click-to-color.
 */
public class GameCanvas extends Pane {

    private final Canvas canvas;
    private LevelData levelData;
    private int hoveredRegionId = -1;
    private int selectedColorNumber = -1;
    private Color selectedColor = null;

    // Transform state
    private double zoom = 1.0;
    private double panX = 0, panY = 0;
    private double lastMouseX, lastMouseY;
    private boolean isPanning = false;

    // Callback
    private Runnable onRegionColored;

    public GameCanvas() {
        canvas = new Canvas();
        getChildren().add(canvas);

        // Bind canvas size to pane
        canvas.widthProperty().bind(widthProperty());
        canvas.heightProperty().bind(heightProperty());

        // Redraw on resize
        widthProperty().addListener((obs, o, n) -> draw());
        heightProperty().addListener((obs, o, n) -> draw());

        setupMouseHandlers();
    }

    public void setLevelData(LevelData data) {
        this.levelData = data;
        autoFitZoom();
        draw();
    }

    public void setSelectedColor(int number, Color color) {
        this.selectedColorNumber = number;
        this.selectedColor = color;
        draw();
    }

    public void setOnRegionColored(Runnable callback) {
        this.onRegionColored = callback;
    }

    private void autoFitZoom() {
        if (levelData == null) return;
        double w = getWidth();
        double h = getHeight();
        if (w <= 0 || h <= 0) {
            zoom = 1.0;
            panX = 0;
            panY = 0;
            return;
        }
        double scaleX = (w - 40) / levelData.getCanvasWidth();
        double scaleY = (h - 40) / levelData.getCanvasHeight();
        zoom = Math.min(scaleX, scaleY);
        if (zoom < 0.1) zoom = 0.1;

        // Center the level
        panX = (w - levelData.getCanvasWidth() * zoom) / 2;
        panY = (h - levelData.getCanvasHeight() * zoom) / 2;
    }

    private void setupMouseHandlers() {
        setOnMouseMoved(e -> {
            int newHovered = findRegionAt(e.getX(), e.getY());
            if (newHovered != hoveredRegionId) {
                hoveredRegionId = newHovered;
                draw();
            }
        });

        setOnMouseClicked(e -> {
            if (isPanning) return;
            if (selectedColor == null) return;

            int regionId = findRegionAt(e.getX(), e.getY());
            if (regionId >= 0 && levelData != null) {
                ColorRegion region = levelData.getRegions().stream()
                        .filter(r -> r.getId() == regionId)
                        .findFirst().orElse(null);
                if (region != null && region.getColorNumber() == selectedColorNumber && !region.isColored()) {
                    region.setColor(selectedColor);
                    draw();
                    if (onRegionColored != null) {
                        onRegionColored.run();
                    }
                }
            }
        });

        // Scroll to zoom
        setOnScroll(e -> {
            double oldZoom = zoom;
            double factor = e.getDeltaY() > 0 ? 1.1 : 0.9;
            zoom *= factor;
            zoom = Math.max(0.3, Math.min(5.0, zoom));

            // Zoom toward mouse position
            double mouseX = e.getX();
            double mouseY = e.getY();
            panX = mouseX - (mouseX - panX) * (zoom / oldZoom);
            panY = mouseY - (mouseY - panY) * (zoom / oldZoom);

            draw();
        });

        // Pan with middle mouse or right click
        setOnMousePressed(e -> {
            if (e.isMiddleButtonDown() || e.isSecondaryButtonDown()) {
                isPanning = true;
                lastMouseX = e.getX();
                lastMouseY = e.getY();
            }
        });

        setOnMouseDragged(e -> {
            if (isPanning) {
                double dx = e.getX() - lastMouseX;
                double dy = e.getY() - lastMouseY;
                panX += dx;
                panY += dy;
                lastMouseX = e.getX();
                lastMouseY = e.getY();
                draw();
            }
        });

        setOnMouseReleased(e -> {
            isPanning = false;
        });

        setOnMouseExited(e -> {
            hoveredRegionId = -1;
            draw();
        });
    }

    private int findRegionAt(double screenX, double screenY) {
        if (levelData == null) return -1;

        // Convert screen coords to level coords
        double levelX = (screenX - panX) / zoom;
        double levelY = (screenY - panY) / zoom;

        // Search in reverse order (top regions first)
        var regions = levelData.getRegions();
        for (int i = regions.size() - 1; i >= 0; i--) {
            ColorRegion r = regions.get(i);
            if (r.containsPoint(levelX, levelY)) {
                return r.getId();
            }
        }
        return -1;
    }

    public void draw() {
        if (levelData == null) return;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double w = canvas.getWidth();
        double h = canvas.getHeight();

        // Clear
        gc.setFill(Color.web("#12121f"));
        gc.fillRect(0, 0, w, h);

        // Draw checkerboard background for canvas area
        gc.save();
        gc.translate(panX, panY);
        gc.scale(zoom, zoom);

        // Canvas background
        gc.setFill(Color.web("#fafafa"));
        gc.fillRect(-2, -2, levelData.getCanvasWidth() + 4, levelData.getCanvasHeight() + 4);

        // Draw regions
        for (ColorRegion region : levelData.getRegions()) {
            drawRegion(gc, region);
        }

        gc.restore();
    }

    private void drawRegion(GraphicsContext gc, ColorRegion region) {
        double[] xPts = region.getXPoints();
        double[] yPts = region.getYPoints();

        // Fill
        if (region.isColored()) {
            gc.setFill(region.getCurrentColor());
        } else if (selectedColorNumber >= 0 && region.getColorNumber() == selectedColorNumber) {
            // Highlight matching regions with a subtle tint
            gc.setFill(Color.web("#e8e8f0"));
        } else {
            gc.setFill(Color.WHITE);
        }
        gc.fillPolygon(xPts, yPts, xPts.length);

        // Hover highlight
        if (region.getId() == hoveredRegionId && !region.isColored()) {
            if (selectedColor != null && region.getColorNumber() == selectedColorNumber) {
                gc.setFill(selectedColor.deriveColor(0, 0.3, 1.3, 0.5));
            } else {
                gc.setFill(Color.rgb(200, 200, 255, 0.3));
            }
            gc.fillPolygon(xPts, yPts, xPts.length);
        }

        // Border
        gc.setStroke(Color.web("#333333", 0.6));
        gc.setLineWidth(1.2 / zoom);
        gc.strokePolygon(xPts, yPts, xPts.length);

        // Number label (only if not colored)
        if (!region.isColored()) {
            double cx = region.getCenterX();
            double cy = region.getCenterY();

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
            gc.fillText(String.valueOf(region.getColorNumber()), cx, cy + fontSize * 0.35);
        }
    }

    public void resetView() {
        autoFitZoom();
        draw();
    }
}
