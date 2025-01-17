package software.ulpgc.ballsimulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SwingBallDsiplay extends JPanel implements BallDisplay{
    public static final int FLOOR_HEIGHT = 5;
    private Grabbed grabbed = null;
    private Released released = null;
    private Optional<Circle> currentCircle;
    private final List<Circle> circles;
    private int width;
    private int height;

    public SwingBallDsiplay(){
        this.circles = new ArrayList<>();
        this.addMouseListener(createMouseListener());
        this.addMouseMotionListener(createMotionListener());
    }

    private MouseListener createMouseListener(){
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (grabbed == null) return;
                currentCircle = findCircle(toReferenceX(e.getX()), toReferenceY(e.getY()));
                currentCircle.ifPresent(circle -> grabbed.at(circle));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(currentCircle.isEmpty()) return;
                currentCircle.ifPresent(circle -> released.at(createNewCircle(circle, e)));
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        };
    }

    private MouseMotionListener createMotionListener(){
        return new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if(grabbed == null || currentCircle.isEmpty()) return;
                currentCircle.ifPresent(circle -> grabbed.at(createNewCircle(circle, e)));
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        };
    }

    private Circle createNewCircle(Circle currentCircle, MouseEvent e) {
        return new Circle(
                currentCircle.id(),
                toReferenceX(e.getX()),
                toReferenceY(e.getY()),
                currentCircle.radius()
        );
    }

    private int toReferenceX(int x) {
        return x - width / 2;
    }

    private int toReferenceY(int y) {
        return height - y;
    }

    private Optional<Circle> findCircle(int x, int y) {
        synchronized (circles){
            return circles.stream()
                    .filter(c -> c.isAt(x, y))
                    .findFirst();
        }
    }

    @Override
    public void draw(List<Circle> circles) {
        synchronized (this.circles){
            this.circles.clear();
            this.circles.addAll(circles);
            repaint();
        }
    }

    @Override
    public void on(Released released) {
        this.released = released;
    }

    @Override
    public void on(Grabbed grabbed) {
        this.grabbed = grabbed;
    }

    @Override
    protected void paintComponent(Graphics g) {
        synchronized (this.circles){
            super.paintComponent(g);
            width = getWidth();
            height = getHeight() - FLOOR_HEIGHT;
            resetCanvas(g, width, height);
            circles.forEach(c -> drawCircle(g, c));
        }
    }

    private void drawCircle(Graphics g, Circle circle) {
        g.setColor(Color.RED);
        g.fillOval(
                width / 2 + circle.x() - circle.radius(),
                height - circle.y() - circle.radius(),
                circle.radius() * 2,
                circle.radius() * 2
        );
    }

    private void resetCanvas(Graphics g, int width, int height) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.WHITE);
        g.fillRect(0, height, width, height);
    }
}
