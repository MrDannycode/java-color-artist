package com.example.colorartist.patterns.decorator;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Interfata Componenta pentru sablonul DECORATOR (Structural).
 * Orice obiect care poate fi desenat pe canvas implementeaza aceasta interfata.
 */
public interface DrawableRegion {
    void draw(GraphicsContext gc, double panX, double panY, double zoom, int selectedColorNumber, Color selectedColor);
    int getId();
}
