import java.awt.*;

class Platforma extends Prostokacik {

    private final double predkoscPlatformy = 0.8;

    private final int szerokoscEkranu;


    private double predkosc = 0.0;

    public Platforma(double x, double y) {

        szerokoscEkranu = (int) x;
        this.x = x / 2;
        this.y = y - 50;
        this.sizeX = 100.0;
        this.sizeY = 15.0;
    }

    public void update() {
        double krokPlatformy = 1.0;
        x += predkosc * krokPlatformy;
    }

    public void stopMove() {
        predkosc = 0.0;
    }

    public void moveLeft() {
        if (left() > 0.0) {
            predkosc = -predkoscPlatformy;
        } else {
            predkosc = 0.0;
        }
    }

    public void moveRight() {
        if (right() < szerokoscEkranu) {
            predkosc = predkoscPlatformy;
        } else {
            predkosc = 0.0;
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.CYAN);
        g.fillRect((int) (left()), (int) (top()), (int) sizeX, (int) sizeY);
    }

}