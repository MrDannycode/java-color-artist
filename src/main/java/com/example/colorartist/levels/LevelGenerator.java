package com.example.colorartist.levels;

import com.example.colorartist.model.ColorRegion;
import com.example.colorartist.model.LevelData;
import javafx.scene.paint.Color;

import java.util.*;

/**
 * Generates game levels with polygon-based regions.
 * Each level is a set of colored polygons forming a picture.
 */
public class LevelGenerator {

    private static final List<LevelData> levels = new ArrayList<>();

    static {
        levels.add(createFlowerLevel());
        levels.add(createHouseLevel());
        levels.add(createLandscapeLevel());
    }

    public static List<LevelData> getAllLevels() {
        return Collections.unmodifiableList(levels);
    }

    public static LevelData getLevel(int index) {
        // Return a fresh copy so levels can be replayed
        return switch (index) {
            case 0 -> createFlowerLevel();
            case 1 -> createHouseLevel();
            case 2 -> createLandscapeLevel();
            default -> createFlowerLevel();
        };
    }

    // =========================================================================
    // LEVEL 1: Flower — Easy (8 regions, 4 colors)
    // =========================================================================
    private static LevelData createFlowerLevel() {
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

        // 5 petals (pentagons approximating ellipses)
        for (int p = 0; p < 5; p++) {
            double angle = Math.toRadians(p * 72 - 90);
            double px = cx + Math.cos(angle) * petalR;
            double py = cy + Math.sin(angle) * petalR;

            // Create petal shape as an elongated hexagon
            double[] xPts = new double[8];
            double[] yPts = new double[8];
            for (int i = 0; i < 8; i++) {
                double a = Math.toRadians(i * 45);
                double rx = 35, ry = 50;
                double localX = Math.cos(a) * rx;
                double localY = Math.sin(a) * ry;
                // Rotate to face outward
                double cos = Math.cos(angle);
                double sin = Math.sin(angle);
                xPts[i] = px + localX * cos - localY * sin;
                yPts[i] = py + localX * sin + localY * cos;
            }
            regions.add(new ColorRegion(id++, 1, xPts, yPts, palette.get(1)));
        }

        // Center circle (octagon)
        double[] cxPts = new double[12];
        double[] cyPts = new double[12];
        for (int i = 0; i < 12; i++) {
            double a = Math.toRadians(i * 30);
            cxPts[i] = cx + Math.cos(a) * centerR;
            cyPts[i] = cy + Math.sin(a) * centerR;
        }
        regions.add(new ColorRegion(id++, 2, cxPts, cyPts, palette.get(2)));

        // Stem (rectangle)
        regions.add(new ColorRegion(id++, 3,
                new double[]{242, 258, 258, 242},
                new double[]{235, 235, 420, 420},
                palette.get(3)));

        // Left leaf
        regions.add(new ColorRegion(id++, 3,
                new double[]{250, 200, 180, 200, 242},
                new double[]{320, 340, 310, 280, 300},
                palette.get(3)));

        // Right leaf
        regions.add(new ColorRegion(id++, 3,
                new double[]{250, 300, 320, 300, 258},
                new double[]{350, 370, 340, 310, 330},
                palette.get(3)));

        return new LevelData("Flower", "Easy", regions, palette, W, H);
    }

    // =========================================================================
    // LEVEL 2: House — Medium (15 regions, 6 colors)
    // =========================================================================
    private static LevelData createHouseLevel() {
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

        // Sky
        regions.add(new ColorRegion(id++, 6,
                new double[]{0, 600, 600, 0},
                new double[]{0, 0, 320, 320},
                palette.get(6)));

        // Grass
        regions.add(new ColorRegion(id++, 5,
                new double[]{0, 600, 600, 0},
                new double[]{320, 320, 500, 500},
                palette.get(5)));

        // Main wall
        regions.add(new ColorRegion(id++, 2,
                new double[]{150, 450, 450, 150},
                new double[]{180, 180, 380, 380},
                palette.get(2)));

        // Roof (triangle)
        regions.add(new ColorRegion(id++, 1,
                new double[]{130, 300, 470},
                new double[]{180, 80, 180},
                palette.get(1)));

        // Door
        regions.add(new ColorRegion(id++, 3,
                new double[]{270, 330, 330, 270},
                new double[]{280, 280, 380, 380},
                palette.get(3)));

        // Left window
        regions.add(new ColorRegion(id++, 4,
                new double[]{180, 240, 240, 180},
                new double[]{220, 220, 270, 270},
                palette.get(4)));

        // Right window
        regions.add(new ColorRegion(id++, 4,
                new double[]{360, 420, 420, 360},
                new double[]{220, 220, 270, 270},
                palette.get(4)));

        // Chimney
        regions.add(new ColorRegion(id++, 1,
                new double[]{370, 400, 400, 370},
                new double[]{90, 90, 155, 155},
                palette.get(1)));

        // Left bush (pentagon)
        regions.add(new ColorRegion(id++, 5,
                new double[]{60, 90, 120, 110, 50},
                new double[]{380, 340, 380, 400, 400},
                palette.get(5)));

        // Right bush
        regions.add(new ColorRegion(id++, 5,
                new double[]{480, 510, 540, 530, 470},
                new double[]{380, 340, 380, 400, 400},
                palette.get(5)));

        // Sun (dodecagon)
        double sunCx = 520, sunCy = 60, sunR = 35;
        double[] sunX = new double[12];
        double[] sunY = new double[12];
        for (int i = 0; i < 12; i++) {
            double a = Math.toRadians(i * 30);
            sunX[i] = sunCx + Math.cos(a) * sunR;
            sunY[i] = sunCy + Math.sin(a) * sunR;
        }
        regions.add(new ColorRegion(id++, 2, sunX, sunY, Color.web("#F1C40F")));

        // Path from door to bottom
        regions.add(new ColorRegion(id++, 3,
                new double[]{270, 330, 340, 260},
                new double[]{380, 380, 500, 500},
                palette.get(3)));

        // Fence left
        regions.add(new ColorRegion(id++, 2,
                new double[]{20, 140, 140, 20},
                new double[]{350, 350, 365, 365},
                palette.get(2)));

        // Fence right
        regions.add(new ColorRegion(id++, 2,
                new double[]{460, 580, 580, 460},
                new double[]{350, 350, 365, 365},
                palette.get(2)));

        // Cloud 1
        regions.add(new ColorRegion(id++, 6,
                new double[]{80, 100, 130, 160, 180, 170, 130, 90},
                new double[]{50, 30, 25, 30, 50, 65, 65, 60},
                Color.web("#B0D4F1")));

        return new LevelData("Cozy House", "Medium", regions, palette, W, H);
    }

    // =========================================================================
    // LEVEL 3: Landscape — Hard (20+ regions, 7 colors)
    // =========================================================================
    private static LevelData createLandscapeLevel() {
        double W = 700, H = 500;
        Map<Integer, Color> palette = new LinkedHashMap<>();
        palette.put(1, Color.web("#2C3E50")); // Dark mountain
        palette.put(2, Color.web("#5D6D7E")); // Light mountain
        palette.put(3, Color.web("#27AE60")); // Green hills
        palette.put(4, Color.web("#2ECC71")); // Light green
        palette.put(5, Color.web("#3498DB")); // Water/lake
        palette.put(6, Color.web("#F39C12")); // Sun/orange
        palette.put(7, Color.web("#85C1E9")); // Sky

        List<ColorRegion> regions = new ArrayList<>();
        int id = 0;

        // Sky (split into 3 sections)
        regions.add(new ColorRegion(id++, 7,
                new double[]{0, 233, 233, 0},
                new double[]{0, 0, 200, 200},
                palette.get(7)));
        regions.add(new ColorRegion(id++, 7,
                new double[]{233, 467, 467, 233},
                new double[]{0, 0, 200, 200},
                palette.get(7)));
        regions.add(new ColorRegion(id++, 7,
                new double[]{467, 700, 700, 467},
                new double[]{0, 0, 200, 200},
                palette.get(7)));

        // Sun
        double sunCx2 = 600, sunCy2 = 80, sunR2 = 40;
        double[] sx = new double[16];
        double[] sy = new double[16];
        for (int i = 0; i < 16; i++) {
            double a = Math.toRadians(i * 22.5);
            sx[i] = sunCx2 + Math.cos(a) * sunR2;
            sy[i] = sunCy2 + Math.sin(a) * sunR2;
        }
        regions.add(new ColorRegion(id++, 6, sx, sy, palette.get(6)));

        // Far mountain left (dark)
        regions.add(new ColorRegion(id++, 1,
                new double[]{0, 100, 200, 300, 350, 300, 200, 100, 0},
                new double[]{250, 120, 100, 130, 200, 250, 250, 250, 250},
                palette.get(1)));

        // Far mountain right (dark)
        regions.add(new ColorRegion(id++, 1,
                new double[]{300, 400, 500, 580, 650, 700, 700, 300},
                new double[]{250, 130, 90, 120, 160, 200, 250, 250},
                palette.get(1)));

        // Near mountain left (lighter)
        regions.add(new ColorRegion(id++, 2,
                new double[]{0, 80, 180, 280, 350, 280, 0},
                new double[]{300, 200, 180, 220, 300, 300, 300},
                palette.get(2)));

        // Near mountain right (lighter)
        regions.add(new ColorRegion(id++, 2,
                new double[]{350, 450, 550, 630, 700, 700, 350},
                new double[]{300, 200, 190, 230, 280, 300, 300},
                palette.get(2)));

        // Green hills (left)
        regions.add(new ColorRegion(id++, 3,
                new double[]{0, 100, 200, 250, 200, 100, 0},
                new double[]{380, 310, 300, 340, 380, 380, 380},
                palette.get(3)));

        // Green hills (center)
        regions.add(new ColorRegion(id++, 4,
                new double[]{200, 300, 400, 500, 450, 350, 250},
                new double[]{380, 320, 310, 350, 380, 380, 380},
                palette.get(4)));

        // Green hills (right)
        regions.add(new ColorRegion(id++, 3,
                new double[]{450, 550, 650, 700, 700, 500},
                new double[]{380, 310, 300, 340, 380, 380},
                palette.get(3)));

        // Lake
        regions.add(new ColorRegion(id++, 5,
                new double[]{150, 250, 350, 450, 550, 500, 400, 300, 200},
                new double[]{400, 385, 380, 385, 400, 430, 440, 440, 430},
                palette.get(5)));

        // Foreground grass (left)
        regions.add(new ColorRegion(id++, 4,
                new double[]{0, 150, 200, 150, 0},
                new double[]{380, 400, 440, 500, 500},
                palette.get(4)));

        // Foreground grass (right)
        regions.add(new ColorRegion(id++, 4,
                new double[]{550, 600, 700, 700, 550},
                new double[]{400, 380, 380, 500, 500},
                palette.get(4)));

        // Tree 1 trunk
        regions.add(new ColorRegion(id++, 1,
                new double[]{85, 95, 95, 85},
                new double[]{340, 340, 390, 390},
                palette.get(1)));

        // Tree 1 foliage (triangle)
        regions.add(new ColorRegion(id++, 3,
                new double[]{60, 90, 120},
                new double[]{340, 280, 340},
                palette.get(3)));

        // Tree 2 trunk
        regions.add(new ColorRegion(id++, 1,
                new double[]{625, 635, 635, 625},
                new double[]{330, 330, 380, 380},
                palette.get(1)));

        // Tree 2 foliage
        regions.add(new ColorRegion(id++, 3,
                new double[]{600, 630, 660},
                new double[]{330, 270, 330},
                palette.get(3)));

        // Small cloud 1
        regions.add(new ColorRegion(id++, 7,
                new double[]{120, 150, 190, 220, 210, 160, 130},
                new double[]{60, 40, 38, 55, 70, 72, 68},
                Color.web("#BDE0F5")));

        // Small cloud 2
        regions.add(new ColorRegion(id++, 7,
                new double[]{330, 360, 400, 430, 420, 370, 340},
                new double[]{45, 28, 25, 42, 58, 60, 55},
                Color.web("#BDE0F5")));

        // Bottom ground
        regions.add(new ColorRegion(id++, 4,
                new double[]{0, 700, 700, 0},
                new double[]{440, 440, 500, 500},
                palette.get(4)));

        return new LevelData("Mountain Lake", "Hard", regions, palette, W, H);
    }
}
