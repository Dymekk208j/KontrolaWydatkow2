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

public class Kategorie extends AppCompatActivity {
    baza_danych BazaDanych;
    ListView listView;
    Intent intent;
    ArrayList<Integer> idListKategorii;
    int idKategorii = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategorie);

        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);
        BazaDanych = new baza_danych(this);


        ArrayList<String> lista = BazaDanych.getKategorie();
        lista.remove(0);

        idListKategorii = BazaDanych.getINTKategorie();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, lista);


        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                idKategorii = idListKategorii.get(position+1);
                edytujKat(position);
            }

        });
    }

    private void edytujKat(int pozycja)
    {
        intent = new Intent(this, DodajKategorie.class);
        intent.putExtra("IdKategorii", idKategorii);

        intent.putExtra("nazwa_okna", "Edycja kategorii:");
        startActivity(intent);
    }

    public void cofnij(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void dodaj(View view) {
        Intent intent = new Intent(this, DodajKategorie.class);
        intent.putExtra("nazwa_okna", "Dodaj nową kategorie:");
        startActivity(intent);
    }




}