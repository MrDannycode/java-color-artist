package com.example.colorartist.patterns.observer;

/**
 * Interfata pentru observatori in sablonul OBSERVER (Behavioral).
 */
public interface GameObserver {
    void onGameStateChanged(int coloredRegions, int totalRegions, boolean isComplete);
}
