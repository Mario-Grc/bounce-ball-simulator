package software.ulpgc.ballsimulator;

public class BallSimlator {
    private final double dt;

    public BallSimlator(double dt) {
        this.dt = dt;
    }

    public Ball simulate(Ball ball) {
        return new Ball(
                ball.id(),
                newHeightOf(ball),
                ball.x(),
                ball.radius(),
                newVelocityOf(ball),
                ball.g(),
                ball.cr()
        );
    }

    private double newHeightOf(Ball ball) {
        return willBounce(ball) ? newHeightAfterBounce(ball) : ball.h() + ball.velocity() * dt;
    }

    private double newHeightAfterBounce(Ball ball) {
        return ball.radius() + newVelocityAfterBounce(ball) * (dt - timeToBounce(ball));
    }

    private double newVelocityAfterBounce(Ball ball) {
        return -ball.cr() * (ball.velocity() + ball.g() * timeToBounce(ball));
    }

    private boolean willBounce(Ball ball) {
        return isFalling(ball) && dt > timeToBounce(ball);
    }

    private double timeToBounce(Ball ball) {
        return - (ball.h() - ball.radius()) / ball.velocity();
    }

    private boolean isFalling(Ball ball) {
        return ball.velocity() < 0;
    }

    private double newVelocityOf(Ball ball) {
        return willBounce(ball) ? newVelocityAfterBounce(ball):  ball.velocity() + ball.g() * dt;
    }
}
