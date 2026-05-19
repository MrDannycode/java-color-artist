package com.example.colorartist;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;

import java.util.Map;
import java.util.function.Consumer;

/**
 * Renders the color palette at the bottom of the game screen.
 */
public class ColorPaletteView extends HBox {

    private final Map<Integer, Color> palette;
    private final Consumer<Integer> onColorSelected;
    
    private Button selectedButton = null;

    public ColorPaletteView(Map<Integer, Color> palette, Consumer<Integer> onColorSelected) {
        this.palette = palette;
        this.onColorSelected = onColorSelected;
        
        setAlignment(Pos.CENTER);
        setSpacing(16);
        setPadding(new Insets(10));
        
        buildPalette();
    }

    private void buildPalette() {
        getChildren().clear();
        
        for (Map.Entry<Integer, Color> entry : palette.entrySet()) {
            int number = entry.getKey();
            Color color = entry.getValue();
            
            Button btn = new Button(String.valueOf(number));
            btn.getStyleClass().add("color-btn");
            btn.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            
            // Text color based on brightness
            double brightness = color.getBrightness();
            btn.setTextFill(brightness > 0.6 ? Color.web("#222222") : Color.WHITE);
            
            // Set background color
            btn.setBackground(new Background(new BackgroundFill(color, new CornerRadii(24), Insets.EMPTY)));
            
            btn.setOnAction(e -> selectColor(btn, number));
            
            getChildren().add(btn);
        }
    }

    private void selectColor(Button btn, int number) {
        if (selectedButton != null) {
            selectedButton.getStyleClass().remove("color-btn-selected");
        }
        
        selectedButton = btn;
        selectedButton.getStyleClass().add("color-btn-selected");
        
        if (onColorSelected != null) {
            onColorSelected.accept(number);
        }
    }
    
    public void markColorCompleted(int number) {
        for (javafx.scene.Node node : getChildren()) {
            if (node instanceof Button btn) {
                if (btn.getText().equals(String.valueOf(number))) {
                    btn.getStyleClass().add("color-btn-completed");
                    btn.setDisable(true); // Disable completed colors
                }
            }
        }
    }
    
    public void reset() {
        if (selectedButton != null) {
            selectedButton.getStyleClass().remove("color-btn-selected");
            selectedButton = null;
        }
        
        for (javafx.scene.Node node : getChildren()) {
            if (node instanceof Button btn) {
                btn.getStyleClass().remove("color-btn-completed");
                btn.setDisable(false);
            }
        }
    }
}
