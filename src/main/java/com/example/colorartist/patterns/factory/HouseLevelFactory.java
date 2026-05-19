package com.example.colorartist.patterns.factory;

import com.example.colorartist.model.ColorRegion;
import com.example.colorartist.model.LevelData;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Concrete Factory pentru nivelul House (Medium).
 */
public class HouseLevelFactory implements LevelFactory {
    @Override
    public LevelData createLevel() {
        double W = 600, H = 500;
        Map<Integer, Color> palette = new LinkedHashMap<>();
        palette.put(1, Color.web("#E74C3C")); // Red roof
        palette.put(2, Color.web("#F5CBA7")); // Beige walls
        palette.put(3, Color.web("#5D4E37")); // Brown door
        palette.put(4, Color.web("#85C1E9")); // Light blue windows
        palette.put(5, Color.web("#27AE60")); // Green grass
        palette.put(6, Color.web("#87CEEB")); // Sky blue

        List<ColorRegion> regions = new ArrayList<>();
        int id = 0;

        regions.add(new ColorRegion(id++, 6, new double[]{0, 600, 600, 0}, new double[]{0, 0, 320, 320}, palette.get(6)));
        regions.add(new ColorRegion(id++, 5, new double[]{0, 600, 600, 0}, new double[]{320, 320, 500, 500}, palette.get(5)));
        regions.add(new ColorRegion(id++, 2, new double[]{150, 450, 450, 150}, new double[]{180, 180, 380, 380}, palette.get(2)));
        regions.add(new ColorRegion(id++, 1, new double[]{130, 300, 470}, new double[]{180, 80, 180}, palette.get(1)));
        regions.add(new ColorRegion(id++, 3, new double[]{270, 330, 330, 270}, new double[]{280, 280, 380, 380}, palette.get(3)));
        regions.add(new ColorRegion(id++, 4, new double[]{180, 240, 240, 180}, new double[]{220, 220, 270, 270}, palette.get(4)));
        regions.add(new ColorRegion(id++, 4, new double[]{360, 420, 420, 360}, new double[]{220, 220, 270, 270}, palette.get(4)));
        regions.add(new ColorRegion(id++, 1, new double[]{370, 400, 400, 370}, new double[]{90, 90, 155, 155}, palette.get(1)));
        regions.add(new ColorRegion(id++, 5, new double[]{60, 90, 120, 110, 50}, new double[]{380, 340, 380, 400, 400}, palette.get(5)));
        regions.add(new ColorRegion(id++, 5, new double[]{480, 510, 540, 530, 470}, new double[]{380, 340, 380, 400, 400}, palette.get(5)));
        
        double sunCx = 520, sunCy = 60, sunR = 35;
        double[] sunX = new double[12];
        double[] sunY = new double[12];
        for (int i = 0; i < 12; i++) {
            double a = Math.toRadians(i * 30);
            sunX[i] = sunCx + Math.cos(a) * sunR;
            sunY[i] = sunCy + Math.sin(a) * sunR;
        }
        regions.add(new ColorRegion(id++, 2, sunX, sunY, Color.web("#F1C40F")));

        regions.add(new ColorRegion(id++, 3, new double[]{270, 330, 340, 260}, new double[]{380, 380, 500, 500}, palette.get(3)));
        regions.add(new ColorRegion(id++, 2, new double[]{20, 140, 140, 20}, new double[]{350, 350, 365, 365}, palette.get(2)));
        regions.add(new ColorRegion(id++, 2, new double[]{460, 580, 580, 460}, new double[]{350, 350, 365, 365}, palette.get(2)));
        regions.add(new ColorRegion(id++, 6, new double[]{80, 100, 130, 160, 180, 170, 130, 90}, new double[]{50, 30, 25, 30, 50, 65, 65, 60}, Color.web("#B0D4F1")));

        return new LevelData("Cozy House", "Medium", regions, palette, W, H);
    }
}
