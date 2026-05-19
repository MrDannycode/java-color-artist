package com.example.colorartist.patterns.command;

/**
 * Interfata pentru sablonul COMMAND (Behavioral).
 */
public interface Command {
    void execute();
    void undo();
}
