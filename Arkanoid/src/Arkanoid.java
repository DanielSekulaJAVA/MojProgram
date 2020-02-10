import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Arkanoid extends JFrame implements KeyListener {

    private final double predkoscKuleczki = 0.5;
    private final int liczCegielkiX = 20;
    private final int liczCegielkiY = 10;

    private final int szerokoscEkranu;
    private final int wysokoscEkranu;

    private final int zycia = 3;

    private boolean running = false;
    private boolean sprobujPonownie = false;
    private double lastFt;
    private double currentSlice;

    private final ZarzadcaPunkcikow statystyki;
    private final Kuleczka pileczka;
    private final Platforma deseczka;

    private int miliCzas = 0;

    private int minuty = 0;
    private int godziny = 0;

    private int przeskokY = 1;
    private int przeskokX = 1;

    private int playLicznik = 0;

    private int iloscCegielOst = 0;


    private final List<Cegielka> cegielki = new ArrayList<>();


    public Arkanoid(int szerokoscOkna, int wysokoscOkna) {

        szerokoscEkranu = szerokoscOkna;
        wysokoscEkranu = wysokoscOkna;

        ustawpoziom();


        statystyki = new ZarzadcaPunkcikow(szerokoscEkranu, wysokoscEkranu, obliczCegly(), zycia);

        deseczka = new Platforma(szerokoscEkranu, wysokoscEkranu);

        pileczka = new Kuleczka(szerokoscEkranu, wysokoscEkranu, predkoscKuleczki);


        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(szerokoscEkranu, wysokoscEkranu);
        this.setVisible(true);
        this.addKeyListener(this);
        this.setLocationRelativeTo(null);

        this.createBufferStrategy(2);

        inicjalizujCegielki(cegielki);

    }


    private boolean przecinanie(AbstraktInterfejs elementPierwszy, AbstraktInterfejs elementDrugi) {
        return !(elementPierwszy.right() >= elementDrugi.left()) || !(elementPierwszy.left() <= elementDrugi.right())
                || !(elementPierwszy.bottom() >= elementDrugi.top()) || !(elementPierwszy.top() <= elementDrugi.bottom());
    }

    private void kolizja(Platforma elementDeska, Kuleczka elementPilka) {
        if (przecinanie(elementDeska, elementPilka))
            return;
        elementPilka.ustawPredkoscY(-predkoscKuleczki);
        if (elementPilka.zwrocX() < elementDeska.x)
            elementPilka.ustawPredkoscX(-predkoscKuleczki);
        else
            elementPilka.ustawPredkoscX(predkoscKuleczki);
    }

    private void kolizja(Cegielka elementcegielka, Kuleczka elementPilka, ZarzadcaPunkcikow statystyki) {
        if (przecinanie(elementcegielka, elementPilka))
            return;

        elementcegielka.czyZniszczyc(true);

        statystyki.zwiekszWynik();

        double overlapLeft = elementPilka.right() - elementcegielka.left();
        double overlapRight = elementcegielka.right() - elementPilka.left();
        double overlapTop = elementPilka.bottom() - elementcegielka.top();
        double overlapBottom = elementcegielka.bottom() - elementPilka.top();

        boolean ballFromLeft = overlapLeft < overlapRight;
        boolean ballFromTop = overlapTop < overlapBottom;

        double minOverlapX = ballFromLeft ? overlapLeft : overlapRight;
        double minOverlapY = ballFromTop ? overlapTop : overlapBottom;

        if (minOverlapX < minOverlapY) {
            elementPilka.ustawPredkoscX(ballFromLeft ? -predkoscKuleczki : predkoscKuleczki);
        } else {
            elementPilka.ustawPredkoscY(ballFromTop ? -predkoscKuleczki : predkoscKuleczki);
        }
    }

    private void inicjalizujCegielki(List<Cegielka> bricks) {

        bricks.clear();


        for (int iX = 0; iX < liczCegielkiX; iX = iX + przeskokY) {
            for (int iY = 0; iY < liczCegielkiY; iY = iY + przeskokX) {
                double wysokoscCegielki = 25.0;
                double szerokoscCegielki = 50.0;
                bricks.add(new Cegielka((iX + 1) * (szerokoscCegielki + 3) + 32,
                        (iY + 2) * (wysokoscCegielki + 3) + 30, szerokoscCegielki, wysokoscCegielki));
            }
        }

        iloscCegielOst = bricks.size();
    }


    public void run() {


        BufferStrategy bf = this.getBufferStrategy();
        Graphics g = bf.getDrawGraphics();

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        running = true;

        while (running) {

            long time1 = System.currentTimeMillis();

            if (!statystyki.zwrocCzyKoniecGry() && !statystyki.zwrocCzyWygrales()) {
                sprobujPonownie = false;
                update();
                rysujScene(pileczka, cegielki, statystyki);

                try {
                    Thread.sleep(20);
                    czas(miliCzas++);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } else {
                if (sprobujPonownie) {
                    if (statystyki.zwrocCzyWygrales()) {

                        ++playLicznik;

                    } else {

                        playLicznik = 0;

                    }
                    ustawpoziom();
                    sprobujPonownie = false;
                    inicjalizujCegielki(cegielki);
                    statystyki.ustawIloscCegiel(iloscCegielOst);
                    statystyki.ustawIloscZyc(zycia);

                    if (playLicznik > 0) {

                        statystyki.kolejnyLevel();

                    } else {

                        statystyki.ustawPunkty(0);

                    }

                    statystyki.ustawWygrana(false);
                    statystyki.ustawKoniecGry(false);
                    statystyki.updateScoreboard();
                    pileczka.ustawX(szerokoscEkranu / 2);
                    pileczka.ustawY(wysokoscEkranu / 2);
                    deseczka.x = szerokoscEkranu / 2;
                    minuty = 0;
                    godziny = 0;
                }
            }

            long time2 = System.currentTimeMillis();
            double czasUplynal = time2 - time1;

            lastFt = czasUplynal;

            double seconds = czasUplynal / 1000.0;
            if (seconds > 0.0) {

                this.setTitle("Arkanoid 2020 Czas poziomu: " + godziny + " : " + minuty);

            }


        }

        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));

    }

    private void update() {

        currentSlice += lastFt;

        double predkoscRozgrywki = 1.0;
        for (; currentSlice >= predkoscRozgrywki; currentSlice -= predkoscRozgrywki) {

            pileczka.update(statystyki, deseczka);
            deseczka.update();
            kolizja(deseczka, pileczka);

            Iterator<Cegielka> it = cegielki.iterator();
            while (it.hasNext()) {
                Cegielka brick = it.next();
                kolizja(brick, pileczka, statystyki);
                if (brick.czyZniszczony()) {
                    it.remove();
                }
            }

        }
    }

    private void rysujScene(Kuleczka ball, List<Cegielka> bricks, ZarzadcaPunkcikow scoreboard) {

        BufferStrategy bf = this.getBufferStrategy();
        Graphics g = null;

        try {

            g = bf.getDrawGraphics();

            g.setColor(Color.black);
            g.fillRect(0, 0, getWidth(), getHeight());

            ball.rysuj(g);
            deseczka.draw(g);
            for (Cegielka brick : bricks) {
                brick.draw(g);
            }
            scoreboard.rysujTekst(g);

        } finally {
            assert g != null;
            g.dispose();
        }

        bf.show();

        Toolkit.getDefaultToolkit().sync();

    }

    @Override
    public void keyPressed(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.VK_L) {
            statystyki.ustawWygrana(true);
            sprobujPonownie = true;
            statystyki.ustawPunkty(iloscCegielOst);
            statystyki.updateScoreboard();
        }

        if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
            running = false;
        }
        if (event.getKeyCode() == KeyEvent.VK_ENTER) {
            sprobujPonownie = true;
        }
        switch (event.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                deseczka.moveLeft();
                break;
            case KeyEvent.VK_RIGHT:
                deseczka.moveRight();
                break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_RIGHT:
                deseczka.stopMove();
                break;
            default:
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent arg0) {

    }


    public void czas(int temp) {

        if (temp == 50) {
            ++minuty;
            miliCzas = 0;
        }
        if (minuty == 60) {
            ++godziny;
            minuty = 0;
            miliCzas = 0;
        }
        if (godziny == 60) {
            godziny = 0;
            minuty = 0;
            miliCzas = 0;

        }
    }

    public void ustawpoziom() {
        if (playLicznik == 1) {
            przeskokX = 2;
            przeskokY = 1;
        } else if (playLicznik == 2) {
            przeskokX = 1;
            przeskokY = 1;
        } else {
            przeskokY = 2;
            przeskokX = 2;
            playLicznik = 0;
        }
    }


    public int obliczCegly() {
        int temp = 0;

        for (int iX = 0; iX < liczCegielkiX; iX = iX + przeskokY) {
            for (int iY = 0; iY < liczCegielkiY; iY = iY + przeskokX) {
                ++temp;
            }
        }
        iloscCegielOst = temp;
        return temp;
    }


}
