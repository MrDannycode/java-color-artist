package com.example.colorartist;

import com.example.colorartist.model.ColorRegion;
import com.example.colorartist.model.LevelData;
import com.example.colorartist.patterns.decorator.DrawableRegion;
import com.example.colorartist.patterns.decorator.HoverRegionDecorator;
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

import java.util.function.Consumer;
import com.example.colorartist.patterns.command.Command;
import com.example.colorartist.patterns.command.ColorCommand;

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

    // Callback pentru sablonul Command
    private Consumer<Command> onCommandExecuted;

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

    public void setOnCommandExecuted(Consumer<Command> callback) {
        this.onCommandExecuted = callback;
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
                    
                    // Sablon COMMAND: Cream comanda, o executam si notificam controller-ul
                    ColorCommand command = new ColorCommand(region, selectedColor);
                    command.execute();
                    draw();
                    
                    if (onCommandExecuted != null) {
                        onCommandExecuted.accept(command);
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

        // Draw regions using Decorator pattern
        for (ColorRegion region : levelData.getRegions()) {
            DrawableRegion drawable = region;
            
            // Daca e regiunea cu hover, o invelim in decorator
            if (region.getId() == hoveredRegionId) {
                drawable = new HoverRegionDecorator(region);
            }
            
            drawable.draw(gc, panX, panY, zoom, selectedColorNumber, selectedColor);
        }

        gc.restore();
    }

    public void resetView() {
        autoFitZoom();
        draw();
    }
}
