package pl.damiandziura.kontrolawydatkow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

public class dodajStalyWydatek extends AppCompatActivity {

    private String nazwa_okna = "";
    private Intent intent;
    private Spinner spinKategorie, spinPodkategorie;
    private baza_danych BazaDanych;
    private String buforNazwa = "";
    private String buforKwota = "";
    private String Powrot = "";
    private EditText txtNazwa, txtKwota;
    private TextView txt1, txt2, txt3;
    private String data_godzina1 = "", data_godzina2 = "", data_godzina3 = "";
    private String buforData = "";
    private Spinner spnrCzestotliowsc, spnrKategoria, spnrPodkategoria;
    ArrayList<Integer> KategoriaIDlist, PodkategoriaIDlist;
    private TextView lblNazwaOkna;
    private boolean edycja = false;
    private int idStalegoWydatku = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodaj_staly_wydatek);
        setTitle("Dodaj cykliczny wydatek");
        BazaDanych = new baza_danych(this);

        spinKategorie = (Spinner) findViewById(R.id.SpinnerKategoria);
        spinPodkategorie = (Spinner) findViewById(R.id.SpinnerPodKategoria);
        txtNazwa = (EditText) findViewById(R.id.txtStalyWydatekNazwa);
        txtKwota = (EditText) findViewById(R.id.txtKwota);
        txt1 = (TextView) findViewById(R.id.txt1);
        txt2 = (TextView) findViewById(R.id.txt2);
        txt3 = (TextView) findViewById(R.id.txt3);
        spnrCzestotliowsc = (Spinner) findViewById(R.id.SpinnerCzestotliwosc);
        spnrKategoria = (Spinner) findViewById(R.id.SpinnerKategoria);
        spnrPodkategoria = (Spinner) findViewById(R.id.SpinnerPodKategoria);
        //TODO: Poszukać sposobu na to zeby do spinnerow podpiac wczytana kategoriee

        Calendar c = Calendar.getInstance();
        String dzien = Integer.toString(c.get(Calendar.DAY_OF_MONTH));
        if(c.get(Calendar.DAY_OF_MONTH) <= 9) dzien = "0" + Integer.toString(c.get(Calendar.DAY_OF_MONTH));
        String miesiac = Integer.toString(c.get(Calendar.MONTH)+1);
        if(c.get(Calendar.MONTH)+1 <= 9) miesiac = "0" + Integer.toString(c.get(Calendar.MONTH)+1);

        data_godzina1 = dzien + "-" + miesiac + "-" + Integer.toString(c.get(Calendar.YEAR));
        data_godzina2 = data_godzina1;
        data_godzina3 = data_godzina1;

        Bundle extras = getIntent().getExtras();

        if(extras != null)
        {
            buforNazwa = extras.getString("Nazwa");
            buforKwota = extras.getString("Kwota");
            Powrot = extras.getString("Powrot");
            buforData = extras.getString("Data");
            nazwa_okna = extras.getString("nazwa_okna");
            edycja = extras.getBoolean("edycja");
            idStalegoWydatku = extras.getInt("IdStalegoWydatku");

            if(extras.getString("data_godzina1") != null) data_godzina1 = extras.getString("data_godzina1");
            if(extras.getString("data_godzina2") != null) data_godzina2 = extras.getString("data_godzina2");
            if(extras.getString("data_godzina3") != null) data_godzina3 = extras.getString("data_godzina3");
        }
        if(edycja == true)
        {
            buforNazwa = BazaDanych.getStalyWydatekName(idStalegoWydatku);
            buforKwota = Double.toString(BazaDanych.getStalyWydatekKwota(idStalegoWydatku));
            setTitle(nazwa_okna);
            data_godzina1 = BazaDanych.getStalyWydatekOD(idStalegoWydatku);
            data_godzina2 = BazaDanych.getStalyWydatekDO(idStalegoWydatku);
            data_godzina3 = BazaDanych.getStalyWydatekNastepnaData(idStalegoWydatku);


        }
        if(nazwa_okna != null) setTitle(nazwa_okna);

        if( buforNazwa != null)
        {
            setTitle(nazwa_okna);
        }

        if(buforKwota != null) txtKwota.setText(buforKwota);

        if(data_godzina1 != null) txt1.setText(data_godzina1);
        if(data_godzina2 != null)txt2.setText(data_godzina2);
        if(data_godzina3 != null)txt3.setText(data_godzina3);

        if(Powrot != null) {
            if (Powrot.equals("nowyStalyWydatek1")) {
                data_godzina1 = buforData;
                txt1.setText(buforData);
            } else if (Powrot.equals("nowyStalyWydatek2")) {
                data_godzina2 = buforData;
                txt2.setText(buforData);
            } else if (Powrot.equals("nowyStalyWydatek3")) {
                data_godzina3 = buforData;
                txt3.setText(buforData);
            }
        }




        kategoria();
        poprawnoscDat();

    }

    public void cofnij(View view) {
        intent = new Intent(this, CykliczneWydatki.class);
        startActivity(intent);
       // onBackPressed();
    }

    public void dodaj(View view) {
        poprawnoscDat();
        boolean poprawnoscDanych = true;

        Calendar cTxt1, cTxt2, cTxt3;
        cTxt1 = Calendar.getInstance();
        cTxt1.set(YEAR, Integer.parseInt(data_godzina1.substring(6, 10)));
        cTxt1.set(MONTH, Integer.parseInt(data_godzina1.substring(3, 5)));
        cTxt1.set(DAY_OF_MONTH, Integer.parseInt(data_godzina1.substring(0, 2)));

        cTxt2 = Calendar.getInstance();
        cTxt2.set(YEAR, Integer.parseInt(data_godzina2.substring(6, 10)));
        cTxt2.set(MONTH, Integer.parseInt(data_godzina2.substring(3, 5)));
        cTxt2.set(DAY_OF_MONTH, Integer.parseInt(data_godzina2.substring(0, 2)));

        cTxt3 = Calendar.getInstance();
        cTxt3.set(YEAR, Integer.parseInt(data_godzina3.substring(6, 10)));
        cTxt3.set(MONTH, Integer.parseInt(data_godzina3.substring(3, 5)));
        cTxt3.set(DAY_OF_MONTH, Integer.parseInt(data_godzina3.substring(0, 2)));

        if(cTxt1.getTimeInMillis() >= cTxt2.getTimeInMillis())
        {
            txt2.setTextColor(getResources().getColor(R.color.RedAsFuck));
            Toast.makeText(this, "Termin końca wydatku musi być większy od " + data_godzina1, Toast.LENGTH_LONG).show();
            poprawnoscDanych = false;
        }else
        {
            txt2.setTextColor(getResources().getColor(R.color.Normalny));
        }


        if(cTxt3.getTimeInMillis() <= cTxt2.getTimeInMillis() &&
                cTxt3.getTimeInMillis() >= cTxt1.getTimeInMillis())
        {
            txt3.setTextColor(getResources().getColor(R.color.Normalny));
        }else
        {
            txt3.setTextColor(getResources().getColor(R.color.RedAsFuck));
            poprawnoscDanych = false;

            if(cTxt1.getTimeInMillis() == cTxt2.getTimeInMillis())
            {
                Toast.makeText(this, "Data początku oraz końca muszą być od siebie różne.", Toast.LENGTH_SHORT).show();
            }else Toast.makeText(this, "Data pierwszego pobrania musi być w przedziale od " + data_godzina1 + " do " + data_godzina2, Toast.LENGTH_LONG).show();
        }

        if(txtNazwa.getText().toString().equals(""))
        {
            poprawnoscDanych = false;
            Toast.makeText(this, "Nazwa nie może być pusta", Toast.LENGTH_SHORT).show();

        }

        if(txtKwota.getText().toString().equals("") || Double.parseDouble(txtKwota.getText().toString()) <= 0.0)
        {
            poprawnoscDanych = false;
            Toast.makeText(this, "Kwota musi być większa od 0", Toast.LENGTH_SHORT).show();

        }

        if(poprawnoscDanych == true)
        {
            int ID_WYBRANEJ_KATEGORII = KategoriaIDlist.get(spnrKategoria.getSelectedItemPosition());
            int ID_WYBRANEJ_PODKATEGORII = PodkategoriaIDlist.get(spnrPodkategoria.getSelectedItemPosition());

            switch(spnrCzestotliowsc.getSelectedItemPosition())
            {
                case 0:
                    BazaDanych.addStalyWydatek(txtNazwa.getText().toString(), Double.parseDouble(txtKwota.getText().toString()), ID_WYBRANEJ_KATEGORII, ID_WYBRANEJ_KATEGORII, data_godzina1, data_godzina2, data_godzina3, baza_danych.CZESTOTLIWOSC.DZIENNIE);
                    Toast.makeText(this, "Dodaje staly wydatek o nazwie " + txtNazwa.getText().toString(), Toast.LENGTH_SHORT).show();
                    break;

                case 1:
                    BazaDanych.addStalyWydatek(txtNazwa.getText().toString(), Double.parseDouble(txtKwota.getText().toString()), ID_WYBRANEJ_KATEGORII, ID_WYBRANEJ_KATEGORII, data_godzina1, data_godzina2, data_godzina3, baza_danych.CZESTOTLIWOSC.TYDZIEN);
                    Toast.makeText(this, "Dodaje staly wydatek o nazwie " + txtNazwa.getText().toString(), Toast.LENGTH_SHORT).show();
                    break;

                case 2:
                    BazaDanych.addStalyWydatek(txtNazwa.getText().toString(), Double.parseDouble(txtKwota.getText().toString()), ID_WYBRANEJ_KATEGORII, ID_WYBRANEJ_KATEGORII, data_godzina1, data_godzina2, data_godzina3, baza_danych.CZESTOTLIWOSC.MIESIAC);
                    Toast.makeText(this, "Dodaje staly wydatek o nazwie " + txtNazwa.getText().toString(), Toast.LENGTH_SHORT).show();
                    break;

                case 3:
                    BazaDanych.addStalyWydatek(txtNazwa.getText().toString(), Double.parseDouble(txtKwota.getText().toString()), ID_WYBRANEJ_KATEGORII, ID_WYBRANEJ_KATEGORII, data_godzina1, data_godzina2, data_godzina3, baza_danych.CZESTOTLIWOSC.KWARTAL);
                    Toast.makeText(this, "Dodaje staly wydatek o nazwie " + txtNazwa.getText().toString(), Toast.LENGTH_SHORT).show();
                    break;

                case 4:
                    BazaDanych.addStalyWydatek(txtNazwa.getText().toString(), Double.parseDouble(txtKwota.getText().toString()), ID_WYBRANEJ_KATEGORII, ID_WYBRANEJ_KATEGORII, data_godzina1, data_godzina2, data_godzina3, baza_danych.CZESTOTLIWOSC.ROK);
                    Toast.makeText(this, "Dodaje staly wydatek o nazwie " + txtNazwa.getText().toString(), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(this, "Błąd dodawania stałego wydatku", Toast.LENGTH_LONG);
                    break;
            }
            // BazaDanych.addStalyWydatek(txtNazwa.getText().toString(), ID_WYBRANEJ_KATEGORII, ID_WYBRANEJ_KATEGORII, data_godzina1, data_godzina2, data_godzina3, baza_danych.CZESTOTLIWOSC.DZIENNIE);
            //Toast.makeText(this, "Dodaje staly wydatek o nazwie " + txtNazwa.getText().toString(), Toast.LENGTH_SHORT).show();

        }
    }

    public void wyczysc(View view) {

    }

    private void kategoria()    {
        ArrayList<String> lista = BazaDanych.getKategorie();
        KategoriaIDlist = BazaDanych.getINTKategorie();

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

        ArrayList<String> lista = BazaDanych.getpodKategorie(numer);
        PodkategoriaIDlist = BazaDanych.getINTpodKategorie(numer);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lista);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinPodkategorie.setAdapter(adapter);
    }

    public void WybierzDateGodzine1(View view) {
        intent = new Intent(this, dataPicker.class);

        intent.putExtra("Nazwa", txtNazwa.getText().toString());
        intent.putExtra("Kwota", txtKwota.getText().toString());
        intent.putExtra("Klasa", "nowyStalyWydatek1");
        //data_godzina3
        intent.putExtra("data_godzina1", data_godzina1);
        intent.putExtra("data_godzina2", data_godzina2);
        intent.putExtra("data_godzina3", data_godzina3);
        startActivity(intent);


    }
    public void WybierzDateGodzine2(View view) {
        intent = new Intent(this, dataPicker.class);

        intent.putExtra("Nazwa", txtNazwa.getText().toString());
        intent.putExtra("Kwota", txtKwota.getText().toString());
        intent.putExtra("Klasa", "nowyStalyWydatek2");
        intent.putExtra("data_godzina1", data_godzina1);
        intent.putExtra("data_godzina2", data_godzina2);
        intent.putExtra("data_godzina3", data_godzina3);
        startActivity(intent);


    }
    public void WybierzDateGodzine3(View view) {
        intent = new Intent(this, dataPicker.class);

        intent.putExtra("Nazwa", txtNazwa.getText().toString());
        intent.putExtra("Kwota", txtKwota.getText().toString());
        intent.putExtra("Klasa", "nowyStalyWydatek3");
        intent.putExtra("data_godzina1", data_godzina1);
        intent.putExtra("data_godzina2", data_godzina2);
        intent.putExtra("data_godzina3", data_godzina3);
        startActivity(intent);


    }


    private void poprawnoscDat()
    {//DD-MM-YYYY


        Calendar cTxt1, cTxt2, cTxt3;
        cTxt1 = Calendar.getInstance();
        cTxt1.set(YEAR, Integer.parseInt(data_godzina1.substring(6, 10)));
        cTxt1.set(MONTH, Integer.parseInt(data_godzina1.substring(3, 5)));
        cTxt1.set(DAY_OF_MONTH, Integer.parseInt(data_godzina1.substring(0, 2)));

        cTxt2 = Calendar.getInstance();
        cTxt2.set(YEAR, Integer.parseInt(data_godzina2.substring(6, 10)));
        cTxt2.set(MONTH, Integer.parseInt(data_godzina2.substring(3, 5)));
        cTxt2.set(DAY_OF_MONTH, Integer.parseInt(data_godzina2.substring(0, 2)));

        cTxt3 = Calendar.getInstance();
        cTxt3.set(YEAR, Integer.parseInt(data_godzina3.substring(6, 10)));
        cTxt3.set(MONTH, Integer.parseInt(data_godzina3.substring(3, 5)));
        cTxt3.set(DAY_OF_MONTH, Integer.parseInt(data_godzina3.substring(0, 2)));

        if(cTxt1.getTimeInMillis() >= cTxt2.getTimeInMillis())
        {
            txt2.setTextColor(getResources().getColor(R.color.RedAsFuck));
        }else
        {
            txt2.setTextColor(getResources().getColor(R.color.Normalny));
        }


        if(cTxt3.getTimeInMillis() <= cTxt2.getTimeInMillis() &&
                cTxt3.getTimeInMillis() >= cTxt1.getTimeInMillis())
        {
            txt3.setTextColor(getResources().getColor(R.color.Normalny));
        }else
        {
            txt3.setTextColor(getResources().getColor(R.color.RedAsFuck));
        }


        //Toast.makeText(this, data_godzina1.substring(0, 2) + "-" + data_godzina1.substring(3, 5) + "-" + data_godzina1.substring(6, 10), Toast.LENGTH_SHORT).show();

    }

}
