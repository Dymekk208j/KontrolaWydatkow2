package pl.damiandziura.kontrolawydatkow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class dodajStalyWydatek extends AppCompatActivity {

    private Intent intent;
    private Spinner spinKategorie, spinPodkategorie;
    private baza_danych BazaDanych;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodaj_staly_wydatek);
        spinKategorie = (Spinner) findViewById(R.id.SpinnerKategoria);
        spinPodkategorie = (Spinner) findViewById(R.id.SpinnerPodKategoria);
        BazaDanych = new baza_danych(this);
        kategoria();
    }

    public void cofnij(View view) {
        intent = new Intent(this, AddNewExpenses.class);
        startActivity(intent);
    }

    public void dodaj(View view) {

    }

    public void wyczysc(View view) {

    }

    private void kategoria()    {
        ArrayList<String> lista = BazaDanych.getKategorie();

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lista);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinKategorie.setAdapter(adapter);

        spinKategorie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Podkategoria(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });

        // Toast.makeText(this, lista.get(1), Toast.LENGTH_SHORT).show();
        //podkategoria();
    }



    private void Podkategoria(int numer)    {
        numer++;
        ArrayList<String> lista = BazaDanych.getpodKategorie(numer);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lista);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinPodkategorie.setAdapter(adapter);
    }
}
