package pl.damiandziura.kontrolawydatkow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class CyklicznyDochod extends AppCompatActivity {

    private baza_danych BazaDanych;
    private ListView listView;
    private Intent intent;
    private ArrayList<Integer> idListStalychDochodow;
    private int idStalegodochodu = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cykliczny_dochod);
        setTitle("Lista cyklicznych dochodów");
        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);
        BazaDanych = new baza_danych(this);


        ArrayList<String> lista = BazaDanych.getStaleDochody();
        idListStalychDochodow = BazaDanych.getINTstaleDochody();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, lista);


        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                idStalegodochodu = idListStalychDochodow.get(position);
                edytujStalyDochod(position+1);
            }

        });
    }

    private void edytujStalyDochod(int pozycja)
    {
        intent = new Intent(this, dodajStalydochod.class);
        intent.putExtra("IdStalegoDochodu", idStalegodochodu);
        intent.putExtra("edycja", true);
        intent.putExtra("nazwa_okna", "Edycja cyklicznego dochodu:");
        startActivity(intent);
    }

    public void cofnij(View view) {
        Intent intent = new Intent(this, nowyPrzychod.class);
        startActivity(intent);
    }

    public void dodaj(View view) {
        Intent intent = new Intent(this, dodajStalydochod.class);
        intent.putExtra("nazwa_okna", "Dodaj nowy cykliczny dochod:");
        intent.putExtra("edycja", false);
        startActivity(intent);
    }


}
