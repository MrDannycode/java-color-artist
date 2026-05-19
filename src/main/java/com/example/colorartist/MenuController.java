package com.example.colorartist;

import com.example.colorartist.levels.LevelGenerator;
import com.example.colorartist.model.LevelData;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @FXML
    private FlowPane levelsGrid;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<LevelData> levels = LevelGenerator.getAllLevels();
        for (int i = 0; i < levels.size(); i++) {
            levelsGrid.getChildren().add(createLevelCard(levels.get(i), i));
        }
    }

    private VBox createLevelCard(LevelData level, int index) {
        VBox card = new VBox();
        card.getStyleClass().add("level-card");
        card.setPrefWidth(280);
        card.setPrefHeight(320);

        // Preview canvas
        Canvas preview = new Canvas(260, 180);
        drawPreview(preview, level);
        StackPane previewPane = new StackPane(preview);
        previewPane.getStyleClass().add("level-preview");
        previewPane.setPadding(new Insets(10));

        // Info section
        VBox infoBox = new VBox(8);
        infoBox.getStyleClass().add("level-card-content");
        infoBox.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label(level.getTitle());
        titleLabel.getStyleClass().add("level-title");

        Label diffLabel = new Label(level.getDifficulty());
        diffLabel.getStyleClass().add("level-difficulty");
        switch (level.getDifficulty()) {
            case "Easy":
                diffLabel.getStyleClass().add("difficulty-easy");
                break;
            case "Medium":
                diffLabel.getStyleClass().add("difficulty-medium");
                break;
            case "Hard":
                diffLabel.getStyleClass().add("difficulty-hard");
                break;
        }

        Label infoLabel = new Label(level.getRegions().size() + " regions · " +
                level.getColorPalette().size() + " colors");
        infoLabel.getStyleClass().add("level-info");

        infoBox.getChildren().addAll(titleLabel, diffLabel, infoLabel);

        card.getChildren().addAll(previewPane, infoBox);

        // Hover animation
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(150), card);
        scaleUp.setToX(1.03);
        scaleUp.setToY(1.03);

        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(150), card);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        card.setOnMouseEntered(e -> scaleUp.playFromStart());
        card.setOnMouseExited(e -> scaleDown.playFromStart());

        // Click handler
        card.setOnMouseClicked(e -> {
            try {
                ColorArtistApp.showGame(index);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        return card;
    }

    private void drawPreview(Canvas canvas, LevelData level) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double w = canvas.getWidth();
        double h = canvas.getHeight();

        // Dark background
        gc.setFill(Color.web("#0f0f1a"));
        gc.fillRect(0, 0, w, h);

        // Scale the level regions to fit preview
        double scaleX = w / level.getCanvasWidth();
        double scaleY = h / level.getCanvasHeight();
        double scale = Math.min(scaleX, scaleY) * 0.85;
        double offsetX = (w - level.getCanvasWidth() * scale) / 2;
        double offsetY = (h - level.getCanvasHeight() * scale) / 2;

        // Draw each region with its target color (as a preview)
        level.getRegions().forEach(region -> {
            double[] xPoints = region.getXPoints();
            double[] yPoints = region.getYPoints();
            double[] scaledX = new double[xPoints.length];
            double[] scaledY = new double[yPoints.length];

            for (int i = 0; i < xPoints.length; i++) {
                scaledX[i] = xPoints[i] * scale + offsetX;
                scaledY[i] = yPoints[i] * scale + offsetY;
            }

            // Fill with target color
            gc.setFill(region.getTargetColor());
            gc.fillPolygon(scaledX, scaledY, xPoints.length);

            // Thin border
            gc.setStroke(Color.web("#0f0f1a", 0.5));
            gc.setLineWidth(0.5);
            gc.strokePolygon(scaledX, scaledY, xPoints.length);
        });
    }
}
