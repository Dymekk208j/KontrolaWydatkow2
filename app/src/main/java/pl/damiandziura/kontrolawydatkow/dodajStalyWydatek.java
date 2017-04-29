package pl.damiandziura.kontrolawydatkow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

public class dodajStalyWydatek extends AppCompatActivity {

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
    private ArrayList<Integer> KategoriaIDlist, PodkategoriaIDlist;
    private boolean edycja = false;
    private int idCyclicalWydatku = 0;
    private Button btUsun;

    private int positionKategoria = 0, positionPodkategoria = 0, positionFREQUENCY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodaj_staly_wydatek);
        setTitle("Dodaj cykliczny Expenses");
        BazaDanych = new baza_danych(this);

        spinKategorie = (Spinner) findViewById(R.id.SpinnerKategoria);
        spinPodkategorie = (Spinner) findViewById(R.id.SpinnerPodKategoria);
        txtNazwa = (EditText) findViewById(R.id.txtNazwa);
        txtKwota = (EditText) findViewById(R.id.txtKwota);
        txt1 = (TextView) findViewById(R.id.txt1);
        txt2 = (TextView) findViewById(R.id.txt2);
        txt3 = (TextView) findViewById(R.id.txt3);

        spnrCzestotliowsc = (Spinner) findViewById(R.id.SpinnerCzestotliwosc);
        spnrKategoria = (Spinner) findViewById(R.id.SpinnerKategoria);
        spnrPodkategoria = (Spinner) findViewById(R.id.SpinnerPodKategoria);
        btUsun = (Button) findViewById(R.id.btWyczysc);

        btUsun.setVisibility(View.INVISIBLE);

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
            edycja = extras.getBoolean("edycja");
            idCyclicalWydatku = extras.getInt("IdStalegoWydatku");

            if(extras.getString("data_godzina1") != null) data_godzina1 = extras.getString("data_godzina1");
            if(extras.getString("data_godzina2") != null) data_godzina2 = extras.getString("data_godzina2");
            if(extras.getString("data_godzina3") != null) data_godzina3 = extras.getString("data_godzina3");

            positionKategoria = extras.getInt("KategoriaID");
            positionPodkategoria = extras.getInt("PodkategoriaID");
            positionFREQUENCY = extras.getInt("FREQUENCYID");
        }
        if(edycja == true)
        {
            btUsun.setVisibility(View.VISIBLE);
            ArrayList<Integer> KategoriaIDlist, PodkategoriaIDlist;
            KategoriaIDlist = BazaDanych.getIdListOfCategory(0);

            positionKategoria = KategoriaIDlist.indexOf(BazaDanych.getCyclicalExpensesCategory(idCyclicalWydatku));

            PodkategoriaIDlist = BazaDanych.getIdListOfSubcategory(KategoriaIDlist.get(positionKategoria));



            positionPodkategoria = PodkategoriaIDlist.indexOf(BazaDanych.getCyclicalIncomeSubcategory(idCyclicalWydatku));
            positionFREQUENCY = BazaDanych.getCyclicalIncomeFrequency(idCyclicalWydatku);

            buforNazwa = BazaDanych.getCyclicalExpensesName(idCyclicalWydatku);
            buforKwota = Double.toString(BazaDanych.getCyclicalIncomeAmount(idCyclicalWydatku));
            setTitle("Edycja cyklicznego wydatku");
            data_godzina1 = BazaDanych.getCyclicalExpensesOD(idCyclicalWydatku);
            data_godzina2 = BazaDanych.getCyclicalExpensesDO(idCyclicalWydatku);
            data_godzina3 = BazaDanych.getCyclicalExpensesNastepnaData(idCyclicalWydatku);

        }

        if(buforNazwa != null) txtNazwa.setText(buforNazwa);
        if(buforKwota != null) txtKwota.setText(buforKwota);

        if(data_godzina1 != null) txt1.setText(data_godzina1);
        if(data_godzina2 != null)txt2.setText(data_godzina2);
        if(data_godzina3 != null)txt3.setText(data_godzina3);

        spnrCzestotliowsc.setSelection(positionFREQUENCY);

        if(Powrot != null) {
            if (Powrot.equals("nowyCyclicalExpenses1")) {
                data_godzina1 = buforData;
                txt1.setText(buforData);
            } else if (Powrot.equals("nowyCyclicalExpenses2")) {
                data_godzina2 = buforData;
                txt2.setText(buforData);
            } else if (Powrot.equals("nowyCyclicalExpenses3")) {
                data_godzina3 = buforData;
                txt3.setText(buforData);
            }
        }




        kategoria();

        spinKategorie.setSelection(positionKategoria);


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
            if(edycja == false)
            {
                switch (spnrCzestotliowsc.getSelectedItemPosition()) {
                    case 0:
                        BazaDanych.addCyclicalExpenses(txtNazwa.getText().toString(), Double.parseDouble(txtKwota.getText().toString()), ID_WYBRANEJ_KATEGORII, ID_WYBRANEJ_PODKATEGORII, data_godzina1, data_godzina2, data_godzina3, baza_danych.FREQUENCY.DAILY);
                        Toast.makeText(this, "Dodaje Cyclical Expenses o nazwie " + txtNazwa.getText().toString(), Toast.LENGTH_SHORT).show();
                        break;

                    case 1:
                        BazaDanych.addCyclicalExpenses(txtNazwa.getText().toString(), Double.parseDouble(txtKwota.getText().toString()), ID_WYBRANEJ_KATEGORII, ID_WYBRANEJ_PODKATEGORII, data_godzina1, data_godzina2, data_godzina3, baza_danych.FREQUENCY.WEEKLY);
                        Toast.makeText(this, "Dodaje Cyclical Expenses o nazwie " + txtNazwa.getText().toString(), Toast.LENGTH_SHORT).show();
                        break;

                    case 2:
                        BazaDanych.addCyclicalExpenses(txtNazwa.getText().toString(), Double.parseDouble(txtKwota.getText().toString()), ID_WYBRANEJ_KATEGORII, ID_WYBRANEJ_PODKATEGORII, data_godzina1, data_godzina2, data_godzina3, baza_danych.FREQUENCY.MONTHLY);
                        Toast.makeText(this, "Dodaje Cyclical Expenses o nazwie " + txtNazwa.getText().toString(), Toast.LENGTH_SHORT).show();
                        break;

                    case 3:
                        BazaDanych.addCyclicalExpenses(txtNazwa.getText().toString(), Double.parseDouble(txtKwota.getText().toString()), ID_WYBRANEJ_KATEGORII, ID_WYBRANEJ_PODKATEGORII, data_godzina1, data_godzina2, data_godzina3, baza_danych.FREQUENCY.QUARTERLY);
                        Toast.makeText(this, "Dodaje Cyclical Expenses o nazwie " + txtNazwa.getText().toString(), Toast.LENGTH_SHORT).show();
                        break;

                    case 4:
                        BazaDanych.addCyclicalExpenses(txtNazwa.getText().toString(), Double.parseDouble(txtKwota.getText().toString()), ID_WYBRANEJ_KATEGORII, ID_WYBRANEJ_PODKATEGORII, data_godzina1, data_godzina2, data_godzina3, baza_danych.FREQUENCY.YEARLY);
                        Toast.makeText(this, "Dodaje Cyclical Expenses o nazwie " + txtNazwa.getText().toString(), Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(this, "Błąd dodawania stałego wydatku", Toast.LENGTH_LONG);
                        break;
                }
            }
            else
            {
                switch (spnrCzestotliowsc.getSelectedItemPosition()) {
                    case 0:
                        BazaDanych.UpdateCyclicalExpenses(idCyclicalWydatku,txtNazwa.getText().toString(), Double.parseDouble(txtKwota.getText().toString()), ID_WYBRANEJ_KATEGORII, ID_WYBRANEJ_PODKATEGORII, data_godzina1, data_godzina2, data_godzina3, baza_danych.FREQUENCY.DAILY);
                        Toast.makeText(this, "Uaktualniono Cyclical Expenses o nazwie " + txtNazwa.getText().toString(), Toast.LENGTH_SHORT).show();
                        break;

                    case 1:
                        BazaDanych.UpdateCyclicalExpenses(idCyclicalWydatku,txtNazwa.getText().toString(), Double.parseDouble(txtKwota.getText().toString()), ID_WYBRANEJ_KATEGORII, ID_WYBRANEJ_PODKATEGORII, data_godzina1, data_godzina2, data_godzina3, baza_danych.FREQUENCY.WEEKLY);
                        Toast.makeText(this, "Uaktualniono Cyclical Expenses o nazwie " + txtNazwa.getText().toString(), Toast.LENGTH_SHORT).show();
                        break;

                    case 2:
                        BazaDanych.UpdateCyclicalExpenses(idCyclicalWydatku,txtNazwa.getText().toString(), Double.parseDouble(txtKwota.getText().toString()), ID_WYBRANEJ_KATEGORII, ID_WYBRANEJ_PODKATEGORII, data_godzina1, data_godzina2, data_godzina3, baza_danych.FREQUENCY.MONTHLY);
                        Toast.makeText(this, "Uaktualniono Cyclical Expenses o nazwie " + txtNazwa.getText().toString(), Toast.LENGTH_SHORT).show();
                        break;

                    case 3:
                        BazaDanych.UpdateCyclicalExpenses(idCyclicalWydatku,txtNazwa.getText().toString(), Double.parseDouble(txtKwota.getText().toString()), ID_WYBRANEJ_KATEGORII, ID_WYBRANEJ_PODKATEGORII, data_godzina1, data_godzina2, data_godzina3, baza_danych.FREQUENCY.QUARTERLY);
                        Toast.makeText(this, "Uaktualniono Cyclical Expenses o nazwie " + txtNazwa.getText().toString(), Toast.LENGTH_SHORT).show();
                        break;

                    case 4:
                        BazaDanych.UpdateCyclicalExpenses(idCyclicalWydatku,txtNazwa.getText().toString(), Double.parseDouble(txtKwota.getText().toString()), ID_WYBRANEJ_KATEGORII, ID_WYBRANEJ_PODKATEGORII, data_godzina1, data_godzina2, data_godzina3, baza_danych.FREQUENCY.YEARLY);
                        Toast.makeText(this, "Uaktualniono Cyclical Expenses o nazwie " + txtNazwa.getText().toString(), Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(this, "Błąd aktualizacji stałego wydatku", Toast.LENGTH_LONG);
                        break;
                }
            }
            // BazaDanych.addCyclicalExpenses(txtNazwa.getText().toString(), ID_WYBRANEJ_KATEGORII, ID_WYBRANEJ_KATEGORII, data_godzina1, data_godzina2, data_godzina3, baza_danych.FREQUENCY.DZIENNIE);
            //Toast.makeText(this, "Dodaje Cyclical Expenses o nazwie " + txtNazwa.getText().toString(), Toast.LENGTH_SHORT).show();
            intent = new Intent(this, CykliczneWydatki.class);
            startActivity(intent);
        }
    }

    public void wyczysc(View view)
    {
        txtNazwa.setText("");
        txtKwota.setText("");
        Calendar c = Calendar.getInstance();
        String dzien = Integer.toString(c.get(Calendar.DAY_OF_MONTH));
        if(c.get(Calendar.DAY_OF_MONTH) <= 9) dzien = "0" + Integer.toString(c.get(Calendar.DAY_OF_MONTH));
        String miesiac = Integer.toString(c.get(Calendar.MONTH)+1);
        if(c.get(Calendar.MONTH)+1 <= 9) miesiac = "0" + Integer.toString(c.get(Calendar.MONTH)+1);

        data_godzina1 = dzien + "-" + miesiac + "-" + Integer.toString(c.get(Calendar.YEAR));
        data_godzina2 = data_godzina1;
        data_godzina3 = data_godzina1;
        txt1.setText(data_godzina1);
        txt2.setText(data_godzina2);
        txt3.setText(data_godzina3);
        positionKategoria = 0;
        positionPodkategoria = 0;
        positionFREQUENCY = 0;
        spnrCzestotliowsc.setSelection(positionFREQUENCY);
        spnrKategoria.setSelection(positionKategoria);
        spnrPodkategoria.setSelection(positionPodkategoria);

    }

    private void kategoria()    {
        ArrayList<String> lista = BazaDanych.getCategory(0);
        KategoriaIDlist = BazaDanych.getIdListOfCategory(0);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lista);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinKategorie.setAdapter(adapter);
        spinKategorie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Podkategoria();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



    }



    private void Podkategoria()    {

        int kat = KategoriaIDlist.get(spinKategorie.getSelectedItemPosition());
        ArrayList<String> lista = BazaDanych.getNameListOfSubcategory(kat);
        PodkategoriaIDlist = BazaDanych.getIdListOfSubcategory(kat);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lista);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinPodkategorie.setAdapter(adapter);
        spinPodkategorie.setSelection(positionPodkategoria);

    }

    public void WybierzDateGodzine1(View view) {
        intent = new Intent(this, dataPicker.class);

        intent.putExtra("Nazwa", txtNazwa.getText().toString());
        intent.putExtra("Kwota", txtKwota.getText().toString());
        intent.putExtra("Klasa", "nowyCyclicalExpenses1");
        //data_godzina3
        intent.putExtra("data_godzina1", data_godzina1);
        intent.putExtra("data_godzina2", data_godzina2);
        intent.putExtra("data_godzina3", data_godzina3);
        intent.putExtra("KategoriaID", spnrKategoria.getSelectedItemPosition());
        intent.putExtra("PodkategoriaID", spnrPodkategoria.getSelectedItemPosition());
        intent.putExtra("FREQUENCYID", spnrCzestotliowsc.getSelectedItemPosition());
        intent.putExtra("IdStalegoWydatku", idCyclicalWydatku);
        intent.putExtra("edycja", edycja);
        startActivity(intent);


    }
    public void WybierzDateGodzine2(View view) {
        intent = new Intent(this, dataPicker.class);

        intent.putExtra("Nazwa", txtNazwa.getText().toString());
        intent.putExtra("Kwota", txtKwota.getText().toString());
        intent.putExtra("Klasa", "nowyCyclicalExpenses2");
        intent.putExtra("data_godzina1", data_godzina1);
        intent.putExtra("data_godzina2", data_godzina2);
        intent.putExtra("data_godzina3", data_godzina3);
        intent.putExtra("KategoriaID", spnrKategoria.getSelectedItemPosition());
        intent.putExtra("PodkategoriaID", spnrPodkategoria.getSelectedItemPosition());
        intent.putExtra("FREQUENCYID", spnrCzestotliowsc.getSelectedItemPosition());
        intent.putExtra("IdStalegoWydatku", idCyclicalWydatku);
        intent.putExtra("edycja", edycja);
        startActivity(intent);


    }
    public void WybierzDateGodzine3(View view) {
        intent = new Intent(this, dataPicker.class);

        intent.putExtra("Nazwa", txtNazwa.getText().toString());
        intent.putExtra("Kwota", txtKwota.getText().toString());
        intent.putExtra("Klasa", "nowyCyclicalExpenses3");
        intent.putExtra("data_godzina1", data_godzina1);
        intent.putExtra("data_godzina2", data_godzina2);
        intent.putExtra("data_godzina3", data_godzina3);
        intent.putExtra("KategoriaID", spnrKategoria.getSelectedItemPosition());
        intent.putExtra("PodkategoriaID", spnrPodkategoria.getSelectedItemPosition());
        intent.putExtra("FREQUENCYID", spnrCzestotliowsc.getSelectedItemPosition());
        intent.putExtra("IdStalegoWydatku", idCyclicalWydatku);
        intent.putExtra("edycja", edycja);

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

    public void usun(View view)
    {
        BazaDanych.RemoveCyclicalExpenses(idCyclicalWydatku);
        intent = new Intent(this, CykliczneWydatki.class);
        startActivity(intent);
    }
}
