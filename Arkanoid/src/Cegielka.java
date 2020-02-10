import java.awt.*;
import java.util.Random;

class Cegielka extends Prostokacik {

    private boolean czyZniszczone = false;

    public Cegielka(double x, double y, double szerokoscCegla, double wysokoscCegla) {

        this.x = x;
        this.y = y;
        this.sizeX = szerokoscCegla;
        this.sizeY = wysokoscCegla;

    }

    public void draw(Graphics g) {
        g.setColor(losujKolor());
        g.fillRect((int) left(), (int) top(), (int) sizeX, (int) sizeY);
    }

    public void czyZniszczyc(boolean temp) {

        czyZniszczone = temp;

    }

    public boolean czyZniszczony() {
        return czyZniszczone;
    }

    final Random r = new Random();

    private Color losujKolor() {
        Color[] temp = new Color[]{Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.MAGENTA, Color.DARK_GRAY};

        return temp[r.nextInt(7)];
    }

}