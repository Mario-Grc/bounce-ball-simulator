package software.ulpgc.ballsimulator.app;

import software.ulpgc.ballsimulator.view.BallDisplay;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final BallDisplay ballDisplay;

    public MainFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Ball Simulator");
        this.setSize(1280, 720);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.add((Component) (ballDisplay = createBallDisplay()));
    }

    private SwingBallDsiplay createBallDisplay() {
        return new SwingBallDsiplay();
    }

    public BallDisplay getBallDisplay() {
        return ballDisplay;
    }
}
