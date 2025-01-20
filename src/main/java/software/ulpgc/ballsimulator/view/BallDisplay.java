package software.ulpgc.ballsimulator.view;

import java.util.List;

public interface BallDisplay {
    void draw(List<Circle> circles);
    void on(Released released);
    void on(Grabbed grabbed);

    record Circle(String id, int x, int y, int radius) {
        public boolean isAt(int x, int y) {
            return calculateDistance(x, y) <= radius;
        }

        private double calculateDistance(int x, int y) {
            return Math.sqrt(Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2));
        }
    }

    interface Released {
        void at(Circle circle);
    }

    interface Grabbed {
        void at(Circle circle);
    }
}
