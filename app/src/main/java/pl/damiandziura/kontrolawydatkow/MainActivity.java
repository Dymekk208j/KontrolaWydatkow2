package pl.damiandziura.kontrolawydatkow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private Intent intent;

    private baza_danych BazaDanych;
    TextView OstatniWydatek[] = new TextView[10];
    String sOstatniWydatek[] = new String[10];

    TextView OstatniDochod[] = new TextView[10];
    String sOstatniDochod[] = new String[10];
    TextView PosiadaneSrodki;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Aplikacja do kontroli wydatków");
        setContentView(R.layout.activity_main);

        BazaDanych = new baza_danych(this);

        BazaDanych.PortfelUstawSaldo(BazaDanych.PortfelPrzeliczSaldo());
        PosiadaneSrodki = (TextView) findViewById(R.id.textView);
        String kwotaa = String.format("%.2f",BazaDanych.getPortfelSaldo());
        PosiadaneSrodki.setText("Saldo konta: " + kwotaa + "zł");

        OstatniWydatek[0] = (TextView) findViewById(R.id.TxtLastExp0);
        OstatniWydatek[1] = (TextView) findViewById(R.id.TxtLastExp1);
        OstatniWydatek[2] = (TextView) findViewById(R.id.TxtLastExp2);
        OstatniWydatek[3] = (TextView) findViewById(R.id.TxtLastExp3);
        OstatniWydatek[4] = (TextView) findViewById(R.id.TxtLastExp4);
        OstatniWydatek[5] = (TextView) findViewById(R.id.TxtLastExp5);
        OstatniWydatek[6] = (TextView) findViewById(R.id.TxtLastExp6);
        OstatniWydatek[7] = (TextView) findViewById(R.id.TxtLastExp7);
        OstatniWydatek[8] = (TextView) findViewById(R.id.TxtLastExp8);
        OstatniWydatek[9] = (TextView) findViewById(R.id.TxtLastExp9);

        OstatniDochod[0] = (TextView) findViewById(R.id.TxtOstatniDochod0);
        OstatniDochod[1] = (TextView) findViewById(R.id.TxtOstatniDochod1);
        OstatniDochod[2] = (TextView) findViewById(R.id.TxtOstatniDochod2);
        OstatniDochod[3] = (TextView) findViewById(R.id.TxtOstatniDochod3);
        OstatniDochod[4] = (TextView) findViewById(R.id.TxtOstatniDochod4);
        OstatniDochod[5] = (TextView) findViewById(R.id.TxtOstatniDochod5);
        OstatniDochod[6] = (TextView) findViewById(R.id.TxtOstatniDochod6);
        OstatniDochod[7] = (TextView) findViewById(R.id.TxtOstatniDochod7);
        OstatniDochod[8] = (TextView) findViewById(R.id.TxtOstatniDochod8);
        OstatniDochod[9] = (TextView) findViewById(R.id.TxtOstatniDochod9);

        sOstatniWydatek = BazaDanych.getOstatnieWydatki();
        sOstatniDochod = BazaDanych.getOstatnieDochody();

        for(int a = 0; a < 10; a++)
        {
            OstatniWydatek[a].setText(sOstatniWydatek[a]);
            OstatniDochod[a].setText(sOstatniDochod[a]);
        }
    }

    public void dodajWydatek(View view) {
        intent = new Intent(this, AddNewExpenses.class);
        startActivity(intent);
    }

    public void dodajdochod(View view) {
        intent = new Intent(this, nowyPrzychod.class);
        startActivity(intent);
    }

    public void kategorie(View view) {
        intent = new Intent(this, Kategorie.class);
        startActivity(intent);
    }

    public void Informacje(View view) {
        intent = new Intent(this, Informacje.class);
        startActivity(intent);
    }


}
