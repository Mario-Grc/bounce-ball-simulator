package software.ulpgc.ballsimulator;

import software.ulpgc.ballsimulator.app.MainFrame;
import software.ulpgc.ballsimulator.model.Ball;
import software.ulpgc.ballsimulator.presenter.BallPresenter;

public class Main {
    public static void main(String[] args) {
        MainFrame frame = new MainFrame();
        BallPresenter ballPresenter = new BallPresenter(frame.getBallDisplay())
                .add(new Ball("1", 20, -15, 1, 0, -9.8, 0.6))
                .add(new Ball("2", 20, 0, 2, 0, -9.8, 0.4))
                .add(new Ball("3", 20, 15, 0.7, 0, -7, 0.8));
        ballPresenter.start();
        frame.setVisible(true);
    }
}
