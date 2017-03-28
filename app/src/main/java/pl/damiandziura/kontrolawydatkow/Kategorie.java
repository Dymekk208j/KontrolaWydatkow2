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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategorie);

        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);
        BazaDanych = new baza_danych(this);

        ArrayList<String> lista = BazaDanych.getKategorie();
        lista.remove(0);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, lista);


        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                edytujKat(position);
            }

        });
    }

    private void edytujKat(int pozycja)
    {
        intent = new Intent(this, DodajKategorie.class);
        intent.putExtra("IdKategorii", pozycja+1);
        startActivity(intent);
    }

    public void cofnij(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void dodaj(View view) {
        Intent intent = new Intent(this, DodajKategorie.class);
        startActivity(intent);
    }




}