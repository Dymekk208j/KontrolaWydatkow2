package pl.damiandziura.kontrolawydatkow;

/**
 * Created by Dymek on 19.03.2017.
 */

public class Portfel {
    private double dostepne_srodki = 0;

    public double getDostepne_srodki() {
        return dostepne_srodki;
    }

    public void dodaj_Dostepne_srodki(double kwota) {
        this.dostepne_srodki += dostepne_srodki;
    }

    public void zmniejsz_Dostepne_srodki(double kwota) {
        this.dostepne_srodki -= dostepne_srodki;
    }
}
