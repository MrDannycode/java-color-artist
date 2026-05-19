package com.example.colorartist.patterns.observer;

/**
 * Interfata pentru subiectul observabil in sablonul OBSERVER (Behavioral).
 */
public interface GameSubject {
    void attach(GameObserver observer);
    void detach(GameObserver observer);
    void notifyObservers();
}
