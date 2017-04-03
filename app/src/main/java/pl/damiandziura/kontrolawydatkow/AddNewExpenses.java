package pl.damiandziura.kontrolawydatkow;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddNewExpenses extends AppCompatActivity {

    private baza_danych BazaDanych;

    private String NAZWA = "", DATA = "", GODZINA = "";
    private double KWOTA = 0;

    private String strData, strGodzina;
    private Intent intent;
    private Calendar c;
    TextView txtData;
    EditText txtNazwa, txtKwota;
    Spinner SpinnerListaKategorii, SpinnerListaPodKategorii;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_expenses);
        Bundle extras = getIntent().getExtras();

        BazaDanych = new baza_danych(this);

        txtData = (TextView) findViewById(R.id.txtDate2);
        txtNazwa = (EditText) findViewById(R.id.txtNazwa2);
        txtNazwa.setText(NAZWA);
        txtKwota = (EditText) findViewById(R.id.txtEditAmount2);
        txtKwota.setText("");
        SpinnerListaKategorii = (Spinner) findViewById(R.id.SpinnerKategoriaWydatki);
        SpinnerListaPodKategorii = (Spinner) findViewById(R.id.SpinnerPodKategoriaWydatki);

        setDataAndHour();
        kategoria();
       // podkategoria();


        if(extras != null)
        {
            DATA = extras.getString("Data");
            GODZINA = extras.getString("Godzina");
            String a = extras.getString("Kwota");
            if(a != null && !a.equals(""))
            {
                KWOTA = Double.parseDouble(a);
            }
            NAZWA = extras.getString("Nazwa");
        }
        txtKwota.setText("");
        if(KWOTA > 0.0) txtKwota.setText(Double.toString(KWOTA));
        txtNazwa.setText(NAZWA);

        txtData.setText(DATA + " " + GODZINA);


        txtKwota.addTextChangedListener(new TextWatcher()
        {
            boolean ignoreChange = false;
            String beforeChange;

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count)
            {
                if (!ignoreChange)
                {
                    String string = charSequence.toString();
                    String[] parts = string.split("\\.");
                    if (parts.length > 1)
                    {
                        String digitsAfterPoint = parts[1];
                        if (digitsAfterPoint.length() > 2)
                        {
                            ignoreChange = true;
                            txtKwota.setText(string.substring(0, string.indexOf(".") + 3));
                            txtKwota.setSelection(txtKwota.getText().length());
                            ignoreChange = false;
                        }
                    }
                    beforeChange = txtKwota.getText().toString();
                }

            }
        });

    }

    private void kategoria()    {
        ArrayList<String> lista = BazaDanych.getKategorie();

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lista);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        SpinnerListaKategorii.setAdapter(adapter);

        SpinnerListaKategorii.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                podkategoria(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });

       // Toast.makeText(this, lista.get(1), Toast.LENGTH_SHORT).show();
        //podkategoria();
    }



    public void podkategoria(int numer)
    {

        ArrayList<String> lista = BazaDanych.getpodKategorie(numer);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lista);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        SpinnerListaPodKategorii.setAdapter(adapter);


    }


    public void cofnij(View view) {
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void DodajStalyWydatek(View view) {
        intent = new Intent(this, dodajStalyWydatek.class);
        startActivity(intent);
    }

    public void dodaj(View view) {
        NAZWA = txtNazwa.getText().toString();

        if(!NAZWA.equals("") && NAZWA != null)
        {
           if(!txtKwota.getText().toString().equals("") && txtKwota.getText().toString() != null)
           {
               KWOTA = Double.parseDouble(txtKwota.getText().toString());
               if (KWOTA > 0.0) {
                   BazaDanych.DodajWydatek(NAZWA, KWOTA, 0, 0, GODZINA, DATA);
                   Toast.makeText(this, "Dodano nowy wydatek " + NAZWA.toString() + " " + Double.toString(KWOTA) + "zł", Toast.LENGTH_SHORT).show();
                   BazaDanych.PortfelUstawSaldo(BazaDanych.PortfelPrzeliczSaldo());
                   intent = new Intent(this, MainActivity.class);

                   startActivity(intent);
               } else Toast.makeText(this, "Kwota musi być większa od 0!", Toast.LENGTH_SHORT).show();
           } else Toast.makeText(this, "Musisz wpisać kwote!", Toast.LENGTH_SHORT).show();

        }else{Toast.makeText(this, "Musisz wpisać nazwe!", Toast.LENGTH_SHORT).show();}




      //  Toast.makeText(this, NAZWA+" / "+Double.parseDouble(txtKwota.getText().toString())+" / "+"0"+" / "+"0"+" / "+GODZINA+" / "+DATA, Toast.LENGTH_SHORT).show();
    }

    public void wyczysc(View view)
    {
      /*  NAZWA="";
        setDataAndHour();
        KWOTA = 0;
        txtData.setText(DATA + " " + GODZINA);
        txtNazwa.setText(NAZWA);
        txtKwota.setText("");
*/
        BazaDanych.AddKategoria("Pierwsza Kategoria");
        BazaDanych.AddKategoria("Druga Kategoria");
        BazaDanych.AddKategoria("Trzecia Kategoria");
        BazaDanych.AddKategoria("Czwarta Kategoria");
        BazaDanych.AddKategoria("Piąta");

        BazaDanych.AddPodKategoria("Kat1. PierwszPodKat", 1);
        BazaDanych.AddPodKategoria("Kat1. DrugaPodKat", 1);
        BazaDanych.AddPodKategoria("Kat2. PierwszPodKat", 2);
        BazaDanych.AddPodKategoria("Kat2. DrugaPodKat", 2);

        BazaDanych.addStalyWydatek("Rata za samochod", 0, 0, "01/01/2017", "01/12/2017", "01/05/2017", baza_danych.CZESTOTLIWOSC.DZIENNIE);
       //BazaDanych.updateNamePodkategoria("dupa1", 1);


    }

    public void WybierzDateGodzine(View view) {
        intent = new Intent(this, dataPicker.class);

        intent.putExtra("Nazwa", txtNazwa.getText().toString());
        intent.putExtra("Kwota", txtKwota.getText().toString());
        intent.putExtra("Klasa", "nowyWydatek");
        startActivity(intent);
    }

    private void setDataAndHour()
    {
        c = Calendar.getInstance();

        String dzien = Integer.toString(c.get(Calendar.DAY_OF_MONTH));
        if(c.get(Calendar.DAY_OF_MONTH) <= 9) dzien = "0" + Integer.toString(c.get(Calendar.DAY_OF_MONTH));
        String miesiac = Integer.toString(c.get(Calendar.MONTH)+1);
        if((c.get(Calendar.MONTH)+1) <= 9) miesiac = "0" + Integer.toString(c.get(Calendar.MONTH)+1);


        DATA = dzien + "-" + miesiac + "-" + Integer.toString(c.get(Calendar.YEAR));
        GODZINA = Integer.toString(c.get(Calendar.HOUR_OF_DAY)) + ":" + Integer.toString(c.get(Calendar.MINUTE));
    }




}
