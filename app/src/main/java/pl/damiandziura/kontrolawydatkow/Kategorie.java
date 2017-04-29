package pl.damiandziura.kontrolawydatkow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;



import java.util.ArrayList;

public class Kategorie extends AppCompatActivity {
    private baza_danych BazaDanych;
    private ListView listView;
    private ArrayList<Integer> idListKategorii;
    private int idKategorii = 0;
    private int KatWydatekDochod = 0; // 0-wydatek; 1-dochod
    private RadioButton RadioWydatek;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategorie);
        setTitle("Lista kategori");
        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);
        RadioWydatek = (RadioButton) findViewById(R.id.RadioWydatki);

        BazaDanych = new baza_danych(this);


        RadioGroup radioWydatekDochod = (RadioGroup) findViewById(R.id.RadioGrup);


        radioWydatekDochod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup arg0, int id) {
                test();
            }
        });

        ArrayList<String> lista = BazaDanych.getCategory(KatWydatekDochod);
        lista.remove(0);

        idListKategorii = BazaDanych.getIdListOfCategory(KatWydatekDochod);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, lista);


        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                idKategorii = idListKategorii.get(position+1);
                edytujKat();
            }

        });
    }

    private void edytujKat()
    {
        Intent intent = new Intent(this, DodajKategorie.class);
        intent.putExtra("IdKategorii", idKategorii);
        intent.putExtra("edycja", true);
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
        intent.putExtra("edycja", false);
        intent.putExtra("nazwa_okna", "Dodaj kategorie:");
        startActivity(intent);
    }

    private void test()
    {

        if(RadioWydatek.isChecked())
        {
            KatWydatekDochod = 0;
        }else
        {
            KatWydatekDochod = 1;
        }

        ArrayList<String> lista = BazaDanych.getCategory(KatWydatekDochod);
        lista.remove(0);

        idListKategorii = BazaDanych.getIdListOfCategory(KatWydatekDochod);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, lista);


        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                idKategorii = idListKategorii.get(position+1);
                edytujKat();
            }

        });


    }


}