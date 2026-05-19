package com.example.colorartist.levels;

import com.example.colorartist.model.LevelData;
import com.example.colorartist.patterns.factory.FlowerLevelFactory;
import com.example.colorartist.patterns.factory.HouseLevelFactory;
import com.example.colorartist.patterns.factory.LandscapeLevelFactory;
import com.example.colorartist.patterns.factory.LevelFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Gestioneaza nivelurile folosind Factory Method (Creational).
 */
public class LevelGenerator {

    // Tinem o lista de fabrici, nu de niveluri statice,
    // astfel incat fiecare "createLevel()" va genera o instanta noua, proaspata.
    private static final List<LevelFactory> levelFactories = new ArrayList<>();

    static {
        levelFactories.add(new FlowerLevelFactory());
        levelFactories.add(new HouseLevelFactory());
        levelFactories.add(new LandscapeLevelFactory());
    }

    public static List<LevelData> getAllLevels() {
        List<LevelData> previews = new ArrayList<>();
        for (LevelFactory factory : levelFactories) {
            previews.add(factory.createLevel());
        }
        return Collections.unmodifiableList(previews);
    }

    public static LevelData getLevel(int index) {
        if (index < 0 || index >= levelFactories.size()) {
            return levelFactories.get(0).createLevel(); // Default la primul
        }
        // Aici este vizibil sablonul Factory Method in actiune
        return levelFactories.get(index).createLevel();
    }
}
