package com.example.colorartist.patterns.factory;

import com.example.colorartist.model.ColorRegion;
import com.example.colorartist.model.LevelData;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Concrete Factory pentru nivelul Flower (Easy).
 */
public class FlowerLevelFactory implements LevelFactory {

    @Override
    public LevelData createLevel() {
        double W = 500, H = 500;
        Map<Integer, Color> palette = new LinkedHashMap<>();
        palette.put(1, Color.web("#FF6B6B")); // Red petals
        palette.put(2, Color.web("#FFE66D")); // Yellow center
        palette.put(3, Color.web("#4ECDC4")); // Green stem/leaves
        palette.put(4, Color.web("#95E1D3")); // Light green bg

        List<ColorRegion> regions = new ArrayList<>();
        int id = 0;

        // Background quadrants (4 pieces)
        regions.add(new ColorRegion(id++, 4,
                new double[]{0, 250, 250, 0},
                new double[]{0, 0, 250, 250},
                palette.get(4)));
        regions.add(new ColorRegion(id++, 4,
                new double[]{250, 500, 500, 250},
                new double[]{0, 0, 250, 250},
                palette.get(4)));
        regions.add(new ColorRegion(id++, 4,
                new double[]{0, 250, 250, 0},
                new double[]{250, 250, 500, 500},
                palette.get(4)));
        regions.add(new ColorRegion(id++, 4,
                new double[]{250, 500, 500, 250},
                new double[]{250, 250, 500, 500},
                palette.get(4)));

        double cx = 250, cy = 200, petalR = 80, centerR = 35;

        // 5 petals
        for (int p = 0; p < 5; p++) {
            double angle = Math.toRadians(p * 72 - 90);
            double px = cx + Math.cos(angle) * petalR;
            double py = cy + Math.sin(angle) * petalR;

            double[] xPts = new double[8];
            double[] yPts = new double[8];
            for (int i = 0; i < 8; i++) {
                double a = Math.toRadians(i * 45);
                double rx = 35, ry = 50;
                double localX = Math.cos(a) * rx;
                double localY = Math.sin(a) * ry;
                double cos = Math.cos(angle);
                double sin = Math.sin(angle);
                xPts[i] = px + localX * cos - localY * sin;
                yPts[i] = py + localX * sin + localY * cos;
            }
            regions.add(new ColorRegion(id++, 1, xPts, yPts, palette.get(1)));
        }

        // Center circle
        double[] cxPts = new double[12];
        double[] cyPts = new double[12];
        for (int i = 0; i < 12; i++) {
            double a = Math.toRadians(i * 30);
            cxPts[i] = cx + Math.cos(a) * centerR;
            cyPts[i] = cy + Math.sin(a) * centerR;
        }
        regions.add(new ColorRegion(id++, 2, cxPts, cyPts, palette.get(2)));

        // Stem
        regions.add(new ColorRegion(id++, 3,
                new double[]{242, 258, 258, 242},
                new double[]{235, 235, 420, 420},
                palette.get(3)));

        // Leaves
        regions.add(new ColorRegion(id++, 3,
                new double[]{250, 200, 180, 200, 242},
                new double[]{320, 340, 310, 280, 300},
                palette.get(3)));
        regions.add(new ColorRegion(id++, 3,
                new double[]{250, 300, 320, 300, 258},
                new double[]{350, 370, 340, 310, 330},
                palette.get(3)));

        return new LevelData("Flower", "Easy", regions, palette, W, H);
    }
}
