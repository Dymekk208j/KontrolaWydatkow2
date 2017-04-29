package pl.damiandziura.kontrolawydatkow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class CykliczneWydatki extends AppCompatActivity {
    private baza_danych BazaDanych;
    private ListView listView;
    private Intent intent;
    private ArrayList<Integer> idListCyclicalWydatkow;
    private int idCyclicalWydatku = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cykliczne_wydatki);
        setTitle(getResources().getString(R.string.str_lista_cykl_wydatkow));
        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);
        BazaDanych = new baza_danych(this);


        ArrayList<String> lista = BazaDanych.getNameListOfCyclilcalExpenses();
        idListCyclicalWydatkow = BazaDanych.getIdListOfCyclicalExpenses();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, lista);


        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                idCyclicalWydatku = idListCyclicalWydatkow.get(position);
                edytujStalyWydatek();
            }

        });
    }

    private void edytujStalyWydatek()
    {
        intent = new Intent(this, dodajStalyWydatek.class);
        intent.putExtra("IdStalegoWydatku", idCyclicalWydatku);
        intent.putExtra("edycja", true);
        intent.putExtra("nazwa_okna", getResources().getString(R.string.str_edycja_cyklicznego_wydatku));
        startActivity(intent);
    }

    public void cofnij(View view) {
        Intent intent = new Intent(this, AddNewExpenses.class);
        startActivity(intent);
    }

    public void dodaj(View view) {
        Intent intent = new Intent(this, dodajStalyWydatek.class);
        intent.putExtra("nazwa_okna", getResources().getString(R.string.str_dodaj_cykliczny_wydatek));
        intent.putExtra("edycja", false);
        startActivity(intent);
    }



}