package pl.damiandziura.kontrolawydatkow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class Dodajpodkategorie extends AppCompatActivity {
    private baza_danych BazaDanych;
    private int IdKategorii =0;
    private int IdPodKategorii =0;
    private EditText NazwaPodkat;
    private String WindowName = "";
    private Button btUsun;
    private boolean EdycjaKategorii = false;
    private  ArrayList<String> ListaPodkategorii;
    private String BufforList[];
    private String Nazwa_kategorii;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodajpodkategorie);
        Bundle extras = getIntent().getExtras();
        BazaDanych = new baza_danych(this);
        NazwaPodkat = (EditText) findViewById(R.id.txtPodKatName);
        btUsun = (Button) findViewById(R.id.btUsun);


        if(extras != null)
        {

            IdKategorii = extras.getInt("NumerKategorii");
            IdPodKategorii = extras.getInt("NumerPodKategorii");
            WindowName = extras.getString("Nazwa_okna");
            EdycjaKategorii = extras.getBoolean("edycja");
            BufforList = extras.getStringArray("listaPodkategorii");
            Nazwa_kategorii = extras.getString("Nazwa_kategorii");

            if(BufforList != null) {
                ListaPodkategorii = new ArrayList<String>(Arrays.asList(BufforList));
            }else{
                ListaPodkategorii = new ArrayList<String>();
            }



        }
        if(IdPodKategorii == 0)//dodajesz nowa podkategorie
        {
            btUsun.setVisibility(View.INVISIBLE);

        }
        else//edytujesz istniejaca pod kategorie
        {
            if(EdycjaKategorii == true)//edytujesz podkategorie istniejaca w bazie danych
            {
                NazwaPodkat.setText(BazaDanych.getPodKategoriaName(IdPodKategorii));
            }
            else//edytujesz podkategorie jeszcze nie istniejaca w bazie danych
            {
                NazwaPodkat.setText(ListaPodkategorii.get(IdPodKategorii-1));
            }
            btUsun.setVisibility(View.VISIBLE);
        }
        //Toast.makeText(this, "NumerPodKategorii: " + IdPodKategorii, Toast.LENGTH_SHORT).show();
        if(!WindowName.equals(""))
        {
            setTitle(WindowName);
        }


    }

    public void cofnij(View view)
    {
        Intent intent = new Intent(this, DodajKategorie.class);
        intent.putExtra("IdKategorii", IdKategorii);
        intent.putExtra("Nazwa_kategorii", Nazwa_kategorii);

        if(EdycjaKategorii == true) //Dodawanie nowej podkategorii do istniejace kategorii
        {
            intent.putExtra("edycja", true);
            intent.putExtra("nazwa_okna", "Edycja kategorii:");
        }
        else
        {
            intent.putExtra("nazwa_okna", "Dodaj kategorie:");
            intent.putExtra("edycja", false);
            intent.putExtra("ListaPodkategori", BufforList);

        }

        startActivity(intent);

    }

    public void dodaj(View view)
    {
        if(EdycjaKategorii == true) //dzialam na istniejacej kategorii
        {
            Intent intent = new Intent(this, DodajKategorie.class);
            intent.putExtra("IdKategorii", IdKategorii);
            intent.putExtra("nazwa_okna", "Edycja kategorii:");
            intent.putExtra("edycja", true);
            intent.putExtra("Nazwa_kategorii", Nazwa_kategorii);

            if(IdPodKategorii == 0)//dodaje nowa podkategorie
            {
                if (!NazwaPodkat.getText().toString().equals("")) {
                    BazaDanych.AddPodKategoria(NazwaPodkat.getText().toString(), IdKategorii);
                    Toast.makeText(this, "Dodano nową podkategorie " + NazwaPodkat.getText().toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Pole nazwy nie może być puste!", Toast.LENGTH_SHORT).show();
                }
            }
            else//edytuje istniejaca podkategorie
            {
                if (!NazwaPodkat.getText().toString().equals("")) {
                    BazaDanych.updateNamePodkategoria(NazwaPodkat.getText().toString(), IdPodKategorii);
                    Toast.makeText(this, "Zmienione nazwe podkategorii na " + NazwaPodkat.getText().toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Pole nazwy nie może być puste!", Toast.LENGTH_SHORT).show();
                }

            }
            startActivity(intent);


        }
        else //dzialam na jeszcze nie istniejacej kategorii
        {

            Intent intent = new Intent(this, DodajKategorie.class);
            intent.putExtra("IdKategorii", IdKategorii);
            intent.putExtra("nazwa_okna", "Dodaj kategorie:");
            intent.putExtra("edycja", false);
            intent.putExtra("Nazwa_kategorii", Nazwa_kategorii);



            if(IdPodKategorii == 0)//dodaje nowa podkategorie
            {
                if (!NazwaPodkat.getText().toString().equals("")) {
                    ListaPodkategorii.add(NazwaPodkat.getText().toString());
                    BufforList = ListaPodkategorii.toArray(new String[0]);
                    Toast.makeText(this, "Dodano nową podkategorie " + NazwaPodkat.getText().toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Pole nazwy nie może być puste!", Toast.LENGTH_SHORT).show();
                }
            }
            else//edytuje istniejaca podkategorie
            {
                if (!NazwaPodkat.getText().toString().equals("")) {
                    ListaPodkategorii.set(IdPodKategorii-1, NazwaPodkat.getText().toString());//-1 jest potrzebne po to zebym mogl operowac na tablicy o numerze 0
                    BufforList = ListaPodkategorii.toArray(new String[0]);
                    Toast.makeText(this, "Zmienione nazwe podkategorii na " + NazwaPodkat.getText().toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Pole nazwy nie może być puste!", Toast.LENGTH_SHORT).show();
                }

            }
            intent.putExtra("ListaPodkategori", BufforList);
            startActivity(intent);


        }
    }

    public void usun(View view)
    {
        if(EdycjaKategorii == true) //dzialam na istniejacej kategorii
        {
            BazaDanych.RemovePodKategoria(IdPodKategorii);
            Toast.makeText(this, "Pomyślnie usunięto podkategorie " + BazaDanych.getPodKategoriaName(IdPodKategorii), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, DodajKategorie.class);
            intent.putExtra("IdKategorii", IdKategorii);
            intent.putExtra("nazwa_okna", "Edycja kategorii:");
            intent.putExtra("edycja", true);
            intent.putExtra("Nazwa_kategorii", Nazwa_kategorii);
            startActivity(intent);

        }
        else // dzialam na jeszcze nie istniejacej kategorii
        {
            Toast.makeText(this, "Pomyślnie usunięto podkategorie " + ListaPodkategorii.get(IdPodKategorii-1), Toast.LENGTH_SHORT).show();
            ListaPodkategorii.remove(IdPodKategorii-1);
            BufforList = ListaPodkategorii.toArray(new String[0]);
            Intent intent = new Intent(this, DodajKategorie.class);
            intent.putExtra("IdKategorii", IdKategorii);
            intent.putExtra("nazwa_okna", "Edycja kategorii:");
            intent.putExtra("edycja", false);
            intent.putExtra("Nazwa_kategorii", Nazwa_kategorii);
            intent.putExtra("ListaPodkategori", BufforList);
            startActivity(intent);

        }

    }


}
