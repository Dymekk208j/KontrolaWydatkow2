package pl.damiandziura.kontrolawydatkow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class DodajKategorie extends AppCompatActivity
{
    private Intent intent;
    private baza_danych BazaDanych;
    private EditText editName;

    private ArrayList<String> listaPodKat;
    private int IdKategorii = 0;
    private ArrayList<Integer> PodkategoriaIdList;
    private String Nazwa_okna = "";
    private String Nazwa_kategorii;
    private  Boolean EdycjaKategorii = false;
    private ArrayList<String> ListaPodkategori;
    private int KatWydatekDochod = 0; //0 - wydatek; 1 dochod
    private RadioButton RadioWydatek;
    RadioGroup radioWydatekDochod;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Button btUsun;
        ListView listView ;

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dodaj_kategorie);
        Bundle extras = getIntent().getExtras();
        btUsun = (Button) findViewById(R.id.btUsun);
        RadioWydatek = (RadioButton) findViewById(R.id.radioWydatek);


        radioWydatekDochod = (RadioGroup) findViewById(R.id.radioWydatekDochod);

        radioWydatekDochod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup arg0, int id) {
                test();
            }
        });


            if(extras != null)
        {
            KatWydatekDochod = extras.getInt("KatWydatekDochod");
            IdKategorii = extras.getInt("IdKategorii");
            Nazwa_okna = extras.getString("nazwa_okna");
            EdycjaKategorii = extras.getBoolean("edycja");
            Nazwa_kategorii = extras.getString("Nazwa_kategorii");

            String Buffor[] = extras.getStringArray("ListaPodkategori");

            if(Buffor == null)
            {
                ListaPodkategori = new ArrayList<>();
                ListaPodkategori.add(0, "(" + getResources().getString(R.string.str_domyślna_podkategoria)+ ")");
            }else
            {
                ListaPodkategori = new ArrayList<>(Arrays.asList(Buffor));
                ListaPodkategori.add(0, "(" + getResources().getString(R.string.str_domyślna_podkategoria)+ ")");
            }

        }


        editName = (EditText) findViewById(R.id.editText);
        BazaDanych = new baza_danych(this);


        if(!Nazwa_okna.equals("")) setTitle(Nazwa_okna);

        listView = (ListView) findViewById(R.id.LVlistaPodkategorii);

        if(EdycjaKategorii)//edycja kategorii
        {
            listaPodKat = BazaDanych.getNameListOfSubcategory(IdKategorii);
            listaPodKat.remove(0);
            PodkategoriaIdList = BazaDanych.getIdListOfSubcategory(IdKategorii);
            editName.setText(BazaDanych.getCategoryName(IdKategorii));
            btUsun.setVisibility(View.VISIBLE);
            KatWydatekDochod = BazaDanych.getTypeOfCategory(IdKategorii);
            if(KatWydatekDochod == 0)
            {
                //RadioWydatek.setSelected(true);
                radioWydatekDochod.check(R.id.radioWydatek);


            }else
            {
                radioWydatekDochod.check(R.id.radioDochod);
               // radioWydatekDochod.setSelected(false);
              //  RadioDochod.setSelected(true);
            }


        }else
        {
            listaPodKat = ListaPodkategori;
            listaPodKat.remove(0);
            btUsun.setVisibility(View.INVISIBLE);
        }

        if(Nazwa_kategorii != null) editName.setText(Nazwa_kategorii);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, listaPodKat);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {
                    edytujPodKat(position);


            }

        });

    }

    public void cofnij(View view) {
       intent = new Intent(this, Kategorie.class);
       startActivity(intent);
    }

    public void dodaj(View view)
    {

        if(EdycjaKategorii)
        {
            BazaDanych.UpdateCategory(editName.getText().toString(), IdKategorii, KatWydatekDochod);
            Toast.makeText(this, getResources().getString(R.string.str_kategoria) + editName.getText().toString() + " " + getResources().getString(R.string.str_zostala_zaktualizowana) + ".", Toast.LENGTH_SHORT).show();
            intent = new Intent(this, Kategorie.class);
            startActivity(intent);
        }
        else
        {
            BazaDanych.AddCategory(editName.getText().toString(), KatWydatekDochod);
            for(int a = 0; a < listaPodKat.size(); a++)
            {
                BazaDanych.AddSubcategory(listaPodKat.get(a), BazaDanych.getCategoryMaxId());
            }
            Toast.makeText(this, getResources().getString(R.string.str_nowa_kategoria) + editName.getText().toString() + " " + getResources().getString(R.string.str_zostala_dodana) + ".", Toast.LENGTH_SHORT).show();
            intent = new Intent(this, Kategorie.class);
            startActivity(intent);
        }

    }

    public void usun(View view)
    {
        Toast.makeText(this, getResources().getString(R.string.str_pomyslnie_usunieto_kategorie) + BazaDanych.getCategoryName(IdKategorii), Toast.LENGTH_SHORT).show();
        PodkategoriaIdList = BazaDanych.getIdListOfSubcategory(IdKategorii);
        for(int a = 0; a < PodkategoriaIdList.size(); a++)
        {
            BazaDanych.RemoveSubcategory(PodkategoriaIdList.get(a));
        }
        BazaDanych.RemoveCategory(IdKategorii);
        intent = new Intent(this, Kategorie.class);
        startActivity(intent);

    }

    public void dodajPodKat(View view)
    {
        Intent intent = new Intent(this, Dodajpodkategorie.class);

        intent.putExtra("KatWydatekDochod", KatWydatekDochod);

        if(EdycjaKategorii)
        {
            intent.putExtra("NumerKategorii", IdKategorii);
            intent.putExtra("NumerPodKategorii", 0);
            intent.putExtra("Nazwa_okna", getResources().getString(R.string.str_dodaj_podk));
            intent.putExtra("edycja", true);
            intent.putExtra("Nazwa_kategorii", editName.getText().toString());

        }
        else
        {
            intent.putExtra("NumerKategorii", 0);
            intent.putExtra("NumerPodKategorii", 0);
            String listapodkat[] = ListaPodkategori.toArray(new String[0]);
            intent.putExtra("Nazwa_okna", "Edytuj podkategorie");
            intent.putExtra("Nazwa_kategorii", editName.getText().toString());
            intent.putExtra("listaPodkategorii", listapodkat);
            intent.putExtra("edycja", false);
        }
        startActivity(intent);


    }

    public void edytujPodKat(int IdPodkategorii)
    {
        if(EdycjaKategorii)
        {
            Intent intent = new Intent(this, Dodajpodkategorie.class);
            intent.putExtra("KatWydatekDochod", KatWydatekDochod);
            intent.putExtra("NumerKategorii", IdKategorii);
            intent.putExtra("NumerPodKategorii", PodkategoriaIdList.get(IdPodkategorii+1));
            intent.putExtra("Nazwa_okna", getResources().getString(R.string.strEdycjaPodKategorii));
            intent.putExtra("Nazwa_kategorii", editName.getText().toString());
            intent.putExtra("edycja", true);
            startActivity(intent);
        }else
        {
            int bufor = IdPodkategorii+1;
            Intent intent = new Intent(this, Dodajpodkategorie.class);
            intent.putExtra("KatWydatekDochod", KatWydatekDochod);
            intent.putExtra("NumerKategorii", 0);
            intent.putExtra("NumerPodKategorii", bufor);
            intent.putExtra("Nazwa_okna", getResources().getString(R.string.strEdycjaPodKategorii));
            intent.putExtra("Nazwa_kategorii", editName.getText().toString());
            intent.putExtra("edycja", false);
            String listapodkat[] = ListaPodkategori.toArray(new String[0]);
            intent.putExtra("listaPodkategorii", listapodkat);

            startActivity(intent);
        }

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


    }


}
