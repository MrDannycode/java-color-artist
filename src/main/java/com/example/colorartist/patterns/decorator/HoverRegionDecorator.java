package com.example.colorartist.patterns.decorator;

import com.example.colorartist.model.ColorRegion;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Decorator concret pentru sablonul DECORATOR (Structural).
 * Adauga un efect vizual (highlight/hover) peste o regiune existenta
 * fara sa-i modifice comportamentul intern.
 */
public class HoverRegionDecorator implements DrawableRegion {

    private final ColorRegion wrappedRegion;

    public HoverRegionDecorator(ColorRegion wrappedRegion) {
        this.wrappedRegion = wrappedRegion;
    }

    @Override
    public int getId() {
        return wrappedRegion.getId();
    }

    @Override
    public void draw(GraphicsContext gc, double panX, double panY, double zoom, int selectedColorNumber, Color selectedColor) {
        // 1. Apelam comportamentul de baza (deseneaza regiunea normala)
        wrappedRegion.draw(gc, panX, panY, zoom, selectedColorNumber, selectedColor);

        // 2. Adaugam decoratiunea noastra (daca nu e colorata)
        if (!wrappedRegion.isColored()) {
            if (selectedColor != null && wrappedRegion.getColorNumber() == selectedColorNumber) {
                // Glow mai vizibil daca ai culoarea corecta selectata
                gc.setFill(selectedColor.deriveColor(0, 0.3, 1.3, 0.5));
            } else {
                // Glow gri simplu daca nu ai selectat inca acea culoare
                gc.setFill(Color.rgb(200, 200, 255, 0.3));
            }
            
            // Desenam doar umplerea pe deasupra
            gc.fillPolygon(wrappedRegion.getXPoints(), wrappedRegion.getYPoints(), wrappedRegion.getXPoints().length);
        }
    }
}
