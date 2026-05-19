package com.example.colorartist.patterns.factory;

import com.example.colorartist.model.ColorRegion;
import com.example.colorartist.model.LevelData;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Concrete Factory pentru nivelul Landscape (Hard).
 */
public class LandscapeLevelFactory implements LevelFactory {
    @Override
    public LevelData createLevel() {
        double W = 700, H = 500;
        Map<Integer, Color> palette = new LinkedHashMap<>();
        palette.put(1, Color.web("#2C3E50"));
        palette.put(2, Color.web("#5D6D7E"));
        palette.put(3, Color.web("#27AE60"));
        palette.put(4, Color.web("#2ECC71"));
        palette.put(5, Color.web("#3498DB"));
        palette.put(6, Color.web("#F39C12"));
        palette.put(7, Color.web("#85C1E9"));

        List<ColorRegion> regions = new ArrayList<>();
        int id = 0;

        regions.add(new ColorRegion(id++, 7, new double[]{0, 233, 233, 0}, new double[]{0, 0, 200, 200}, palette.get(7)));
        regions.add(new ColorRegion(id++, 7, new double[]{233, 467, 467, 233}, new double[]{0, 0, 200, 200}, palette.get(7)));
        regions.add(new ColorRegion(id++, 7, new double[]{467, 700, 700, 467}, new double[]{0, 0, 200, 200}, palette.get(7)));

        double sunCx2 = 600, sunCy2 = 80, sunR2 = 40;
        double[] sx = new double[16];
        double[] sy = new double[16];
        for (int i = 0; i < 16; i++) {
            double a = Math.toRadians(i * 22.5);
            sx[i] = sunCx2 + Math.cos(a) * sunR2;
            sy[i] = sunCy2 + Math.sin(a) * sunR2;
        }
        regions.add(new ColorRegion(id++, 6, sx, sy, palette.get(6)));

        regions.add(new ColorRegion(id++, 1, new double[]{0, 100, 200, 300, 350, 300, 200, 100, 0}, new double[]{250, 120, 100, 130, 200, 250, 250, 250, 250}, palette.get(1)));
        regions.add(new ColorRegion(id++, 1, new double[]{300, 400, 500, 580, 650, 700, 700, 300}, new double[]{250, 130, 90, 120, 160, 200, 250, 250}, palette.get(1)));
        regions.add(new ColorRegion(id++, 2, new double[]{0, 80, 180, 280, 350, 280, 0}, new double[]{300, 200, 180, 220, 300, 300, 300}, palette.get(2)));
        regions.add(new ColorRegion(id++, 2, new double[]{350, 450, 550, 630, 700, 700, 350}, new double[]{300, 200, 190, 230, 280, 300, 300}, palette.get(2)));
        regions.add(new ColorRegion(id++, 3, new double[]{0, 100, 200, 250, 200, 100, 0}, new double[]{380, 310, 300, 340, 380, 380, 380}, palette.get(3)));
        regions.add(new ColorRegion(id++, 4, new double[]{200, 300, 400, 500, 450, 350, 250}, new double[]{380, 320, 310, 350, 380, 380, 380}, palette.get(4)));
        regions.add(new ColorRegion(id++, 3, new double[]{450, 550, 650, 700, 700, 500}, new double[]{380, 310, 300, 340, 380, 380}, palette.get(3)));
        regions.add(new ColorRegion(id++, 5, new double[]{150, 250, 350, 450, 550, 500, 400, 300, 200}, new double[]{400, 385, 380, 385, 400, 430, 440, 440, 430}, palette.get(5)));
        regions.add(new ColorRegion(id++, 4, new double[]{0, 150, 200, 150, 0}, new double[]{380, 400, 440, 500, 500}, palette.get(4)));
        regions.add(new ColorRegion(id++, 4, new double[]{550, 600, 700, 700, 550}, new double[]{400, 380, 380, 500, 500}, palette.get(4)));
        regions.add(new ColorRegion(id++, 1, new double[]{85, 95, 95, 85}, new double[]{340, 340, 390, 390}, palette.get(1)));
        regions.add(new ColorRegion(id++, 3, new double[]{60, 90, 120}, new double[]{340, 280, 340}, palette.get(3)));
        regions.add(new ColorRegion(id++, 1, new double[]{625, 635, 635, 625}, new double[]{330, 330, 380, 380}, palette.get(1)));
        regions.add(new ColorRegion(id++, 3, new double[]{600, 630, 660}, new double[]{330, 270, 330}, palette.get(3)));
        regions.add(new ColorRegion(id++, 7, new double[]{120, 150, 190, 220, 210, 160, 130}, new double[]{60, 40, 38, 55, 70, 72, 68}, Color.web("#BDE0F5")));
        regions.add(new ColorRegion(id++, 7, new double[]{330, 360, 400, 430, 420, 370, 340}, new double[]{45, 28, 25, 42, 58, 60, 55}, Color.web("#BDE0F5")));
        regions.add(new ColorRegion(id++, 4, new double[]{0, 700, 700, 0}, new double[]{440, 440, 500, 500}, palette.get(4)));

        return new LevelData("Mountain Lake", "Hard", regions, palette, W, H);
    }
}
