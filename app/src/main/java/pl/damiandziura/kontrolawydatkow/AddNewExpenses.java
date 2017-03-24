package pl.damiandziura.kontrolawydatkow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
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
        setDataAndHour();

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
                   Toast.makeText(this, "Dodano nowy wydatek " + NAZWA + " " + Double.toHexString(KWOTA) + "zł", Toast.LENGTH_SHORT).show();
                   intent = new Intent(this, MainActivity.class);
                   startActivity(intent);
               } else Toast.makeText(this, "Kwota musi być większa od 0!", Toast.LENGTH_SHORT).show();
           } else Toast.makeText(this, "Musisz wpisać kwote!", Toast.LENGTH_SHORT).show();

        }else{Toast.makeText(this, "Musisz wpisać nazwe!", Toast.LENGTH_SHORT).show();}




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
        DATA = Integer.toString(c.get(Calendar.DAY_OF_MONTH)) + "-" + Integer.toString(c.get(Calendar.MONTH)+1) + "-" + Integer.toString(c.get(Calendar.YEAR));
        GODZINA = Integer.toString(c.get(Calendar.HOUR_OF_DAY)) + ":" + Integer.toString(c.get(Calendar.MINUTE));
    }




}
