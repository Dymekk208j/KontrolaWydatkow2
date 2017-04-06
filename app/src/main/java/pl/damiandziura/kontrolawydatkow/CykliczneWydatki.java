package pl.damiandziura.kontrolawydatkow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;

public class CykliczneWydatki extends AppCompatActivity {
    baza_danych BazaDanych;
    ListView listView;
    Intent intent;
    ArrayList<Integer> idListStalychWydatkow;
    int idStalegoWydatku = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cykliczne_wydatki);
        setTitle("Lista cyklicznych wydatków");
        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);
        BazaDanych = new baza_danych(this);


        ArrayList<String> lista = BazaDanych.getStaleWydatki();
        idListStalychWydatkow = BazaDanych.getINTstaleWydatki();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, lista);


        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                idStalegoWydatku = idListStalychWydatkow.get(position);
                edytujStalyWydatek(position+1);
            }

        });
    }

    private void edytujStalyWydatek(int pozycja)
    {
        intent = new Intent(this, dodajStalyWydatek.class);
        intent.putExtra("IdStalegoWydatku", idStalegoWydatku);
        intent.putExtra("edycja", true);
        intent.putExtra("nazwa_okna", "Edycja cyklicznego wydatku:");
        startActivity(intent);
    }

    public void cofnij(View view) {
     //   Intent intent = new Intent(this, MainActivity.class);
       // startActivity(intent);
        Intent intent = new Intent(this, AddNewExpenses.class);
        startActivity(intent);
    }

    public void dodaj(View view) {
        Intent intent = new Intent(this, dodajStalyWydatek.class);
        intent.putExtra("nazwa_okna", "Dodaj nowy cykliczny wydatek:");
        intent.putExtra("edycja", false);
        startActivity(intent);
    }




}