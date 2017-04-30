package pl.damiandziura.kontrolawydatkow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class nowyPrzychod extends AppCompatActivity {

    private baza_danych BazaDanych;

    private String NAZWA = "", DATA = "", GODZINA = "";
    private double KWOTA = 0;

    private Intent intent;
    TextView txtData;
    EditText txtNazwa, txtKwota;
    Spinner SpinnerListaKategorii, SpinnerListaPodKategorii;
    private ArrayList<Integer> KategoriaIDlist, PodkategoriaIDlist;

    private int positionKategoria = 0, positionPodkategoria = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nowy_przychod);
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
            positionKategoria = extras.getInt("KategoriaID");
            positionPodkategoria = extras.getInt("PodkategoriaID");
            // if( != null)
            /*
            intent.putExtra("KategoriaID", positionKategoria);
        intent.putExtra("PodkategoriaID", positionPodkategor
             */
        }

        kategoria();

        txtKwota.setText("");
        //if(KWOTA > 0.0) txtKwota.setText(Double.toString(KWOTA));
        if(KWOTA > 0.0) txtKwota.setText(String.format(Locale.getDefault(), "%.2f", KWOTA));
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
        ArrayList<String> lista = BazaDanych.getCategory(1);
        KategoriaIDlist = BazaDanych.getIdListOfCategory(1);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lista);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        SpinnerListaKategorii.setAdapter(adapter);
        SpinnerListaKategorii.setSelection(positionKategoria);

        SpinnerListaKategorii.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                podkategoria(KategoriaIDlist.get(position));
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

        ArrayList<String> lista = BazaDanych.getNameListOfSubcategory(numer);
        PodkategoriaIDlist = BazaDanych.getIdListOfSubcategory(numer);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lista);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        SpinnerListaPodKategorii.setAdapter(adapter);
        SpinnerListaPodKategorii.setSelection(positionPodkategoria);

    }


    public void cofnij(View view) {
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void DodajStalyDochod(View view)
    {
        Intent intent = new Intent(this, CyklicznyDochod.class);
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
                    int ID_WYBRANEJ_KATEGORII = KategoriaIDlist.get(SpinnerListaKategorii.getSelectedItemPosition());
                    int ID_WYBRANEJ_PODKATEGORII = PodkategoriaIDlist.get(SpinnerListaPodKategorii.getSelectedItemPosition());

                    BazaDanych.AddIncome(NAZWA, KWOTA, ID_WYBRANEJ_KATEGORII, ID_WYBRANEJ_PODKATEGORII, GODZINA, DATA);

                    Toast.makeText(this, getResources().getString(R.string.str_nowy_dochod) + NAZWA + " " + Double.toString(KWOTA) + getResources().getString(R.string.str_skrot_waluty), Toast.LENGTH_SHORT).show();

                    BazaDanych.WalletSetBalance(BazaDanych.WalletRecalculate());

                    intent = new Intent(this, MainActivity.class);

                    startActivity(intent);
                } else Toast.makeText(this, getResources().getString(R.string.str_kwota_wieksza_od_0), Toast.LENGTH_SHORT).show();
            } else Toast.makeText(this, getResources().getString(R.string.str_musisz_wpisac_kwote), Toast.LENGTH_SHORT).show();

        }else{Toast.makeText(this, getResources().getString(R.string.str_musisz_wpisac_nazwe), Toast.LENGTH_SHORT).show();}




        //  Toast.makeText(this, NAZWA+" / "+Double.parseDouble(txtKwota.getText().toString())+" / "+"0"+" / "+"0"+" / "+GODZINA+" / "+DATA, Toast.LENGTH_SHORT).show();
    }

    public void wyczysc(View view)
    {
        NAZWA="";
        setDataAndHour();
        KWOTA = 0;
        txtData.setText(DATA + " " + GODZINA);
        txtNazwa.setText(NAZWA);
        txtKwota.setText("");
        SpinnerListaKategorii.setSelection(0);
        SpinnerListaPodKategorii.setSelection(0);

    }

    public void WybierzDateGodzine(View view) {
        intent = new Intent(this, dataPicker.class);

        intent.putExtra("Nazwa", txtNazwa.getText().toString());
        intent.putExtra("Kwota", txtKwota.getText().toString());
        intent.putExtra("Klasa", "nowyDochod");
        intent.putExtra("KategoriaID", SpinnerListaKategorii.getSelectedItemPosition());
        intent.putExtra("PodkategoriaID", SpinnerListaPodKategorii.getSelectedItemPosition());
        startActivity(intent);
    }

    private void setDataAndHour()
    {
        Calendar c = Calendar.getInstance();

        String dzien = Integer.toString(c.get(Calendar.DAY_OF_MONTH));
        if(c.get(Calendar.DAY_OF_MONTH) <= 9) dzien = "0" + Integer.toString(c.get(Calendar.DAY_OF_MONTH));
        String miesiac = Integer.toString(c.get(Calendar.MONTH)+1);
        if((c.get(Calendar.MONTH)+1) <= 9) miesiac = "0" + Integer.toString(c.get(Calendar.MONTH)+1);


        DATA = dzien + "-" + miesiac + "-" + Integer.toString(c.get(Calendar.YEAR));
        GODZINA = Integer.toString(c.get(Calendar.HOUR_OF_DAY)) + ":" + Integer.toString(c.get(Calendar.MINUTE));
    }




}
