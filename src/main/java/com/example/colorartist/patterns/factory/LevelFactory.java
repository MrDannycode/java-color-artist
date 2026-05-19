package com.example.colorartist.patterns.factory;

import com.example.colorartist.model.LevelData;

/**
 * Sablonul FACTORY METHOD (Creational).
 * Interfata comuna pentru toate clasele "fabricant" de niveluri.
 */
public interface LevelFactory {
    /**
     * Factory method care deleaga crearea efectiva a nivelului claselor derivate.
     */
    LevelData createLevel();
}
