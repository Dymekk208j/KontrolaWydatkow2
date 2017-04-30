package pl.damiandziura.kontrolawydatkow;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import static java.util.Calendar.*;

public class dataPicker extends AppCompatActivity {

    private String buforNazwa = "", buforKwota = "";

    private String Powrot;
    private String DATA, GODZINA;
    private Intent intent;
    private TimePicker czas;
    private DatePicker data;
    private Calendar AktualnaData, dataPobrana;

    TextView lblGodzina, lblData;
    String data_godzina1 = "", data_godzina2 = "", data_godzina3 = "";
    int positionKategoria = 0;
    int positionPodkategoria = 0;
    int positionCzestotliwosc = 0;
    boolean edycja = false;
    int IdStalegoWydatku = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_picker);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            buforNazwa = extras.getString("Nazwa");
            buforKwota = extras.getString("Kwota");
            Powrot = extras.getString("Klasa");

            data_godzina1 = extras.getString("data_godzina1");
            data_godzina2 = extras.getString("data_godzina2");
            data_godzina3 = extras.getString("data_godzina3");

            positionKategoria = extras.getInt("KategoriaID");
            positionPodkategoria = extras.getInt("PodkategoriaID");
            positionCzestotliwosc = extras.getInt("CzestotliwoscID");
            IdStalegoWydatku = extras.getInt("IdStalegoWydatku");
            edycja = extras.getBoolean("edycja");

        }

        lblGodzina = (TextView) findViewById(R.id.lblGodzina);
        lblData = (TextView) findViewById(R.id.lblData);
        data = (DatePicker) findViewById(R.id.datePicker2);
        czas = (TimePicker) findViewById(R.id.timePicker3);
        czas.setIs24HourView(true);

        switch (Powrot) {
            case "nowyPrzychod":
                intent = new Intent(this, nowyPrzychod.class);
                setTitle(getResources().getString(R.string.str_wybierz_date_i_godzine));
                break;
            case "nowyWydatek":
                intent = new Intent(this, AddNewExpenses.class);
                setTitle(getResources().getString(R.string.str_wybierz_date_i_godzine));
                break;
            case "nowyCyclicalExpenses1":
            case "nowyCyclicalExpenses2":
            case "nowyCyclicalExpenses3":
                intent = new Intent(this, dodajStalyWydatek.class);
                czas.setVisibility(View.INVISIBLE);
                lblGodzina.setVisibility(View.INVISIBLE);
                setTitle(getResources().getString(R.string.str_wybierz_date));
                break;
            case "nowyCyclicalIncome1":
            case "nowyCyclicalIncome2":
            case "nowyCyclicalIncome3":
                intent = new Intent(this, dodajStalydochod.class);
                czas.setVisibility(View.INVISIBLE);
                lblGodzina.setVisibility(View.INVISIBLE);
                setTitle(getResources().getString(R.string.str_wybierz_date));
                break;
        }

    }

    public void btCofnij(View view) {
        onBackPressed();
    }
    
    public void btZatwierdz(View view)
    {

        switch (Powrot) {
            case "nowyPrzychod":
                sprawdzDateZogarniczeniem();
                break;
            case "nowyWydatek":
            case "nowyDochod":
                sprawdzDateZogarniczeniem();
                break;
            case "nowyCyclicalExpenses1":
            case "nowyCyclicalExpenses2":
            case "nowyCyclicalExpenses3":
                sprawdzDateBezogarniczenia();
                break;
            case "nowyCyclicalIncome1":
            case "nowyCyclicalIncome2":
            case "nowyCyclicalIncome3":
                sprawdzDateBezogarniczenia();
                break;
        }

    }

    public void btTeraz(View view)
    {
        AktualnaData = Calendar.getInstance();
        data.updateDate(AktualnaData.get(Calendar.YEAR), AktualnaData.get(Calendar.MONTH), AktualnaData.get(Calendar.DAY_OF_MONTH));
    }

    private void sprawdzDateZogarniczeniem()
    {
        AktualnaData = Calendar.getInstance();
        dataPobrana = Calendar.getInstance();

        dataPobrana.set(YEAR, data.getYear());
        dataPobrana.set(MONTH, data.getMonth());
        dataPobrana.set(DAY_OF_MONTH, data.getDayOfMonth());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) //Metoda dla API >=23
        {
            dataPobrana.set(HOUR_OF_DAY, czas.getHour());
            dataPobrana.set(MINUTE, czas.getMinute());
        }
        else
        {
            dataPobrana.set(HOUR_OF_DAY, czas.getCurrentHour());
            dataPobrana.set(MINUTE, czas.getCurrentMinute());
        }



        if (AktualnaData.getTimeInMillis() >= dataPobrana.getTimeInMillis())
        {
            String dzien = Integer.toString(data.getDayOfMonth());
            if(data.getDayOfMonth() <= 9) dzien = "0" + Integer.toString(data.getDayOfMonth());
            String miesiac = Integer.toString(data.getDayOfMonth()+1);
            if((data.getMonth()+1) <= 9) miesiac = "0" + Integer.toString(data.getMonth()+1);

            DATA = dzien + "-" + miesiac + "-" + Integer.toString(data.getYear());
            String minuty, godziny;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) //Metoda dla API >=23
            {
                if(czas.getMinute() <= 9) {
                    minuty = "0" + Integer.toString(czas.getMinute());
                }else minuty = Integer.toString(czas.getMinute());
                if(czas.getHour() <= 9) {
                    godziny = "0" + Integer.toString(czas.getHour());
                }else godziny = Integer.toString(czas.getHour());
            }
            else
            {
                if(czas.getCurrentMinute() <= 9) {
                    minuty = "0" + Integer.toString(czas.getCurrentMinute());
                }else minuty = Integer.toString(czas.getCurrentMinute());
                if(czas.getCurrentHour() <= 9) {
                    godziny = "0" + Integer.toString(czas.getCurrentHour());
                }else godziny = Integer.toString(czas.getCurrentHour());
            }

            GODZINA = godziny + ":" + minuty;
            intent.putExtra("Data", DATA);
            intent.putExtra("Godzina", GODZINA);
            intent.putExtra("Kwota", buforKwota);
            intent.putExtra("Nazwa", buforNazwa);

            intent.putExtra("KategoriaID", positionKategoria);
            intent.putExtra("PodkategoriaID", positionPodkategoria);

            intent.putExtra("IdStalegoWydatku", IdStalegoWydatku);



            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, getResources().getString(R.string.str_data_z_przyszlosci), Toast.LENGTH_SHORT).show();
        }
    }

    private void sprawdzDateBezogarniczenia()
    {
        dataPobrana = Calendar.getInstance();
        dataPobrana.set(YEAR, data.getYear());
        dataPobrana.set(MONTH, data.getMonth());
        dataPobrana.set(DAY_OF_MONTH, data.getDayOfMonth());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) //Metoda dla API >=23
        {
            dataPobrana.set(HOUR_OF_DAY, czas.getHour());
            dataPobrana.set(MINUTE, czas.getMinute());
        }
        else
        {
            dataPobrana.set(HOUR_OF_DAY, czas.getCurrentHour());
            dataPobrana.set(MINUTE, czas.getCurrentMinute());
        }




        String dzien = Integer.toString(data.getDayOfMonth());
        if(data.getDayOfMonth() <= 9) dzien = "0" + Integer.toString(data.getDayOfMonth());
        String miesiac = Integer.toString(data.getDayOfMonth()+1);
        if((data.getMonth()+1) <= 9) miesiac = "0" + Integer.toString(data.getMonth()+1);

        DATA = dzien + "-" + miesiac + "-" + Integer.toString(data.getYear());
        String minuty, godziny;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) //Metoda dla API >=23
        {
            if(czas.getMinute() <= 9) {
                minuty = "0" + Integer.toString(czas.getMinute());
            }else minuty = Integer.toString(czas.getMinute());
            if(czas.getHour() <= 9) {
                godziny = "0" + Integer.toString(czas.getHour());
            }else godziny = Integer.toString(czas.getHour());
        }
        else
        {
            if(czas.getCurrentMinute() <= 9) {
                minuty = "0" + Integer.toString(czas.getCurrentMinute());
            }else minuty = Integer.toString(czas.getCurrentMinute());
            if(czas.getCurrentHour() <= 9) {
                godziny = "0" + Integer.toString(czas.getCurrentHour());
            }else godziny = Integer.toString(czas.getCurrentHour());
        }
            GODZINA = godziny + ":" + minuty;
            intent.putExtra("Data", DATA);
            intent.putExtra("Godzina", GODZINA);
            intent.putExtra("Kwota", buforKwota);
            intent.putExtra("Nazwa", buforNazwa);
            intent.putExtra("Powrot", Powrot);
            intent.putExtra("data_godzina1", data_godzina1);
            intent.putExtra("data_godzina2", data_godzina2);
            intent.putExtra("data_godzina3", data_godzina3);
            intent.putExtra("KategoriaID", positionKategoria);
            intent.putExtra("PodkategoriaID", positionPodkategoria);
            intent.putExtra("CzestotliwoscID", positionCzestotliwosc);
            intent.putExtra("IdStalegoWydatku", IdStalegoWydatku);
            intent.putExtra("edycja", edycja);

            startActivity(intent);
        }




}
