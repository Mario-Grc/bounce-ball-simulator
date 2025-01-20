package software.ulpgc.ballsimulator.presenter;

import software.ulpgc.ballsimulator.BallSimlator;
import software.ulpgc.ballsimulator.model.Ball;
import software.ulpgc.ballsimulator.view.BallDisplay;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BallPresenter {
    private static final double DT = 0.001;
    private static final int PERIOD = (int)(DT * 1000);
    private static final double PIXELS_PER_METER = 5 / 0.2;

    private final Timer timer = new Timer();
    private final BallDisplay ballDisplay;
    private final BallSimlator ballSimulator;
    private List<Ball> balls;
    private Ball grabbedBall;

    public BallPresenter(BallDisplay ballDisplay) {
        this.ballDisplay = ballDisplay;
        this.ballSimulator = new BallSimlator(DT);
        this.balls = new ArrayList<>();

        setup();
    }

    private void setup() {
        this.ballDisplay.on(createGrabbed());
        this.ballDisplay.on(createReleased());
    }

    private BallDisplay.Released createReleased() {
        return _ -> grabbedBall = null;
    }

    private BallDisplay.Grabbed createGrabbed() {
        return circle -> grabbedBall = circleToBall(circle);
    }

    private Ball circleToBall(BallDisplay.Circle circle) {
        Ball ball = findBallById(circle.id());
        return new Ball(
                ball.id(),
                toMeters(circle.y()),
                toMeters(circle.x()),
                ball.radius(),
                0,
                ball.g(),
                ball.cr()
                );
    }

    private Ball findBallById(String id) {
        return balls.stream()
                .filter(b -> b.id().equals(id))
                .findFirst().orElse(null);
    }

    public BallPresenter add(Ball ball) {
        this.balls.add(ball);
        return this;
    }

    public void start() {
        timer.scheduleAtFixedRate(createTimerTask(), 0, PERIOD);
    }

    private TimerTask createTimerTask() {
        return new TimerTask() {
            @Override
            public void run() {
                simulateBalls();
                drawBalls();
            }
        };
    }

    private void drawBalls() {
        ballDisplay.draw(ballsToCircles(balls));
    }

    private List<BallDisplay.Circle> ballsToCircles(List<Ball> balls) {
        return balls.stream()
                .map(this::ballToCircle)
                .toList();
    }

    private BallDisplay.Circle ballToCircle(Ball ball) {
        return new BallDisplay.Circle(
                ball.id(),
                toPixels(ball.x()),
                toPixels(ball.h()),
                toPixels(ball.radius())
        );
    }

    private static double toMeters(int pixels) {
        return pixels / PIXELS_PER_METER;
    }

    private static int toPixels(double meters) {
        return (int)(meters * PIXELS_PER_METER);
    }

    private void simulateBalls() {
        balls = balls.stream()
                .map(this::simulateBall)
                .toList();
    }

    private Ball simulateBall(Ball ball) {
        if (grabbedBall != null && ball.id().equals(grabbedBall.id())) return grabbedBall;
        return ballSimulator.simulate(ball);
    }


}
