import java.awt.*;

class Kuleczka extends AbstraktInterfejs {


    private final double promien = 12.0;
    private final double predkoscKuleczki;


    private final int szerokoscEkranu;
    private final int wysokoscEkranu;


    private double x, y;
    private final double radius = promien;
    private double predkoscX;
    private double predkoscY;

    public Kuleczka(int x, int y, double predkosc) {
        this.x = x / 2;
        this.y = y / 2;

        szerokoscEkranu = x;
        wysokoscEkranu = y;
        predkoscKuleczki = predkosc;
        predkoscX = predkoscKuleczki;
        predkoscY = predkoscKuleczki;

    }

    public void rysuj(Graphics g) {

        g.setColor(Color.RED);
        g.fillOval((int) left(), (int) top(), (int) radius * 2,
                (int) radius * 2);
    }

    public void update(ZarzadcaPunkcikow scoreBoard, Platforma paddle) {

        double krokKuleczki = 1.0;
        x += predkoscX * krokKuleczki;
        y += predkoscY * krokKuleczki;

        if (left() < 0)
            predkoscX = predkoscKuleczki;
        else if (right() > szerokoscEkranu)
            predkoscX = -predkoscKuleczki;
        if (top() < 0) {
            predkoscY = predkoscKuleczki;
        } else if (bottom() > wysokoscEkranu) {
            predkoscY = -predkoscKuleczki;
            x = paddle.x;
            y = paddle.y - 50;
            scoreBoard.kuniecGry();
        }

    }

    public void ustawX(double temp) {

        x = temp;
    }

    public void ustawY(double temp) {

        y = temp;
    }


    public double zwrocX() {

        return x;
    }

    public void ustawPredkoscX(double temp) {

        predkoscX = temp;
    }

    public void ustawPredkoscY(double temp) {

        predkoscY = temp;
    }

    public double left() {

        return x - radius;
    }

    public double right() {

        return x + radius;
    }

    public double top() {

        return y - radius;
    }

    public double bottom() {

        return y + radius;
    }

}