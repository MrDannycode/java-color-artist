package com.example.colorartist;

import com.example.colorartist.levels.LevelGenerator;
import com.example.colorartist.patterns.singleton.GameManager;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;

public class VictoryController {

    @FXML
    private Label statsLabel;
    
    @FXML
    private Button nextLevelBtn;
    
    private int currentLevelIndex;

    public void setup(int levelIndex, int totalRegions) {
        this.currentLevelIndex = levelIndex;
        statsLabel.setText("Brilliantly colored " + totalRegions + " regions!");
        
        // Hide next button if there are no more levels
        if (levelIndex >= LevelGenerator.getAllLevels().size() - 1) {
            nextLevelBtn.setVisible(false);
            nextLevelBtn.setManaged(false);
        }
        
        // Simple entrance animation
        ScaleTransition st = new ScaleTransition(Duration.millis(800), statsLabel.getParent());
        st.setFromX(0.5);
        st.setFromY(0.5);
        st.setToX(1.0);
        st.setToY(1.0);
        
        // Bouncy easing (simulated)
        st.setInterpolator(javafx.animation.Interpolator.EASE_OUT);
        st.play();
    }

    @FXML
    protected void onMenuClick() {
        GameManager.getInstance().navigateToMenu();
    }

    @FXML
    protected void onNextLevelClick() {
        GameManager.getInstance().navigateToGame(currentLevelIndex + 1);
    }
}
