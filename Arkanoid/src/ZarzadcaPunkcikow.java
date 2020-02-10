import java.awt.*;

class ZarzadcaPunkcikow {

    private int ileCegielek;

    private final int szerokoscEkranu;
    private final int wysokoscEkranu;

    private int wynik = 0;
    private int lives;
    private boolean wygrana = false;
    private boolean koniecGry = false;
    private String text;

    private Font font;

    public ZarzadcaPunkcikow(int szerokoscOkna, int wysokoscOkna, int iloscBlokow, int iloscZyc) {

        szerokoscEkranu = szerokoscOkna;
        wysokoscEkranu = wysokoscOkna;
        ileCegielek = iloscBlokow;
        lives = iloscZyc;


        String FONT = "TimesRoman";
        font = new Font(FONT, Font.PLAIN, 12);
        text = "Arkanoid 2020";
    }

    public void zwiekszWynik() {
        wynik++;
        if (wynik == ileCegielek) {
            wygrana = true;
            text = "Wygrałeś! \nTwój wynik to: " + wynik
                    + "\n\nWciśnij Enter aby grać dalej";
        } else {
            updateScoreboard();
        }
    }

    public void kuniecGry() {
        lives--;
        if (lives == 0) {
            koniecGry = true;
            text = "Przegrałeś! \nTwój wynik to: " + wynik
                    + "\n\nWciśnij Enter aby zagrać ponownie";
        } else {
            updateScoreboard();
        }
    }

    public void updateScoreboard() {

        text = "Wynik: " + wynik + "  Życia: " + lives;
    }

    public void rysujTekst(Graphics g) {
        if (wygrana || koniecGry) {
            font = font.deriveFont(50f);
            FontMetrics fontMetrics = g.getFontMetrics(font);
            g.setColor(Color.WHITE);
            g.setFont(font);
            int wysokoscTytulu = fontMetrics.getHeight();
            int przesuniecieLini = 3;
            for (String line : text.split("\n")) {
                int dlugoscTytulu = fontMetrics.stringWidth(line);
                g.drawString(line, (szerokoscEkranu / 2) - (dlugoscTytulu / 2),
                        (wysokoscEkranu / 4) + (wysokoscTytulu * przesuniecieLini));
                przesuniecieLini++;

            }
        } else {
            font = font.deriveFont(34f);
            FontMetrics fontMetrics = g.getFontMetrics(font);
            g.setColor(Color.BLUE);
            g.setFont(font);
            int dlugoscTytulu = fontMetrics.stringWidth(text);
            int wysokoscTytulu = fontMetrics.getHeight();
            g.drawString(text, (szerokoscEkranu / 2) - (dlugoscTytulu / 2),
                    (wysokoscEkranu / 2) + (wysokoscTytulu));

        }
    }

    public void ustawIloscZyc(int temp) {

        lives = temp;

    }

    public void ustawPunkty(int temp) {

        wynik = temp;
    }

    public void ustawWygrana(boolean temp) {

        wygrana = temp;
    }

    public void ustawKoniecGry(boolean temp) {

        koniecGry = temp;
    }

    public void ustawIloscCegiel(int temp) {

        ileCegielek = temp;
    }

    public void kolejnyLevel() {

        ileCegielek += wynik;

    }


    public boolean zwrocCzyWygrales() {

        return wygrana;
    }

    public boolean zwrocCzyKoniecGry() {

        return koniecGry;
    }

}
