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
    private ArrayList<Integer> idListCyclicalIncome;
    private int idCyclicalIncomeu = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cykliczny_dochod);
        setTitle(getResources().getString(R.string.str_lista_cykl_dochodow));
        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);
        BazaDanych = new baza_danych(this);


        ArrayList<String> lista = BazaDanych.getNameListOfCyclicalIncome();
        idListCyclicalIncome = BazaDanych.getIdListOfCyclicalIncome();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, lista);


        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                idCyclicalIncomeu = idListCyclicalIncome.get(position);
                edytujCyclicalIncome();
            }

        });
    }

    private void edytujCyclicalIncome()
    {
        intent = new Intent(this, dodajStalydochod.class);
        intent.putExtra("IdStalegoWydatku", idCyclicalIncomeu);
        intent.putExtra("edycja", true);
        intent.putExtra("nazwa_okna", getResources().getString(R.string.str_edycja_cyklicznego_dochodu));
        startActivity(intent);
    }

    public void cofnij(View view) {
        Intent intent = new Intent(this, nowyPrzychod.class);
        startActivity(intent);
    }

    public void dodaj(View view) {
        Intent intent = new Intent(this, dodajStalydochod.class);
        intent.putExtra("nazwa_okna", getResources().getString(R.string.str_dodaj_cykliczny_dochod));
        intent.putExtra("edycja", false);
        startActivity(intent);
    }


}
