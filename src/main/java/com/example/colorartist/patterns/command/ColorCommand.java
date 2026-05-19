package com.example.colorartist.patterns.command;

import com.example.colorartist.model.ColorRegion;
import javafx.scene.paint.Color;

/**
 * Concrete Command (Behavioral).
 * Memoreaza actiunea de colorare a unei regiuni, permitand operatia de "Undo".
 */
public class ColorCommand implements Command {

    private final ColorRegion region;
    private final Color previousColor;
    private final boolean wasColored;
    private final Color newColor;

    public ColorCommand(ColorRegion region, Color newColor) {
        this.region = region;
        this.newColor = newColor;
        this.previousColor = region.getCurrentColor();
        this.wasColored = region.isColored();
    }

    @Override
    public void execute() {
        region.setColor(newColor);
    }

    @Override
    public void undo() {
        if (wasColored) {
            region.setColor(previousColor);
        } else {
            region.reset();
        }
    }
}
